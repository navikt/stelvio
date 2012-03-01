###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################
#******************************************************************************
# File Name:    utils6.py 
# Description:  This file contains utility procedures for use with 
#		WebSphere 6.0.2.x wsadmin scripting.  An assumption has
#		been made, in a number of cases, that servers will have 
#		unique names in the cell (as they should).
#
# History: 		
#		
#******************************************************************************	
#
# List of Procedures:
#******************************************************************************
# addHostAlias
# appControl
# createJAASAuthAlias
# doesAppExist
# findCell
# findConfigTarget
# findConfigTargetWithScope
# findDataSourceWithScope
# findJDBCProvider
# findJDBCProviderWithScope
# findNode
# findScopeEntry
# findServer
# findServerCluster
# findServerOnNode
# first
# getActiveDeploymentManagersList
# getActiveNodeAgentsList
# getConfigId
# getConfigItemId
# getConfigObject
# getConfigObjectScoped
# getConfigObjectValues
# getPort
# getProperty
# isPMIEnabled
# readProperties
# restartClusters
# restartServers
# setConfigObjectValues
# setTrace
# startCluster
# startClusters
# startServer
# startServers
# stopApp
# stopCluster
# stopClusters
# stopServer
# stopServers
# syncNodesToMaster
# toggleService
# updatePort
# wsadminToList
#--------------------------------------------------------------------
import sys, os, re
import java

from java import util
from java.io import *
from java.lang import *
from java.util import *
import java.io as io

def conditional(testCondition, trueValue, falseResult,):
        if(testCondition): return trueResult
        else: return falseResult
def regsub(pattern, string, replacement, flags=0, count=1):
        return re.compile(pattern, flags).sub(replacement, string, count)
def regexp(pattern, string, flags=0):
        if(re.compile(pattern, flags).search(string) ==None): return 0
        else: return 1
def regexpn(pattern, string, flags=0):
        r = re.compile(pattern, flags).subn("", string)
        return r[1]
#endDef

lineSeparator = java.lang.System.getProperty('line.separator')
props = None

#--------------------------------------------------------------------
# Procedure:	addHostAlias
# Description:  Adds host alias if it doesn't already exist
#
# Parameters:	hostName 	(eg: "default_host")
#		dnsHost 	(eg: "*")
#		port 		(eg: 9085)
#
# Returns:	None
#--------------------------------------------------------------------
def addHostAlias ( hostName, dnsHost, port ):
        global AdminConfig, AdminControl

        oHostNamePattern = [["hostname", dnsHost]]
        oPortPattern = [["port", port]]
	hostTarget = findConfigTarget(hostName, "VirtualHost")
	if (hostTarget == 0):
		print "Can not find "+hostName
		return
        #endIf

        # determin whether alias exists
        oNeedToDefine = 1
	aliasList = AdminConfig.list("HostAlias", hostTarget).split(lineSeparator)
        for HAEntry in aliasList:
        	oHostName = AdminConfig.showAttribute(HAEntry, "hostname")
                oPort = AdminConfig.showAttribute(HAEntry, "port")
                if oHostName == oHostNamePattern:
                        if oPort == oPortPattern:
                                print "default host has already defined Host Alias: "+HAEntry
                                print "new entry will not be added"
                                oNeedToDefine = 0
                                break 
                        #endIf 
                #endIf 
        #endFor 

        if (oNeedToDefine == 1):
                attrHA = [["hostname", dnsHost], ["port", port]]
                print "Adding Host Alias to "+hostName
                AdminConfig.create("HostAlias", hostTarget, attrHA )
        #endIf 
#endDef 

#--------------------------------------------------------------------
# Procedure:   	appControl
# Description: 	Start/stop application.
#
# Parameters:	command  (startAppplication, stopApplication)
#		appName 
#		appServer  
#    
# Returns:	0 = success
#--------------------------------------------------------------------
def appControl ( command, appName, appServer ):
        global AdminApp, AdminConfig, AdminControl
	appExists  = doesAppExist(appName)
        if ( not appExists ):
                print "WARNING (appControl) :  Application "+appName+" does not exist."
                return 1
        #endIf 

        if (findServer(appServer ) == 0):
                print "ERROR (appControl) :  Server "+appServer+" does not exist."
                return 1
        #endIf 
	
        try:
                _excp_ = 0
        	am = AdminControl.completeObjectName("type=ApplicationManager,process="+appServer+",*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                am = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                #print "ERROR (appControl): "+am
                return 1
        #endIf 

        try:
                _excp_ = 0
                error = AdminControl.invoke(am, command, appName )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                error = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                #print "ERROR (appControl): = "+error
                return 1
        #endIf 

        return 0
#endDef 

#--------------------------------------------------------------------
# Procedure:   	createJAASAuthAlias
# Description:  Create an authentication alias 
#
# Parameters:	cellName
#		authAliasName 
#		user
#		password
#		desc
#
# Returns:	targetName = success;
#		0 = does not exist
#
# History:
#--------------------------------------------------------------------
def createJAASAuthAlias ( cellName, authAliasName, user, password, desc ):
        global AdminApp, AdminConfig

        # Check if alias already exists	
        listJAAS = AdminConfig.list("JAASAuthData").split(lineSeparator)
	if (listJAAS != ['']):
		for eachJAAS in listJAAS:
			jaasname = AdminConfig.showAttribute(eachJAAS, "alias")
			if (jaasname == authAliasName):
				print ""+authAliasName+" already exists"	
                                return 0
                        #endIf 
                #endFor 
        #endIf

        # Get the config ID for the cell's security object
        sec = AdminConfig.getid("/Cell:"+cellName+"/Security:/")
        attrs0 = [["alias", authAliasName], ["userId", user], ["password", password], ["description", desc]]
	try:
      		_excp_ = 0
        	authDataAlias = AdminConfig.create("JAASAuthData", sec, attrs0 )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                authDataAlias = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Caught Exception creating JVM property"
                print authDataAlias
                return
        #endIf 

        return authDataAlias
#endDef 

#--------------------------------------------------------------------
# Procedure:	doesAppExist
# Description:  Determine whether an appication exists
#
# Parameters:	appName
#
# Returns:	1 (exists)
#		0 (does not exist)
#--------------------------------------------------------------------
def doesAppExist ( appName ):
		
        appExists = 1
        application = AdminConfig.getid("/Deployment:"+appName+"/" )
        print ("doesAppExist appName="+appName+" installedAppID="+application )
        if (len(application) == 0):
                appExists = 0
                print("doesAppExist: FALSE for appName="+appName )
      
        #endElse
        return appExists
#endDef 

#--------------------------------------------------------------------
# Procedure:	doesAppExist
# Description:  Determine whether an appication exists
#
# Parameters:	appName
#
# Returns:	1 (exists)
#		0 (does not exist)
#--------------------------------------------------------------------
def doesOlderAppVersionsExist ( appName ):
		applications = AdminApp.list().split(lineSeparator);
		
		for app in applications:
			if(app.find(appName) >= 0):
				return 1;
			
		return 0;
#endDef 



#--------------------------------------------------------------------
# Procedure:   	findCell
# Description: 	Determine whether a cell exists
#
# Parameters:	cellName 
#
# Returns:	cellName = success; 
#		0 = cell does not exist
#--------------------------------------------------------------------
def findCell ( cellName ):
        global AdminApp, AdminConfig

        oCellToUse = findConfigTarget(cellName, "Cell" )
        return oCellToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findConfigTarget
# Description: 	Determine if there is a config element for given
#		name and type
#
# Parameters:	nameSearch	 
#		type		(Cell, Node, JDBCProvider)
#
# Returns:	targetName = success 
#		0  = does not exist
#--------------------------------------------------------------------
def findConfigTarget ( nameSearch, type ):
        global AdminApp, AdminConfig

        elements = AdminConfig.list(type)
	if (elements == " "):
		return 0
	#endIf
	elementList = elements.split(lineSeparator)
	for element in elementList:
		element=element.rstrip()
		if (len(element) > 0 ):
			name=AdminConfig.showAttribute(element,"name")
			if (nameSearch == name):
				return element
			#endIf
		#endIf
	#endFor
	return 0	
#endDef 

#--------------------------------------------------------------------
# Procedure:   	findConfigTargetWithScope
# Description: 	Determine if there's a config element for a given scope
#		type, scope name, and search string
#
# Parameters:	scope 		(Cell or Node)
#		scopeName	(cellName or nodeName)
#		nameSearch	(name string to search)
#		type 		(Server, JDBCProvider, ...)
#
# Returns:	targetName = success
#		0 = does not exist
#--------------------------------------------------------------------
def findConfigTargetWithScope ( scope, scopeName, nameSearch, type ):
        global AdminApp, AdminConfig

        scopeToUse = findScopeEntry(scope, scopeName )
        if (scopeToUse == 0):
                return 0
        #endIf 

        elements = AdminConfig.list(type, scopeToUse )
	elementList = elements.split(lineSeparator)
	for element in elementList:
		element = element.rstrip()
		if ((element.find(scopeName+"|") >= 0) or (element.find(nameSearch+"|") >= 0)):
			if (len(element) > 0 ):
				name = AdminConfig.showAttribute(element, "name")
				if (nameSearch == name):
					return element
				#endIf
			#endIf
		#endIf
	#endFor
        return 0
#endDef 

#--------------------------------------------------------------------
# Procedure:	findDataSourceWithScope
# Description:	Determines whether a datasource exists
#
# Parameters:	scope		(Cell or Node)
#		scopeName	(cellName or nodeName)
#		dataSourceName	 
#
# Returns:	targetName = success;
#		0 = does not exist
#--------------------------------------------------------------------
def findDataSourceWithScope ( scope, scopeName, dataSourceName ):
        global AdminApp, AdminConfig

        datasource = findConfigTargetWithScope(scope, scopeName, dataSourceName, "DataSource" )
        return datasource
#endDef 

#--------------------------------------------------------------------
# Procedure:   	findJDBCProvider
# Description:  Determine whether JDBC Provider exists. 
#
# Parameters:	name	JDBC Provider name
#
# Returns:	targetName=success; 
#		0 = does not exist
#--------------------------------------------------------------------
def findJDBCProvider ( name ):
        global AdminApp, AdminConfig

        oNameToUse = findConfigTarget(name+"*", "JDBCProvider" )
        return oNameToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findJDBCProviderWithScope
# Description:	Determine whether JDBC Provider exists for scope 
#
# Parameters: 	scope 		(Cell or Node)
#		scopeName	(cellName or nodeName)
#		name           	(JDBC provider name)
#
# Returns:	targetName=success; 
#		0 = does not exist
#--------------------------------------------------------------------
def findJDBCProviderWithScope ( scope, scopeName, name ):
        global AdminApp, AdminConfig

        oNameToUse = findConfigTargetWithScope(scope, scopeName, name, "JDBCProvider" )
        return oNameToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findNode
# Description:	Determines whether a node exists 
#
# Parameters:	nodeName 
#
# Returns:	nodeName = success; 
#		0 = node does not exist
#--------------------------------------------------------------------
def findNode ( nodeName ):
        global AdminApp, AdminConfig

	oNodeToUse = findConfigTarget(nodeName,"Node")
        return oNodeToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findScopeEntry
# Description:	Find cell, node, cluster, or server  entry
#
# Parameters:	scope 		(Cell | Node| Cluster | Server) 
#		scopeName	(cellName | nodeName | clusterName | serverName)
#
# Returns:	targetName = success
#                        0 = entry does not exist
#--------------------------------------------------------------------
def findScopeEntry ( scope, scopeName ):
        global AdminApp, AdminConfig
        scopeEntry = 0
        if (scope.lower() == "cell"):
                scopeEntry = findCell(scopeName )
        elif (scope.lower() == "node"):
                scopeEntry = findNode(scopeName )
	elif (scope.lower() == "cluster"):
		scopeEntry = findServerCluster(scopeName )
	elif (scope.lower() == "server"):
		scopeEntry = findServer(scopeName )
        #endIf 

        return scopeEntry
#endDef 

#--------------------------------------------------------------------
# Procedure:	findServer
# Description: 	Determine whether server exists
#
# Parameters:	serverName 
#
# Returns:	serverName=success; 
#		0 =server does not exist
#--------------------------------------------------------------------
def findServer ( serverName ):
        global AdminApp, AdminConfig

	oServerToUse = findConfigTarget(serverName,"Server")
        return oServerToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findServerCluster
# Description: 	Determines whether the cluster exists
#
# Parameters:	serverClusterName 
#
# Returns:	serverClusterName=succes; 
#		0 = server cluster does not exist
#--------------------------------------------------------------------
def findServerCluster ( serverClusterName ):
        global AdminApp, AdminConfig
        oServerClusterToUse = findConfigTarget(serverClusterName, "ServerCluster" )
        return oServerClusterToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	findServerOnNode
# Description: 	Determine whether server exists on a given node
#
# Parameters:	serverName 
#		nodeName
#
# Returns:	serverName=success; 
#		0 =server does not exist
#--------------------------------------------------------------------
def findServerOnNode ( serverName, nodeName ):
        global AdminApp, AdminConfig

        oServerToUse = findConfigTargetWithScope("Node", nodeName, serverName, "Server" )
        return oServerToUse
#endDef 

#--------------------------------------------------------------------
# Procedure:	first
# Description: 	Get the first item in a list
#
# Parameters:   list
#
# Returns:	first item
#--------------------------------------------------------------------
def first ( list ):
	retList = wsadminToList(list)
        return retList[0]
#endDef 

#--------------------------------------------------------------------
# Procedure:   	getActiveDeploymentManagersList 
# Description:	Displays the list of Deployment Manager servers 
#              	currently active in the cell.
#
# Parameters:	None
#
# Returns:	deploymentMgrsList
#--------------------------------------------------------------------
def getActiveDeploymentManagersList (  ):
        global AdminControl

        try:
                _excp_ = 0
                deploymentMgrsList = AdminControl.queryNames("processType=DeploymentManager,*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                deploymentMgrsList = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Error getting Deployment Managers list"
                print "Error Message = "+deploymentMgrsList
                return 
        #endIf 
        return deploymentMgrsList
#endDef 

#--------------------------------------------------------------------
# Procedure:   getActiveNodeAgentsList
# Description: Displays the list of NodeAgent servers currently
#              active in the cell.
#--------------------------------------------------------------------
def getActiveNodeAgentsList (  ):
        global AdminControl

        try:
                _excp_ = 0
                nodeAgentsList = AdminControl.queryNames("processType=NodeAgent,*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                nodeAgentsList = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Error getting Active NodeAgents list"
                print "Error Message = "+nodeAgentsList
                return 
        #endIf 
        return nodeAgentsList
#endDef 

#--------------------------------------------------------------------
# Procedure:	getConfigId
# Description:	Gets the Config Identifier
#
# Parameters:	scope
#		scopeName
#		nodeName    (only used for server scope)
#		objectType
#
# Returns:	ConfId
#--------------------------------------------------------------------
def getConfigId (scope, scopeName, nodeName, objectType):
	global AdminConfig

	scope = scope.title()
	if (scope == "Cell"):
		confId = AdminConfig.getid("/Cell:"+scopeName+"/"+objectType+":/")
	elif (scope == "Node"):
		confId = AdminConfig.getid("/Node:"+scopeName+"/"+objectType+":/")
	elif (scope == "Cluster"):
		confId = AdminConfig.getid("/ServerCluster:"+scopeName+"/"+objectType+":/")
	elif (scope == "Server"):
		confId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+scopeName+"/"+objectType+":/")
	#endIf
	return confId
#endDef
	  	
#--------------------------------------------------------------------
# Procedure:	getConfigItemId
# Description:	Gets the Config Item Identifier
#
# Parameters:	scope
#		scopeName
#		nodeName    (only used for server scope)
#		objectType
#		item
#
# Returns:	ConfItemId
#--------------------------------------------------------------------
def getConfigItemId (scope, scopeName, nodeName, objectType, item):
	global AdminConfig

	scope = scope.title()
	if (scope == "Cell"):
		confItemId = AdminConfig.getid("/Cell:"+scopeName+"/"+objectType+":"+item)
	elif (scope == "Node"):
		confItemId = AdminConfig.getid("/Node:"+scopeName+"/"+objectType+":"+item)
	elif (scope == "Cluster"):
		confItemId = AdminConfig.getid("/ServerCluster:"+scopeName+"/"+objectType+":"+item)
	elif (scope == "Server"):
		confItemId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+scopeName+"/"+objectType+":"+item)
	#endIf
	return confItemId
#endDef

#--------------------------------------------------------------------
# Procedure:	getConfigObject
# Description:	Gets the object identifier
# Parameters: 	objectName
#		objectType
#
# Returns:	objID
#		1 = error
# History:
#--------------------------------------------------------------------
def getConfigObject ( objectName, objectType ):
        global AdminConfig

        try:
                _excp_ = 0
                objID = AdminConfig.getid("/"+objectType+":"+objectName+"/" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                objID = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Error getting object ID of Config Object"
                print "Error Message = "+objID
                return 1
        #endIf 

        if (len(objID) == 0):
                print objectType+" named "+objectName+" not found"
                return 1
        #endIf 

        return objID
#endDef 


#--------------------------------------------------------------------
# Procedure:   	getConfigObjectScoped
# Description: 	Get the object 
#
# Parameters: 	scopeObjectID
#		objectType
#
# Returns:    	obj 
#--------------------------------------------------------------------
def getConfigObjectScoped ( scopeObjectID, objectType ):
        global AdminConfig

        try:
                _excp_ = 0
                obj = AdminConfig.list(objectType, scopeObjectID )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                obj = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                global errorInfo
                print "Error getting "+objectType+" object"
                print "Error Message = "+obj
                print errorInfo
                return 0
        #endIf 
        return obj
#endDef 

#------------------------------------------------------------------------
# Procedure:	getConfigObjectValues
# Description:	Get values for a runtime Object
#
# Parameters:	objectNameList  ([server1,server2])
#		objectType      ("Server")
#		args		(["name", "node"])
#
# Returns:	A list of values for a runtime object
#------------------------------------------------------------------------
def getConfigObjectValues (objectNameList, objectType, args):
	global AdminConfig

	resultList = []
	objectList = objectNameList.split(',')
	for objectName in objectList:
		objID = getConfigObject(objectName, objectType)
		if (objID != 1):
			for arg in args:
				newArg = arg.lower()
				if (newArg == "node"):
					regex = re.compile("nodes/(\\w*)/")
					aVal = regex.findall(objID)
					argValue0 = aVal[0]
				else:
					try:
      						_excp_ = 0
                				argValue = AdminConfig.showAttribute(objID, arg)
        				except:
                				_type_, _value_, _tbck_ = sys.exc_info()
                				argValue = `_value_`
                				_excp_ = 1
        				#endTry 
        				if (_excp_ ):
                				print "Error getting value of attribute "+arg
                				print "Error Message = "+argValue
        				#endIf 
				#endIf

			#endFor
			resultList += [[argValue, argValue0]]
		#endIf
	#endFor
	return resultList
#endDef

#------------------------------------------------------------------------
# Procedure:	getPort
# Description:	This procedure determines the WC_defaulthost port for
#		a given server/node combination
#
# Parameters:	nodeName, serverName
#
# Returns:	port number (endPoint)
#
#------------------------------------------------------------------------
def getPort ( nodeName, serverName ):
	global AdminConfig

	node = AdminConfig.getid("/Node:"+nodeName+"/")
	serverEntries = AdminConfig.list('ServerEntry', node).split(lineSeparator)

	for serverEntry in serverEntries:
		sName = AdminConfig.showAttribute(serverEntry, "serverName")
		if sName == serverName:
			aServerEntry = AdminConfig.getid("/ServerEntry:"+serverName+"/")
			endpoints = AdminConfig.showAttribute(aServerEntry, "specialEndpoints")
			specialEndPoints = wsadminToList(endpoints)

			for specialEndPoint in specialEndPoints:
				endPointNm = AdminConfig.showAttribute(specialEndPoint, "endPointName")
       				if endPointNm == "WC_defaulthost":
	 				ePoint = AdminConfig.showAttribute(specialEndPoint, "endPoint")
					port = AdminConfig.showAttribute(ePoint, "port")
					break
				#endIf
			#endFor
		#endIf
	#endFor
	return port
#endDef
#------------------------------------------------------------------------
# Procedure:	getProperty
# Description:	Get the value of a property and return an empty string
#		     	if it does not exist.  Please note than an empty string
#			"" inserted in the configuration means a blank value.
#
# Parameters:	propertyName
#------------------------------------------------------------------------
def getProperty (propertyName):
	pval = props.getProperty(propertyName)
	if pval == None:
		return ""
	#endIf

	return pval.strip()
#endDef

#------------------------------------------------------------------------
# Procedure:	isPMIEnabled
# Description: 	Determine whether performance monitoring is enabled or not.
#
# Parameters:  	inServerName = Server name
#
# Returns: 	0 = disabled; 1 = enabled
#
#------------------------------------------------------------------------
def isPMIEnabled ( inServerName ):
        global AdminControl

        rc = 1
        perfName = AdminControl.completeObjectName("type=Perf,", "process="+inServerName+",*" )
        if ("perfName" == ""):
                rc = 0
        #endIf 

        return rc
#endDef 

#--------------------------------------------------------------------
# Procedure:	readProperties
# Description:	Create a java properties class and init the props 
#			from the provided file.
#
# Parameters:	fileName
#
#--------------------------------------------------------------------
def readProperties ( fileName ):
	global props

	props = Properties()
	f = FileInputStream(fileName)
	props.load(f)
	return
#endDef

#--------------------------------------------------------------------
# Procedure:   	restartClusters
# Description:	Restart clusters in list
#
# Parameters:   clusterNameList
#
# Returns:
#
# Note: This procedure only functions in a WAS ND environment
#--------------------------------------------------------------------
def restartClusters ( clusterNameList ):
        for clusterName in clusterNameList:
                stopClusters([clusterName] )
                startClusters([clusterName] )
        #endFor 
#endDef 

#--------------------------------------------------------------------
# Procedure:   	restartServers
# Description:  Stop/Start servers for a given node
#
# Parameters:  	serverNodeList
# 
# Returns:	None
#
# Note: This procedure only functions in a ND environment
#--------------------------------------------------------------------
def restartServers ( serverNodeList ):
        stopServers(serverNodeList )
        startServers(serverNodeList )
#endDef 

#----------------------------------------------------------------------
# Procedure:	setConfigObjectValues
# Description: 	Sets Configuration Object values
#
# Parameters:	objectNameList
#		objectType
#		args 
# Returns:      error msg if appropriate
#
# History:	03/10/06:  Added DebugMode, DebugArguments	
#----------------------------------------------------------------------
def setConfigObjectValues ( objectNameList, objectType, args ):
        global AdminConfig

        for objectName in objectNameList:
	     objectNm = objectName.split(' ')
	     for obj in objectNm:
	        for name, value in args:
                  objectType = objectType.lower()
                  if ( objectType == "javavirtualmachine" ):
                       # Get the parent object of the JVM since JVM's are not named objects
                       scopingID = getConfigObject(obj, "Server" )
                       # Get the JVM object of the specified server
                       jvm = getConfigObjectScoped(scopingID, "JavaVirtualMachine" )
                       name = name.lower()
		       
                       if ( name == "heapmin" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["initialHeapSize", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                       	    if (_excp_ ):
                              	 print "Error setting JVM Minimum Heap Size"
                                 print "Error Message = "+error
                                 return 
                            else:
				 print obj+" JVM Minimum Heap Size set to "+value
                            #endElse 

                       elif ( name == "heapmax" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["maximumHeapSize", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                                 print "Error setting JVM Maximum Heap Size"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" JVM Maximum Heap Size set to "+value
                            #endElse 

                       elif ( name == "classpath" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["classpath", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                              	 print "Error setting JVM Classpath"
                                 print "Error Message = "+error
                                 return 
                            else:
                               	 print obj+" JVM Classpath set to "+value
                            #endElse 
                       elif ( name == "genericjvmarguments" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["genericJvmArguments", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                              	 print "Error setting JVM Generic Arguments"
                                 print "Error Message = "+error
                                 return 
                            else:
                               	 print obj+" JVM Generic Arguments set to "+value
                            #endElse 
                       elif ( name == "debugmode" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["debugMode", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                              	 print "Error setting JVM Debug Mode"
                                 print "Error Message = "+error
                                 return 
                            else:
                               	 print obj+" JVM Debug Mode is set to "+value
                            #endElse 
                       elif ( name == "debugarguments" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["debugArgs", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                              	 print "Error setting JVM Debug Arguments"
                                 print "Error Message = "+error
                                 return 
                            else:
                               	 print obj+" JVM Debug Arguments are set to "+value
                            #endElse 

                       elif ( name == "verbosegc" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [["verboseModeGarbageCollection", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                               	 print "Error setting JVM Verbose GC Collection"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" JVM Verbose GC Collection set to "+value
                            #endElse 

                       else:
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(jvm, [[name, value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                               	 print "Error setting "+name+" to "+value
                                 print "Error Message = "+error
                                 return 
                            #endIf 
                       #endElse

                  elif ( objectType == "sessionmanager" ):
                       scopingID = getConfigObject(obj, "Server" )
                       sm = AdminConfig.list("SessionManager", scopingID )
                       tp = AdminConfig.showAttribute(sm, "tuningParams" )
                       name = name.lower()
                       if ( name == "maxinmemorysessioncount" ):
                            try:
                                _excp_ = 0
                                error = AdminConfig.modify(tp, [["maxInMemorySessionCount", value]] )
                            except:
                                _type_, _value_, _tbck_ = sys.exc_info()
                                error = `_value_`
                                _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                               	print "Error setting Max in Memory Session Count"
                                print "Error Message = "+error
                                return 
                            else:
                                print obj+" Max in Memory Session Count set to "+value
                            #endElse 
                       else:
                       	    try:
                                _excp_ = 0
                                error = AdminConfig.modify(jvm, [[name, value]] )
                            except:
                              	_type_, _value_, _tbck_ = sys.exc_info()
                               	error = `_value_`
                               	_excp_ = 1
                            #endTry 
                            if (_excp_ ):
                               	print "Error setting "+name+" to "+value
                               	print "Error Message = "+error
                               	return 
                            #endIf 
                       #endIf

		  elif ( objectType == "threadpool" ):
                       # Get a parent/scoping object of the Web Container since 
                       # Web Containers are not named objects
                       scopingID = getConfigObject(obj, "Server" )
                       # Get the Thread Pool Manager of the specified Server
                       tpManager = getConfigObjectScoped(scopingID, "ThreadPoolManager" )
		       threadPool = AdminConfig.showAttribute(tpManager, "threadPools").split(' ')
		       name = name.lower()
		       for tp in threadPool:
		            if tp.find('WebContainer') >= 0:
                                 if ( name == "threadpoolmin" ):
                            	      try:
                                           _excp_ = 0
                                           error = AdminConfig.modify(tp, [["minimumSize", value]] )
                                      except:
                                 	   _type_, _value_, _tbck_ = sys.exc_info()
                                 	   error = `_value_`
                                 	   _excp_ = 1
                            	      #endTry 
                            	      if (_excp_ ):
                                           print "Error setting Web Container Minimum Thread Pool Size"
                                           print "Error Message = "+error
                                           return 
                            	      else:
                                 	   print obj+" Web Container Minimum Thread Pool Size set to "+value
                            	      #endElse 
			 	 #endIf
                       		 elif ( name == "threadpoolmax" ):
                            	      try:
                                           _excp_ = 0
                               		   error = AdminConfig.modify(tp, [["maximumSize", value]] )
                              	      except:
                               	           _type_, _value_, _tbck_ = sys.exc_info()
                              	           error = `_value_`
                               	           _excp_ = 1
                            	      #endTry 
                            	      if (_excp_ ):
                              		   print "Error setting Web Container Maximum Thread Pool Size"
                               		   print "Error Message = "+error
                               		   return 
                            	      else:
                               		   print obj+" Web Container Maximum Thread Pool Size set to "+value
                            	      #endElse 
                       		 elif ( name == "isgrowable" ):
                                      try:
                                	   _excp_ = 0
                                	   error = AdminConfig.modify(tp, [["isGrowable", value]] )
                            	      except:
                                	   _type_, _value_, _tbck_ = sys.exc_info()
                                	   error = `_value_`
                                	   _excp_ = 1
                            	      #endTry 
                            	      if (_excp_ ):
                               		   print "Error setting Web Container Thread Pool IsGrowable"
                                	   print "Error Message = "+error
                                	   return 
                            	      else:
                                	   print obj+" Web Container is Thread Pool IsGrowable set to "+value
                            	      #endElse 
				 #endIf
			    #endIf
		       #endFor

                  elif ( objectType == "webcontainer" ):
                       # Get a parent/scoping object of the Web Container since 
                       # Web Containers are not named objects
                       scopingID = getConfigObject(obj, "Server" )
                       wc = getConfigObjectScoped(scopingID, "WebContainer" )
		       name = name.lower()
                       if ( name == "enableservletcaching" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(wc, [["enableServletCaching", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                                 print "Error setting Web Container Enable Servlet Caching"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" Web Container Enable Servlet Caching set to "+value
                            #endElse 
		       #endIf	
		  #endIf
                  elif ( objectType == "objectrequestbroker" ):
                       # Get a parent/scoping object of the ORB
                       scopingID = getConfigObject(obj, "Server" )
                       orb = getConfigObjectScoped(scopingID, "ObjectRequestBroker" )
		       name = name.lower()
                       if ( name == "nolocalcopies" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(orb, [["noLocalCopies", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                                 print "Error setting ORB Pass By Reference"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" ORB Pass By Reference set to "+value
                            #endElse 
		       elif ( name == "requesttimeout" ):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(orb, [["requestTimeout", value]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                                 print "Error setting ORB Request Timeout"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" ORB Request Timeout set to "+value
                            #endElse 
		       #endIf	
		  elif ( objectType == "statemanagement" ):
                       scopingID = getConfigObject(obj, "Server" )
		       name = name.lower()
		       if (name == "initialstate"):
                            try:
                                 _excp_ = 0
                                 error = AdminConfig.modify(scopingID, [["stateManagement", [["initialState", value]]]] )
                            except:
                                 _type_, _value_, _tbck_ = sys.exc_info()
                                 error = `_value_`
                                 _excp_ = 1
                            #endTry 
                            if (_excp_ ):
                                 print "Error setting Initial Server State"
                                 print "Error Message = "+error
                                 return 
                            else:
                                 print obj+" Inital Server State set to "+value
                            #endElse 
		       #endIf	
		  #endIf
		#endFor
	     #endFor
        #endFor 
#endDef 

#--------------------------------------------------------------------
# Procedure:	setServerId
# Description:	Sets the Server identifier
# Parameters:	nodeName
#		serverName
#
# Returns:	serverId = success
#		0 = failure
#--------------------------------------------------------------------
def setServerId ( nodeName, serverName ):
        global AdminConfig

        serverId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName )
        if (len(serverId) == 0):
                return 0
        #endIf 
        return serverId
#endDef 


#--------------------------------------------------------------------
# Procedure:   	setTrace
# Description: 	Procedure used to set the trace specification on a 
#              	server 
# Parameters:  	serverName - Name of server you are setting trace on
#              	nodeName - Name of node the server is on
#              	isRuntime - true/false value indicating whether you
#                          are changing a runtime on-the-fly value (true)
#                          or changing a configuration value (false)
#           	traceStr - Trace String you want to set on the server
#		traceLog - used for setting up the trace log
#
# Returns:	0 = success
#		1 = failure
#--------------------------------------------------------------------
def setTrace ( serverName, nodeName, isRuntime, traceStr, traceLog ):
        global AdminConfig, AdminControl

        # Check if we are changing the MBeans (runtime) or XML configuration
        if (isRuntime == "true"):
                try:
                        _excp_ = 0
                        traceService = AdminControl.queryNames("type=TraceService,node="+nodeName+",process="+serverName+",*" )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        traceService = `_value_`
                        _excp_ = 1
                #endTry 

                if (_excp_ ):
                        print "Error getting Trace Service"
                        print "Trace Service = "+traceService
			return 1
                #endIf 

                # Set the trace specification for the indicated server
                try:
                        _excp_ = 0
                        error = AdminControl.setAttribute(traceService, "traceSpecification", traceStr )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        error = `_value_`
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "Error setting trace specification"
                        print "Error Message = "+error
			return 1
                #endIf 
                # Check if we need to set a trace log file
                if (len(traceLog) > 0):
			argsList = wsadminToList(traceLog)
                        tLog = argsList[0]
                        # Set the Trace Log value
                        try:
                                _excp_ = 0
                                error = AdminControl.invoke(traceService, "setTraceOutputToFile", [tLog, 20, 1, "basic"] )
                        except:
                                _type_, _value_, _tbck_ = sys.exc_info()
                                error = `_value_`
                                _excp_ = 1
                        #endTry 
                        if (_excp_ ):
                                print "Error setting the runtime Trace Log value"
                                print "Error Message = "+error
                                return 1
                        #endIf 
		#endIf
        else:
                # Get the ID of the server we want to trace
                try:
                        _excp_ = 0
                        serverID = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        serverID = `_value_`
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "Error getting server ID"
                        print "Server ID = "+serverID
                        return 1
                #endIf 

                # Get the TraceService of the server
                try:
                        _excp_ = 0
                        traceService = AdminConfig.list("TraceService", serverID )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        traceService = `_value_`
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "Error getting Trace Service"
                        print "Trace Service = "+traceService
                        return 1
                #endIf 

                # Set the trace specification for the indicated server
                try:
                        _excp_ = 0
                        error = AdminConfig.modify(traceService, [["startupTraceSpecification", traceStr]] )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        error = `_value_`
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "Error setting trace specification"
                        print "Error Message = "+error
			return 1
                #endIf 

                # Check if we need to set a trace log file
                if (len(traceLog) > 0):
			argsList = wsadminToList(traceLog)
                        tLog = argsList[0]
                        # Get the Trace Log Object
                        try:
                                _excp_ = 0
                                traceLogObj = AdminConfig.list("TraceLog", traceService )
                        except:
                                _type_, _value_, _tbck_ = sys.exc_info()
                                traceLogObj = `_value_`
                                _excp_ = 1
                        #endTry 
                        if (_excp_ ):
                                print "Error getting Trace Log object"
                                print "TraceLog Object = "+traceLogObj
                                sys.exit(1)
                        #endIf 
                        # Set the Trace Log value
                        try:
                                _excp_ = 0
                                error = AdminConfig.modify(traceLogObj, [["fileName", tLog]] )
                        except:
                                _type_, _value_, _tbck_ = sys.exc_info()
                                error = `_value_`
                                _excp_ = 1
                        #endTry 
                        if (_excp_ ):
                                print "Error setting the Trace Log value"
                                print "Error Message = "+error
                                print 1
                        #endIf 
                #endIf 

                # Save the changes to the XML repository
                AdminConfig.save( )
		return 0
        #endElse 
#endDef 

#--------------------------------------------------------------------
# Procedure:	startCluster
# Description: 	Start the cluster for a given cell
#
# Parameters:	cellName
#		clusterName
#
# Returns:	None
# -------------------------------------------------------------------
def startCluster ( cellName, clusterName ):
        global AdminConfig, AdminControl

        # Check if cluster exists
        if  (findServerCluster(clusterName ) == 0):
                print "Error: Cluster "+clusterName+" doesn't exist."
	else:
        	cluster = AdminControl.completeObjectName("cell="+cellName+",type=Cluster,name="+clusterName+",*")
		clusterState = AdminControl.getAttribute(cluster, "state")
		if (clusterState.find('running')) > 0:
			print "Error -- Server "+clusterName+" is already running."
		else:
			try:
      				_excp_ = 0
        			result = AdminControl.invoke(cluster, "start" )
        		except:
                		_type_, _value_, _tbck_ = sys.exc_info()
                		result = `_value_`
                		_excp_ = 1
        		#endTry 
        		if (_excp_ ):
                		print "Caught Exception starting cluster."
                		print result
                		return
			else:
				print ""+clusterName+" has started sucessfully."
        		#endIf 
		#endIf
	#endIf
#endDef 

#--------------------------------------------------------------------
# Procedure:   	startClusters
# Description:	Start all clusters in list
#
# Parameters:   clusterNameList
#
# Returns:	None
#
# Note: This procedure only functions in a ND environment
#--------------------------------------------------------------------
def startClusters ( clusterNameList ):
        global AdminControl

	clusterList = clusterNameList.split(',')
	cellName = AdminControl.getCell()
        for clusterName in clusterList:
		startCluster(cellName, clusterName)
        #endFor 
#endDef 

#--------------------------------------------------------------------
# Procedure:	StartServer
# Description:	Procedure used to start an individual app server
#
# Parameters:	nodeName
#		ServerName
#
# Returns:	None
#--------------------------------------------------------------------
def startServer (nodeName, serverName):
	global AdminControl

	if (findServerOnNode(serverName, nodeName) == 0):
		print "Error: Server: "+serverName+" on Node: "+nodeName+" does not exist."
	else:

		server  = AdminControl.completeObjectName("type=Server,node="+nodeName+",name="+serverName+",*")

		if len(server) > 0:
			serverState = AdminControl.getAttribute(server, "state")
			print serverName+" current state is "+serverState
			if (serverState == "STARTED"):
				print "Error -- Server "+serverName+" is already running on node "+nodeName
			#endIf
		else:
			try:
      				_excp_ = 0
				result = AdminControl.startServer(serverName, nodeName)
        		except:
                		_type_, _value_, _tbck_ = sys.exc_info()
                		result = `_value_`
                		_excp_ = 1
        		#endTry 
        		if (_excp_ ):
                		print "Caught Exception starting server "+serverName
                		print result
                		return
			else:
				print ""+serverName+" on "+nodeName+" has started successfully."
        		#endIf 
		#endIf
	#endIf
#endDef

#--------------------------------------------------------------------
# Procedure:    startServers
# Description:  Procedure used to start multiple server processes. 
#
# Parameters: 	serverNodeList - List of server names and nodes the servers are on 
#
#		Example call: startServers (testServerName, testNodeName)
#
# Returns:  	None
#--------------------------------------------------------------------
def startServers ( serverNodeList ):
        global AdminControl

	for serverName, nodeName in serverNodeList:
        	print "Starting Server "+serverName+" on node "+nodeName
		startServer(nodeName,serverName)
	#endFor
#endDef 

#--------------------------------------------------------------------
# Procedure:	stopCluster
# Description:  Stop the cluster for a given cell
#
# Parameters:	cellName
#		clusterName
#
# Returns:	None 
#--------------------------------------------------------------------
def stopCluster ( cellName, clusterName ):
        global AdminControl

        # Check if cluster exists
        if  (findServerCluster(clusterName ) == 0):
                print "Error: Cluster "+clusterName+" doesn't exist."
	else:
        	cluster = AdminControl.completeObjectName("cell="+cellName+",type=Cluster,name="+clusterName+",*")
		clusterState = AdminControl.getAttribute(cluster, "state")
		if (clusterState.find('stopped')) > 0:
			print "Error -- Server "+clusterName+" is not running."
		else:

			try:
      				_excp_ = 0
        			result = AdminControl.invoke(cluster, "stop" )
        		except:
                		_type_, _value_, _tbck_ = sys.exc_info()
                		result = `_value_`
                		_excp_ = 1
        		#endTry 
        		if (_excp_ ):
                		print "Caught Exception stopping cluster "+clusterName
                		print result
                		return
			else:
				print "Stopping cluster "+clusterName+" was successful."
        		#endIf 
		#endIf
	#endIf

#endDef 

#--------------------------------------------------------------------
# Procedure:   	stopClusters
# Description:	Stop clusters in list
#
# Parameters:   clusterNameList
#
# Returns:	None
#
# Note: This procedure only functions in a WAS 5.0 ND environment
#--------------------------------------------------------------------
def stopClusters ( clusterNameList ):
        global AdminControl

	cellName = AdminControl.getCell()
	clusterList = clusterNameList.split(",")
        for clusterName in clusterList:
		stopCluster(cellName, clusterName)
        #endFor 
#endDef 


#--------------------------------------------------------------------
# Procedure:   	stopServer
# Description: 	Procedure used to stop an individual server.
# 
# Parameters:  	nodeName
#              	appServer
#
# Returns:	None
#--------------------------------------------------------------------
def stopServer ( nodeName, appServer ):
        global AdminControl

        if (findServerOnNode(appServer, nodeName) == 0):
                print "Error:  Server: "+appServer+" on Node: "+nodeName+ " does not exist."
                return 
        #endIf 

        server = AdminControl.completeObjectName("type=Server,node="+nodeName+",name="+appServer+",*")
	if len(server) > 0:
                serverState = AdminControl.getAttribute(server, "state" )
                print appServer+" current state is "+serverState
                if (serverState == "STARTED"):
			try:
      				_excp_ = 0
                        	result = AdminControl.stopServer(appServer )
        		except:
                		_type_, _value_, _tbck_ = sys.exc_info()
                		result = `_value_`
                		_excp_ = 1
        		#endTry 
        		if (_excp_ ):
                		print "Caught Exception stopping server "+appServer
                		print result
                		return
			else:
				print "Stopping "+appServer+" was successful."
        		#endIf 
                #endIf 
        else:
                print "Server "+appServer+" is not running on node "+nodeName
        #endElse 
#endDef 

#--------------------------------------------------------------------
# Procedure:    stopServers
# Description:  Procedure used to stop multiple server processes. 
#
# Parameters: 	serverNodeList - List of server names and nodes the servers are on 
#
#		Example call: stopServers (testServerName, testNodeName)
#
# Returns:  	None
#--------------------------------------------------------------------
def stopServers ( serverNodeList ):
        global AdminControl

	for serverName, nodeName in serverNodeList:
        	print "Stopping Server "+serverName+" on node "+nodeName
		stopServer(nodeName,serverName)
	#endFor
#endDef 

#--------------------------------------------------------------------
# Procedure:	syncNodesToMaster
# Description: 	Force a synchronization  between the master repository
#              	and the list of nodes.  This is faster than waiting 
#              	for the synchronization timeout.
#
# Parameters:	nodeList
#
# Returns:	none
#
# History:
#--------------------------------------------------------------------
def syncNodesToMaster ( nodeList ):
        global AdminControl

        # Get the ConfigRepository object for the DeploymentManager
        repos = AdminControl.completeObjectName("type=ConfigRepository,process=dmgr,*" )
	if(isEmpty(repos)):
		print "INFO (syncNodesToMaster): Not DeploymentManager!"
		return
        # Reset the epoch so nodes are forced to resync
        AdminControl.invoke(repos, "refreshRepositoryEpoch" )

        # Invoke the resync on each node individually so we know they have been run instead of waiting
        # for the sync interval

        #nList = nodeList.split(',')
        for node in nodeList:
                nodeSync = AdminControl.completeObjectName("type=NodeSync,node="+node+",*" )
                if (nodeSync == ""):
               		 print ("INFO (syncNodesToMaster): Cannot syncNDNode (stopped?) nodeName="+node )
               		 return
               		 
                try:
                        _excp_ = 0
                        error = AdminControl.invoke(nodeSync, "sync" )
                        print ("INFO (syncNodesToMaster): Synchronization with node "+node +" successful :-)")
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        error = `_value_`
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "ERROR (syncNodesToMaster): Error synchronization with node "+node
                #endIf 
        #endFor 
#endDef 

#-----------------------------------------------------------------------
# Procedure:	toggleService
# Description:	Enables/Disables a type of service on a specified server
#
# Parameters:	serviceName
#		serverName
#		nodeName
#		flag
#
# Returns:	0 = success
#		1 = failure
#-----------------------------------------------------------------------
def toggleService ( serviceName, serverName, nodeName, flag ):
	global AdminConfig, AdminControl
	
        # Get the ID of the server
        try:
                _excp_ = 0
                serverID = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                serverID = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Error getting server ID"
                print "Server ID = "+serverID
                return 1
        #endIf 

        # Get the service for the server
        try:
                _excp_ = 0
		service = AdminConfig.list('"'+serviceName+'"', serverID)
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                service = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
		print service
                print "Error getting Service"
                print "Toggle Service = "+serviceName
                return 1
        #endIf 

        # Enable the service for next server start
        try:
                _excp_ = 0
                error = AdminConfig.modify(service, [["enable", flag]] )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                error = `_value_`
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "Error toggling Service"
                print "Error Message = "+error
		return 1
        #endIf 
	return 0
#endDef

#--------------------------------------------------------------------
# Procedure:   	updatePort
# Description: 	Update the transport with the given port number
#
# Parameters:	cellName
#		nodeName 
#		serverName
#              	port
#
# Returns:	None
# 
#--------------------------------------------------------------------
def updatePort ( cellName, nodeName, serverName, port ):
        global AdminConfig, AdminControl

        # assign transport port number (assume non-secure)
        pserver = AdminConfig.getid("/Cell:"+cellName+"/Node:"+nodeName+"/Server:"+serverName+"/")
	httpNonSecureAddress = [["sslEnabled", "false"], ["address", [["host", ""], ["port", port]]]]
	#httpSecureAddress = [["sslEnabled", "true"], ["address", [["host", ""], ["port", sPort]]], ["sslConfig", "DefaultSSLSettings"]]
	#transports = [["transports:HTTPTransport", [httpNonSecureAddress, httpSecureAddress]]]
	transports = [["transports:HTTPTransport", [httpNonSecureAddress]]]
	webContainer = AdminConfig.list("WebContainer", pserver)
       	AdminConfig.modify(webContainer, transports)
	
#endDef   

#--------------------------------------------------------------------
# Procedure:   	wsadminToList
# Description:  Used to simplify conversion from Jacl to Jython
#
# Parameters:   inStr
# Returns:	outList
#
#--------------------------------------------------------------------
def wsadminToList (inStr):
	
	outList=[]
	if ( len(inStr) > 0 and inStr[0] == '[' and inStr[-1] == ']'):
		inStr = inStr[1:-1]
		tmpList = inStr.split(" ")
	else:
		tmpList = inStr.split("\n")  # splits for Windows or Linux
	for item in tmpList:
		item = item.rstrip();        # removes any Windows "\r"
		if ( len(item) > 0 ):
			outList.append(item)
	return outList
#endDef  

##########################################################################
#
# FUNCTION:
#    isEmpty: Checks if variable is empty or None
#
# SYNTAX:
#    isEmpty(variable)
#
# PARAMETERS:
#    
#	variable - Name of variable to validate
#
# RETURNS:
#    	1:	variable = "" or None
#	0:	variable != "" and != None
#
##########################################################################
def isEmpty(var):
	retval = (var == "" or var == None or var == "[]")	
	return retval
	
def deleteEarFile(appPath): 
	print ("INFO (deleteEarFile): Deleting "+appPath)
	File(str(appPath)).delete()
	print 'INFO (deleteEarFile): Done.'
			
def readDistributionDirectory(distDir):     
    robFile = io.File(distDir);
    listDirs = None
    ears = []
    
    if ( not robFile.exists()):
            print "ERROR (readDistributionDirectory): does not exist, distDir="+distDir
    if(robFile.canRead() and robFile.isDirectory()):
    	listDirs = robFile.list();
    	if (len(listDirs) == 0):
                print "WARNING (readDistributionDirectory): no files found in distDir="+distDir
    	
    	for name in listDirs:
    		dot = name.rfind(".")
                if (dot > 1):
                        ext = name[dot:]
                        ext = ext.lower()
                else:
                        ext = ""
                #endElse
                if (ext == ".ear"):
                        ears.append(name)
                #endIf
        #endFor
       
    else:	
  	  print "ERROR (listDirectory): Unable to read directory"
    
    return ears
   
def noLeaveModuleFileExits(appName, distDir): 

    robFile = io.File(distDir, appName + ".leave");
    print "DEBUG (noLeaveModuleFileExits): Looking for file" +  appName + ".leave";
    if ( not robFile.exists()):
		return 1;
    else:
    	return 0;
    
def parseApplicationNames ( ears ):
    
        appNames = []
        for ear in ears:
                app = ear
                dot = app.find(".ear")
                if (dot > 0):
                        app = app[0:dot]
                #endIf
                sl = app.rfind("/")
                bs = app.rfind("\\")
                if (bs > sl):
                        sl = bs
                #endIf
                if (sl > 0):
                        app = app[(sl+1):]
                #endIf
               	#print ("INFO (parseApplicationNames): appName="+app )
        		
                appNames.append(app)
        #endFor
        return appNames
#endDef
def checkServerStopped ( nodeName, serverName ):
        serverID = ""
        state = ""
        try:
                _excp_ = 0
                serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp != 0):
                print("INFO (checkServerStopped):checkServerStopped: exception="+`temp`+" trying to access "+nodeName+" "+serverName )
        #endIf
        if (len(serverID) == 0):
                #print("INFO (checkServerStopped): checkServerStopped: cannot access node="+nodeName+" server="+serverName+" (STOPPED?)" )
                return 1
        else:
                state = AdminControl.getAttribute(serverID, "state" )
        #endElse
        if (state== "STOPPED"):
         	return 1
        else:
         	return 0
       
#endDef
def sync():
	lineSeparator = java.lang.System.getProperty('line.separator')
	nodeList = AdminTask.listNodes().split(lineSeparator)
	print ("INFO (sync): Syncronizing...")
	if(len(nodeList)>1):
		syncNodesToMaster( nodeList )
	print 'INFO (sync): Done.'
#endDef

def save():
	print ("INFO (save): Saving...")
	AdminConfig.save()
	print 'INFO (save): Done.'
#endDef