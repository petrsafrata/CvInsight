package cz.jpmad.cvinsight.model.cv;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a structured CV stored in the database.
 */
@Entity
@Table(name = "cvs")
@Getter
@Setter
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String region;

    @Column(name = "postal_code")
    private String postalCode;

    private String nationality;

    private String gender;

    @ElementCollection
    @CollectionTable(
            name = "cv_languages",
            joinColumns = @JoinColumn(name = "cv_id")
    )
    private List<CvLanguage> languages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "cv_driving_licenses",
            joinColumns = @JoinColumn(name = "cv_id")
    )
    @Column(name = "driving_license")
    private List<String> drivingLicenses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "cv_work_experiences",
            joinColumns = @JoinColumn(name = "cv_id")
    )
    private List<CvWorkExperience> workExperience = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "cv_educations",
            joinColumns = @JoinColumn(name = "cv_id")
    )
    private List<CvEducation> education = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Cv cv = (Cv) o;
        return id != null && Objects.equals(id, cv.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}