#***************************************************************************
#							                              
# File name: InstallApp.py 			                    
#							                    
# Description: This script performs the following steps, 
# 	
#	1. Uninstall other version(s) of the application, if it/they exist
# 	2. Install the new one
# 	3. Configures the shared library reference
# 	4. Configures the classloader order if needed 
# 	       
# Used by the plugin: 
# 	maven-wasdeploy-plugin:deploy-or-bundle (DeployOrBundleMojo.java)
#							                    
# Author: test@example.com			                   
#							 		
#***************************************************************************


import sys
import re
import os.environ
import time
from time import strftime

APP_NAME = sys.argv[0]
APP_LOCATION = sys.argv[1]
SERVER_NAME = sys.argv[2]
NODE_NAME = sys.argv[3]
CLUSTER_NAME = sys.argv[4]
APP = sys.argv[5]

cmdLine = os.environ.get("IBM_JAVA_COMMAND_LINE")
scriptName = re.sub(".*-f ([^ ]*).*", r"\1", cmdLine).strip()

def main():
	start = time.clock()
	uninstall()
	install()
	referenceSharedLibrary( APP, APP_NAME )
	
	# if APP == pen/psak/pselv, change classloader order.
	if (APP == "pen"):
		changeClassLoaderOrder(APP_NAME)
	elif (APP == "psak"):
		changeClassLoaderOrder(APP_NAME)
	elif (APP == "pselv"):		
		changeClassLoaderOrder(APP_NAME)
	
	stop = time.clock()
	log("INFO", "Time elapsed: " + str(round(stop - start, 2)) + " seconds.")
	log("INFO", "Saving ...")
	AdminConfig.save()
	log("INFO", "Done!")

def uninstall():
	APP_NAME_NOVERSION = re.split("-[0-9]+\.", APP_NAME)[0]
	INST_APPS = AdminApp.list().split()
	APP_TO_UNINST = []
	
	for app in INST_APPS:
		if (app.count(APP_NAME_NOVERSION) > 0):
			APP_TO_UNINST.append(app)
	if (len(APP_TO_UNINST) > 0):
		for match in APP_TO_UNINST:
			log("INFO", "Found another version of app: " + match + ", uninstalling...")
			AdminApp.uninstall(match)
	else:
		log("WARN", "No matches found for " + APP_NAME_NOVERSION + ", will not uninstall any applications...")	
	
def install():
	serverId = AdminConfig.getid("/Node:"+NODE_NAME+"/Server:"+SERVER_NAME+"/")
	options = "-verbose -appname " + APP_NAME + " -nodeployejb -usedefaultbindings -defaultbinding.virtual.host default_host -cluster " + CLUSTER_NAME
	log("INFO", "Installing application, " + APP_NAME + " with the following options: " + options)
	
	
	try:
		_excp_ = 0
		installed = AdminApp.install(APP_LOCATION, options)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		installed = `_value_`
		_excp_ = 1
	#endTry
	temp = _excp_
	if (temp != 0):
		msg = "Exception installing "+APP_NAME+" to "+CLUSTER_NAME
		log("ERROR", msg)
		log("ERROR", installed)
	#endIf

def referenceSharedLibrary( libName, appName ):
	libraries = AdminConfig.getid('/Library:/').split(java.lang.System.getProperty('line.separator'))
	for lib in libraries:
		classpath = AdminConfig.showAttribute(lib, "classPath")
		libraryname = AdminConfig.showAttribute(lib, "name")
		classpathName = classpath.split("/")[-1].upper()
		if(classpathName == libName.upper()):
			deployment = AdminConfig.getid("/Deployment:" + appName)
			deployedapplication = AdminConfig.showAttribute(deployment, "deployedObject")
			classloader = AdminConfig.showAttribute(deployedapplication, "classloader")
			AdminConfig.create('LibraryRef', classloader, [['libraryName', libraryname], ['sharedClassloader', 'true']])
			log("INFO", "Succesfully referenced Shared Library: "+libraryname+" with: "+appName )

def changeClassLoaderOrder( appName ):
	log("INFO", "Modifying classloader setting for: "+appName )
	dep = AdminConfig.getid("/Deployment:" + appName)
	depObject = AdminConfig.showAttribute(dep, "deployedObject")
	classloader = AdminConfig.showAttribute(depObject, "classloader")
	AdminConfig.modify(classloader,[['mode', 'PARENT_LAST']])
	log("INFO", "Succesfully modified classloader settings of: "+appName+", value was set to PARENT_LAST" )	

def log(loglevel, msg):
	print "[" + strftime("%Y-%m-%d %H:%M:%S") + ", " + loglevel + ", " + scriptName + "] " + msg
	
main()