package cz.jpmad.cvinsight.api.cvs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jpmad.cvinsight.api.cv.dto.CvEducationResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvLanguageResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvWorkExperienceResponse;

import java.util.List;

/**
 * Request for storing a structured CV.
 */
public record CvsCreateRequest(

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