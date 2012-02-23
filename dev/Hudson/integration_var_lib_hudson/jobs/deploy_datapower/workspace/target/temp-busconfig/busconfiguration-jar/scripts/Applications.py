#******************************************************************************
# File Name:	applications.py
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
#Uninstall will try to uninstall an application based on the file name of the module to deploy
#Install will install an application based on the "displayName" in the deployment descriptor.
#Therefore mismatches will occur when the displayName value does not equal the module's file name.

import sys
import java
import re

from java.lang 	import System
from java.net 	import InetAddress
from java.util 	import Properties
from java.util  import Calendar
from java.io 	import FileInputStream
from java.io 	import File



APPLICATION_NAME 	 = sys.argv[0]
ENVIRONMENT 	 	 = sys.argv[1]
WSADMIN_SCRIPTS_HOME	 = sys.argv[2]
WSADMIN_SCRIPTS_HOME      = WSADMIN_SCRIPTS_HOME.replace('\t','\\t')
APP_PROPS_HOME 		 = WSADMIN_SCRIPTS_HOME+"/app_props/"+ENVIRONMENT+"/"
APP_PROPS_HOME 	   	 = APP_PROPS_HOME.replace('\t','\\t')
whereIsProperties 	 = APPLICATION_NAME+".properties"
DISTDIR			 = sys.argv[3]
DISTDIR 		 = DISTDIR.replace('\t','\\t')

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/utils6.py" )

def markAppState ( appVersion, isEnabled ):

	#---------------------------------------------------------------------------------
	# Mark the version of the application code enabled or disabled
	#---------------------------------------------------------------------------------

	global AdminConfig
	print "\n ====== Mark "+appVersion+" with enable="+isEnabled+" ======"

	try:
		_excp_ = 0
		app = AdminConfig.getid("/Deployment:"+appVersion+"/" )
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		app = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error getting AppVersion"
		print "server ID = "+app
		sys.exit()
	#endIf 

	do = AdminConfig.showAttribute(app, "deployedObject" )
	tm = AdminConfig.showAttribute(do, "targetMappings" )
	tml = first(tm)
	try:
		_excp_ = 0
		error = AdminConfig.modify(tml, [["enable", isEnabled]] )
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		error = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error Marking Application"
		print "Error Message = "+error
		sys.exit()
	#endIf 

	print "Marking "+appVersion+" was successful."
	
#endDef

#******************************************************************************
# Method Name:   	startApp
# Description:		Starts deployed application 
#
#****************************************************************************** 
def startApp(appName):
       	global AdminApp, AdminConfig, AdminControl
        readProperties( APP_PROPS_HOME + whereIsProperties )
	scope			=	getProperty("SCOPE")
	clusterName	 	=	getProperty("CLUSTER")
	serverName	  	=	getProperty("SERVER")
	nodeName		= 	getProperty("NODE")
	
        if(scope == "cluster"):
		scopeId = findScopeEntry(scope, clusterName)
		if (scopeId == 0):
			print "ERROR (startApp): Unable to find "+scope 
			return
		#endIf
        	
		clusterMembers = AdminConfig.list("ClusterMember",scopeId )
		clusterMemberList = clusterMembers.split(lineSeparator)

		for clusterMember in clusterMemberList:
			server = clusterMember.rstrip()
			serverName = AdminConfig.showAttribute( server, "memberName")
			nodeName = AdminConfig.showAttribute( server, "nodeName")
			
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (startApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to start application " +  appName
				continue
			#print "Starting Applicaiton "+appName+" on Cluster Member "+serverName
			rc = appControl("startApplication", appName, serverName)
			if (rc):
				print "INFO (startApp): Start Application "+appName+" failed on Cluster Member" + serverName
			else:
				print "INFO (startApp): Start Application "+appName+" was successful Cluster Member " + serverName
			#endIf

	elif(scope == "server" or scope == "node"):
		if(scope == "server"):
			scopeId = findScopeEntry(scope, serverName)
		elif(scope == "node"):
			scopeId = findScopeEntry(scope, nodeName)
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (startApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to start application " +  appName
				return
		if (scopeId == 0):
			print "ERROR (startApp):Unable to find "+scope 
			return
		#endIf
		#print "Starting Applicaiton "+appName+" on Server "+scopeName
		rc = appControl("startApplication", appName, serverName)
		if (rc):
			print "WARNING (startApp): Start Application "+appName+" failed."
		else:
			print "INFO (startpApp): Start Application "+appName+" was successful."
		#endIf
	else:
		print "ERROR (startApp): Method does not support the scope: "+scope 
		return

#endDef

#******************************************************************************
# Method Name:   	stopApp
# Description:	Stops deployed application 
#
#****************************************************************************** 
def stopApp(appName):
       	global AdminApp, AdminConfig, AdminControl
        
        readProperties( APP_PROPS_HOME + whereIsProperties )
	scope			=	getProperty("SCOPE")
	clusterName	 	=	getProperty("CLUSTER")
	serverName	  	=	getProperty("SERVER")
	nodeName		= 	getProperty("NODE")
	
        if(scope == "cluster"):
		scopeId = findScopeEntry(scope, clusterName)
		if (scopeId == 0):
			print "ERROR (stopApp): Unable to find "+scope 
			return
		#endIf
        	
		clusterMembers = AdminConfig.list("ClusterMember",scopeId )
		clusterMemberList = clusterMembers.split(lineSeparator)
				
		for clusterMember in clusterMemberList:
			server = clusterMember.rstrip()
			serverName = AdminConfig.showAttribute( server, "memberName")
			nodeName = AdminConfig.showAttribute( server, "nodeName")
			
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (stopApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to stop application " +  appName
				continue	
			#print "Stoping Applicaiton "+appName+" on Cluster Member "+serverName
			
			rc = appControl("stopApplication", appName, serverName)
			
			if (rc):
				print "WARNING (stopApp): Stop Application "+appName+" failed on Cluster Member "+serverName
			else:
				print "INFO (stopApp):Stop Application "+appName+" was successful on Cluster Member "+serverName
			#endIf

	elif(scope == "server" or scope == "node"):
		if(scope == "server"):
			scopeId = findScopeEntry(scope, serverName)
		elif(scope == "node"):
			scopeId = findScopeEntry(scope, nodeName)
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (stopApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to stop application " +  appName
				return
		if (scopeId == 0):
			print "ERROR (stopApp): Unable to find "+scope 
			return
		#endIf
		#print "Stoping Applicaiton "+appName+" on Server "+scopeName
		
		rc = appControl("stopApplication", appName, serverName)
		if (rc):
			print "WARNING (stopApp): Stop Application "+appName+" failed."
		else:
			print "INFO (stopApp): Stop Application "+appName+" was successful."
		#endIf
	else:
		print "ERROR (stopApp): Method does not support the scope: "+scope 
		return

#endDef
##########################################################################
# FUNCTION:
#    installEAR: Installs a J2EE application
# SYNTAX:
#    installEAR appName
# PARAMETERS:
#    appName 	-	Application name 
# USAGE NOTES:
#    Installs EAR file onto target as specified. 
##########################################################################
def installEAR ( appName ):
	readProperties( APP_PROPS_HOME + whereIsProperties )
	scope			=	getProperty("SCOPE")
	clusterName	 	=	getProperty("CLUSTER")
	serverName	  	=	getProperty("SERVER")
	nodeName		= 	getProperty("NODE")
	installed = ""

	appPath = DISTDIR +"/"+appName+".ear"
	
        if (serverName != "" and nodeName != ""):
                print ("INFO (installEAR): node=" + nodeName +" server=" + serverName +" appName="+appName )
                try:
                        _excp_ = 0
                        options = "-verbose -node "+nodeName+" -server "+serverName+" -distributeApp "
                        installed = AdminApp.install(appPath,options)
                        save()
                        #deleteEarFile(appPath)
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()     
                        installed = `_value_`                   
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp != 0):
                        msg = "ERROR (installEAR): Exception installing "+appName+" to "+nodeName+" "+serverName                   
                        print(msg)
                        print installed
                #endIf
        #endIf
        elif (clusterName != ""):
                print("INFO (installEAR): cluster="+clusterName+" appName="+appName)
                try:
                        _excp_ = 0
                        installed = AdminApp.install(appPath, " -verbose -cluster "+clusterName+" -distributeApp " )
                        save()
                        #deleteEarFile(appPath)
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()   
                        installed = `_value_`                   
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp != 0):
                        msg = "ERROR (installEAR): Exception installing "+appName+" to "+clusterName
                        print(msg)
                        print installed
                #endIf 	
        #endIf
        else:
                msg = "ERROR (installEAR): no serverName/nodeName nor clusterName specified"
                print (msg )
        #endElse
        if (len(installed) > 0):
                print("INFO (installEAR): " + installed )
        print ("INFO (installEAR): "+ appName + " DONE." )
        
#endDef

##########################################################################
# FUNCTION:
#    uninstallOlderVersions: Uninstalls all previous versions of the application
# SYNTAX:
#    uninstallOlderVersions name
# PARAMETERS:
#    name	-	Application Name
# USAGE NOTES:
#    Uninstalls J2EE application(s)
# RETURNS:
#    0    Success
#    1    Failure
##########################################################################
def uninstallOlderVersions(appName):
	retval = 1
	foundApp = 0
	try:		
		if isEmpty(appName):
			raise StandardError("ERROR (uninstallOlderVersions): Application name not specified")
				
		print ("INFO (uninstallOlderVersions): Running command: AdminApp.uninstall(%s)" % (appName))
		
		applications = AdminApp.list().split(lineSeparator);
		for app in applications:
			if(app.find(appName) >= 0):
				if(re.search(appName + "-" + "(\d+\.)+\d+(-SNAPSHOT)?$",app)):
					print("INFO: Uninstalling application: " + app)
					AdminApp.uninstall(app)
					save()
					foundApp = 1
					appExists = doesAppExist(appName )
					if (appExists):
						retval = 1
						print("ERROR (uninstallOlderVersions): failed to uninstallEAR application="+appName )
						break

	except:
		print("ERROR (uninstallOlderVersions): An error was encountered uninstalling the J2EE Application")
		retval = 1
	
	
	if(foundApp == 0):
		print ("INFO: (uninstallOlderVersions): Did not find any versions of application: " + appName)
	print ("INFO: (uninstallOlderVersions): DONE.")
	return retval
	
#endDef
	
#******************************************************************************
# File Name:   	stopAppsAll
# Description:	Stops all Applications
#
#  
#****************************************************************************** 
def stopAppsAll(distDir):
	ears = readDistributionDirectory(distDir)
	print("INFO (stopAppsAll): "+`ears` )
	appNames = parseApplicationNames(ears)	 
	#print("INFO (stopAppsAll): appNames="+`appNames` )
	appNameAddOnstr = ""
	for appName in appNames:
		appNameAddOnstr = appName
		if ( appNameAddOnstr.startswith('nav') and doesAppExist(appNameAddOnstr)):
			stopApp(appNameAddOnstr)
		else:
			print ("WARNING (stopAppsAll): application does not exsists or not a NAV application, " +appNameAddOnstr)
									 	
#endDef
#******************************************************************************
# File Name:   	startAppsAll
# Description:	start all Applications
#
#  
#****************************************************************************** 
def startAppsAll(distDir):	
	ears = readDistributionDirectory(distDir)
	print("INFO (startAppsAll): "+`ears` )
	appNames = parseApplicationNames(ears)	 
	print("INFO (stopAppsAll): appNames="+`appNames` )
	appNameAddOnstr = ""
	for appName in appNames:
		appNameAddOnstr = appName
		if ( appNameAddOnstr.startswith('nav') and doesAppExist(appNameAddOnstr)):
			startApp(appNameAddOnstr)
		else:
			print ("WARNING (startAppsAll): application does not exsists or not a NAV application, " +appNameAddOnstr)
									 	
#endDef

#******************************************************************************
# File Name:   	getTimeIntervalString
# Description:	returns a pretty print string of a TTG based on total minutes
#		and total seconds
#
#  
#****************************************************************************** 
def calcTTGString(totSec, divider, remaining):   
    avgSec = float(totSec) / float(divider)
    remSec = avgSec * remaining
    remHour = 0
    remMin = 0
    while remSec > 3600:
        remHour = remHour + 1
        remSec = remSec - 3600
    while remSec > 60:
        remMin = remMin + 1
        remSec = remSec - 60
    if remHour > 0:
        return "%d Hour(s) %d Minute(s) %d Seconds" %(remHour, remMin, remSec)
    if remMin > 0:
        return "%d Minute(s) %d Second(s)" %(remMin, remSec)
    return "%d Second(s)" %(remSec)
#endef
#******************************************************************************
# File Name:   	getTimeInterval
# Description:	returns (minutes, seconds) interval of two datetimes
#****************************************************************************** 
def getTimeInterval(start, end):
    seconds = int((end.getTimeInMillis() - start.getTimeInMillis()) / 1000)
    return seconds
#endef

def intervalToString(intervalSec):
    hour = 0
    minute = 0
    while intervalSec > 3600:
        hour = hour + 1
        intervalSec = intervalSec - 3600
    #endWhile
    while intervalSec > 60:
        minute = minute + 1
        intervalSec = intervalSec - 60
    if hour > 0:
        return "%d Hour(s) %d Minute(s) %d Seconds" %(hour, minute, intervalSec)
    if minute > 0:
        return "%d Minute(s) %d Second(s)" %(minute, intervalSec)
    return "%d Second(s)" %(intervalSec)
#******************************************************************************
# File Name:   	ininstallAll
# Description:	Iinstalls applications form a aistribution airectory
#
#  
#****************************************************************************** 
def installAll(distDir):
    ears = readDistributionDirectory(distDir)
    print("INFO (installAll): Deployment ears="+`ears` )
    appNames = parseApplicationNames(ears)	 
    #print("INFO (installAll): Deployment appNames="+`appNames` )
    appNameAddOnstr = ""
    installCounter = 1
    displayCounter = 1
    totalSeconds = 0
    for appName in appNames:
        print "#############################################################\n"
        if installCounter > 1:
            print "Installing ",appName," [ ", displayCounter," / ",len(appNames)," ]\t\t Estimated TTG: " + calcTTGString(totalSeconds, installCounter, len(appNames) - displayCounter) + "\n"
        else:
            print "Installing ",appName," [ ", displayCounter," / ",len(appNames)," ]\n"
        print "#############################################################"
        installCounter = installCounter + 1
        displayCounter = displayCounter + 1
        startTime = Calendar.getInstance()
        installEAR(appName)
        endTime = Calendar.getInstance()
        sec = getTimeInterval(startTime,endTime)
        totalSeconds = totalSeconds + sec
        print "Deployment time: %s" %(intervalToString(sec))
    #endfor    
    print "Total Deploy Time: " + intervalToString(totalSeconds)
#endef 

#******************************************************************************
# File Name:   	uninstallAll
# Description:	Uninstalls applications form a aistribution airectory
#
#  
#****************************************************************************** 
def uninstallAll(distDir):
    ears = readDistributionDirectory(distDir)
    print("INFO (uninstallAll): "+`ears` )
    appNames = parseApplicationNames(ears)	 
    #print("INFO (uninstallAll): Deployment appNames="+`appNames` )
    appNameAddOnstr = ""
    displayCounter = 1
    installCounter = 1
    totalMinutes = 0
    totalSeconds = 0

    for appName in appNames: 
	
        #appExists = doesAppExist(appName)	
		#remove this when having a working delta deploy
        appExists = 0	
	    #-------Modified to be able to always deploy SNAPSHOT modules (test@example.com) 
        if ( appName.endswith("-SNAPSHOT") ):
            print ( "INFO (uninstallAll): Found SNAPSHOT: " + appName + " - will force uninstall" )
            appExists = 0
        #-------------------------------------------------------------------------------------------------------------------------
    	
	#if (appExists):
	#	retval = 1
	#	print("WARNING (uninstallApplication): Application " + appName + " exists. Not uninstalling. Deleting the file on the file system." )
	#	deleteEarFile(DISTDIR +"/"+appName+".ear")
	#	continue
	#endif
    
	match = re.search("-" + "(\d+\.)+\d+(-SNAPSHOT)?$", appName)
	applicationId = appName[:match.start()]
	print ("i actually did something that took effect....")
	print ("INFO (uninstallApplication): Stripped application name is : " + applicationId)
    	
	if (noLeaveModuleFileExits(appName, distDir)):
		if (doesOlderAppVersionsExist(applicationId)):
			print "#############################################################\n"
			if installCounter > 1:
				print "Uninstalling ",appName," [ ", displayCounter," / ",len(appNames)," ]\t\t Estimated TTG: " + calcTTGString(totalSeconds, installCounter, len(appNames) -displayCounter) + "\n"
			else:
				print "Uninstalling ",appName," [ ", displayCounter," / ",len(appNames)," ]\n"
			#endif
			print "#############################################################"
			displayCounter = displayCounter + 1
			installCounter = installCounter + 1
			startTime = Calendar.getInstance()
			retval = uninstallOlderVersions(applicationId)
			endTime = Calendar.getInstance()
			sec = getTimeInterval(startTime,endTime)
			#print "Min: %d Sec: %d" %(min,sec)
			totalSeconds = totalSeconds + sec
			#print "TotMin: %d TotSec: %d" %(totalMinutes, totalSeconds)
			print "Uninstall time: %s" %(intervalToString(sec))
		else:
			print ("WARNING (uninstallAll): application does not exist, " +appName)
		#endif	
	else:
		print ("INFO (uninstallAll): Older versions of module are not uninstalled due to .leave file, " +appName)
		displayCounter = displayCounter + 1
        #endif
    #endfor
    print "Total Uninstall Time: " + intervalToString(totalSeconds)
#endDef

def appNameAddOn(appName):
	return appName + "App" 
#endDef	

###############  Main Section ###############################################
#stopAppsAll(DISTDIR)
uninstallAll(DISTDIR)
installAll(DISTDIR)
#startAppsAll(DISTDIR)
############### End Main Section #############################################

