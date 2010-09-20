#!/usr/bin/python

#*****************************************************************************
#							                              
# File name: processApplications.py 			                    
#							                    
# Description: This script processes the input strings for the two zones, and
#	       deploys/bundles the applications to the specified environment(s).
# 
# Author: test@example.com			                   
#							 		
#*****************************************************************************

import sys
import subprocess

def main():
	
	global CHECKLIST_SENSITIV
	global CHECKLIST_INTERN
	global CHECKLIST_SENSITIV_BACKUP
	global CHECKLIST_INTERN_BACKUP
	
	ENVS = sys.argv[1].split(",")
	SENSITIV_LIST = sys.argv[2].split("=")[1].split(",")
	INTERN_LIST = sys.argv[3].split("=")[1].split(",")
	
	for ENV in ENVS:  
		if appCount(SENSITIV_LIST) > 0:	
			deploy(ENV, SENSITIV_LIST, "sensitiv")
		else:
			print "[INFO] No applications to install in sensitiv zone."
	
		if appCount(INTERN_LIST) > 0:	
			deploy(ENV, INTERN_LIST, "intern")
		else:
			print "[INFO] No applications to install in intern zone."
		
		print "[INFO] Completed processing of " + ENV + "."
		ENVS_CHECKLIST.remove(ENV)
		
		print "remaining envs: ", ENVS_CHECKLIST
		
		print "CHECKLIST_SENSITIV_BACKUP: ", CHECKLIST_SENSITIV_BACKUP
		print "CHECKLIST_INTERN_BACKUP: ", CHECKLIST_INTERN_BACKUP
		print "CHECKLIST_SENSITIV: ", CHECKLIST_SENSITIV
		print "CHECKLIST_INTERN: ", CHECKLIST_INTERN
		
		print "copying..."
		
		CHECKLIST_SENSITIV = CHECKLIST_SENSITIV_BACKUP[:]
		CHECKLIST_INTERN = CHECKLIST_INTERN_BACKUP[:]
		
		print "CHECKLIST_SENSITIV_BACKUP: ", CHECKLIST_SENSITIV_BACKUP
		print "CHECKLIST_INTERN_BACKUP: ", CHECKLIST_INTERN_BACKUP
		print "CHECKLIST_SENSITIV: ", CHECKLIST_SENSITIV
		print "CHECKLIST_INTERN: ", CHECKLIST_INTERN


def appCount(APP_LIST):
	if len(APP_LIST) > 0:
		if APP_LIST[0] != "":
			return len(APP_LIST)
		else:
			return 0

def printStatus(ENV, APP, VERSION, ZONE):
	print "[INFO]"
	print "[INFO] ################################################################################"
	print "[INFO]"
	print "[INFO] Processing  " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + " ...  [ ", INSTALLED_COUNT ," / ", TOTAL_APP_COUNT ," ]"
	print "[INFO]"
	print "[INFO] ################################################################################"
	print "[INFO]"

def printError(ENV, APP, VERSION, ZONE):
	SENSITIV_FORMATTED = ",".join(CHECKLIST_SENSITIV)
	INTERN_FORMATTED = ",".join(CHECKLIST_INTERN)
	
	
	if (MULTI_ENV_DEPLOY == 0):
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
	else:
		print "[INFO]"
		print "[INFO] #################################################################################################################"
		print "[INFO] FAILURE"
		print "[INFO] #################################################################################################################"  
		print "[INFO] The deployment failed when deploying application, " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + "."
		print "[INFO] Please correct the errors, and first relaunch the deployment with the remaining applications for " + ENV
		print "[INFO]"
		print "[INFO] Environment: " + ENV
		print "[INFO] Sensitiv: ", SENSITIV_FORMATTED
		print "[INFO] Intern: ", INTERN_FORMATTED
		print "[INFO]"
		print "[INFO] Then, relaunch the deployment with the following parameters:"
		print "[INFO]"
		print "[INFO]"
		print "[INFO]"
		print "[INFO]"
		print "[INFO] #################################################################################################################"
		print "[INFO]"
	

def deploy(ENV, APP_LIST, ZONE):
	global INSTALLED_COUNT
	
	for APP_COMP in APP_LIST:		
		SPLITTED = APP_COMP.split(":")
		APP = SPLITTED[0]
		VERSION = SPLITTED[1]
		printStatus(ENV, APP, VERSION, ZONE)
		MAVEN_STRING = "mvn clean install -Denv=" + ENV + " -Dapp=" + APP + " -Dversion=" + VERSION + " -Dzone=" + ZONE + " -Dconfig=" + CONFIG_VERSION
		print "[INFO] Executing the following Maven string: \"" + MAVEN_STRING + "\""
		sys.stdout.flush()
		retval = subprocess.call(MAVEN_STRING, shell=True)
		if (retval != 0):
			printError(ENV, APP, VERSION, ZONE)
			sys.exit(1)
		else:
			print "[INFO] The processing of application " + APP + ":" + VERSION + " finished successfully."		
			sys.stdout.flush()
			INSTALLED_COUNT += 1
			if ZONE == "sensitiv":
				CHECKLIST_SENSITIV.remove(APP_COMP)
			else:
				CHECKLIST_INTERN.remove(APP_COMP)


MULTI_ENV_DEPLOY = 0
ENVS = sys.argv[1].split(",")
ENVS_CHECKLIST = ENVS[:]
ENVS_LENGTH = len(ENVS)

if (ENVS_LENGTH > 1):
	MULTI_ENV_DEPLOY = 1

CHECKLIST_SENSITIV = sys.argv[2].split("=")[1].split(",")
CHECKLIST_INTERN = sys.argv[3].split("=")[1].split(",")

CHECKLIST_SENSITIV_BACKUP = CHECKLIST_SENSITIV[:]
CHECKLIST_INTERN_BACKUP = CHECKLIST_INTERN[:]

CONFIG_VERSION = sys.argv[4]

TOTAL_APP_COUNT = (appCount(CHECKLIST_SENSITIV) + appCount(CHECKLIST_INTERN)) * ENVS_LENGTH   
INSTALLED_COUNT = 1

main()

