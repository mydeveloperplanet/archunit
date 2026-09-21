package com.mydeveloperplanet.archunit.taikai.complete;

import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;
import static com.enofex.taikai.java.ImportPatterns.lombok;

import java.util.List;

import com.enofex.taikai.Taikai;
import com.enofex.taikai.TaikaiRule;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

class ArchitectureTest {

    private static final String BASE_PACKAGE = ArchitectureTest.class.getPackageName();

    @Test
    void testShouldFulfillConstraints() {
        Taikai.builder()
                .namespace(BASE_PACKAGE)
                .excludeClasses("^com\\.mydeveloperplanet\\.archunit\\.taikai\\.complete\\.(openapi|jooq).*$") // exclude generated code
                .java(java -> java
                        // Spring Configuration rules
                        .classesShouldResideInPackage(".*Config", BASE_PACKAGE + ".config")
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, Configuration.class)
                        .classesAnnotatedWithShouldNotBeAnnotatedWith(ConfigurationProperties.class, EnableConfigurationProperties.class)
                        .classesShouldBeAnnotatedWith(".*Application", ConfigurationPropertiesScan.class)
                        .classesAnnotatedWithShouldBeRecords(ConfigurationProperties.class)
                        // end Spring Configuration rules
                        .noUsageOfDeprecatedAPIs()
                        .methodsShouldNotDeclareGenericExceptions()
                        .utilityClassesShouldBeFinalAndHavePrivateConstructor()
                        .imports(imports -> imports
                                .shouldHaveNoCycles()
                                .shouldNotImport(lombok()))
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
//                        .noAutowiredFields() // 1.
//                        .noAutowiredFields(TaikaiRule.Configuration.of("com.mydeveloperplanet.archunit.taikai.complete.service")) // 2. this fails for the CustomerService, Repository is valid
//                        .noAutowiredFields(TaikaiRule.Configuration.of(List.of("com.mydeveloperplanet.archunit.taikai.complete.service.CustomerService"))) // 3. this works
//                        .noAutowiredFields(TaikaiRule.Configuration.of(List.of("com.mydeveloperplanet.archunit.taikai.complete.*Service"))) // 4. this works
                        .noAutowiredFields(TaikaiRule.Configuration.of(List.of("com.mydeveloperplanet.archunit.taikai.complete.Cus*Service"))) // 5. this does not work, 2 violations found
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
                .build()
                .checkAll();
    }

}
