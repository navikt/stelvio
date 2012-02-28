#****************************************************************************
#							                              
# File name: RemoveOldBPModule.py 			                    
#							                    
# Description: This script removes the older version of the business process                     
# 	       provided as argument. 
# 	       
# 	       Used by the plugin: 
# 	       maven-wpsdeploy-plugin:remove-bp (RemoveBPMojo.java)
#							                    
# Author:      test@example.com
#	       test@example.com		                   
#							 		
#****************************************************************************

import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs

import lib.logUtil as l

APPLICATION_NAME = sys.argv[0]
NEW_VERSION = sys.argv[1]

def main():

	l.info("RemoveOldBPModule: Checking whether older version of application " + APPLICATION_NAME + " exists.")

	APPS_INSTALLED = AdminApp.list()

	APP_TO_UNINSTALL = APPS_INSTALLED[APPS_INSTALLED.find(APPLICATION_NAME, 0, len(APPS_INSTALLED)):].split("\n")[0]
	
	if (len(APP_TO_UNINSTALL) < 2):
		l.info("Application not found!")
		return

	if (version_Check(APP_TO_UNINSTALL) == 0):
		l.info("No newer version to install!")
		return
			
	l.info("Uninstalling application: " + APP_TO_UNINSTALL)
	try:
		AdminApp.uninstall(APP_TO_UNINSTALL)
	except:
		type, value, tracebackObj = sys.exc_info()
		l.error("Uninstall failed:\n"+value)
	AdminConfig.save()
	
#*************************************************************************
# versionCheck(APP_TO_UNINSTALL)
#
# Checks if the application currently installed on the server is older 
# than the one in the current busrelease, if so - return 1, else return 0
#*************************************************************************
	
def version_Check(APP_TO_UNINSTALL):

	pattern = re.compile(r'\s+')
	OLD_VERSION = re.sub(pattern, '', AdminApp.view(APP_TO_UNINSTALL, '-buildVersion').replace('Application Build ID:  ', ''))
	
	if (NEW_VERSION.find("SNAPSHOT") >= 0):
		print "SNAPSHOT version, will uninstall: " + APP_TO_UNINSTALL
		return 1
				
	elif (OLD_VERSION[0] == 'Unknown'):
		print "Unknown version, will uninstall: " + APP_TO_UNINSTALL
		return 1
		
	elif (OLD_VERSION == NEW_VERSION):
		print "Equal version, will not uninstall: " + APP_TO_UNINSTALL
		return 0
	else:
		print "Different version, will uninstall: " + APP_TO_UNINSTALL
		return 1

main()