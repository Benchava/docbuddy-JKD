# docbuddy-JKD
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/901f00cedc844fb4b63ee2296bf550c4)](https://www.codacy.com/app/Benchava/docbuddy-JKD?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nbantar/docbuddy-JKD&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/nbantar/docbuddy-JKD.svg?branch=master)](https://travis-ci.org/nbantar/docbuddy-JKD)

Note: This is a continuous WORK IN PROGRESS.

## SUMMARY

Jeet Kune Do (JDK) is a service that acts as a gateway in the micro-services architecture 
of the Docbuddy app.
DocBuddy is a REST API developed as part of a test for implementing a small micro-services architecture.
The complete set of micro-services are meant to work as a small system to manage Doctor-Client relationships and also to
serve as a way of having a centralized clinical history for them.

## TECHNOLOGIES

JDK is built using Java 8, Gradle, Spring-Boot, Lombok, JWT and Zuul (Netflix) among other technologies.


## HOW TO RUN IT

JDK uses Spring-Boot so, once the project is cloned and built (using Gradle), all you have to do is run the 
"bootRun" gradle task and the service will come up.
The API can be hit using Postman or any other tool that you see fit.