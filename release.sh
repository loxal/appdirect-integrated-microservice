#!/usr/bin/env bash

# Switch to Java 8 JDK
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
mvn clean package cf:push -P cf-aws-stage
# mvn clean package appengine:update # GAE (managed)
# mvn clean validate gcloud:deploy # gcloud (managed / unmanaged)