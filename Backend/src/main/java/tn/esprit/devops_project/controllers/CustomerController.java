package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Customer;
import tn.esprit.devops_project.services.Iservices.ICustomerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    ICustomerService customerService;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.retrieveAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer retrieveCustomer(@PathVariable Long customerId) {
        return customerService.retrieveCustomer(customerId);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @DeleteMapping("/{customerId}")
    public void removeCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping
    public Customer modifyCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }
}
