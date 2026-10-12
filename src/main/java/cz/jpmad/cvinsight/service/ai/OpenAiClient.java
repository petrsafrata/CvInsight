package cz.jpmad.cvinsight.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.jpmad.cvinsight.exception.OpenAiUnavailableException;
import cz.jpmad.cvinsight.exception.OpenApiException;
import cz.jpmad.cvinsight.exception.RateLimitExceededException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;

/**
 * Client for communication with the OpenAI Responses API.
 */
@Slf4j
@Component
public class OpenAiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    /**
     * Creates an OpenAI API client.
     *
     * @param objectMapper configured Jackson object mapper
     * @param apiKey OpenAI API key
     * @param baseUrl OpenAI API base URL
     * @param model OpenAI model
     */
    public OpenAiClient(
            final ObjectMapper objectMapper,
            @Value("${openai.api-key}") final String apiKey,
            @Value("${openai.base-url}") final String baseUrl,
            @Value("${openai.model}") final String model
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("OpenAI API key must not be empty");
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("OpenAI API base URL must not be empty");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("OpenAI model must not be empty");
        }

        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.model = model.trim();

        final String normalizedBaseUrl = baseUrl.trim().replaceAll("/+$", "");

        this.webClient = WebClient.builder()
                .baseUrl(normalizedBaseUrl)
                .defaultHeaders(headers ->
                        headers.setBearerAuth(apiKey.trim())
                )
                .build();

        log.info("OpenAI client initialized: model={}, baseUrl={}", this.model, normalizedBaseUrl);
    }

    /**
     * Sends a structured response request to OpenAI.
     *
     * @param instructions trusted system instructions
     * @param input untrusted input text
     * @param schema JSON schema defining the expected output
     * @return OpenAI API response
     */
    public JsonNode createResponse(final String instructions, final String input, final JsonNode schema) {
        Objects.requireNonNull(instructions, "Instructions must not be null");
        Objects.requireNonNull(input, "Input must not be null");
        Objects.requireNonNull(schema, "JSON schema must not be null");

        final ObjectNode format = objectMapper.createObjectNode();
        format.put("type", "json_schema");
        format.put("name", "cv_parse");
        format.put("strict", true);
        format.set("schema", schema);

        final ObjectNode text = objectMapper.createObjectNode();
        text.set("format", format);

        final ObjectNode request = objectMapper.createObjectNode();
        request.put("model", model);
        request.put("instructions", instructions);
        request.put("input", input);
        request.set("text", text);

        final String requestBody;

        try {
            requestBody = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize OpenAI request", e);
            throw new RuntimeException("Failed to serialize OpenAI request", e);
        }

        log.info("Sending structured request to OpenAI: model={}", model);

        final String responseBody;

        try {
            responseBody = webClient.post()
                    .uri("/responses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            final int status = e.getStatusCode().value();

            log.error("OpenAI API request failed: status={}, type={}, code={}, param={}",
                    status,
                    extractErrorField(e, "type"),
                    extractErrorField(e, "code"),
                    extractErrorField(e, "param")
            );

            if (status == 429) {
                throw new RateLimitExceededException("OpenAI API rate limit exceeded", e);
            }

            if (status >= 500) {
                throw new OpenAiUnavailableException("OpenAI API is temporarily unavailable", e);
            }

            throw new OpenApiException("OpenAI API request failed with HTTP status " + status, e);

        } catch (WebClientRequestException e) {
            log.error("Failed to connect to OpenAI API", e);
            throw new OpenAiUnavailableException("Unable to connect to OpenAI API", e);
        }

        if (responseBody == null || responseBody.isBlank()) {
            throw new OpenApiException("OpenAI API returned an empty response", null);
        }

        try {
            final JsonNode response = objectMapper.readTree(responseBody);

            if (response == null || !response.isObject()) {
                throw new OpenApiException("OpenAI API returned an invalid response", null);
            }

            log.info("OpenAI request completed successfully: responseId={}, status={}",
                    response.path("id").asText("unknown"),
                    response.path("status").asText("unknown")
            );

            return response;

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize OpenAI response", e);
            throw new OpenApiException("Failed to parse OpenAI JSON response", e);
        }
    }

    /**
     * Extracts a diagnostic field from an OpenAI error response.
     *
     * @param exception HTTP response exception
     * @param field error field name
     * @return diagnostic field value or unknown
     */
    private String extractErrorField(final WebClientResponseException exception, final String field) {
        try {
            final JsonNode response = objectMapper.readTree(exception.getResponseBodyAsString());
            final JsonNode value = response.path("error").path(field);

            if (value.isMissingNode() || value.isNull()) {
                return "unknown";
            }

            return value.asText();

        } catch (JsonProcessingException e) {
            return "unknown";
        }
    }
}