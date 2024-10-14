package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devops_project.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
