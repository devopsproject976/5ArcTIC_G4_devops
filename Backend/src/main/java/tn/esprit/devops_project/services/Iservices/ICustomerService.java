package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> retrieveAllCustomers();

    Customer addCustomer(Customer customer);

    void deleteCustomer(Long id);

    Customer updateCustomer(Customer customer);

    Customer retrieveCustomer(Long id);
}
