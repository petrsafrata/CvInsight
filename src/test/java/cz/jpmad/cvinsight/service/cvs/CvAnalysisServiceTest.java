package cz.jpmad.cvinsight.service.cvs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jpmad.cvinsight.model.cv.Cv;
import cz.jpmad.cvinsight.service.ai.CvAnalysisPromptProvider;
import cz.jpmad.cvinsight.service.ai.OpenAiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CvAnalysisServiceTest {

    private CvsService cvsService;
    private OpenAiClient openAiClient;
    private CvAnalysisPromptProvider promptProvider;
    private ObjectMapper objectMapper;
    private CvAnalysisService analysisService;

    @BeforeEach
    void setUp() {
        cvsService = mock(CvsService.class);
        openAiClient = mock(OpenAiClient.class);
        promptProvider = mock(CvAnalysisPromptProvider.class);
        objectMapper = new ObjectMapper();

        analysisService = new CvAnalysisService(
                cvsService,
                openAiClient,
                promptProvider,
                objectMapper
        );
    }

    @Test
    void analyzeShouldRejectEmptyJobOffer() {
        assertThrows(
                IllegalArgumentException.class,
                () -> analysisService.analyze(
                        UUID.randomUUID(),
                        " "
                )
        );

        verifyNoInteractions(openAiClient);
    }

    @Test
    void analyzeShouldReturnCandidateAnalysis() throws Exception {
        UUID cvId = UUID.randomUUID();

        Cv cv = createCv();

        JsonNode response = objectMapper.readTree("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "{\\"candidateSummary\\":\\"Java developer\\",\\"matchingSkills\\":[\\"Java\\"],\\"missingSkills\\":[\\"Docker\\"]}"
                        }
                      ]
                    }
                  ]
                }
                """);

        when(cvsService.findById(cvId)).thenReturn(cv);

        when(promptProvider.getInstructions())
                .thenReturn("Analyze candidate");

        when(promptProvider.getSchema())
                .thenReturn(objectMapper.createObjectNode());

        when(openAiClient.createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        )).thenReturn(response);

        var result = analysisService.analyze(
                cvId,
                "Java and Docker experience required"
        );

        assertNotNull(result);
        assertEquals(
                "Java developer",
                result.candidateSummary()
        );

        assertTrue(result.matchingSkills().contains("Java"));
        assertTrue(result.missingSkills().contains("Docker"));

        verify(cvsService).findById(cvId);
        verify(openAiClient).createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        );
    }

    @Test
    void analyzeShouldExcludePersonalCharacteristics() throws Exception {
        UUID cvId = UUID.randomUUID();

        Cv cv = createCv();
        cv.setGender("male");
        cv.setNationality("Czech");

        when(cvsService.findById(cvId)).thenReturn(cv);

        when(promptProvider.getInstructions())
                .thenReturn("Analyze candidate");

        when(promptProvider.getSchema())
                .thenReturn(objectMapper.createObjectNode());

        when(openAiClient.createResponse(
                anyString(),
                anyString(),
                any(JsonNode.class)
        )).thenReturn(objectMapper.readTree("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "{\\"candidateSummary\\":\\"Developer\\",\\"matchingSkills\\":[],\\"missingSkills\\":[]}"
                        }
                      ]
                    }
                  ]
                }
                """));

        analysisService.analyze(cvId, "Java Developer");

        ArgumentCaptor<String> inputCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(openAiClient).createResponse(
                anyString(),
                inputCaptor.capture(),
                any(JsonNode.class)
        );

        JsonNode input = objectMapper.readTree(
                inputCaptor.getValue()
        );

        JsonNode candidate = input.path("candidate");

        assertFalse(candidate.has("gender"));
        assertFalse(candidate.has("nationality"));
        assertFalse(candidate.has("email"));
        assertFalse(candidate.has("phone"));

        assertEquals(
                "Java Developer",
                input.path("jobOffer").asText()
        );
    }

    private Cv createCv() {
        Cv cv = new Cv();

        cv.setLanguages(List.of());
        cv.setDrivingLicenses(List.of("B"));
        cv.setWorkExperience(List.of());
        cv.setEducation(List.of());

        return cv;
    }
}