# YADIC (Yet Another Dependency Injection Container)

[![GitHub Actions](https://github.com/ref-humbold/YADIC/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/ref-humbold/YADIC/actions/workflows/build-and-test.yml)

![Release](https://img.shields.io/github/v/release/ref-humbold/YADIC?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/YADIC?style=plastic)

**Yet Another Dependency Injection Container**

Simple dependency injection container in Java

-----

## System requirements

> versions used by the author are in italics

+ Operating system \
  *Debian testing*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) \
  *APT package `openjdk-17-jdk`, version 17 SE*
+ [Gradle](https://gradle.org/) or [Apache ANT](http://ant.apache.org/) \
  *SDK-Man `gradle`, version 8.10* \
  *APT package `ant`, version 1.10.+*

## Dependencies

> dependencies are automatically downloaded during build process

+ JUnit 5.+
+ AssertJ 3.+

-----

## How to build with Gradle?

YADIC can be built with **Gradle**. All dependencies are downloaded during build, so
make sure your Internet connection is working!

Possible Gradle tasks are:

+ `gradle build` - resolve dependencies & compile source files & create jar & run all tests
+ `gradle jar` - resolve dependencies & compile source files & create jar
+ `gradle test` - run all tests
+ `gradle javadoc` - generate Javadoc
+ `gradle rebuild` - remove additional build files & resolve dependencies & compile source files &
  create jar & run all tests

## How to build with ANT?

YADIC can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies.
Ivy itself and all dependencies are downloaded during build, so make sure your Internet
connection is working!

Possible ANT targets are:

+ `ant`, `ant build` - resolve dependencies & compile source files & create jar & run all tests
+ `ant resolve` - resolve dependencies
+ `ant jar` - compile source files & create jar
+ `ant test` - run all tests
+ `ant docs` - generate Javadoc
+ `ant clean` - remove additional build files
+ `ant rebuild` - remove additional build files & resolve dependencies & compile source files &
  create jar & run all tests

## How to include it?

Simply add the *jar* file to your classpath from the directory:

+ `build/libs` for Gradle builds
+ `antBuild/dist` for ANT builds
