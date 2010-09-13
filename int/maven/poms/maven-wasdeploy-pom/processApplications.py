#!/usr/bin/python

#*****************************************************************************
#							                              
# File name: processApplications.py 			                    
#							                    
# Description: This script processes the input strings for the two zones, and
# 	       deploys/bundles the applications to the specific environment.
# 
# Author: test@example.com			                   
#							 		
#*****************************************************************************

import sys
import subprocess

def main():
	ENV = sys.argv[1]
	SENSITIV_LIST = sys.argv[2].split("=")[1].split(",")
	INTERN_LIST = sys.argv[3].split("=")[1].split(",")
	
	if appCount(SENSITIV_LIST) > 0:	
		deploy(SENSITIV_LIST, "sensitiv", ENV)
	else:
		print "[INFO] No applications to install in sensitiv zone."

	if appCount(INTERN_LIST) > 0:	
		deploy(INTERN_LIST, "intern", ENV)
	else:
		print "[INFO] No applications to install in intern zone."

def appCount(APP_LIST):
	if len(APP_LIST) > 0:
		if APP_LIST[0] != "":
			return len(APP_LIST)
		else:
			return 0

def printStatus(APP, VERSION, ZONE):
	print "[INFO]"
	print "[INFO] ################################################################################"
	print "[INFO]"
	print "[INFO] Installing  " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + " ...  [ ", INSTALLED_COUNT ," / ", TOTAL_APP_COUNT ," ]"
	print "[INFO]"
	print "[INFO] ################################################################################"
	print "[INFO]"

def printError(APP, VERSION, ZONE):
	SENSITIV_FORMATTED = ",".join(CHECKLIST_SENSITIV)
	INTERN_FORMATTED = ",".join(CHECKLIST_INTERN)
	
	print "[INFO]"
	print "[INFO] #################################################################################################################"
	print "[INFO] FAILURE"
	print "[INFO] #################################################################################################################"  
	print "[INFO] The deployment failed when deploying application, " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + "."
	print "[INFO] Please correct the errors, and relaunch the deployment with the remaining applications described below."
	print "[INFO]"
	print "[INFO] Sensitiv: ", SENSITIV_FORMATTED
	print "[INFO] Intern: ", INTERN_FORMATTED
	print "[INFO]"
	print "[INFO] #################################################################################################################"
	print "[INFO]"
	

def deploy(APP_LIST, ZONE, ENV):
	global INSTALLED_COUNT
	for APP_COMP in APP_LIST:		
		SPLITTED = APP_COMP.split(":")
		APP = SPLITTED[0]
		VERSION = SPLITTED[1]
		printStatus(APP, VERSION, ZONE)
		MAVEN_STRING = "mvn clean install -Denv=" + ENV + " -Dapp=" + APP + " -Dversion=" + VERSION + " -Dzone=" + ZONE + " -Dconfig=" + CONFIG_VERSION
		print "[INFO] Executing the following Maven string: \"" + MAVEN_STRING + "\""
		sys.stdout.flush()
		retval = subprocess.call(MAVEN_STRING, shell=True)
		if (retval != 0):
			printError(APP, VERSION, ZONE)
			sys.exit(1)
      		else:
			print "[INFO] The processing of application " + APP + ":" + VERSION + " finished successfully."		
			sys.stdout.flush()
			INSTALLED_COUNT += 1
			if ZONE == "sensitiv":
				CHECKLIST_SENSITIV.remove(APP_COMP)
			else:
				CHECKLIST_INTERN.remove(APP_COMP)

ENV = sys.argv[1]
CHECKLIST_SENSITIV = sys.argv[2].split("=")[1].split(",")
CHECKLIST_INTERN = sys.argv[3].split("=")[1].split(",")
CONFIG_VERSION = sys.argv[4]


TOTAL_APP_COUNT = appCount(CHECKLIST_SENSITIV) + appCount(CHECKLIST_INTERN)
INSTALLED_COUNT = 1

main()

