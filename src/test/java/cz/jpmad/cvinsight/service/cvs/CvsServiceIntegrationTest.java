package cz.jpmad.cvinsight.service.cvs;

import cz.jpmad.cvinsight.api.cvs.dto.CvsCreateRequest;
import cz.jpmad.cvinsight.repository.CvRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
class CvsServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("cvinsight")
                    .withUsername("cv")
                    .withPassword("cv");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );

        registry.add("spring.flyway.enabled", () -> false);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private CvsService cvsService;

    @Autowired
    private CvRepository cvRepository;

    @BeforeEach
    void setUp() {
        cvRepository.deleteAll();
    }

    @Test
    void createShouldPersistCvInDatabase() {
        CvsCreateRequest request = createRequest();

        var result = cvsService.create(request);

        assertNotNull(result.id());

        assertTrue(
                cvRepository.existsById(result.id())
        );

        assertEquals("Jan", result.firstName());
        assertEquals("Novak", result.lastName());
    }

    @Test
    void findAllShouldReturnStoredCvs() {
        cvsService.create(createRequest());

        var results = cvsService.findAll();

        assertEquals(1, results.size());
        assertEquals("Jan", results.get(0).firstName());
    }

    @Test
    void getByIdShouldReturnStoredCv() {
        var created = cvsService.create(createRequest());

        var result = cvsService.getById(created.id());

        assertEquals(created.id(), result.id());
        assertEquals("Jan", result.firstName());
        assertEquals("Novak", result.lastName());
    }

    @Test
    void getByIdShouldThrowWhenCvDoesNotExist() {
        UUID id = UUID.randomUUID();

        assertThrows(
                NoSuchElementException.class,
                () -> cvsService.getById(id)
        );
    }

    @Test
    void deleteShouldRemoveCvFromDatabase() {
        var created = cvsService.create(createRequest());

        cvsService.delete(created.id());

        assertFalse(
                cvRepository.existsById(created.id())
        );
    }

    @Test
    void deleteShouldThrowWhenCvDoesNotExist() {
        UUID id = UUID.randomUUID();

        assertThrows(
                NoSuchElementException.class,
                () -> cvsService.delete(id)
        );
    }

    private CvsCreateRequest createRequest() {
        CvsCreateRequest request = mock(CvsCreateRequest.class);

        when(request.firstName()).thenReturn("Jan");
        when(request.lastName()).thenReturn("Novak");
        when(request.email()).thenReturn("jan.novak@example.com");
        when(request.phone()).thenReturn("+420777123456");
        when(request.city()).thenReturn("Praha");

        when(request.languages()).thenReturn(List.of());
        when(request.drivingLicenses()).thenReturn(List.of("B"));
        when(request.workExperience()).thenReturn(List.of());
        when(request.education()).thenReturn(List.of());

        return request;
    }
}