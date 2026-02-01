package com.mydeveloperplanet.archunit.introduction;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.mydeveloperplanet.archunit.introduction")
class ArchitectureRuleTest {

    @ArchTest
    static final ArchRule myRule = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..repository..");

}
