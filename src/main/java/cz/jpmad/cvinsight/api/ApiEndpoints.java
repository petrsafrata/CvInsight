package cz.jpmad.cvinsight.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Component that holds the API endpoints for the application.
 * The endpoints are constructed using the API version specified in the application properties.
 */
@Component
@Getter
public class ApiEndpoints {

    public static final String API_VERSION_PROPERTY = "${spring.general-config.api-version}";

    public static final String CV_ENDPOINT = "/" + API_VERSION_PROPERTY + "/cv";
    public static final String CVS_ENDPOINT = "/" + API_VERSION_PROPERTY + "/cvs";
    public static final String AUTH_ENDPOINT = "/auth";
    public static final String INFO_ENDPOINT = "/info";

    private final String apiVersion;

    /**
     * Constructor that initializes the API version from the application properties.
     */
    public ApiEndpoints(@Value(API_VERSION_PROPERTY) String apiVersion) {
        this.apiVersion = apiVersion;
    }

}
