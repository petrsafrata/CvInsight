package cz.jpmad.cvinsight.api;

import lombok.experimental.UtilityClass;

/**
 * Swagger constants for API documentation.
 */
@UtilityClass
public final class Constants {

    /**
     * Literals for messages.
     */
    public static final String SWAGGER_OK = "**OK** = request was successful";
    public static final String SWAGGER_CREATED = "**CREATED** = resource was successfully created";
    public static final String SWAGGER_NO_CONTENT = "**NO CONTENT** = request was successful but no content to return";
    public static final String SWAGGER_DELETED = "**DELETED** = resource was successfully deleted";
    public static final String SWAGGER_BAD_REQUEST = "**BAD REQUEST** = invalid request parameters";
    public static final String SWAGGER_UNAUTHORIZED = "**UNAUTHORIZED** = missing or invalid authentication";
    public static final String SWAGGER_FORBIDDEN = "**FORBIDDEN** = insufficient permissions";
    public static final String SWAGGER_NOT_FOUND = "**NOT FOUND** = resource not found";
    public static final String SWAGGER_CONFLICT = "**CONFLICT** = resource already exists or unique constraint violated";
    public static final String SWAGGER_INTERNAL_SERVER_ERROR = "**INTERNAL SERVER ERROR** = unexpected error on the server";
    public static final String SWAGGER_UNSUPPORTED_MEDIA_TYPE = "**UNSUPPORTED MEDIA TYPE** = unsupported file type";
    public static final String SWAGGER_TOO_MANY_REQUESTS = "**TOO MANY REQUESTS** = rate limit exceeded";
    public static final String SWAGGER_BAD_GATEWAY = "**BAD GATEWAY** = OpenAI API error";
    public static final String SWAGGER_SERVICE_UNAVAILABLE = "**SERVICE UNAVAILABLE** = OpenAI API is temporarily unavailable";
}