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

import lib.logUtil as log
l = log.getLogger(__name__)

APPLICATION_NAME = sys.argv[1]
NEW_VERSION = sys.argv[2]

def main():

	l.info("RemoveOldBPModule: Checking whether older version of application " + APPLICATION_NAME + " exists.")

	appsInstalled = AdminApp.list()

	appToUninstall = appsInstalled[appsInstalled.find(APPLICATION_NAME, 0, len(appsInstalled)):].split("\n")[0]
	
	if (len(appToUninstall) < 2):
		l.info("Application not found!")
		return

	if not outdatedVersion(appToUninstall, NEW_VERSION):
		l.info("No newer version to install!")
		return
			
	l.info("Uninstalling application: " + appToUninstall)
	try:
		AdminApp.uninstall(appToUninstall)
	except:
		l.exception("Uninstall failed!")
	AdminConfig.save()
	
def outdatedVersion(appToUninstall, newVersion):
	'''
	Checks if the application currently installed on the server is older 
	than the one in the current busrelease, if so - return True, else return False
	'''
	pattern = re.compile(r'\s+')
	oldVersion = re.sub(pattern, '', AdminApp.view(appToUninstall, '-buildVersion').replace('Application Build ID:  ', ''))
	
	if (newVersion.find("SNAPSHOT") >= 0):
		l.info("SNAPSHOT version, will uninstall: " + appToUninstall)
		return True
				
	elif (oldVersion[0] == 'Unknown'):
		l.info("Unknown version, will uninstall: " + appToUninstall)
		return True
		
	elif (oldVersion == newVersion):
		l.info("Equal version, will not uninstall: " + appToUninstall)
		return False
	else:
		l.info("Different version, will uninstall: " + appToUninstall)
		return True

main()