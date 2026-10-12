package cz.jpmad.cvinsight.service.cvs;

import cz.jpmad.cvinsight.model.cv.CvEducation;
import cz.jpmad.cvinsight.model.cv.CvLanguage;
import cz.jpmad.cvinsight.model.cv.CvWorkExperience;

import java.util.List;

/**
 * Professional candidate data provided to AI for candidate analysis.
 * Personal characteristics and contact information are intentionally excluded.
 *
 * @param languages candidate language skills
 * @param drivingLicenses candidate driving licenses
 * @param workExperience candidate work experience
 * @param education candidate education
 */
public record CvAnalysisCandidateData(
        List<CvLanguage> languages,
        List<String> drivingLicenses,
        List<CvWorkExperience> workExperience,
        List<CvEducation> education
) {
}