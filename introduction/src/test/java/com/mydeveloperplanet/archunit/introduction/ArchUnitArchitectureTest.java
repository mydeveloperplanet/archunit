package com.mydeveloperplanet.archunit.introduction;

import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;
import static com.tngtech.archunit.core.domain.JavaModifier.STATIC;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Disabled;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * Plain ArchUnit counterpart of the Taikai based ArchitectureTest.
 *
 * <p>Every rule below mirrors one Taikai rule, but is expressed with the plain ArchUnit DSL.</p>
 */
@AnalyzeClasses(
        packages = ArchUnitArchitectureTest.BASE_PACKAGE,
        importOptions = ArchUnitArchitectureTest.DoNotIncludeGeneratedCode.class)
class ArchUnitArchitectureTest {

    static final String BASE_PACKAGE = "com.mydeveloperplanet.archunit.introduction";

    /** Excludes the generated OpenAPI and jOOQ sources from the analysis. */
    static final class DoNotIncludeGeneratedCode implements ImportOption {

        @Override
        public boolean includes(com.tngtech.archunit.core.importer.Location location) {
            return !location.matches(java.util.regex.Pattern.compile(
                    ".*/com/mydeveloperplanet/archunit/introduction/(openapi|jooq)/.*"));
        }
    }

    // ---------------------------------------------------------------------
    // Spring Configuration rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule configClassesShouldResideInConfigPackage = classes()
            .that().haveSimpleNameEndingWith("Config")
            .should().resideInAPackage(BASE_PACKAGE + ".config")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule configurationPropertiesShouldNotBeAnnotatedWithConfiguration = noClasses()
            .that().areAnnotatedWith(ConfigurationProperties.class)
            .should().beAnnotatedWith(Configuration.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule configurationPropertiesShouldNotBeAnnotatedWithEnableConfigurationProperties =
            noClasses()
                    .that().areAnnotatedWith(ConfigurationProperties.class)
                    .should().beAnnotatedWith(EnableConfigurationProperties.class)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule applicationClassesShouldBeAnnotatedWithConfigurationPropertiesScan = classes()
            .that().haveSimpleNameEndingWith("Application")
            .should().beAnnotatedWith(ConfigurationPropertiesScan.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule configurationPropertiesShouldBeRecords = classes()
            .that().areAnnotatedWith(ConfigurationProperties.class)
            .should().beRecords()
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // General Java rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule noUsageOfDeprecatedApis = noClasses()
            .should().dependOnClassesThat().areAnnotatedWith(Deprecated.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule methodsShouldNotDeclareGenericExceptions = noMethods()
            .should().declareThrowableOfType(Exception.class)
            .orShould().declareThrowableOfType(RuntimeException.class)
            .orShould().declareThrowableOfType(Throwable.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule utilityClassesShouldBeFinal = classes()
            .that(areUtilityClasses())
            .should().haveModifier(FINAL)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule utilityClassesShouldHavePrivateConstructor = constructors()
            .that().areDeclaredInClassesThat(areUtilityClasses())
            .should().haveModifier(PRIVATE)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noCyclicDependencies = slices()
            .matching(BASE_PACKAGE + ".(*)..")
            .should().beFreeOfCycles();

    @ArchTest
    static final ArchRule shouldNotImportLombok = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage("lombok..")
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // Naming rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule classesShouldNotBeNamedImpl = noClasses()
            .should().haveNameMatching(".*Impl")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule fieldsShouldNotBeNamedAfterTheirCollectionType = noFields()
            .should().haveNameMatching(".*(List|Set|Map)$")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule constantsShouldFollowConventions = fields()
            .that().areStatic().and().areFinal()
            .and(areConstantCandidates())
            .should().haveNameMatching("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule interfacesShouldNotHavePrefixI = noClasses()
            .that().areInterfaces()
            .should().haveNameMatching(".*\\.I[A-Z].*")
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // Logging rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule loggersShouldFollowConventions = fields()
            .that().haveRawType(Logger.class)
            .should().haveName("logger")
            .andShould().haveModifier(PRIVATE)
            .andShould().haveModifier(FINAL)
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // JUnit 5 rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule classesShouldNotBeAnnotatedWithDisabled = noClasses()
            .should().beAnnotatedWith(Disabled.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule methodsShouldNotBeAnnotatedWithDisabled = noMethods()
            .should().beAnnotatedWith(Disabled.class)
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // Spring rules
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule noAutowiredFields = noFields()
            .should().beAnnotatedWith(Autowired.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule applicationClassShouldResideInBasePackage = classes()
            .that().areAnnotatedWith(SpringBootApplication.class)
            .should().resideInAPackage(BASE_PACKAGE)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllersShouldBeAnnotatedWithRestController = classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().beAnnotatedWith(RestController.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllerNamesShouldEndWithController = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllersShouldNotDependOnOtherControllers = noClasses()
            .that().areAnnotatedWith(RestController.class)
            .should().dependOnClassesThat(are(annotatedWithRestControllerButNotSelf()))
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllersShouldNotDependOnRepositories = noClasses()
            .that().areAnnotatedWith(RestController.class)
            .should().dependOnClassesThat().areAnnotatedWith(Repository.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule servicesShouldBeAnnotatedWithService = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().beAnnotatedWith(Service.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule serviceNamesShouldEndWithService = classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule servicesShouldNotDependOnControllers = noClasses()
            .that().areAnnotatedWith(Service.class)
            .should().dependOnClassesThat().areAnnotatedWith(RestController.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoriesShouldBeAnnotatedWithRepository = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().beAnnotatedWith(Repository.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoryNamesShouldEndWithRepository = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith("Repository")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoriesShouldNotDependOnServices = noClasses()
            .that().areAnnotatedWith(Repository.class)
            .should().dependOnClassesThat().areAnnotatedWith(Service.class)
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoriesShouldNotDependOnControllers = noClasses()
            .that().areAnnotatedWith(Repository.class)
            .should().dependOnClassesThat().areAnnotatedWith(RestController.class)
            .allowEmptyShould(true);

    // ---------------------------------------------------------------------
    // Helper predicates and conditions
    // ---------------------------------------------------------------------

    private static DescribedPredicate<JavaClass> areUtilityClasses() {
        return new DescribedPredicate<>("utility classes") {
            @Override
            public boolean test(JavaClass javaClass) {
                return !javaClass.isInterface()
                        && !javaClass.isEnum()
                        && !javaClass.isRecord()
                        && !javaClass.getModifiers().contains(JavaModifier.ABSTRACT)
                        && javaClass.getAnnotations().isEmpty()
                        && !javaClass.getMethods().isEmpty()
                        && javaClass.getMethods().stream()
                        .allMatch(method -> method.getModifiers().contains(STATIC));
            }
        };
    }

    /**
     * Only primitive and {@link String} constants are subject to the naming convention; other
     * static final fields (such as ArchUnit rules or immutable helper objects) are ignored.
     */
    private static DescribedPredicate<JavaField> areConstantCandidates() {
        return new DescribedPredicate<>("of a primitive or String type") {
            @Override
            public boolean test(JavaField field) {
                JavaClass type = field.getRawType();
                return !field.getName().contains("$")
                        && (type.isPrimitive() || String.class.getName().equals(type.getName()));
            }
        };
    }

    private static DescribedPredicate<JavaClass> annotatedWithRestControllerButNotSelf() {
        return new DescribedPredicate<>("annotated with @RestController") {
            @Override
            public boolean test(JavaClass javaClass) {
                return javaClass.isAnnotatedWith(RestController.class);
            }
        };
    }

}
