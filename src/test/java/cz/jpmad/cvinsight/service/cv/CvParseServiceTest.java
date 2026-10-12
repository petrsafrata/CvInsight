package cz.jpmad.cvinsight.service.cv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jpmad.cvinsight.service.ai.CvParsePromptProvider;
import cz.jpmad.cvinsight.service.ai.OpenAiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CvParseServiceTest {

    private OpenAiClient openAiClient;
    private CvParsePromptProvider promptProvider;
    private ObjectMapper objectMapper;
    private CvParseService parseService;

    @BeforeEach
    void setUp() {
        openAiClient = mock(OpenAiClient.class);
        promptProvider = mock(CvParsePromptProvider.class);
        objectMapper = new ObjectMapper();

        parseService = new CvParseService(
                openAiClient,
                promptProvider,
                objectMapper
        );
    }

    @Test
    void parseShouldRejectNullText() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parseService.parse(null)
        );

        verifyNoInteractions(openAiClient);
    }

    @Test
    void parseShouldRejectBlankText() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parseService.parse("   ")
        );

        verifyNoInteractions(openAiClient);
    }

    @Test
    void parseShouldReturnStructuredCv() throws Exception {
        JsonNode response = objectMapper.readTree("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "{\\"firstName\\":\\"Jan\\",\\"lastName\\":\\"Novak\\"}"
                        }
                      ]
                    }
                  ]
                }
                """);

        when(promptProvider.getInstructions())
                .thenReturn("Parse CV");

        when(promptProvider.getSchema())
                .thenReturn(objectMapper.createObjectNode());

        when(openAiClient.createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        )).thenReturn(response);

        var result = parseService.parse("Jan Novak, Java Developer");

        assertNotNull(result);
        assertEquals("Jan", result.firstName());
        assertEquals("Novak", result.lastName());

        verify(openAiClient, times(1)).createResponse(
                eq("Parse CV"),
                eq("Jan Novak, Java Developer"),
                any(JsonNode.class)
        );
    }

    @Test
    void parseShouldRejectResponseWithoutOutputText() throws Exception {
        JsonNode response = objectMapper.readTree("""
                {
                  "output": []
                }
                """);

        when(promptProvider.getInstructions())
                .thenReturn("Parse CV");

        when(promptProvider.getSchema())
                .thenReturn(objectMapper.createObjectNode());

        when(openAiClient.createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        )).thenReturn(response);

        assertThrows(
                IllegalStateException.class,
                () -> parseService.parse("Jan Novak")
        );
    }

    @Test
    void parseShouldRejectInvalidJsonOutput() throws Exception {
        JsonNode response = objectMapper.readTree("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "invalid-json"
                        }
                      ]
                    }
                  ]
                }
                """);

        when(promptProvider.getInstructions())
                .thenReturn("Parse CV");

        when(promptProvider.getSchema())
                .thenReturn(objectMapper.createObjectNode());

        when(openAiClient.createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        )).thenReturn(response);

        assertThrows(
                IllegalStateException.class,
                () -> parseService.parse("Jan Novak")
        );
    }
}