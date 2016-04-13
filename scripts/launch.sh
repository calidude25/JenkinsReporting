#!/bin/sh
BIN=$(cd -- "$(dirname "$0")" && pwd)
pwd
echo $BIN
cd "$BIN"
pwd
java -classpath ../target/jenkins-reporting.jar:../conf com.disney.wdpr.jenkins.Launch "$1" "$2" "$3"
