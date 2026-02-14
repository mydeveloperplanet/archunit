# Testing Architecture Made Simple

This repository contains examples how to use Taikai in order to test the architecture of your application.

## Base
[base](base): The base application used in all the examples. It is a basic Spring Boot 4 application with Java 25. It is a Customer CRUD application. It consists out of:
* a controller (partially generated code based on an OpenAPI spec)
* a service
* a repository (using jOOQ)
* a model

## ArchUnit Introduction
[introduction](introduction): Make use of ArchUnit.
* Create an [ArchUnitRule](introduction/src/test/java/com/mydeveloperplanet/archunit/introduction/ArchitectureRuleTest.java) where the repository may only be accessed from a service or other repository.
* Create an [ArchUnit test](introduction/src/test/java/com/mydeveloperplanet/archunit/introduction/ArchitectureLayerTest.java) using the built-in layered architecture rules.

## Taikai Introduction
[takai-introduction](taikai-introduction): Make use of Taikai.

Layered architecture [example](taikai-introduction/src/test/java/com/mydeveloperplanet/archunit/introduction/ArchitectureLayerTest.java), but using Taikai.

## Taikai Dependency
[taikai-dependency](taikai-dependency): exclude the use of certain dependencies, lombok in this case.

## Taikai Spring Configuration
[taikai-springconfig](taikai-springconfig): enforce how Spring Boot configuration should be implemented.
* Configuration classes in `config` package and Configuration class names should end with `Config`
* Configuration classes annotated with `ConfigurationProperties` should not be annotated with `Configuration`
* Configuration classes annotated with `ConfigurationProperties` should not be annotated with `EnableConfigurationProperties`
* The main Spring Boot application class should be annotated with `ConfigurationPropertiesScan`
* Configuration classes should be Records

## Taikai WebConfig
[taikai-webconfig](taikai-webconfig): Spring Boot 3 application. Adding a `WebConfig`, the Spring classes throw a generic Exception. A general rule exists where you want to forbid generic exceptions, but in this case, you cannot do much about it. Exclude the `WebConfig` class.

## Taikai Complete
[taikai-complete](taikai-complete): a complete set of rules for an application.

Generated code contains rule violations. Exclude the generated code from the test using `excludeClasses`.