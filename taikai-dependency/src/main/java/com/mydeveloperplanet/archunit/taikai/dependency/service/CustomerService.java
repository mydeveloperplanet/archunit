package com.mydeveloperplanet.archunit.taikai.dependency.service;

import java.util.List;
import java.util.Optional;

import com.mydeveloperplanet.archunit.taikai.dependency.model.Customer;
import com.mydeveloperplanet.archunit.taikai.dependency.repository.CustomerRepository;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.getCustomerById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.createCustomer(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.updateCustomer(id, customerDetails);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteCustomer(id);
    }
}
