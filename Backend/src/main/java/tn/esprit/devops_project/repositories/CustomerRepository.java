package tn.esprit.devops_project.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devops_project.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Optional<Customer> findByEmail(String email);


    List<Customer> findByPhone(String phone);


    List<Customer> findByNameContainingIgnoreCase(String name);
}
