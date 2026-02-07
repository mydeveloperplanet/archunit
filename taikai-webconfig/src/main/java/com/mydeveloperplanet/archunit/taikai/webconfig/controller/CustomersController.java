package com.mydeveloperplanet.archunit.taikai.webconfig.controller;

import java.util.List;
import java.util.Optional;

import com.mydeveloperplanet.archunit.taikai.webconfig.model.Customer;
import com.mydeveloperplanet.archunit.taikai.webconfig.openapi.api.CustomersApi;
import com.mydeveloperplanet.archunit.taikai.webconfig.repository.CustomerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomersController implements CustomersApi {

    private CustomerRepository customerRepository;

    public CustomersController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<List<com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer>> customersGet() {
        List<Customer> customers = customerRepository.getAllCustomers();
        return new ResponseEntity<>(convertToOpenAPIModel(customers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> customersPost(@RequestBody com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer openAPICustomer) {
        Customer customer = convertToDomainModel(openAPICustomer);
        customerRepository.createCustomer(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer> customersIdGet(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerRepository.getCustomerById(id);
        if (customerOptional.isPresent()) {
            return new ResponseEntity<>(convertToOpenAPIModel(customerOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> customersIdPut(@PathVariable Long id, @RequestBody com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer openAPICustomer) {
        Customer customerDetails = convertToDomainModel(openAPICustomer);
        customerRepository.updateCustomer(id, customerDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> customersIdDelete(@PathVariable Long id) {
        customerRepository.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer> convertToOpenAPIModel(List<Customer> domainCustomers) {
        return domainCustomers.stream()
                .map(this::convertToOpenAPIModel)
                .toList();
    }

    private com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer convertToOpenAPIModel(Customer customer) {
        com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer openAPICustomer =
                new com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer();
        openAPICustomer.setId(customer.getId());
        openAPICustomer.setFirstName(customer.getFirstName());
        openAPICustomer.setLastName(customer.getLastName());
        return openAPICustomer;
    }

    private Customer convertToDomainModel(com.mydeveloperplanet.archunit.taikai.webconfig.openapi.model.Customer openAPICustomer) {
        return new Customer(
                openAPICustomer.getId(),
                openAPICustomer.getFirstName(),
                openAPICustomer.getLastName()
        );
    }
}
