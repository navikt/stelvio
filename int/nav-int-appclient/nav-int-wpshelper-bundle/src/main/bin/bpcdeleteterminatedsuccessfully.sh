#!/bin/bash
command="/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/launchClient.sh ./bpchelper-ear-0.9.1.ear -CCtrace=no.nav -CCpropfile=$1 -cf $1 -ns -a DELETE -pcp terminatedSuccessful=true"
echo "Executing command: $command"
$command
