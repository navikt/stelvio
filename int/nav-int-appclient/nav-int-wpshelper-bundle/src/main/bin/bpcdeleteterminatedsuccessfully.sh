#!/bin/bash
command="${launchClient.home}/launchClient.sh ./bpchelper-ear.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -ns -a DELETE -pcp terminatedSuccessful=true"
echo "Executing command: $command"
$command
