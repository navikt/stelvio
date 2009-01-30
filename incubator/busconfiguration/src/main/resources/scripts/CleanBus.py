#******************************************************************************
# File Name:	cleanBus.py
# Description:	This file contains the following application-related procedures:
#		
#			markAppState
#			startApp
#			stopApp
#			installEAR
#			uninstallApplication
#			stopAppsAll
#			startAppsAll
#			installAll
#			uninstallAll
#			save
#
#
#******************************************************************************
import sys
import java
from java.lang 	import System
from java.net 	import InetAddress
from java.util 	import Properties
from java.io 	import FileInputStream

APPLICATION_NAME 	 = sys.argv[0]
ENVIRONMENT 	 	 = sys.argv[1]
WSADMIN_SCRIPTS_HOME	 = sys.argv[2]
APP_PROPS_HOME 		 = WSADMIN_SCRIPTS_HOME+"/app_props/"+ENVIRONMENT+"/"


execfile( WSADMIN_SCRIPTS_HOME+"/py/utils6.py" )


def listApplications(displayFlag = 1):
	retval = None
	try:
		print ("INFO (listApplications): Running command: AdminApp.list()")
		appList = AdminApp.list()
		if isEmpty(appList):
			retval = []
		else:	
			lineSeparator = java.lang.System.getProperty('line.separator')
			retval = str.split(lineSeparator)
		if displayFlag:
			print "\nJ2EE Applications\n-------------------------\n%s\n-------------------------\n" % (str)

	except:
		print ("ERROR (listApplications):An error was encountered listing J2EE Applications")
		retval = []

	return retval

	
