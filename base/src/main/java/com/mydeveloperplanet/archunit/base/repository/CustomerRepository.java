package com.mydeveloperplanet.archunit.base.repository;

import java.util.List;
import java.util.Optional;

import com.mydeveloperplanet.archunit.base.model.Customer;
import com.mydeveloperplanet.archunit.base.jooq.tables.Customers;
import com.mydeveloperplanet.archunit.base.jooq.tables.records.CustomersRecord;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    @Autowired
    private DSLContext dslContext;

    public List<Customer> getAllCustomers() {
        return dslContext.selectFrom(Customers.CUSTOMERS)
                .fetchInto(CustomersRecord.class)
                .stream()
                .map(this::convertToCustomer)
                .toList();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return dslContext.selectFrom(Customers.CUSTOMERS)
                .where(Customers.CUSTOMERS.ID.eq(id))
                .fetchOptionalInto(CustomersRecord.class)
                .map(this::convertToCustomer);
    }

    public Customer createCustomer(Customer customer) {
        CustomersRecord customerRecord = dslContext.insertInto(Customers.CUSTOMERS,
                        Customers.CUSTOMERS.FIRST_NAME,
                        Customers.CUSTOMERS.LAST_NAME)
                .values(customer.getFirstName(), customer.getLastName())
                .returning()
                .fetchOne();
        return convertToCustomer(customerRecord);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        boolean exists = dslContext.fetchExists(dslContext.selectFrom(Customers.CUSTOMERS));
        if (exists) {
            dslContext.update(Customers.CUSTOMERS)
                    .set(Customers.CUSTOMERS.FIRST_NAME, customerDetails.getFirstName())
                    .set(Customers.CUSTOMERS.LAST_NAME, customerDetails.getLastName())
                    .where(Customers.CUSTOMERS.ID.eq(id))
                    .returning()
                    .fetchOne();
            return customerDetails;
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public void deleteCustomer(Long id) {
        boolean exists = dslContext.fetchExists(dslContext.selectFrom(Customers.CUSTOMERS));
        if (exists) {
            dslContext.deleteFrom(Customers.CUSTOMERS)
                    .where(Customers.CUSTOMERS.ID.eq(id))
                    .execute();
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    private Customer convertToCustomer(CustomersRecord customerRecord) {
        return new Customer(
                customerRecord.getId(),
                customerRecord.getFirstName(),
                customerRecord.getLastName()
        );
    }
}
