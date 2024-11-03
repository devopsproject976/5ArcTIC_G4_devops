package tn.esprit.devops_project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.devops_project.entities.Operator;

import java.util.List;

public interface OperatorRepository extends CrudRepository<Operator, Long> {
    List<Operator> findByFname(String fname);


}
