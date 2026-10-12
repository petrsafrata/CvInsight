package cz.jpmad.cvinsight.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Provides trusted instructions and JSON schema for CV analysis.
 */
@Component
public class CvAnalysisPromptProvider {

    private final Resource instructionsResource;
    private final Resource schemaResource;
    private final ObjectMapper objectMapper;

    @Getter
    private String instructions;
    @Getter
    private JsonNode schema;

    public CvAnalysisPromptProvider(
            @Value("${cv.analysis.instructions}") final Resource instructionsResource,
            @Value("${cv.analysis.schema}") final Resource schemaResource,
            final ObjectMapper objectMapper
    ) {
        this.instructionsResource = instructionsResource;
        this.schemaResource = schemaResource;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void initialize() throws IOException {
        instructions = instructionsResource.getContentAsString(StandardCharsets.UTF_8);

        try (var inputStream = schemaResource.getInputStream()) {
            schema = objectMapper.readTree(inputStream);
        }
    }
}