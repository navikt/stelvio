#!/usr/bin/python

#********************************************************************************
#							                              
# File name: hudsonDeploymentProcessor.py 			                    
#							                    
# Description: This script processes the input strings for the two zones, and
#	       deploys/bundles the applications to the specified environment(s).
#	       Also provides status and proper failure information. 
# 
# Author: test@example.com			                   
#							 		
#********************************************************************************

import sys
import subprocess

########### defs ###########

def main(ENVS):
	
	global CHECKLIST_SENSITIV
	global CHECKLIST_INTERN
	global CHECKLIST_SENSITIV_BACKUP
	global CHECKLIST_INTERN_BACKUP
	
	SENSITIV_LIST = sys.argv[2].split("=")[1].split(",")
	INTERN_LIST = sys.argv[3].split("=")[1].split(",")
	
	for ENV in ENVS:  
		if getCount(SENSITIV_LIST) > 0:	
			deploy(ENV, SENSITIV_LIST, "sensitiv")
		else:
			print "[INFO] No applications to install in sensitiv zone."
	
		if getCount(INTERN_LIST) > 0:	
			deploy(ENV, INTERN_LIST, "intern")
		else:
			print "[INFO] No applications to install in intern zone."
		
		print "[INFO] Updating confluence environment page ..."
		CONF_STRING = "ssh test@example.com /fixpacks/envupdate/spread_script.py " + ENV 
		retval = subprocess.call(CONF_STRING, shell=True)
		if retval != 0:
			print "[ERROR] Something went wrong when updating confluence."
			
		
		if BOUNCE == true:
			APPS = getApps()
			BOUNCE_STRING = "cd ../maven-bounce-plugin/ && mvn clean install -Dapps=" + APPS + " -Denv=" + ENV + " -Dda-configuration-version=" + CONFIG_VERSION + " -DexcludeBus=" + EXCLUDE_BUS   
			retval = subprocess.call(BOUNCE_STRING, shell=True) 		
			if retval != 0:
				print "[ERROR] Something went wrong when performing restarts."

		print "[INFO] Completed processing of " + ENV + "."
		sys.stdout.flush()
		ENVS_CHECKLIST.remove(ENV)
		CHECKLIST_SENSITIV = CHECKLIST_SENSITIV_BACKUP[:]
		CHECKLIST_INTERN = CHECKLIST_INTERN_BACKUP[:]


def getCount(APP_LIST):
	if len(APP_LIST) > 0:
		if APP_LIST[0] != "":
			return len(APP_LIST)
		else:
			return 0
			
def getApps():
		ALL_APPS = CHECKLIST_INTERN_BACKUP + CHECKLIST_SENSITIV_BACKUP
		RET_STRING = ""
		for APP_COMP in ALL_APPS:
			if APP_COMP == "":
				continue

			APP = APP_COMP.split(":")[0]

			if RET_STRING != "":
				RET_STRING += "," + APP
			else:
				RET_STRING += APP
		return RET_STRING
	
			
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
	ENVS_FORMATTED = ",".join(ENVS)
	
	if (MULTI_ENV_DEPLOY == 0):
		print "[INFO]"
		print "[INFO] #################################################################################################################"
		print "[INFO] FAILURE"
		print "[INFO] #################################################################################################################"
		print "[INFO]"  
		print "[INFO] The deployment failed when deploying application, " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + "."
		print "[INFO] Please correct the errors, and relaunch the deployment with the remaining applications described below."
		print "[INFO]"
		print "[INFO] Sensitiv: ", SENSITIV_FORMATTED
		print "[INFO] Intern: ", INTERN_FORMATTED
		print "[INFO]"
		print "[INFO] #################################################################################################################"
		print "[INFO]"
	else:
		SENSITIV_FORMATTED_FULL = ",".join(CHECKLIST_SENSITIV_BACKUP)
		INTERN_FORMATTED_FULL = ",".join(CHECKLIST_INTERN_BACKUP)
		
		print "[INFO]"
		print "[INFO] #################################################################################################################"
		print "[INFO] FAILURE"
		print "[INFO] #################################################################################################################"  
		print "[INFO]"
		print "[INFO] Failed when deploying application, " + APP + ":" + VERSION + " to " + ZONE + " zone in " + ENV + "."
		print "[INFO] Please correct the errors, and relaunch the job with the remaining applications for " + ENV
		print "[INFO]"
		print "[INFO] Environments: " + ENV
		print "[INFO] Sensitiv: ", SENSITIV_FORMATTED
		print "[INFO] Intern: ", INTERN_FORMATTED
		print "[INFO]"
		
		if (len(ENVS_FORMATTED) > 0):
			print "[INFO] Then, relaunch the job with the following parameters:"
			print "[INFO]"
			print "[INFO] Environments: ", ENVS_FORMATTED
			print "[INFO] Sensitiv: ", SENSITIV_FORMATTED_FULL
			print "[INFO] Intern: ", INTERN_FORMATTED_FULL
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
		
		if (retval != 0 and retval != 10):
			ENVS.remove(ENV)
			printError(ENV, APP, VERSION, ZONE)
			sys.exit(1)
		else:
			print "[INFO] The processing of application " + APP + ":" + VERSION + " completed successfully."		
			sys.stdout.flush()
			INSTALLED_COUNT += 1
			if ZONE == "sensitiv":
				CHECKLIST_SENSITIV.remove(APP_COMP)
			else:
				CHECKLIST_INTERN.remove(APP_COMP)

########### /defs ###########

MULTI_ENV_DEPLOY = 0

ENVS = sys.argv[1].split("=")[1].split(",")

if (getCount(ENVS) == 0):
	print "[ERROR] No environment(s) specified."
	sys.exit(1)

# Make sure we have uppercase environment names
ENVS = [ENV.upper() for ENV in ENVS]

ENVS_CHECKLIST = ENVS[:]
ENVS_LENGTH = len(ENVS)

# Set the multiple environment flag
if (ENVS_LENGTH > 1):
	MULTI_ENV_DEPLOY = 1

CHECKLIST_SENSITIV = sys.argv[2].split("=")[1].split(",")
CHECKLIST_INTERN = sys.argv[3].split("=")[1].split(",")

# Create a backup we can use to refresh the array when done processing of an environment
CHECKLIST_SENSITIV_BACKUP = CHECKLIST_SENSITIV[:]
CHECKLIST_INTERN_BACKUP = CHECKLIST_INTERN[:]

CONFIG_VERSION = sys.argv[4]
BOUNCE = sys.argv[5]
EXCLUDE_BUS = sys.argv[6]
	
TOTAL_APP_COUNT = (getCount(CHECKLIST_SENSITIV) + getCount(CHECKLIST_INTERN)) * ENVS_LENGTH   
INSTALLED_COUNT = 1

main(ENVS)


