#!/bin/bash
command="${launchClient.home}/launchClient.sh ./bpchelper-ear.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -a STATUS -pcp terminatedSuccessful=false"
echo "Executing command: $command"
$command
