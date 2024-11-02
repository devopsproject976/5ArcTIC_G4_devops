package tn.esprit.devops_project.services.Iservices;
import tn.esprit.devops_project.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {

    List<Customer> retrieveAllCustomers();
    Customer addCustomer(Customer customer);
    void deleteCustomer(Long id);Customer retrieveCustomer(Long id);




    Optional<Customer> findCustomerByEmail(String email);

    List<Customer> findCustomersByPhone(String phone);

    List<Customer> findCustomersByName(String name);
}
