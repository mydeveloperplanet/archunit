package com.mydeveloperplanet.archunit.taikai.springconfig;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

class ArchitectureSpringConfigTest {

    private static final String BASE_PACKAGE = ArchitectureSpringConfigTest.class.getPackageName();
    
    @Test
    void enforceSpringBootConfiguration() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .java(java -> java
                        .classesShouldResideInPackage(".*Config", BASE_PACKAGE + ".config")
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, Configuration.class)
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, EnableConfigurationProperties.class)
                        .classesShouldBeAnnotatedWith(".*Application", ConfigurationPropertiesScan.class)
                        .classesAnnotatedWithShouldBeRecords(ConfigurationProperties.class))

                .build()
                .check();
    }

}
