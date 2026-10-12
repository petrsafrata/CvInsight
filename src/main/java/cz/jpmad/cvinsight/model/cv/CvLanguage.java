package cz.jpmad.cvinsight.model.cv;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Embeddable representation of a language contained in a CV.
 */
@Embeddable
@Getter
@Setter
public class CvLanguage {

    @Column(name = "language")
    private String language;

    @Column(name = "level")
    private String level;
}