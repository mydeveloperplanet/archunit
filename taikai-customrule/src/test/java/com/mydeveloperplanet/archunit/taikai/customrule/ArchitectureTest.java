package com.mydeveloperplanet.archunit.taikai.customrule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.enofex.taikai.Taikai;
import com.enofex.taikai.TaikaiRule;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private static final String BASE_PACKAGE = ArchitectureTest.class.getPackageName();

    private static final ArchRule EXIT_METHODS_SHOULD_NOT_BE_CALLED = noClasses()
            .should().callMethod(System.class, "exit", int.class)
            .orShould().callMethod(Runtime.class, "exit", int.class)
            .orShould().callMethod(Runtime.class, "halt", int.class)
            .as("Classes should not call JVM exit methods")
            .because("abruptly terminating the JVM is dangerous in a server application (SonarQube java:S1147)");

    @Test
    void checkArchitecture() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .addRule(TaikaiRule.of(EXIT_METHODS_SHOULD_NOT_BE_CALLED))

                .build()
                .check();
    }

}
