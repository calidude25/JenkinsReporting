#!/bin/sh
BIN=$(cd -- "$(dirname "$0")" && pwd)
pwd
echo $BIN
java -classpath target/jenkins-reporting.jar:$BIN/conf com.disney.wdpr.jenkins.Launch "$1" "$2" "$3"
