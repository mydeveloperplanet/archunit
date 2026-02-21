package com.mydeveloperplanet.archunit.taikai.dependency;

import static com.enofex.taikai.java.ImportPatterns.lombok;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;

class ArchitectureDependencyTest {

    private static final String BASE_PACKAGE = ArchitectureDependencyTest.class.getPackageName();
    
    @Test
    void doNotAllowLombok() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .java(java -> java
                        .imports(imports -> imports
                                .shouldNotImport(lombok())))

                .build()
                .check();
    }

}
