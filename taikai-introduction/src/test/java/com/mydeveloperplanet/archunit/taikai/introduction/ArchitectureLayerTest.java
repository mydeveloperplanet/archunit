package com.mydeveloperplanet.archunit.taikai.introduction;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;

class ArchitectureLayerTest {

    private static final String BASE_PACKAGE = ArchitectureLayerTest.class.getPackageName();

    @Test
    void layeredArchitecture() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .java(java -> java
                        .imports(imports -> imports
                                .shouldNotImport(".*Controller", ".*Repository")))

                .spring(spring -> spring
                        .controllers(controllers -> controllers
                                .shouldBeAnnotatedWithRestController()
                                .namesShouldEndWithController()
                                .shouldNotDependOnOtherControllers())
                        .services(services -> services
                                .shouldBeAnnotatedWithService()
                                .namesShouldEndWithService()
                                .shouldNotDependOnControllers())
                        .repositories(repositories -> repositories
                                .shouldBeAnnotatedWithRepository()
                                .namesShouldEndWithRepository()
                                .shouldNotDependOnServices()))

                .build()
                .check();
    }

}
