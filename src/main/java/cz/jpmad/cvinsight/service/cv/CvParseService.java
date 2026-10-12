package cz.jpmad.cvinsight.service.cv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jpmad.cvinsight.api.cv.dto.CvParseResponse;
import cz.jpmad.cvinsight.service.ai.CvParsePromptProvider;
import cz.jpmad.cvinsight.service.ai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for converting extracted CV text into structured data using AI.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CvParseService {

    private final OpenAiClient openAiClient;
    private final CvParsePromptProvider promptProvider;
    private final ObjectMapper objectMapper;

    /**
     * Converts extracted CV text into structured CV data using AI.
     *
     * @param cvText extracted CV text
     * @return structured CV data
     */
    public CvParseResponse parse(final String cvText) {
        validateText(cvText);

        log.info("Parsing CV text using AI: characters={}", cvText.length());

        final JsonNode response = openAiClient.createResponse(
                promptProvider.getInstructions(),
                cvText,
                promptProvider.getSchema()
        );

        final String outputText = extractOutputText(response);

        try {
            final CvParseResponse result = objectMapper.readValue(outputText, CvParseResponse.class);
            log.info("CV text parsing completed successfully");
            return result;

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize AI CV parsing response", e);
            throw new IllegalStateException("Failed to parse AI response", e);
        }
    }

    /**
     * Validates CV text before sending it to the AI service.
     *
     * @param cvText extracted CV text
     */
    private void validateText(final String cvText) {
        if (cvText == null || cvText.isBlank()) {
            log.warn("CV parsing requested with empty text");
            throw new IllegalArgumentException("CV text must not be empty");
        }
    }

    /**
     * Extracts structured output text from the OpenAI Responses API response.
     *
     * @param response OpenAI API response
     * @return structured JSON output
     */
    private String extractOutputText(final JsonNode response) {
        if (response == null) {
            throw new IllegalStateException("OpenAI response must not be null");
        }

        final JsonNode output = response.path("output");

        for (final JsonNode item : output) {
            if (!"message".equals(item.path("type").asText())) {
                continue;
            }

            for (final JsonNode content : item.path("content")) {
                if (!"output_text".equals(content.path("type").asText())) {
                    continue;
                }

                final String text = content.path("text").asText();

                if (!text.isBlank()) {
                    return text;
                }
            }
        }

        throw new IllegalStateException("OpenAI response does not contain output text");
    }
}