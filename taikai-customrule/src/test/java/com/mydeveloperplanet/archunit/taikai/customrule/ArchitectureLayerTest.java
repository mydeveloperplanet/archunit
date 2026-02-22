package com.mydeveloperplanet.archunit.taikai.customrule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.enofex.taikai.Taikai;
import com.enofex.taikai.TaikaiRule;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Test;

class ArchitectureLayerTest {

    private static final String BASE_PACKAGE = ArchitectureLayerTest.class.getPackageName();

    static final ArchRule layeredArchitectureRule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..repository..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");

    @Test
    void checkArchitecture() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .addRule(TaikaiRule.of(layeredArchitectureRule))

                .build()
                .check();
    }

}
