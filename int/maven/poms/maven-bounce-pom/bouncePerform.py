#!/usr/bin/python

import sys
import subprocess

if (len(sys.argv) < 9):
    print "[ERROR] Missing parameters. Usage: bouncePerform.py envs action da_config_verson wasSs wasIs wps joark onlyAppTarget [env_class zone bounce_app_servers bounce_dmgr_na install_ifix]"
    print "[EXAMPLE 1] bouncePerform.py T1,T2 stop 7.0.1 true false true true false - this will stop WAS SS (both PensjonsCluster adn Joark) and WPS (all clusters) in T1 and T2"
    print "[EXAMPLE 2] bouncePerform.py K1 start 7.0.1 false false true false true T sensitiv false true true  - this will start only dmgr and nodeagents for WPS in K1 after trying to install ifixes"
    print "[EXAMPLE 3] bouncePerform.py K1 restart 7.0.1 true true false false false T sensitiv true true false - this will restart WAS SS and IS (only AppTarget) in K1, bounce dmgr and node agents without installation of ifixes"
    sys.exit(1)

ENVS = sys.argv[1].split(",")
ACTION = sys.argv[2]
DA_CONFIG_VER = sys.argv[3]
WAS_SS = sys.argv[4]
WAS_IS = sys.argv[5]
WPS = sys.argv[6]
JOARK = sys.argv[7]
ONLY_APP_TARGET = sys.argv[8]

if(len(sys.argv) > 10):
    ENV_CLASS = sys.argv[9]
    ZONE = sys.argv[10]
    BOUNCE_APP_SERVERS = sys.argv[11]
    BOUNCE_DMGR_NA = sys.argv[12]
    IFIX = sys.argv[13]
else:
    ENV_CLASS = "T"
    ZONE = "sensitiv"
    BOUNCE_APP_SERVERS = "true"
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
    BOUNCE_STRING += " -DwasSs=" + WAS_SS + " -DwasIs=" + WAS_IS 
    BOUNCE_STRING += " -Dwps=" + WPS + " -DincludeJoark=" + JOARK 
    BOUNCE_STRING += " -DonlyAppTarget=" + ONLY_APP_TARGET
    BOUNCE_STRING += " -DenvClass=" + ENV_CLASS 
    BOUNCE_STRING += " -Dzone=" + ZONE
    BOUNCE_STRING += " -Dbounce_app_servers=" + BOUNCE_APP_SERVERS
    BOUNCE_STRING += " -Dbounce_dmgr_na=" + BOUNCE_DMGR_NA
    BOUNCE_STRING += " -Dinstall_ifix=" + IFIX
	
    print BOUNCE_STRING
    retval = subprocess.call(BOUNCE_STRING, shell=True)
    if (retval != 0):
        FAIL = "true"
        FAILED_ENVS += ENV + ", "

 
if (FAIL == "true"):
    print "############################################################" 
    print "[ERROR] Could not perform " + ACTION + " on " + FAILED_ENVS
    print "############################################################" 
    sys.exit(1)