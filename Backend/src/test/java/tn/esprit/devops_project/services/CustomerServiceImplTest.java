package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.repositories.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setIdCustomer(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.addCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("Test Customer", savedCustomer.getName());
        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    void testRetrieveCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.retrieveCustomer(1L);

        assertNotNull(foundCustomer);
        assertEquals(customer.getName(), foundCustomer.getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        List<Customer> customers = customerService.retrieveAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testDeleteNonExistingCustomer() {
        Long nonExistingId = 999L;

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            customerService.deleteCustomer(nonExistingId);
        });

        String expectedMessage = "Customer not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void testFindCustomerByEmail() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        Optional<Customer> foundCustomer = customerService.findCustomerByEmail("test@example.com");

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getEmail(), foundCustomer.get().getEmail());
        verify(customerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindCustomerByPhone() {
        when(customerRepository.findByPhone("1234567890")).thenReturn(Arrays.asList(customer));

        List<Customer> foundCustomers = customerService.findCustomersByPhone("1234567890");

        assertNotNull(foundCustomers);
        assertEquals(1, foundCustomers.size());
        verify(customerRepository, times(1)).findByPhone("1234567890");
    }
}
