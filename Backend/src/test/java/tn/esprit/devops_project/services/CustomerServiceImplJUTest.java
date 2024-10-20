package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomerServiceImplJUTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");

        customer = customerRepository.save(customer);
    }

    @Test
    void testAddCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("New Customer");
        newCustomer.setEmail("new@example.com");
        newCustomer.setPhone("0987654321");

        Customer savedCustomer = customerService.addCustomer(newCustomer);

        assertNotNull(savedCustomer);
        assertEquals("New Customer", savedCustomer.getName());
        assertEquals("new@example.com", savedCustomer.getEmail());
    }

    @Test
    void testUpdateCustomer() {
        customer.setName("Updated Customer");
        customer.setEmail("updated@example.com");

        Customer updatedCustomer = customerService.updateCustomer(customer.getIdCustomer(), customer);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Customer", updatedCustomer.getName());
        assertEquals("updated@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testRetrieveCustomer() {
        Customer foundCustomer = customerService.retrieveCustomer(customer.getIdCustomer());

        assertNotNull(foundCustomer);
        assertEquals(customer.getName(), foundCustomer.getName());
    }

    @Test
    void testRetrieveAllCustomers() {
        List<Customer> customers = customerService.retrieveAllCustomers();

        assertNotNull(customers);
        assertFalse(customers.isEmpty());
    }

    @Test
    void testDeleteCustomer() {
        customerService.deleteCustomer(customer.getIdCustomer());

        assertFalse(customerRepository.findById(customer.getIdCustomer()).isPresent());
    }

    @Test
    void testFindCustomerByEmail() {
        Optional<Customer> foundCustomer = customerService.findCustomerByEmail("test@example.com");

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getEmail(), foundCustomer.get().getEmail());
    }

    @Test
    void testFindCustomersByPhone() {
        List<Customer> foundCustomers = customerService.findCustomersByPhone("1234567890");

        assertNotNull(foundCustomers);
        assertFalse(foundCustomers.isEmpty());
        assertEquals(customer.getPhone(), foundCustomers.get(0).getPhone());
    }

    @Test
    void testFindCustomersByName() {
        List<Customer> foundCustomers = customerService.findCustomersByName("Test");

        assertNotNull(foundCustomers);
        assertFalse(foundCustomers.isEmpty());
        assertEquals(customer.getName(), foundCustomers.get(0).getName());
    }
}
