    package tn.esprit.devops_project.services;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.transaction.annotation.Transactional;
    import tn.esprit.devops_project.entities.Customer;
    import tn.esprit.devops_project.repositories.CustomerRepository;

    import java.util.List;
    import java.util.NoSuchElementException;
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Test
        void testAddCustomerWithDuplicateEmail() {
            Customer duplicateCustomer = new Customer();
            duplicateCustomer.setName("Duplicate Customer");
            duplicateCustomer.setEmail("test@example.com"); // Same email as the initial customer
            duplicateCustomer.setPhone("1112223333");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                customerService.addCustomer(duplicateCustomer);
            });

            String expectedMessage = "Email already exists";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        ///////////////////////////////////////////////////
        @Test
        void testDeleteNonExistingCustomer() {
            Long nonExistingId = 999L; // Assuming this ID does not exist

            Exception exception = assertThrows(NoSuchElementException.class, () -> {
                customerService.deleteCustomer(nonExistingId);
            });

            String expectedMessage = "Customer not found";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));
        }
        ///////////////////////////////////////////////////

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
