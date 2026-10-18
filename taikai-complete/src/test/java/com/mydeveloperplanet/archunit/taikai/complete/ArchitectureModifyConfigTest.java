package com.mydeveloperplanet.archunit.taikai.complete;

import static com.enofex.taikai.java.ImportPatterns.lombok;
import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;

import java.util.List;

import com.enofex.taikai.ClassCriteria;
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
                        .noUsageOfSystemOutOrErr()
                        .fieldsShouldNotBePublic()
                        .finalClassesShouldNotHaveProtectedMembers()
                        .methodsShouldNotDeclareGenericExceptions()
                        .utilityClassesShouldBeFinalAndHavePrivateConstructor()
                        .serialVersionUIDFieldsShouldBeStaticFinalLong()
                        .classesShouldImplementHashCodeAndEquals()
                        .imports(imports -> imports
                                .shouldHaveNoCycles()
                                .shouldNotImport(lombok()))
                        .naming(naming -> naming
                                .packagesShouldMatchDefault()
                                .classesShouldNotMatch(".*Impl")
                                .fieldsShouldNotMatch(".*(List|Set|Map)$")
                                .constantsShouldFollowConventions()
                                .enumConstantsShouldFollowConventions()
                                .interfacesShouldNotHavePrefixI()))
                .logging(logging -> logging
                        .loggersShouldFollowConventions(Logger.class, "logger", List.of(PRIVATE, FINAL)))
                .test(test -> test
                        .junit(junit5 -> junit5
                                .classesShouldBePackagePrivate(ClassCriteria.nameMatching(".*Test"))
                                .classesShouldNotBeAnnotatedWithDisabled()
                                .methodsShouldBePackagePrivate()
                                .methodsShouldNotBeAnnotatedWithDisabled()
                                .methodsShouldNotDeclareExceptions()
                                .methodsShouldContainAssertionsOrVerifications()))
                .spring(spring -> spring
                        .noAutowiredFields()
                        .noSelfInvocationOfProxiedMethods()
                        .boot(boot -> boot
                                .applicationClassShouldResideInPackage(BASE_PACKAGE))
                        .controllers(controllers -> controllers
                                .shouldBeAnnotatedWithRestController()
                                .shouldBePackagePrivate()
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
                                .shouldNotDependOnControllers())
                        .transactional(transactional -> transactional
                                .methodsShouldBePublic()
                                .shouldNotBeSelfInvoked()
                                .shouldNotBeUsedInControllers()))
                .build();

        Taikai customConfig = defaultConfig.toBuilder()
                .excludeClasses(ClassCriteria.nameMatching(".*(openapi|jooq).*$")) // exclude generated code
                .java(java -> java
                        // Spring Configuration rules
                        .classesShouldResideInPackage(ClassCriteria.nameMatching(".*Config"), BASE_PACKAGE + ".config")
                        .classesShouldNotBeAnnotatedWith(ClassCriteria.annotatedWith(ConfigurationProperties.class), Configuration.class)
                        .classesShouldNotBeAnnotatedWith(ClassCriteria.annotatedWith(ConfigurationProperties.class), EnableConfigurationProperties.class)
                        .classesShouldBeAnnotatedWith(ClassCriteria.nameMatching(".*Application"), ConfigurationPropertiesScan.class)
                        .classesShouldBeRecords(ClassCriteria.annotatedWith(ConfigurationProperties.class))
                        // end Spring Configuration rules
                        .imports(imports -> imports
                                .shouldNotImport(lombok())))
                .build();
        customConfig.check();
    }

}
