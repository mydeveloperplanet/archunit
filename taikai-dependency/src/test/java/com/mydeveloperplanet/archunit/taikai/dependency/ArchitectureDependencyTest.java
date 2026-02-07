package com.mydeveloperplanet.archunit.taikai.dependency;

import static com.enofex.taikai.java.ImportPatterns.lombok;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;

class ArchitectureDependencyTest {

    
    @Test
    void doNotAllowLombok() {
        Taikai.builder()
                .namespace("com.mydeveloperplanet.archunit.taikai.dependency")

                .java(java -> java
                        .imports(imports -> imports
                                .shouldNotImport(lombok())))

                .build()
                .check();
    }

}
