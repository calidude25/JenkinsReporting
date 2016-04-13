#!/bin/sh
BIN=$(cd -- "$(dirname "$0")" && pwd)

java -classpath target/jenkins-reporting.jar;$BIN com.disney.wdpr.jenkins.Launch "$@"