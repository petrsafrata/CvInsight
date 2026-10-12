package cz.jpmad.cvinsight.service.cvs;

import cz.jpmad.cvinsight.api.cv.dto.CvEducationResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvLanguageResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvWorkExperienceResponse;
import cz.jpmad.cvinsight.api.cvs.dto.CvsCreateRequest;
import cz.jpmad.cvinsight.api.cvs.dto.CvsResponse;
import cz.jpmad.cvinsight.model.cv.Cv;
import cz.jpmad.cvinsight.model.cv.CvEducation;
import cz.jpmad.cvinsight.model.cv.CvLanguage;
import cz.jpmad.cvinsight.model.cv.CvWorkExperience;
import cz.jpmad.cvinsight.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Service for operations with stored CVs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CvsService {

    private final CvRepository cvRepository;

    /**
     * Stores a structured CV.
     *
     * @param request structured CV data
     * @return stored CV
     */
    @Transactional
    public CvsResponse create(final CvsCreateRequest request) {
        final Cv cv = toEntity(request);
        final Cv savedCv = cvRepository.save(cv);

        log.info("CV created: id={}", savedCv.getId());

        return toResponse(savedCv);
    }

    /**
     * Returns all stored CVs.
     *
     * @return stored CVs
     */
    @Transactional(readOnly = true)
    public List<CvsResponse> findAll() {
        log.debug("Loading all stored CVs");

        return cvRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns a stored CV.
     *
     * @param id CV identifier
     * @return stored CV
     */
    @Transactional(readOnly = true)
    public CvsResponse getById(final UUID id) {
        return toResponse(findEntityById(id));
    }

    /**
     * Finds a CV entity by its identifier.
     *
     * This method is used internally when another service requires
     * access to the persistent CV.
     *
     * @param id CV identifier
     * @return CV entity
     */
    @Transactional(readOnly = true)
    public Cv findById(final UUID id) {
        return findEntityById(id);
    }

    /**
     * Deletes a stored CV.
     *
     * @param id CV identifier
     */
    @Transactional
    public void delete(final UUID id) {
        final Cv cv = findEntityById(id);

        cvRepository.delete(cv);

        log.info("CV deleted: id={}", id);
    }

    /**
     * Finds a CV entity by its identifier.
     *
     * @param id CV identifier
     * @return CV entity
     */
    private Cv findEntityById(final UUID id) {
        log.debug("Loading CV: id={}", id);

        return cvRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("CV not found: id={}", id);

                    return new NoSuchElementException(
                            "CV not found: " + id
                    );
                });
    }

    /**
     * Converts a structured CV request to a persistent entity.
     *
     * @param request structured CV data
     * @return persistent CV entity
     */
    private Cv toEntity(final CvsCreateRequest request) {
        final Cv cv = new Cv();

        cv.setFirstName(request.firstName());
        cv.setLastName(request.lastName());
        cv.setEmail(request.email());
        cv.setPhone(request.phone());
        cv.setAddress(request.address());
        cv.setCity(request.city());
        cv.setRegion(request.region());
        cv.setPostalCode(request.postalCode());
        cv.setNationality(request.nationality());
        cv.setGender(request.gender());

        cv.setLanguages(toLanguages(request.languages()));
        cv.setDrivingLicenses(copyList(request.drivingLicenses()));
        cv.setWorkExperience(
                toWorkExperience(request.workExperience())
        );
        cv.setEducation(toEducation(request.education()));

        return cv;
    }

    /**
     * Converts a list of structured CV language requests to persistent entities.
     *
     * @param languages structured CV language data
     * @return persistent CV language entities
     */
    private List<CvLanguage> toLanguages(final List<CvLanguageResponse> languages) {
        if (languages == null) {
            return new ArrayList<>();
        }

        return languages.stream()
                .map(language -> {
                    final CvLanguage entity = new CvLanguage();

                    entity.setLanguage(language.language());
                    entity.setLevel(language.level());

                    return entity;
                })
                .toList();
    }

    /**
     * Converts a list of structured CV work experience requests to persistent entities.
     *
     * @param workExperience structured CV work experience data
     * @return persistent CV work experience entities
     */
    private List<CvWorkExperience> toWorkExperience(final List<CvWorkExperienceResponse> workExperience) {
        if (workExperience == null) {
            return new ArrayList<>();
        }

        return workExperience.stream()
                .map(experience -> {
                    final CvWorkExperience entity =
                            new CvWorkExperience();

                    entity.setPosition(experience.position());
                    entity.setCompany(experience.company());
                    entity.setFrom(experience.from());
                    entity.setTo(experience.to());

                    return entity;
                })
                .toList();
    }

    /**
     * Converts a list of structured CV education requests to persistent entities.
     *
     * @param education structured CV education data
     * @return persistent CV education entities
     */
    private List<CvEducation> toEducation(final List<CvEducationResponse> education) {
        if (education == null) {
            return new ArrayList<>();
        }

        return education.stream()
                .map(item -> {
                    final CvEducation entity = new CvEducation();

                    entity.setSchool(item.school());
                    entity.setField(item.field());
                    entity.setDegree(item.degree());

                    return entity;
                })
                .toList();
    }

    /**
     * Converts a persistent CV entity to a structured CV response.
     *
     * @param cv persistent CV entity
     * @return structured CV data
     */
    private CvsResponse toResponse(final Cv cv) {
        return new CvsResponse(
                cv.getId(),
                cv.getFirstName(),
                cv.getLastName(),
                cv.getEmail(),
                cv.getPhone(),
                cv.getAddress(),
                cv.getCity(),
                cv.getRegion(),
                cv.getPostalCode(),
                cv.getNationality(),
                cv.getGender(),
                toLanguageResponses(cv.getLanguages()),
                copyList(cv.getDrivingLicenses()),
                toWorkExperienceResponses(cv.getWorkExperience()),
                toEducationResponses(cv.getEducation()),
                cv.getCreatedAt(),
                cv.getUpdatedAt()
        );
    }

    /**
     * Converts a list of persistent CV language entities to structured CV language responses.
     *
     * @param languages persistent CV language entities
     * @return structured CV language data
     */
    private List<CvLanguageResponse> toLanguageResponses(final List<CvLanguage> languages) {
        if (languages == null) {
            return List.of();
        }

        return languages.stream()
                .map(language -> new CvLanguageResponse(
                        language.getLanguage(),
                        language.getLevel()
                ))
                .toList();
    }

    /**
     * Converts a list of persistent CV work experience entities to structured CV work experience responses.
     *
     * @param workExperience persistent CV work experience entities
     * @return structured CV work experience data
     */
    private List<CvWorkExperienceResponse> toWorkExperienceResponses(final List<CvWorkExperience> workExperience) {
        if (workExperience == null) {
            return List.of();
        }

        return workExperience.stream()
                .map(experience -> new CvWorkExperienceResponse(
                        experience.getPosition(),
                        experience.getCompany(),
                        experience.getFrom(),
                        experience.getTo()
                ))
                .toList();
    }

    /**
     * Converts a list of persistent CV education entities to structured CV education responses.
     *
     * @param education persistent CV education entities
     * @return structured CV education data
     */
    private List<CvEducationResponse> toEducationResponses(final List<CvEducation> education) {
        if (education == null) {
            return List.of();
        }

        return education.stream()
                .map(item -> new CvEducationResponse(
                        item.getSchool(),
                        item.getField(),
                        item.getDegree()
                ))
                .toList();
    }

    /**
     * Creates a copy of a list of strings.
     *
     * @param values list of strings
     * @return copy of the list of strings
     */
    private List<String> copyList(final List<String> values) {
        if (values == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(values);
    }
}