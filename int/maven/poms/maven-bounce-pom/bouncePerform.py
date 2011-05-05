#!/usr/bin/python

import sys
import subprocess

def execute(command):
    process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    output = ''

    # Poll process for new output until finished
    for line in iter(process.stdout.readline, ""):
        print line,
        output += line


    process.wait()
    exitCode = process.returncode

    if (exitCode == 0):
        return output
    else:
        raise Exception(command, exitCode, output)

if (len(sys.argv) < 9):
    print "[ERROR] Missing parameters. Usage: bouncePerform.py envs action da_config_verson wasSs wasIs wps joark onlyAppTarget [env_class zone bounce_dmgr_na install_ifix]"
    sys.exit(1)

ENVS = sys.argv[1].split(",")
ACTION = sys.argv[2]
DA_CONFIG_VER = sys.argv[3]
WAS_SS = sys.argv[4]
WAS_IS = sys.argv[5]
WPS = sys.argv[6]
JOARK = sys.argv[7]
ONLY_APP_TARGET = sys.argv[8]

# envClass and zone
if(len(sys.argv) > 10):
    ENV_CLASS = sys.argv[9]
    ZONE = sys.argv[10]
else:
    ENV_CLASS = "T"
    ZONE = "sensitiv"
	
# bounce_app_servers, bounce_dmgr, ifix_install
if(len(sys.argv) > 12):
    BOUNCE_DMGR_NA = sys.argv[11]
    IFIX = sys.argv[12]
else:
    BOUNCE_DMGR_NA = "false"
    IFIX = "false"


if (len(ENVS) == 0):
    print "[ERROR] No environment(s) specified."
    sys.exit(1)

FAIL = "false"
FAILED_ENVS = ""
BOUNCE_STRING = ""
for ENV in ENVS:
    print "###########################"
    print "Starting bounce for " + ENV
    print "###########################"
    BOUNCE_STRING = "mvn clean install -Denv=" + ENV + " -Daction=" + ACTION
    BOUNCE_STRING += " -Dda-configuration-version=" + DA_CONFIG_VER
    BOUNCE_STRING += " -Dss_pensjons_cluster=" + WAS_SS + " -Dis_pensjons_cluster=" + WAS_IS 
    BOUNCE_STRING += " -Dapptarget=" + WPS + " -DincludeJoark=" + JOARK 
    BOUNCE_STRING += " -Dmsg_sup=" + ONLY_APP_TARGET
    BOUNCE_STRING += " -DenvClass=" + ENV_CLASS 
    BOUNCE_STRING += " -Dzone=" + ZONE
    BOUNCE_STRING += " -Dbounce_dmgr_na=" + BOUNCE_DMGR_NA
    BOUNCE_STRING += " -Dinstall_ifix=" + IFIX
	
    print BOUNCE_STRING
    try:
	execute(BOUNCE_STRING)
    except Exception:
        FAIL = "true"
        FAILED_ENVS += ENV + ", "

 
if (FAIL == "true"):
    print "############################################################" 
    print "[ERROR] Could not perform " + ACTION + " on " + FAILED_ENVS
    print "############################################################" 
    sys.exit(1)
