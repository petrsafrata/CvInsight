package cz.jpmad.cvinsight.api.cvs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jpmad.cvinsight.api.cv.dto.CvEducationResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvLanguageResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvWorkExperienceResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Structured CV stored in the database.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvsResponse(

        @JsonProperty("id")
        UUID id,

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
        List<CvEducationResponse> education,

        @JsonProperty("createdAt")
        Instant createdAt,

        @JsonProperty("updatedAt")
        Instant updatedAt

) {
}