#!/usr/bin/python

import sys
import subprocess

if (len(sys.argv) < 9):
    print "[ERROR] Missing parameters"
    sys.exit(1)

ENVS = sys.argv[1].split(",")
ACTION = sys.argv[2]
DA_CONFIG_VER = sys.argv[3]
WAS_SS = sys.argv[4]
WAS_IS = sys.argv[5]
WPS = sys.argv[6]
JOARK = sys.argv[7]
ONLY_APP_TARGET = sys.argv[8]

if (len(ENVS) == 0):
        print "[ERROR] No environment(s) specified."
        sys.exit(1)

FAIL = "false"
FAILED_ENVS = ""

for ENV in ENVS:
    print "###########################"
    print "Starting bounce for " + ENV
    print "###########################"
    BOUNCE_STRING = "mvn clean install -Denv=" + ENV + " -Daction=" + ACTION + " -Dda-configuration-version=" + DA_CONFIG_VER + " -DwasSs=" + WAS_SS + " -DwasIs=" + WAS_IS + " -Dwps=" + WPS + " -DincludeJoark=" + JOARK + " -DonlyAppTarget=" + ONLY_APP_TARGET
	print BOUNCE_STRING
    #retval = subprocess.call(BOUNCE_STRING, shell=True)
    #if (retval != 0):
        #FAIL = "true"
        #FAILED_ENVS += ENV + ", "

 
if (FAIL == "true"):
    print "############################################################" 
    print "[ERROR] Could not perform " + ACTION + " on " + FAILED_ENVS
    print "############################################################" 
    sys.exit(1)