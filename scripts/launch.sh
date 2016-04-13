#!/bin/sh

java -classpath target/jenkins-reporting.jar;conf com.disney.wdpr.jenkins.Launch "$@"
