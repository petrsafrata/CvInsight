package cz.jpmad.cvinsight.repository;

import cz.jpmad.cvinsight.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing User entities in the database using Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
