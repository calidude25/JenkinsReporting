#!/bin/sh

#export JAVA_HOME=/opt/apps/jboss/java16
#export PATH=$JAVA_HOME/bin:$PATH


BIN=$(cd -- "$(dirname "$0")" && pwd)

cd $BIN
DATE=`date '+%m/%d/%y - %H:%M:%S'`


	PID=`ps -ef | grep java | grep LMSProctorAudit | grep $1 | awk '{ print $2 }'`


# If batch is not already running, start
if [ -z "$PID" ]; then
        echo "$DATE - Launching batch service $1 $2 $3 $4"
        nohup java -classpath $BIN/LMSProctorAudit.jar:$BIN/conf com.disney.lms.Launch $1 $2 $3 $4  >> $BIN/logs/lms.log &
else
	echo "Batch $1 process is already running, PID: $PID"
fi
