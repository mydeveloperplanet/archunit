package com.mydeveloperplanet.archunit.taikai.dependency;

import static com.enofex.taikai.java.ImportPatterns.lombok;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.enofex.taikai.Taikai;
import com.enofex.taikai.TaikaiRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import org.junit.jupiter.api.Test;

class ArchitectureDependencyTest {

    @Test
    void disallowLombok() {
        Taikai.builder()
                .namespace("com.mydeveloperplanet.archunit.taikai.dependency")

                .java(java -> java
                        .imports(imports -> imports
                                .shouldNotImport(lombok())))

                .build()
                .check();
    }

}
