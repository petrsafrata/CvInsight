package cz.jpmad.cvinsight.repository;

import cz.jpmad.cvinsight.model.cv.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for stored CV entities.
 */
@Repository
public interface CvRepository extends JpaRepository<Cv, UUID> {
}