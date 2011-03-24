#!/usr/bin/python

#********************************************************************************
#							                              
# File name: wps_datapower_deploy.py 			                    
#							                    
# Description: This script processes the input in format
# wps_datapower_deploy.py env=<environment e.g. T6> bus-config=<bus config version> esb=<esb version> deploy_darapower=true
# and starts wps deploy followed by datapower deploy
# 
# Author: test@example.com			                   
#							 		
#********************************************************************************

import sys
import subprocess

def wps_deploy(ENV, BUS_CONFIG, ESB_VERSION, INTERACTIVE_MODE):
	MAVEN_STRING = "mvn clean deploy -Denvironment=" + ENV + " -Dbus-configuration-version=" + BUS_CONFIG
	MAVEN_STRING += " -Desb-modules-version=" + ESB_VERSION
	MAVEN_STRING += " -Dnav-modules-version=" + ESB_VERSION
	MAVEN_STRING += " -Dpensjon-modules-version=" + ESB_VERSION
	MAVEN_STRING += " -Dpensjon-process-modules-version=" + ESB_VERSION
	MAVEN_STRING += " -Dekstern-pensjon-modules-version=" + ESB_VERSION
	MAVEN_STRING += " -DinteractiveMode=" + INTERACTIVE_MODE
	print "[INFO] Executing the following Maven string: \n\"" + MAVEN_STRING + "\" from ../maven-deploy-pom-70/"
	retval = subprocess.call(MAVEN_STRING, shell=True)
	if retval != 0:
		print "[ERROR] Something went wrong while performing deploy to wps. Please consult the logs."
#end wps_deploy

def datapower_deploy(GATEWAY, ENV, ESB_VERSION, BUS_CONFIG):
	MAVEN_STRING = "mvn install -Dgateway-name=" + GATEWAY + " -Denvclass=" + ENV[0]
	MAVEN_STRING += " -Desb-release-version=" + ESB_VERSION
	MAVEN_STRING += " -Denv=" + ENV
	MAVEN_STRING += " -Dbus-configuration-version=" + BUS_CONFIG
	print "[INFO] Executing the following Maven string: \n\"" + MAVEN_STRING + "\" from ../deployer/"
	COMMAND = "cd ../deployer/ && " + MAVEN_STRING
	retval = subprocess.call(COMMAND, shell=True)
	if retval != 0:
		print "[ERROR] Something went wrong while performing deploy to " + GATEWAY + ". Please consult the logs."
#end datapower_deploy

ENVIRONMENT = sys.argv[1].split("=")[1].upper()
BUS_CONFIGURATION = sys.argv[2].split("=")[1]
ESB_V = sys.argv[3].split("=")[1]
DEPLOY_SECGW = sys.argv[4].split("=")[1].upper()
DEPLOY_PARTNERGW = sys.argv[5].split("=")[1].upper()

wps_deploy(ENVIRONMENT, BUS_CONFIGURATION, ESB_V, "false")
if DEPLOY_SECGW == "TRUE":
	datapower_deploy("secgw", ENVIRONMENT, ESB_V, BUS_CONFIGURATION)
if DEPLOY_PARTNERGW == "TRUE":
	datapower_deploy("partner-gw", ENVIRONMENT, ESB_V, BUS_CONFIGURATION)