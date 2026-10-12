package cz.jpmad.cvinsight.model.cv;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Embeddable representation of education contained in a CV.
 */
@Embeddable
@Getter
@Setter
public class CvEducation {

    @Column(name = "school")
    private String school;

    @Column(name = "field")
    private String field;

    @Column(name = "degree")
    private String degree;
}