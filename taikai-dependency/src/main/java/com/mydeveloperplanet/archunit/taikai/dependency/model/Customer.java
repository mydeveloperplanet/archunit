package com.mydeveloperplanet.archunit.taikai.dependency.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;

}
