###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	serverSetup.py
# Description:	This file contains the following server setup procedures:
#		
#			addPrefServersToPolicy
#			configCookie
#			configDatabasePersistence
#			configDataRepPersistence
#			configDynamicCache
#			configTuningParams
#			configWebContainerSessionMgr
#			createCluster
#			createClusterMember 
#			createClusterWithMember
#			createJVMProperty
#			createServer
#			setClusterMemberWeight
#
#
# History:					
#******************************************************************************
#******************************************************************************
# Procedure:  	addPrefServersToPolicy
# Description:	Add preferred servers from core group servers for a particular
#			DefaultCoreGroup policy.
#		
#
#******************************************************************************
def addPrefServersToPolicy ( policyName, ServerList ):

	defaultCoreGrp = getConfigItemId ("cell", cellName, "", "CoreGroup", "DefaultCoreGroup")
	coreGroupServerList = AdminConfig.showAttribute(defaultCoreGrp, "coreGroupServers")
	coreGroupServerList = coreGroupServerList [1:len(coreGroupServerList)-1].split(" ")

	serverList = ServerList.split(" ")
	preferredServerList = []

	index = 0
	for server in coreGroupServerList:
		if server.find(serverList[index]) >= 0:
			preferredServerList.append(server)
			index += 1
		#endIf
		if index == len(serverList):
			break
	#endFor

	policyList = AdminConfig.showAttribute(defaultCoreGrp, "policies")
	policyList = policyList [1:len(policyList)-1].split('"')

	for item in policyList:
		if item.find(policyName) >= 0:
			policyId = item
			break
		#endIf
	#endFor

	try:
		_excp_ = 0
		result = AdminConfig.modify(policyId, [["preferredServers", preferredServerList]])
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
        #endTry 
	if (_excp_ ):
		print "Caught Exception adding preferred servers to policy"
		print result
		return
        #endIf 

	print "Add Preferred Servers to Policy was successful"
	
#endDef
 
#******************************************************************************
# Procedure:  	configCookie
# Description:	Modify an existing cookie or create a new one
# 
# History:	
#****************************************************************************** 
def configCookie ( sessionMgr, propertyFileName ):

	readProperties(propertyFileName)

	cookieName =		getProperty("COOKIE_NAME")
	restrictCookies =		getProperty("RESTRICT_COOKIES")
	cookieDomain =		getProperty("COOKIE_DOMAIN")
	cookiePath =		getProperty("COOKIE_PATH")
	maxAgeCookie =		getProperty("COOKIE_MAX_AGE")


	attrs = []
	attrs.append(["name", cookieName])
	attrs.append(["domain", cookieDomain])
	attrs.append(["secure", restrictCookies])
	attrs.append(["path", cookiePath])
	attrs.append(["maximumAge", maxAgeCookie])
	
	cookie = AdminConfig.showAttribute(sessionMgr, "defaultCookieSettings")
	if (cookie != '[]'):	
		# Modify existing cookie
		try:
			_excp_ = 0
			result = AdminConfig.modify(cookie, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
       		#endTry 
		if (_excp_ ):
			print "Caught Exception modifying Cookie"
			print result
			return
       		#endIf 
	else:
		# create a new cookie
		try:
			_excp_ = 0
			result = AdminConfig.create("Cookie", sessionMgr, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
       		#endTry 
		if (_excp_ ):
			print "Caught Exception creating Cookie"
			print result
			return
       		#endIf 
	#endIf
	return

#endDef

#******************************************************************************
# Procedure:  	configDynamicCache
# Description:	Configure Dynamic Cache 
# 
# History:	
#****************************************************************************** 
def configDynamicCache ( propertyFileName ):

	readProperties(propertyFileName)

	nodeName =			getProperty("NODE_NAME")
	serverName =		getProperty("SERVER_NAME")
	startEnable =		getProperty("ENABLE_AT_START")
	cacheSize =			getProperty("CACHE_SIZE")
	defaultPriority =		getProperty("DEFAULT_PRIORITY")
	enableDiskOffld = 	getProperty("ENABLE_DISK_OFFLOAD")
	offLoadLoc =		getProperty("OFFLOAD_LOCATION")
	diskFlush =			getProperty("FLUSH_TO_DISK")
	enableCacheRep =		getProperty("ENABLE_CACHE_REPLICATION")
	replicationType =		getProperty("REPLICATION_TYPE")	
	pushFrequency =		getProperty("PUSH_FREQUENCY")


	# Make sure the node exists
	oNodeToUse = findNode(nodeName )
	if (oNodeToUse == 0):
		print "Node "+nodeName+" does not exist"
		return
	#endIf
	
	# Make sure the server exists
	oServerToUse = findServer(serverName )
	if (oServerToUse == 0):
		print "Server "+serverName+" does not exist"
		return
	#endIf 

	serverId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/")

	attrs = []
	attrs.append(["enable", startEnable])
	attrs.append(["cacheSize", cacheSize])
	attrs.append(["defaultPriority", defaultPriority])
	attrs.append(["enableDiskOffload", enableDiskOffld])
	attrs.append(["diskOffloadLocation", offLoadLoc])
	attrs.append(["flushToDiskOnStop", diskFlush])
	attrs.append(["enableCacheReplication", enableCacheRep])
	attrs.append(["replicationType", replicationType])
	attrs.append(["pushFrequency", pushFrequency])
	
	# Check if there's an existing Dynamic Cache, if so, modify
	dynaCache = AdminConfig.list("DynamicCache", serverId)
	if (dynaCache != ['']):
		try:
			_excp_ = 0
			result = AdminConfig.modify(dynaCache, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying Dynamic Cache"
			print result
			return
        	#endIf 

	else:
		try:
			_excp_ = 0
			result = AdminConfig.create("DynamicCache", serverId, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating Dynamic Cache"
			print result
			return
        	#endIf 
	#endElse

	print "Config Dynamic Cache was successful"
	
#endDef

#******************************************************************************
# Procedure:  	configDatabasePersistence
# Description:	Configure database persistence as part of session management
#			of the web container
# 
# History:	
#****************************************************************************** 
def configDatabasePersistence ( sessionMgr, propertyFileName ):

	readProperties(propertyFileName)

	dsJNDIName =	getProperty("DATASOURCE_JNDI_NAME")
	sessionUserId =	getProperty("SESSION_USERID")
	sessionPassword =	getProperty("SESSION_PASSWORD")
	db2RowSize =	getProperty("DB2_ROW_SIZE")
	tableSpaceName =	getProperty("TABLE_SPACE_NAME")

	attrs = []
	attrs.append(["datasourceJNDIName", dsJNDIName])
	attrs.append(["userId", sessionUserId])
	attrs.append(["password", sessionPassword])
	attrs.append(["db2RowSize", db2RowSize])
	attrs.append(["tableSpaceName", tableSpaceName])

	sessionDb = AdminConfig.showAttribute(sessionMgr, "sessionDatabasePersistence")
	if (sessionDb != '[]'):
		# Modify existing session database persistence
		try:
			_excp_ = 0
			result = AdminConfig.modify(sessionDb, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying session database persistence"
			print result
			return
        	#endIf 
	else:
		# Create a session database persistence object
		try:
			_excp_ = 0
			result = AdminConfig.create("sessionDatabasePersistence", sessionMgr, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating session database persistence"
			print result
			return
        	#endIf 
	#endIf
	return

#endDef

#******************************************************************************
# Procedure:  	configDataRepPersistence
# Description:	Configure data replication (memory-to-memory) persistence as 
#			part of session management of the web container
# 
# History:	
#****************************************************************************** 
def configDataRepPersistence ( sessionMgr, propertyFileName ):

	readProperties(propertyFileName)

	repDomain =		getProperty("REPLICATION_DOMAIN")
	repMode =		getProperty("REPLICATION_MODE")

	attrs1 = []
	attrs1.append(["messageBrokerDomainName", repDomain]) 	
	attrs1.append(["dataReplicationMode", repMode])

	persist = AdminConfig.showAttribute(sessionMgr, "sessionDRSPersistence")
	if (persist != '[]'):
		# Modify existing data replication information
		try:
			_excp_ = 0
			result = AdminConfig.modify(persist, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying data replication information"
			print result
			return
        	#endIf 
	else:
		# Create existing data replication information
		try:
			_excp_ = 0
			result = AdminConfig.create("sessionDRSPersistence", sessionMgr, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying data replication information"
			print result
			return
        	#endIf 
	#endIf
	return
#endDef

#******************************************************************************
# Procedure:  	configTuningParams
# Description:	Configure tuning parameters for the session manager
# 
# History:	
#****************************************************************************** 
def configTuningParams ( sessionMgr, propertyFileName ):
 
	readProperties(propertyFileName)

	multiRowSchema =	getProperty("USE_MULTI_ROW_SCHEMA")
	maxInMemCount =	getProperty("MAX_INMEMORY_SESSION_COUNT")
	allowOverflow =	getProperty("ALLOW_OVERFLOW")
	writeFrequency =	getProperty("WRITE_FREQUENCY")
	writeInterval = 	getProperty("WRITE_INTERVAL")
	writeContents =	getProperty("WRITE_CONTENTS")
	schedInval =	getProperty("SCHEDULE_INVALIDATION")
	invalTimeOut =	getProperty("INVALIDATION_TIMEOUT")
	firstTime =		getProperty("FIRST_TIME_OF_DAY")
	secondTime =	getProperty("SECOND_TIME_OF_DAY")

	attrs1 = []
	attrs1.append(["usingMultiRowSchema", multiRowSchema])
	attrs1.append(["maxInMemorySessionCount", maxInMemCount])
	attrs1.append(["allowOverflow", allowOverflow])
	attrs1.append(["scheduleInvalidation", schedInval])
	attrs1.append(["writeFrequency", writeFrequency])
	attrs1.append(["writeInterval", writeInterval])
	attrs1.append(["writeContents", writeContents])
	attrs1.append(["invalidationSchedule", [["firstHour", firstTime], ["secondHour", secondTime]]])
	attrs1.append(["invalidationTimeout", invalTimeOut])
	
	tuneParms = AdminConfig.showAttribute(sessionMgr, "tuningParams")
	if (tuneParms != '[]'):
		# Modify existing tuning parameters
		try:
			_excp_ = 0
			result = AdminConfig.modify(tuneParms, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying session tuning parameters"
			print result
			return
        	#endIf 
	else:
		# Create tuning parameters object
		try:
			_excp_ = 0
			result = AdminConfig.create("TuningParams", sessionMgr, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating session tuning parameters"
			print result
			return
        	#endIf 
	#endIf
	return
#endDef

#******************************************************************************
# Procedure:  	ContainerSessionMgr
# Description:	Configure session manager for web container
# 
# History:	
#****************************************************************************** 
def configWebContainerSessionMgr ( propertyFileName ):

	global AdminConfig

	readProperties(propertyFileName)
	nodeName =			getProperty("NODE_NAME")
	serverName =		getProperty("SERVER_NAME")
	
	enableSSL =			getProperty("ENABLE_SSL_TRACKING")
	enableCookies =		getProperty("ENABLE_COOKIES")
	enableURL = 		getProperty("ENABLE_URL_REWRITING")
	enableProto =		getProperty("ENABLE_PROTOCOL_SWITCH")
	secIntegration =		getProperty("SECURITY_INTEGRATION")
	allowSerial =		getProperty("ALLOW_SERIAL_ACCESS")
	maxWaitTime = 		getProperty("MAX_WAIT_TIME")	
	accessOnTimeout =		getProperty("ALLOW_ACCESS_ON_TIMEOUT")
	sessionPersistence =	getProperty("SESSION_PERSISTENCE")


	# Make sure the node exists
	oNodeToUse = findNode(nodeName )
	if (oNodeToUse == 0):
		print "Node "+nodeName+" does not exist"
		return
	#endIf
	
	# Make sure the server exists
	oServerToUse = findServer(serverName )
	if (oServerToUse == 0):
		print "Server "+serverName+" does not exist"
		return
	#endIf 

	serverId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/")
	sessionMgr = AdminConfig.list("SessionManager", serverId)

	attrs = []
	attrs.append(["enableSSLTracking", enableSSL])
	attrs.append(["enableCookies", enableCookies])
	attrs.append(["enableUrlRewriting", enableURL])
	attrs.append(["enableProtocolSwitchRewriting", enableProto])
	attrs.append(["enableSecurityIntegration", secIntegration])
	attrs.append(["allowSerializedSessionAccess", allowSerial])
	attrs.append(["maxWaitTime", maxWaitTime])
	attrs.append(["accessSessionOnTimeout", accessOnTimeout])
	attrs.append(["sessionPersistenceMode", sessionPersistence])

	try:
		_excp_ = 0
		result = AdminConfig.modify(sessionMgr, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
        #endTry 
	if (_excp_ ):
		print "Caught Exception modifying Session Manager General Properties"
		print result
		return
        #endIf 

	if (enableCookies.lower() == "true"):
		configCookie(sessionMgr, propertyFileName)
	#endIf
	if (sessionPersistence == "DATABASE"): 
		configDatabasePersistence(sessionMgr, propertyFileName)

	elif (sessionPersistence == "DATA_REPLICATION"):
		configDataRepPersistence(sessionMgr, propertyFileName)
	#endIf

	configTuningParams(sessionMgr, propertyFileName)

	print "Config Web Container Session Manager was successful"
	
#endDef
	
#******************************************************************************
# Procedure:  	createCluster
# Description:	Create new cluster 
# 
# History:	
#		
#****************************************************************************** 
def createCluster ( clusterName, prefLocal, repDomain):

	#---------------------------------------------------------------------------------
	# Create server cluster if it does not exist
	#---------------------------------------------------------------------------------

	global AdminApp, AdminConfig

	print '\n====== Create cluster '+clusterName+', if it does not exist ======'

	# Check if server cluster already exists
	oClusterToUse = findServerCluster(clusterName )
	if (oClusterToUse != 0):
		print "Cluster already exists, so no cluster will be created"
		return
	#endIf 

	oCellToUse = findCell(cellName )
	if (oCellToUse == 0):
		print "Cellname: "+cellName+" does not exist."
		return
	#endIf 

	#---------------------------------------------------------------------
	# Construct the attribute list to be used in creating a server cluster
	#---------------------------------------------------------------------
	attrs = [["name", clusterName], ["preferLocal", prefLocal]]

	#---------------------------------------------------------
	# Create the server cluster 
	#---------------------------------------------------------
	print "Attempting to create the cluster: "+clusterName
	try:
		_excp_ = 0
		createCluster = AdminConfig.create("ServerCluster", oCellToUse, attrs )
		result = createCluster
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating cluster"
		print result
		return
	#endIf 

	# Create replication domain if necessary
	if (repDomain.lower() == "true"):
		nameAttr = ["name", clusterName]
		timeOutAttr = ["requestTimeout", 5]
		encryptAttr = ["encryptionType", "NONE"]
		replAttr = ["defaultDataReplicationSettings", [timeOutAttr, encryptAttr]]
		attrs = [nameAttr, replAttr]
		
		try:
			_excp_ = 0
			createDomain = AdminConfig.create("DataReplicationDomain", oCellToUse, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			createDomain = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating replication domain"
			print createDomain
			return
        	#endIf 
	#endIf

	print "Cluster "+clusterName+" created successfully"
	
#endDef


#******************************************************************************
# Procedure:  	createClusterMember
# Description:	Create new cluster member with a given cluster name 
#
# History:	03/13/06:  Removed serverTemplate as an input
#****************************************************************************** 
def createClusterMember ( clusterMemberName, clusterName, nodeName, weightValue):

	#--------------------------------------------------------------------------------------
	# Create cluster member if it does not exist
	#--------------------------------------------------------------------------------------
	global AdminApp, AdminConfig

	print '\n====== Create cluster member '+clusterMemberName+', if it does not exist ======'

	# Check if server already exists
	oServerToUse = findServer(clusterMemberName )
	if (oServerToUse != 0):
		print "Server "+clusterMemberName+" already exists"
		return
	#endIf 

	# Check if cluster exists
	oClusterToUse = findServerCluster(clusterName )
	if (oClusterToUse == 0):
		print "Cluster doesn't exist."
		return
	#endIf 

	# Find the node we want to add to the server cluster
	oNodeToUse = findNode(nodeName )
	if (oNodeToUse == 0):
		print "Node doesn't exist"
		return
	#endIf 

	attrServer = [["memberName", clusterMemberName], ["weight", weightValue]]
	print "Attempting to create the server member"
	print "Using Server Name:	"+clusterMemberName
	print "Using Weight:		"+weightValue

	clusterTemplate = AdminConfig.listTemplates("ClusterMember")

	print "Cluster member Templates "+clusterTemplate

	try:
		_excp_ = 0
		server = AdminConfig.createClusterMember(oClusterToUse, oNodeToUse, attrServer,
clusterTemplate )
		result = server
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating cluster member: "+clusterMemberName
		print result
		return
	#endIf 

	print "Cluster member "+clusterMemberName+" created successfully"

	
#endDef

#******************************************************************************
# Procedure:	createClusterWithMember
# Description:	Create new cluster with a server member. 
#****************************************************************************** 
def createClusterWithMember ( clusterName, nodeName, serverName ):

	#---------------------------------------------------------------------------------
	# Create server cluster with one member if it does not exist
	#---------------------------------------------------------------------------------

	global AdminApp, AdminConfig

	print '\n====== Create cluster '+clusterName+', if it does not exist ======'

	# Check if server cluster already exists
	oClusterToUse = findServerCluster(clusterName )
	if (oClusterToUse != 0):
		print "Cluster already exists, so no cluster will be created"
		return
	#endIf 

	# Find the node 
	oNodeToUse = findNode(nodeName )
	if (oNodeToUse == 0):
		print "Node: "+nodeName+" does not exist."
		return
	#endIf 

	oCellToUse = findCell(cellName )
	if (oCellToUse == 0):
		print "Cellname: "+cellName+" does not exist."
		return
	#endIf 

	#---------------------------------------------------------
	# Create the server cluster 
	#---------------------------------------------------------
	print "Attempting to create the cluster: "+clusterName+" with member "+serverName
	serverId = AdminConfig.getid("/Server:"+serverName+"/")

	try:
		_excp_ = 0
		createCluster = AdminConfig.convertToCluster(serverId, clusterName) 
		result = createCluster
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating cluster"
		print result
		return
	#endIf 

	print "Cluster "+clusterName+" with member "+serverName+" created successfully"
	
#endDef


#******************************************************************************
# Procedure:  	createJVMProperty
# Description:	Create a custom JVM property
#
# History:	 
#****************************************************************************** 
def createJVMProperty ( nodeName, serverName, propName, propValue, propDesc ):

	#------------------------------------------------------------------------------
	# Create JVM custom property if it does not exist.  
	#------------------------------------------------------------------------------

	global AdminConfig

	print '\n====== Create custom JVM property '+propName+' with value '+propValue+', if it does not exist ======'
	try:
		_excp_ = 0
		serverId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/")
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		serverId = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error getting Server ID"
		print "server ID = "+serverId
		return
	#endIf 

	# Prepare customer properties list
	name = ["name", propName]
	value = ["value", propValue]
	desc = ["description", propDesc]
	customProp = [name, value, desc]

	# Prepare the system properties list
	systemPropsList = ["systemProperties", [customProp]]
	jvmAttrs = [systemPropsList]
	processId = AdminConfig.list("ProcessDef", serverId)
	jvmId = AdminConfig.list("JavaVirtualMachine", processId)

	# Check to see if jvm property already exists
	propList = AdminConfig.showAttribute(jvmId, "systemProperties")
	propList = propList [1:len(propList)-1].split(" ")
	modifiedOne = 0
	if (propList != ['']):
		for item in propList:
			itemName = AdminConfig.showAttribute(item, "name")
			if (itemName == propName):
				modifiedOne = 1
				print "Modifying value and desc of "+propName+" to "+propValue+" and "+propDesc

				try:
					_excp_ = 0
					result = AdminConfig.modify(item, [["value", propValue], ["description", propDesc]])
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
				#endTry 
				if (_excp_ ):
					print "Caught Exception modifying JVM property"
					print result
					return
				#endIf 
				break
			#endIf
		#endFor
	#endIf

	if (not modifiedOne):
		# Set the JVM Properties
		try:
			_excp_ = 0
			result = AdminConfig.modify(jvmId, jvmAttrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			print "Caught Exception creating JVM property"
			print result
			return
		#endIf 
	#endIf 

	print "Create/Modify JVM custom property successful."
	
#endDef

#******************************************************************************
# Procedure:	createServer
# Description:	Create new application server 
#****************************************************************************** 
def createServer ( nodeName, serverName, hostName, dnsHost ):

	#------------------------------------------------------------------------------
	# Create server if it does not exist.  Set to defined port and host alias
	#------------------------------------------------------------------------------

	global AdminApp, AdminConfig

	print '\n====== Create app server '+serverName+', if it does not exist ======'

	# Check if node exists 
	node = findNode(nodeName)
	if (node == 0):
		print "Node does not exist"
		return
	#endIf

	# Check if server already exists
	oServerToUse = findServer(serverName )
	if (oServerToUse != 0):
		print "Server already exists"
		return
	#endIf 

	#---------------------------------------------------------
	# Create the server  
	#---------------------------------------------------------
	print "Attempting to create the server="+serverName+" node="+nodeName
	try:
		_excp_ = 0
		server = AdminConfig.create("Server", node, [["name", serverName]] )
		result = server
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating server"
		print result
		return
	#endIf 

	port = getPort(nodeName, serverName)
	addHostAlias(hostName, dnsHost, port)
	print "Server "+serverName+" create successfully"

#endDef

#******************************************************************************
# Procedure:	setClusterMemberWeight
# Description:	Set the weight for a cluster member.
#****************************************************************************** 
def setClusterMemberWeight ( memberName, weight ):

	#---------------------------------------------------------------------------------
	# Set the weight for cluster member
	#---------------------------------------------------------------------------------

	global AdminConfig

	print "\n ====== Set the weight for cluster member "+memberName+" to "+weight+" ======"

	clusterServer = getConfigObject(memberName, "ClusterMember")
	if (clusterServer == 1):
		return
	else:
		# Set the weight of the server to the value of newWeight so plugin/WLM will direct traffic accordingly
		try:
			_excp_ = 0
			error = AdminConfig.modify(clusterServer, [["weight", weight]] )
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			error = `_value_`
			_excp_ = 1
		#endTry
		if (_excp_):
			print "Error modifying weight"
			print "Error Message = "+error
			return
		#endIf
	#endIf

	print "Set cluster member weight successful."
	
#endDef


