#!/bin/bash

command="/cygdrive/C/IBM/WID61/pf/wps01/bin/launchClient.bat ./femhelper-ear-1.0-SNAPSHOT.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -a STATUS" 
echo "Executing command: $command"
$command
