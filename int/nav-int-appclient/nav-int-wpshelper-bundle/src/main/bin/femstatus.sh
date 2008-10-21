#!/bin/bash

command="/cygdrive/C/IBM/WID61/pf/wps01/bin/launchClient.bat ./femhelper-ear-0.9.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -a STATUS" 
echo "Executing command: $command"
$command
