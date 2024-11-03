package tn.esprit.devops_project.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OperatorTest {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorServiceImpl operatorService;

    private Long operatorId1;
    private Long operatorId2;

    @BeforeEach
    public void setUp() {
        // Create and save Operators
        Operator operator1 = new Operator(1L, "hamouda", "attia", "password", 5, 4.5, "Network", null);
        Operator operator2 = new Operator(2L, "naim", "attia", "password", 10, 3.0, "Security", null);
        operatorRepository.saveAll(Arrays.asList(operator1, operator2));

        // Store the IDs of the operators for testing
        operatorId1 = operator1.getIdOperator();
        operatorId2 = operator2.getIdOperator();
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        operatorRepository.deleteAll();
    }

    @Test
    public void testRetrieveAllOperators() {
        // Act
        List<Operator> operators = operatorService.retrieveAllOperators();

        // Assert
        assertEquals(2, operators.size());
    }

    @Test
    public void testAddOperator() {
        // Arrange
        Operator newOperator = new Operator(null, "hamou", "ha", "password", 7, 4.0, "Network", null);

        // Act
        Operator savedOperator = operatorService.addOperator(newOperator);

        // Assert
        assertNotNull(savedOperator);
        assertEquals(newOperator.getFname(), savedOperator.getFname());
    }

    @Test
    public void testRetrieveOperatorsByFirstName() {
        // Act
        List<Operator> operators = operatorService.retrieveOperatorsByFirstName("hamouda");

        // Assert
        assertEquals(1, operators.size());
        assertEquals("hamouda", operators.get(0).getFname());
    }

    @Test
    public void testFindHighestRatedOperator() {
        // Act
        Operator highestRatedOperator = operatorService.findHighestRatedOperator().orElse(null);

        // Assert
        assertNotNull(highestRatedOperator);
        assertEquals("hamouda", highestRatedOperator.getFname());
        assertEquals(4.5, highestRatedOperator.getRating());
    }

    @Test
    public void testCountOperatorsBySpecialization() {
        // Act
        Map<String, Long> specializationCount = operatorService.countOperatorsBySpecialization();

        // Assert
        Map<String, Long> expectedCount = new HashMap<>();
        expectedCount.put("Network", 1L);
        expectedCount.put("Security", 1L);

        assertEquals(expectedCount, specializationCount);
    }

    @Test
    public void testFindByExperience() {
        // Act
        List<Operator> experiencedOperators = operatorService.findByExperience(5);

        // Assert
        assertEquals(2, experiencedOperators.size());
        assertTrue(experiencedOperators.stream().anyMatch(o -> o.getFname().equals("hamouda")));
        assertTrue(experiencedOperators.stream().anyMatch(o -> o.getFname().equals("naim")));
    }

    @Test
    public void testFindBySpecialization() {
        // Act
        List<Operator> networkOperators = operatorService.findBySpecialization("Network");
        List<Operator> securityOperators = operatorService.findBySpecialization("Security");

        // Assert for Network specialization
        assertEquals(1, networkOperators.size());
        assertEquals("hamouda", networkOperators.get(0).getFname());

        // Assert for Security specialization
        assertEquals(1, securityOperators.size());
        assertEquals("naim", securityOperators.get(0).getFname());
    }
}
