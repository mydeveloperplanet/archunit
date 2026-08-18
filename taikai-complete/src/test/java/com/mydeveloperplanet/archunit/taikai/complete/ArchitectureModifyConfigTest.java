package com.mydeveloperplanet.archunit.taikai.complete;

import static com.enofex.taikai.java.ImportPatterns.lombok;
import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;

import java.util.List;

import com.enofex.taikai.Taikai;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

class ArchitectureModifyConfigTest {

    private static final String BASE_PACKAGE = ArchitectureModifyConfigTest.class.getPackageName();

    @Test
    void testShouldFulfillConstraints() {
        Taikai defaultConfig = Taikai.builder()
                .namespace(BASE_PACKAGE)
                .java(java -> java
                        .noUsageOfDeprecatedAPIs()
                        .methodsShouldNotDeclareGenericExceptions()
                        .utilityClassesShouldBeFinalAndHavePrivateConstructor()
                        .imports(imports -> imports
                                .shouldNotImport(".*Controller", ".*Repository") // Controllers should not depend on Repositories
                                .shouldHaveNoCycles())
                        .naming(naming -> naming
                                .classesShouldNotMatch(".*Impl")
                                .fieldsShouldNotMatch(".*(List|Set|Map)$")
                                .constantsShouldFollowConventions()
                                .interfacesShouldNotHavePrefixI()))
                .logging(logging -> logging
                        .loggersShouldFollowConventions(Logger.class, "logger", List.of(PRIVATE, FINAL)))
                .test(test -> test
                        .junit(junit5 -> junit5
                                .classesShouldNotBeAnnotatedWithDisabled()
                                .methodsShouldNotBeAnnotatedWithDisabled()))
                .spring(spring -> spring
                        .noAutowiredFields()
                        .boot(boot -> boot
                                .applicationClassShouldResideInPackage(BASE_PACKAGE))
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
                .build();

        Taikai customConfig = defaultConfig.toBuilder()
                .excludeClasses("^com\\.mydeveloperplanet\\.archunit\\.taikai\\.complete\\.(openapi|jooq).*$") // exclude generated code
                .java(java -> java
                        // Spring Configuration rules
                        .classesShouldResideInPackage(".*Config", BASE_PACKAGE + ".config")
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, Configuration.class)
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, EnableConfigurationProperties.class)
                        .classesShouldBeAnnotatedWith(".*Application", ConfigurationPropertiesScan.class)
                        .classesAnnotatedWithShouldBeRecords(ConfigurationProperties.class)
                        // end Spring Configuration rules
                        .imports(imports -> imports
                                .shouldNotImport(lombok())))
                .build();
        customConfig.check();
    }

}
