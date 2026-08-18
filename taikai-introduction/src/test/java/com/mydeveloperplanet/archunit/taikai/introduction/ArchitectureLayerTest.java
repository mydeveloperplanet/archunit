package com.mydeveloperplanet.archunit.taikai.introduction;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;

class ArchitectureLayerTest {

    private static final String BASE_PACKAGE = ArchitectureLayerTest.class.getPackageName();

    @Test
    void layeredArchitecture() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)

                .spring(spring -> spring
                        .controllers(controllers -> controllers
                                .shouldBeAnnotatedWithRestController()
                                .namesShouldEndWithController()
                                .shouldNotDependOnOtherControllers()
                                .shouldNotDependOnRepositories())
                        .services(services -> services
                                .shouldBeAnnotatedWithService()
                                .namesShouldEndWithService()
                                .shouldNotDependOnControllers())
                        .repositories(repositories -> repositories
                                .shouldBeAnnotatedWithRepository()
                                .namesShouldEndWithRepository()
                                .shouldNotDependOnServices()
                                .shouldNotDependOnControllers()))

                .build()
                .check();
    }

}
