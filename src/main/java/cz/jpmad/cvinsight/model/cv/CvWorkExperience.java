package cz.jpmad.cvinsight.model.cv;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Embeddable representation of work experience contained in a CV.
 */
@Embeddable
@Getter
@Setter
public class CvWorkExperience {

    @Column(name = "position")
    private String position;

    @Column(name = "company")
    private String company;

    @Column(name = "date_from")
    private String from;

    @Column(name = "date_to")
    private String to;
}