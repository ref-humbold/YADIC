# YADIC (Yet Another Dependency Injection Container)

[![GitHub Actions](https://github.com/ref-humbold/YADIC/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/ref-humbold/YADIC/actions/workflows/build-and-test.yml)

[![Release](https://img.shields.io/github/v/release/ref-humbold/YADIC?style=plastic)](https://github.com/ref-humbold/YADIC/releases)
[![License](https://img.shields.io/github/license/ref-humbold/YADIC?style=plastic)](./LICENSE)

Simple dependency injection container in Java

-----

## System requirements

> versions used by the author are in italics

+ Operating system \
  *Debian testing*
+ Java \
  *APT package `openjdk-21-jdk`, version 21 SE*
+ Gradle \
  *SDK-Man `gradle`, version 8.14.+*

## Dependencies

> dependencies are automatically downloaded during build process

*none*

## Test dependencies

> dependencies are automatically downloaded during build process

+ JUnit 5.+
+ AssertJ 3.+

-----

## How to build?

YADIC can be built with **Gradle**. All dependencies are downloaded during build, so
make sure your Internet connection is working!

Possible Gradle tasks are:

+ `gradle build` - resolve dependencies & compile source files & create jar & run all tests
+ `gradle jar` - resolve dependencies & compile source files & create jar
+ `gradle test` - run all tests
+ `gradle javadoc` - generate Javadoc
+ `gradle rebuild` - remove additional build files & resolve dependencies & compile source files &
  create jar & run all tests

## How to include it?

Simply add the *jar* file to your module path from the `build/libs` directory:

```sh
$ /path-to-project-directory/build/libs/YADIC-{version}.jar
```
