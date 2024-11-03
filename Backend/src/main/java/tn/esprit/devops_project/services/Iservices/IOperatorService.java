package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.DTO.OperatorRatingStatistics;
import tn.esprit.devops_project.entities.Operator;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface IOperatorService {

	List<Operator> retrieveAllOperators();

	Operator addOperator(Operator operator);

	void deleteOperator(Long id);

	Operator updateOperator(Operator operator);

	Operator retrieveOperator(Long id);


	List<Operator> retrieveOperatorsByFirstName(String fname);

	Optional<Operator> findHighestRatedOperator();

	List<Operator> findByExperience(int minExperience);

	List<Operator> findBySpecialization(String specialization);

	Map<String, Long> countOperatorsBySpecialization();


	OperatorRatingStatistics getOperatorRatingStatistics();

}
