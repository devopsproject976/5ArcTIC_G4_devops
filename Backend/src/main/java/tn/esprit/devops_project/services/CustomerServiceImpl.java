package tn.esprit.devops_project.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.repositories.CustomerRepository;
import tn.esprit.devops_project.services.Iservices.ICustomerService;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements ICustomerService {

    CustomerRepository customerRepository;

    @Override
    public List<Customer> retrieveAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer retrieveCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NullPointerException("Customer not found"));
    }
}
