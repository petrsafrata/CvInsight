package cz.jpmad.cvinsight.service.cvs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.jpmad.cvinsight.api.cvs.dto.CvAnalysisResponse;
import cz.jpmad.cvinsight.model.cv.Cv;
import cz.jpmad.cvinsight.service.ai.CvAnalysisPromptProvider;
import cz.jpmad.cvinsight.service.ai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for AI-based analysis of stored CVs against job offers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CvAnalysisService {

    private final CvsService cvsService;
    private final OpenAiClient openAiClient;
    private final CvAnalysisPromptProvider promptProvider;
    private final ObjectMapper objectMapper;

    /**
     * Analyzes a stored CV against a job offer.
     *
     * @param cvId identifier of the stored CV
     * @param jobOfferText text of the job offer
     * @return candidate analysis
     */
    public CvAnalysisResponse analyze(final UUID cvId, final String jobOfferText) {
        validateJobOffer(jobOfferText);

        final Cv cv = cvsService.findById(cvId);

        log.info("Analyzing CV against job offer: cvId={}, jobOfferCharacters={}",
                cvId,
                jobOfferText.length()
        );

        final CvAnalysisCandidateData candidateData = createCandidateData(cv);
        final String input = createAnalysisInput(candidateData, jobOfferText);

        final JsonNode response = openAiClient.createResponse(
                promptProvider.getInstructions(),
                input,
                promptProvider.getSchema()
        );

        final String outputText = extractOutputText(response);

        try {
            final CvAnalysisResponse result = objectMapper.readValue(outputText, CvAnalysisResponse.class);
            log.info("CV analysis completed successfully: cvId={}", cvId);
            return result;

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize AI CV analysis response: cvId={}", cvId, e);
            throw new IllegalStateException("Failed to parse AI analysis response", e);
        }
    }

    /**
     * Validates the job offer text.
     *
     * @param jobOfferText text of the job offer
     */
    private void validateJobOffer(final String jobOfferText) {
        if (jobOfferText == null || jobOfferText.isBlank()) {
            throw new IllegalArgumentException("Job offer text must not be empty");
        }
    }

    /**
     * Creates candidate data from the CV for analysis.
     *
     * @param cv the CV to analyze
     * @return candidate data for analysis
     */
    private CvAnalysisCandidateData createCandidateData(final Cv cv) {
        return new CvAnalysisCandidateData(
                cv.getLanguages(),
                cv.getDrivingLicenses(),
                cv.getWorkExperience(),
                cv.getEducation()
        );
    }

    /**
     * Creates the input for the AI analysis.
     *
     * @param candidateData candidate data for analysis
     * @param jobOfferText text of the job offer
     * @return JSON string representing the input for the AI analysis
     */
    private String createAnalysisInput(final CvAnalysisCandidateData candidateData, final String jobOfferText) {
        try {
            final ObjectNode input = objectMapper.createObjectNode();
            input.set("candidate", objectMapper.valueToTree(candidateData));

            input.put("jobOffer", jobOfferText);

            return objectMapper.writeValueAsString(input);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to prepare CV analysis input", e);
        }
    }

    /**
     * Extracts the output text from the AI response.
     *
     * @param response the AI response
     * @return the output text from the AI response
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
                if (!"output_text".equals(
                        content.path("type").asText()
                )) {
                    continue;
                }

                final String text =
                        content.path("text").asText();

                if (!text.isBlank()) {
                    return text;
                }
            }
        }

        throw new IllegalStateException("OpenAI response does not contain output text");
    }
}