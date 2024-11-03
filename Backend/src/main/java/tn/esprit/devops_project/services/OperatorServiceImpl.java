package tn.esprit.devops_project.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.DTO.OperatorRatingStatistics;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.Iservices.IOperatorService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperatorServiceImpl implements IOperatorService {

	OperatorRepository operatorRepository;

	@Override
	public List<Operator> retrieveAllOperators() {
		return (List<Operator>) operatorRepository.findAll();
	}

	@Override
	public Operator addOperator(Operator operator) {
		return operatorRepository.save(operator);
	}

	@Override
	public void deleteOperator(Long id) {
		operatorRepository.deleteById(id);
	}

	@Override
	public Operator updateOperator(Operator operator) {
		return operatorRepository.save(operator);
	}

	@Override
	public Operator retrieveOperator(Long id) {
		return operatorRepository.findById(id).orElseThrow(() -> new NullPointerException("Operator not found"));
	}

	@Override
	public List<Operator> retrieveOperatorsByFirstName(String fname) {
		return operatorRepository.findByFname(fname);
	}

	@Override
	public Optional<Operator> findHighestRatedOperator() {
		return ((List<Operator>) operatorRepository.findAll())
				.stream()
				.filter(operator -> operator.getRating() != null)
				.max(Comparator.comparingDouble(Operator::getRating));
	}


	@Override
	public List<Operator> findByExperience(int minExperience) {
		return ((List<Operator>) operatorRepository.findAll())
				.stream()
				.filter(operator -> operator.getExperienceYears() != null && operator.getExperienceYears() >= minExperience)
				.collect(Collectors.toList());
	}

	@Override
	public List<Operator> findBySpecialization(String specialization) {
		return ((List<Operator>) operatorRepository.findAll())
				.stream()
				.filter(operator -> specialization.equalsIgnoreCase(operator.getSpecialization()))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, Long> countOperatorsBySpecialization() {
		return ((List<Operator>) operatorRepository.findAll())
				.stream()
				.filter(operator -> operator.getSpecialization() != null)
				.collect(Collectors.groupingBy(Operator::getSpecialization, Collectors.counting()));
	}
	@Override
	public OperatorRatingStatistics getOperatorRatingStatistics() {
		List<Operator> operators = (List<Operator>) operatorRepository.findAll();

		// Calculate the average rating
		double averageRating = operators.stream()
				.filter(operator -> operator.getRating() != null) // Exclude null ratings
				.mapToDouble(Operator::getRating)
				.average()
				.orElse(0.0); // Default to 0.0 if no ratings are available

		// Count ratings in specified ranges
		long count1to2 = operators.stream().filter(op -> op.getRating() != null && op.getRating() >= 1.0 && op.getRating() < 2.0).count();
		long count2to3 = operators.stream().filter(op -> op.getRating() != null && op.getRating() >= 2.0 && op.getRating() < 3.0).count();
		long count3to4 = operators.stream().filter(op -> op.getRating() != null && op.getRating() >= 3.0 && op.getRating() < 4.0).count();
		long count4to5 = operators.stream().filter(op -> op.getRating() != null && op.getRating() >= 4.0 && op.getRating() <= 5.0).count();

		// Debugging output
		System.out.println("Operators: " + operators);
		System.out.println("Average Rating: " + averageRating);
		System.out.println("Count 1-2: " + count1to2);
		System.out.println("Count 2-3: " + count2to3);
		System.out.println("Count 3-4: " + count3to4);
		System.out.println("Count 4-5: " + count4to5);

		// Return the statistics as an OperatorRatingStatistics object
		return new OperatorRatingStatistics(averageRating, count1to2, count2to3, count3to4, count4to5);
	}

}

