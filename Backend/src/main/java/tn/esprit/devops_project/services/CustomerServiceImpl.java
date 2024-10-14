package tn.esprit.devops_project.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.repositories.CustomerRepository;
import tn.esprit.devops_project.services.Iservices.ICustomerService;

import java.util.List;
import java.util.Optional;

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

    public Customer updateCustomer(Long idCustomer, Customer customer) {

        Customer existingCustomer = customerRepository.findById(idCustomer)
                .orElseThrow(() -> new NullPointerException("Customer not found"));
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        return customerRepository.save(existingCustomer);
    }


    @Override
    public Customer retrieveCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NullPointerException("Customer not found"));
    }

    // New methods added for searching
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public List<Customer> findCustomersByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public List<Customer> findCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
}
