package tn.esprit.devops_project.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.repositories.CustomerRepository;
import tn.esprit.devops_project.services.Iservices.ICustomerService;

import java.util.List;
import java.util.NoSuchElementException;
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
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        return customerRepository.save(customer);
    }


    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new NoSuchElementException("Customer not found");
        }
        customerRepository.deleteById(id);
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
