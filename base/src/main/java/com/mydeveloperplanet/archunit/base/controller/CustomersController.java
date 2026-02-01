package com.mydeveloperplanet.archunit.base.controller;

import java.util.List;
import java.util.Optional;

import com.mydeveloperplanet.archunit.base.openapi.model.Customer;
import com.mydeveloperplanet.archunit.base.openapi.api.CustomersApi;
import com.mydeveloperplanet.archunit.base.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomersController implements CustomersApi {

    private CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<List<Customer>> customersGet() {
        List<com.mydeveloperplanet.archunit.base.model.Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(convertToOpenAPIModel(customers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> customersPost(@RequestBody Customer openAPICustomer) {
        com.mydeveloperplanet.archunit.base.model.Customer customer = convertToDomainModel(openAPICustomer);
        customerService.createCustomer(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Customer> customersIdGet(@PathVariable Long id) {
        Optional<com.mydeveloperplanet.archunit.base.model.Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isPresent()) {
            return new ResponseEntity<>(convertToOpenAPIModel(customerOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> customersIdPut(@PathVariable Long id, @RequestBody Customer openAPICustomer) {
        com.mydeveloperplanet.archunit.base.model.Customer customerDetails = convertToDomainModel(openAPICustomer);
        customerService.updateCustomer(id, customerDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> customersIdDelete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<Customer> convertToOpenAPIModel(List<com.mydeveloperplanet.archunit.base.model.Customer> domainCustomers) {
        return domainCustomers.stream()
                .map(this::convertToOpenAPIModel)
                .toList();
    }

    private Customer convertToOpenAPIModel(com.mydeveloperplanet.archunit.base.model.Customer customer) {
        Customer openAPICustomer =
                new Customer();
        openAPICustomer.setId(customer.getId());
        openAPICustomer.setFirstName(customer.getFirstName());
        openAPICustomer.setLastName(customer.getLastName());
        return openAPICustomer;
    }

    private com.mydeveloperplanet.archunit.base.model.Customer convertToDomainModel(Customer openAPICustomer) {
        return new com.mydeveloperplanet.archunit.base.model.Customer(
                openAPICustomer.getId(),
                openAPICustomer.getFirstName(),
                openAPICustomer.getLastName()
        );
    }
}
