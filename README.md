# DI-Container
[![Build Status](https://travis-ci.org/ref-humbold/DI-Container.svg?branch=master)](https://travis-ci.org/ref-humbold/DI-Container)

![Release](https://img.shields.io/github/v/release/ref-humbold/DI-Container?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/DI-Container?style=plastic)

Simple dependency injection container in Java

-----

## Dependencies

### Standard build & run
> *versions used by the author are in double parentheses and italic*

Build process:
+ operating system *((Debian testing))*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) *((Standard Edition 11))*
+ [Apache ANT](http://ant.apache.org/) *((1.10.+))*

### Unit testing
> libraries are automatically downloaded during build process

+ JUnit 5.+

-----

## How to build?
DI_Container can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies. Ivy and all libraries are downloaded during build, so make sure your Internet connection is working!

> Possible ANT targets are:
> + `ant` - same as `ant all`
> + `ant build` - compile source files & create jar
> + `ant rebuild` - resolve dependencies & compile source files & create jar
> + `ant test` - run all tests
> + `ant docs` - generate Javadoc
> + `ant main` - compile source and test files & create jar & run all tests
> + `ant all` - resolve dependencies & compile source and test files & create jar & run all tests
> + `ant refresh` - remove additional build files & resolve dependencies & compile source files & create jar
> + `ant refresh-main` - remove additional build files & compile source and test files & create jar & run all tests
> + `ant refresh-all` - remove additional build files & resolve dependencies & compile source and test files & create jar & run all tests

## How to include it?
Simply add the *jar* file from the `dist` directory to your classpath.
