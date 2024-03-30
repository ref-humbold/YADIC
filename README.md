# DI-Container

![GitHub Actions](https://github.com/ref-humbold/DI-Container/workflows/GitHub%20Actions/badge.svg?branch=master)
[![CircleCI](https://circleci.com/gh/ref-humbold/DI-Container/tree/master.svg?style=shield)](https://circleci.com/gh/ref-humbold/DI-Container/tree/master)

![Release](https://img.shields.io/github/v/release/ref-humbold/DI-Container?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/DI-Container?style=plastic)

Simple dependency injection container in Java

-----

## System requirements

> versions used by the author are in italics

+ Operating system \
  *Debian testing*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) \
  *APT package `openjdk-17-jdk`, version 17 SE*
+ [Apache ANT](http://ant.apache.org/) \
  *APT package `ant`, version 1.10.+*

## Dependencies

> dependencies are automatically downloaded during build process

+ JUnit 5.+
+ AssertJ 3.+

-----

## How to build with ANT?

DI\_Container can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies.
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

## How to build with Gradle?

DI\_Container can be built with **Gradle**. All dependencies are downloaded during build, so
make sure your Internet connection is working!

Possible Gradle tasks are:

+ `gradle build` - resolve dependencies & compile source files & create jar & run all tests
+ `gradle jar` - resolve dependencies & compile source files & create jar
+ `gradle test` - run all tests
+ `gradle javadoc` - generate Javadoc
+ `gradle rebuild` - remove additional build files & resolve dependencies & compile source files &
  create jar & run all tests

## How to include it?

Simply add the *jar* file to your classpath from the directory:

+ `antBuild/dist` for ANT builds
+ `build/libs` for Gradle builds
