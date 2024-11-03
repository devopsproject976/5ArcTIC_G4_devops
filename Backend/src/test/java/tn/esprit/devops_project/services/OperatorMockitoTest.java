package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.DTO.OperatorRatingStatistics;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperatorMockitoTest {

    @InjectMocks
    private OperatorServiceImpl operatorService;

    @Mock
    private OperatorRepository operatorRepository;

    private Operator operator1;
    private Operator operator2;
    private Operator operator3;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        operator1 = new Operator(1L, "hamouda", "attia", "pass123", 5, 1.5, "Network", null);
        operator2 = new Operator(2L, "naim", "attia", "pass456", 10, 4.0, "Security", null);
        operator3 = new Operator(3L, "hamou", "ha", "pass789", 2, 3.5, "Network", null);
    }

    @Test
    public void testRetrieveOperator() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator1));

        Operator retrievedOperator = operatorService.retrieveOperator(1L);

        assertEquals(operator1, retrievedOperator);
        verify(operatorRepository, times(1)).findById(1L);
    }

    @Test
    public void testRetrieveOperatorsByFirstName() {
        when(operatorRepository.findByFname("hamouda")).thenReturn(Arrays.asList(operator1));

        List<Operator> operators = operatorService.retrieveOperatorsByFirstName("hamouda");

        assertEquals(1, operators.size());
        assertEquals(operator1, operators.get(0));
        verify(operatorRepository, times(1)).findByFname("hamouda");
    }

    @Test
    public void testFindHighestRatedOperator() {
        // Arrange
        when(operatorRepository.findAll()).thenReturn(Arrays.asList(operator1, operator2, operator3));

        // Act
        Optional<Operator> highestRatedOperator = operatorService.findHighestRatedOperator();

        // Assert
        assertTrue(highestRatedOperator.isPresent());
        assertEquals(operator2, highestRatedOperator.get()); // operator2 has the highest rating of 4.0
    }

    @Test
    public void testFindByExperience() {
        when(operatorRepository.findAll()).thenReturn(Arrays.asList(operator1, operator2, operator3));

        List<Operator> experiencedOperators = operatorService.findByExperience(5);

        assertEquals(2, experiencedOperators.size());
        verify(operatorRepository, times(1)).findAll();
    }

    @Test
    public void testFindBySpecialization() {
        when(operatorRepository.findAll()).thenReturn(Arrays.asList(operator1, operator2, operator3));

        List<Operator> networkSpecialists = operatorService.findBySpecialization("Network");

        assertEquals(2, networkSpecialists.size());
        verify(operatorRepository, times(1)).findAll();
    }

    @Test
    public void testCountOperatorsBySpecialization() {
        when(operatorRepository.findAll()).thenReturn(Arrays.asList(operator1, operator2, operator3));

        Map<String, Long> specializationCount = operatorService.countOperatorsBySpecialization();

        Map<String, Long> expectedCount = new HashMap<>();
        expectedCount.put("Network", 2L);
        expectedCount.put("Security", 1L);

        assertEquals(expectedCount, specializationCount);
        verify(operatorRepository, times(1)).findAll();
    }

    @Test
    public void testGetOperatorRatingStatistics() {
        // Arrange
        List<Operator> operators = Arrays.asList(operator1, operator2, operator3);
        when(operatorRepository.findAll()).thenReturn(operators);

        // Act
        OperatorRatingStatistics statistics = operatorService.getOperatorRatingStatistics();

        // Assert
        assertEquals(3.0, statistics.getAverageRating(), 0.01); // Adjust average based on current operators
        assertEquals(1, statistics.getCount1to2()); // Expecting 1 operator in range [1.0, 2.0)
        assertEquals(0, statistics.getCount2to3()); // Corrected to expect 0 for range [2.0, 3.0)
        assertEquals(1, statistics.getCount3to4()); // Expecting 1 operator in range [3.0, 4.0)
        assertEquals(1, statistics.getCount4to5()); // Expecting 1 operator in range [4.0, 5.0)
    }



}


