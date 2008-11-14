#!/bin/bash
command="${launchClient.home}/launchClient.sh ./femhelper-ear.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -a STATUS"
echo "Executing command: $command"
$command
