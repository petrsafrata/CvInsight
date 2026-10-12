package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Structured data parsed from CV text.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvParseResponse(

        @JsonProperty("firstName")
        String firstName,

        @JsonProperty("lastName")
        String lastName,

        @JsonProperty("email")
        String email,

        @JsonProperty("phone")
        String phone,

        @JsonProperty("address")
        String address,

        @JsonProperty("city")
        String city,

        @JsonProperty("region")
        String region,

        @JsonProperty("postalCode")
        String postalCode,

        @JsonProperty("nationality")
        String nationality,

        @JsonProperty("gender")
        String gender,

        @JsonProperty("languages")
        List<CvLanguageResponse> languages,

        @JsonProperty("drivingLicenses")
        List<String> drivingLicenses,

        @JsonProperty("workExperience")
        List<CvWorkExperienceResponse> workExperience,

        @JsonProperty("education")
        List<CvEducationResponse> education
) {
}