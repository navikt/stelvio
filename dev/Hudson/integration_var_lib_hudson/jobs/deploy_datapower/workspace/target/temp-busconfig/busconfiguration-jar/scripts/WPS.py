###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	WPS.py
# Description:	This file contains the following procedures:
#		
#			addUserOrGroupToForeignBus
#			configSCA
#			createSIBEngine
#			createSIBForeignBus
#			createSIBLink
#			createSIBMQLink
#			createWASClusterWithMember
#			createWPSClusterWithMember
#			deleteSIBForeignBus
#			deleteSIBLink
#			deleteSIBMQLink
#			listSIBEngines
#			listSIBForeignBuses
#			listSIBLinks
#			listSIBMQLinks
#			listSCAModules
#			listSCAExports
#			listSCAImports
#			modifySCAImportSCABinding
#			modifySIBForeignBus
#			modifySIBLink
#			modifySIBMQLink
#			removeUserOrGroupFromForeignBus
#			showSCAExport
#			showSCAExportBinding
#			showSCAImport
#			showSCAImportBinding
#			showSCAModule
#			showSIBForeignBus
#			showSIBLink
#			showSIBMQLink
#			updateMEDataStore
#
#
#
# History:		
#******************************************************************************



#******************************************************************************
# Procedure:   	addUserOrGroupToForeignBus
# Description:	Add users or groups to foreign bus roles
#****************************************************************************** 
def addUserOrGroupToForeignBus ( busName, foreignBusName, roleName, userName, groupName ):

	global AdminTask
	print '\n====== Adding users or groups to foreign bus roles ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName+ ' -foreignBus ' + foreignBusName + ' -role ' + roleName 

	if (userName != ""):
		parms += ' -user '+userName
	#endIf

	else:
		parms += ' -group '+groupName
	#endElse
		
	try:
		_excp_ = 0
		result = AdminTask.addUserToForeignBusRole(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error adding "+userName+" to "+foreignBusName+ "connector roles"
		print "Error = "+result
		return
	#endIf 

	print "Adding to foreign bus roles was successful."
	
#endDef





#******************************************************************************
# Procedure:   	configSCA
# Description:	Configure SCA for either a cluster or a server that is based on the defaultProcessServer template
#****************************************************************************** 
def configSCA ( propertyFileName):

	global AdminTask
	print '\n====== Configure SCA ====='


	readProperties(propertyFileName)

	clusterName = 		getProperty("CLUSTERNAME")
	nodeName = 		getProperty("NODENAME")
	serverName = 		getProperty("SERVERNAME")
	systemBusDataSource =   getProperty("SYSTEMBUSDATASOURCE")
	appBusDataSource = 	getProperty("APPBUSDATASOURCE")
	remoteDestLocation = 	getProperty("REMOTEDESTLOCATION")
	meAuthAlias = 		getProperty("MEAUTHALIAS")
	systemBusSchemaName = 	getProperty("SYSTEMBUSSCHEMANAME")
	appBusSchemaName = 	getProperty("APPBUSSCHEMANAME")
	createTables = 		getProperty("CREATETABLES")

	if (clusterName != ""):
		parms = ' -clusterName '+clusterName
	#endIf

	else:
		parms = '-nodeName '+nodeName+' -serverName '+serverName
		
	#endElse

	if (remoteDestLocation != ""):
		parms += ' -remoteDestLocation '+remoteDestLocation
	#endIf

	else:
		parms += ' -systemBusDataSource '+systemBusDataSource+' -appBusDataSource '+appBusDataSource
		
	#endElse
	
	parms += ' -meAuthAlias '+meAuthAlias+' -systemBusSchemaName '+systemBusSchemaName+' -appBusSchemaName '+appBusSchemaName+' -createTables '+createTables
	
	try:
		_excp_ = 0
		if (clusterName != ""):
			result = AdminTask.configSCAForCluster(parms)
		#endIf
	
		else:
			result = AdminTask.configSCAForServer(parms)
		#endElse

		
		
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error configuring SCA for cluster "+clusterName+' node '+nodeName+ ' server '+serverName
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	createSIBEngine
# Description:	Create a messaging engine for a given cluster bus member
#****************************************************************************** 
def createSIBEngine ( propertyFileName ):

	global AdminTask
	print '\n====== Create SIB Engine ====='


	readProperties(propertyFileName)

	bus = 				getProperty("BUS")
	cluster = 			getProperty("CLUSTER")
	description =			getProperty("DESCRIPTION")
	highMessageThreshold = 		getProperty("HIGHMESSAGETHRESHOLD")
	initialState = 			getProperty("INITIALSTATE")
	createDefaultDatasource = 	getProperty("CREATEDEFAULTDATASOURCE")
	datasourceJndiName = 		getProperty("DATASOURCEJNDINAME")
	
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor


	parms = '-bus '+bus+' -cluster '+cluster+' -datasourceJndiName '+datasourceJndiName

	if (description != ""):
		parms += ' -description '+description
	#endIf

	if (highMessageThreshold != ""):
		parms += ' -highMessageThreshold '+highMessageThreshold
	#endIf

	if (initialState != ""):
		parms += ' -initialState '+initialState
	#endIf

	if (createDefaultDatasource != ""):
		parms += ' -createDefaultDatasource '+createDefaultDatasource
	#endIf
	
	try:
		_excp_ = 0
		result = AdminTask.createSIBEngine(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error creating SIB Engine for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	createSIBForeignBus
# Description:	Create a SIB foreign bus
#****************************************************************************** 

def createSIBForeignBus (propertyFileName):

	global AdminTask
	print '\n====== Create SIB Foreign Bus ====='


	readProperties(propertyFileName)

	busName			= 	getProperty("BUS")
	name			= 	getProperty("NAME")
	routingType 		= 	getProperty("ROUTINGTYPE")
	type_ 			= 	getProperty("TYPE")
	description 		=	getProperty("DESCRIPTION")
	sendAllowed 		=	getProperty("SENDALLOWED")
	inboundUserid 		=	getProperty("INBOUNDUSERID")
	outboundUserid 		=	getProperty("OUTBOUNDUSERID")
	nextHopBus 		=	getProperty("NEXTHOPBUS")
	destinationDefaults 	=	getProperty("DESTINATIONDEFAULTS")
	topicSpaceMappings 	= 	getProperty("TOPICSPACEMAPPINGS")
	

	parms = 'name '+name+' -routingType '+routingType

	if (type_ != ""):
		parms += ' -type '+type_
	#endIf

	if (description != ""):
		parms += ' -description '+description
	#endIf

	if (sendAllowed != ""):
		parms += ' -sendAllowed '+sendAllowed
	#endIf

	if (inboundUserid != ""):
		parms += ' -inboundUserid '+inboundUserid
	#endIf

	if (outboundUserid != ""):
		parms += ' -outboundUserid '+outboundUserid
	#endIf

	if (nextHopBus != ""):
		parms += ' -nextHopBus '+nextHopBus
	#endIf

	if (destinationDefaults != ""):
		parms += ' -destinationDefaults '+destinationDefaults
	#endIf

	if (topicSpaceMappings != ""):
		parms += ' -topicSpaceMappings '+topicSpaceMappings
	#endIf

	
	try:
		_excp_ = 0
		print parms
		#result = AdminTask.createSIBForeignBus(parms)
		result = AdminConfig.create('SIBForeignBus',busName, parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error creating SIB foreign buses for bus "+busName
		print "Error = "+result
		return
	#endIf 
		
#endDef



#******************************************************************************
# Procedure:   	createSIBLink
# Description:	Create a SIB Link
#****************************************************************************** 
def createSIBLink (propertyFileName):

	global AdminTask
	print '\n====== Create SIB Link ====='

	readProperties(propertyFileName)

	bus = 		getProperty("BUS")
	messagingEngine = getProperty("MESSAGINGENGINE")
	name = 		getProperty("NAME")
	bootstrapEndpoints = getProperty("BOOTSTRAPENDPOINTS")
	description =		getProperty("DESCRIPTION")
	protocolName =		getProperty("PROTOCOLNAME")
	authAlias =	getProperty("AUTHALIAS")
	initialState =		getProperty("INITIALSTATE")

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor


	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -name '+name

	if (bootstrapEndpoints != ""):
		parms += ' -bootstrapEndpoints '+bootstrapEndpoints
	#endIf

	if (remoteMessagingEngineName != ""):
		parms += ' -remoteMessagingEngineName '+remoteMessagingEngineName
	#endIf

	if (description != ""):
		parms += ' -description '+description
	#endIf

	if (protocolName != ""):
		parms += ' -protocolName '+protocolName
	#endIf

	if (authAlias != ""):
		parms += ' -authAlias '+authAlias
	#endIf

	if (initialState != ""):
		parms += ' -initialState '+initialState
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.createSIBLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error creating SIB link for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef



#******************************************************************************
# Procedure:   	createSIBMQLink
# Description:	Create a SIB MQ Link
#****************************************************************************** 
def createSIBMQLink ( propertyFileName):

	global AdminTask
	print '\n====== Create SIB MQ Link ====='


	readProperties(propertyFileName)

	bus 				= 	getProperty("BUS")
	messagingEngine	 		= 	getProperty("MESSAGINGENGINE")
	mqLink 				= 	getProperty("MQLINK")
	name 				= 	getProperty("NAME")
	queueManagerName 		= 	getProperty("QUEUEMANAGERNAME")
	desc 				=	getProperty("DESCRIPTION")
	batchSize	 		=	getProperty("BATCHSIZE")
	maxMsgSize 			=	getProperty("MAXMSGSIZE")
	heartBeat 			=	getProperty("HEARTBEAT")
	sequenceWrap 			=	getProperty("SEQUENCEWRAP")
	nonPersistentMessageSpeed 	=	getProperty("NONPERSISTENTMSGSPEED")
	adoptable 			= 	getProperty("ADOPTABLE")
	initialState 			=	getProperty("INITIALSTATE")
	senderChannelName 		=	getProperty("SENDERCHANNELNAME")
	hostName			=	getProperty("HOSTNAME")
	port 				=	getProperty("PORT")
	discInterval		 	= 	getProperty("DISCINTERVAL")
	shortRetryCount 		= 	getProperty("SHORTRETRYCOUNT")
	shortRetryInterval 		=	getProperty("SHORTRETRYINTERVAL")
	longRetryCount 			=	getProperty("LONGRETRYCOUNT")
	longRetryInterval 		=	getProperty("LONGRETRYINTERVAL")
	senderChannelInitialState 	=	getProperty("SENDERCHANNELINITIALSTATE")
	receiverChannelName 		=	getProperty("RECEIVERCHANNELNAME")
	inboundNonPersistentReliability =	getProperty("INBOUNDNONPERSISTENTRELIABILITY")
	inboundPersistentReliability 	=	getProperty("INBOUNDPERSISTENTRELIABILITY")
	receiverChannelInitialState 	= 	getProperty("RECEIVERCHANNELINITIALSTATE")
	
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor


	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -mqLink '+mqLink

	if (name != ""):
		parms += ' -name '+name
	#endIf

	if (queueManagerName != ""):
		parms += ' -queueManagerName '+queueManagerName
	#endIf

	if (desc != ""):
		parms += ' -desc '+desc
	#endIf

	if (batchSize != ""):
		parms += ' -batchSize '+batchSize
	#endIf

	if (maxMsgSize != ""):
		parms += ' -maxMsgSize '+maxMsgSize
	#endIf

	if (heartBeat != ""):
		parms += ' -heartBeat '+heartBeat
	#endIf

	if (sequenceWrap != ""):
		parms += ' -sequenceWrap '+sequenceWrap
	#endIf

	if (nonPersistentMessageSpeed != ""):
		parms += ' -nonPersistentMessageSpeed '+nonPersistentMessageSpeed
	#endIf

	if (adoptable != ""):
		parms += ' -adoptable '+adoptable
	#endIf

	if (initialState != ""):
		parms += ' -initialState '+initialState
	#endIf

	if (senderChannelName != ""):
		parms += ' -senderChannelName '+senderChannelName
	#endIf

	if (hostName != ""):
		parms += ' -hostName '+hostName
	#endIf

	if (discInterval != ""):
		parms += ' -discInterval '+discInterval
	#endIf

	if (shortRetryCount != ""):
		parms += ' -shortRetryCount '+shortRetryCount
	#endIf


	if (shortRetryInterval != ""):
		parms += ' -shortRetryInterval '+shortRetryInterval
	#endIf

	if (longRetryCount != ""):
		parms += ' -longRetryCount '+longRetryCount
	#endIf


	if (longRetryInterval != ""):
		parms += ' -longRetryInterval '+longRetryInterval
	#endIf


	if (senderChannelInitialState != ""):
		parms += ' -senderChannelInitialState '+senderChannelInitialState
	#endIf


	if (receiverChannelName != ""):
		parms += ' -receiverChannelName '+receiverChannelName
	#endIf

	if (inboundNonPersistentReliability != ""):
		parms += ' -inboundNonPersistentReliability '+inboundNonPersistentReliability
	#endIf


	if (receiverChannelInitialState != ""):
		parms += ' -receiverChannelInitialState '+receiverChannelInitialState
	#endIf


	
	try:
		_excp_ = 0
		result = AdminTask.modifySIBMQLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error creating SIB MQ links for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef

#******************************************************************************
# Procedure:	createWASClusterWithMember
# Description:	Create new WPS cluster with a server member. 
#****************************************************************************** 
def createWASClusterWithMember (ClusterNameParm, cellName, nodeName, ClusterMemberNameParm, prefLocal, repDomain ):

	#---------------------------------------------------------------------------------
	# Create server cluster with one member if it does not exist
	#---------------------------------------------------------------------------------

	global AdminTask, AdminConfig

	print '\n====== Create cluster '+ClusterNameParm+', if it does not exist ======'

	parms = ('[-clusterName ' + ClusterNameParm + ' -memberConfig [[' + nodeName + ' ' + ClusterMemberNameParm + ' "" "" true false]] -firstMember [[default "" "" "" ""]]]')

#ClusterMemberNameParm + ' "" "" true false]] -firstMember [["default" "" "" "" ""]]]')

	print parms

	# Check if server cluster already exists
	oClusterToUse = findServerCluster(ClusterNameParm )
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

	#---------------------------------------------------------------------
	# Construct the attribute list to be used in creating a server cluster
	#---------------------------------------------------------------------
	attrs = [["name", ClusterNameParm], ["preferLocal", prefLocal]]

	#---------------------------------------------------------
	# Create the server cluster 
	#---------------------------------------------------------
	print "Attempting to create the cluster: "+ClusterNameParm
	try:
		_excp_ = 0
		createCluster = AdminConfig.create("ServerCluster", oCellToUse, attrs )
		result = createCluster
		print "create the cluster: "+createCluster
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

	print "Cluster "+ClusterNameParm+" created successfully"


	try:
		_excp_ = 0
		createClusterMember = AdminTask.createClusterMember(parms)
		result = createClusterMember
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating cluster member"
		print result
		return
	#endIf 

	print "Cluster "+ClusterNameParm+" with member "+ClusterMemberNameParm+" created successfully"
	
#endDef



#******************************************************************************
# Procedure:	createWPSClusterWithMember
# Description:	Create new WPS cluster with a server member. 
#****************************************************************************** 
def createWPSClusterWithMember (ClusterNameParm, cellName, nodeName, ClusterMemberNameParm, prefLocal, repDomain ):

	#---------------------------------------------------------------------------------
	# Create server cluster with one member if it does not exist
	#---------------------------------------------------------------------------------

	global AdminTask, AdminConfig

	print '\n====== Create cluster '+ClusterNameParm+', if it does not exist ======'

	parms = ('[-clusterName ' + ClusterNameParm + ' -memberConfig [[' + nodeName + ' ' + ClusterMemberNameParm + ' "" "" true false]] -firstMember [[defaultProcessServer "" "" "" ""]]]')

#ClusterMemberNameParm + ' "" "" true false]] -firstMember [["defaultProcessServer" "" "" "" ""]]]')

	print parms

	# Check if server cluster already exists
	oClusterToUse = findServerCluster(ClusterNameParm )
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

	#---------------------------------------------------------------------
	# Construct the attribute list to be used in creating a server cluster
	#---------------------------------------------------------------------
	attrs = [["name", ClusterNameParm], ["preferLocal", prefLocal]]

	#---------------------------------------------------------
	# Create the server cluster 
	#---------------------------------------------------------
	print "Attempting to create the cluster: "+ClusterNameParm
	try:
		_excp_ = 0
		createCluster = AdminConfig.create("ServerCluster", oCellToUse, attrs )
		result = createCluster
		print "create the cluster: "+createCluster
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

	print "Cluster "+ClusterNameParm+" created successfully"


	try:
		_excp_ = 0
		createClusterMember = AdminTask.createClusterMember(parms)
		result = createClusterMember
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception creating cluster member"
		print result
		return
	#endIf 

	print "Cluster "+ClusterNameParm+" with member "+ClusterMemberNameParm+" created successfully"
	
#endDef



#******************************************************************************
# Procedure:   	deleteSIBEngine
# Description:	Delete a messaging engine for a given cluster bus member
#****************************************************************************** 
def deleteSIBEngine ( bus, node, server, cluster):

	global AdminTask
	print '\n====== Delete SIB Engine ====='


		
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus

	if (clusterName != ""):
		parms += ' -cluster '+cluster
	#endIf

	else:
		parms += '-node '+node+' -server '+server		
	#endElse
	
	try:
		_excp_ = 0
		result = AdminTask.deleteSIBEngine(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error deleting SIB Engine for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	deleteSIBForeignBus
# Description:	Delete a SIB foreign bus
#****************************************************************************** 
def deleteSIBForeignBus ( bus, name):

	global AdminTask
	print '\n====== Delete SIB Foreign Bus ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+name+' -name '+name
	
	try:
		_excp_ = 0
		result = AdminTask.deleteSIBForeignBus(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error deleting SIB foreign bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef





#******************************************************************************
# Procedure:   	deleteSIBLink
# Description:	Delete a SIB Link
#****************************************************************************** 
def deleteSIBLink ( bus, messagingEngine, sibLink):

	global AdminTask
	print '\n====== Delete SIB Link ====='

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -sibLink '+sibLink

	try:
		_excp_ = 0
		result = AdminTask.deleteSIBLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error deleting SIB Link "+sibLink
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	deleteSIBMQLink
# Description:	Delete a SIB MQ Link
#****************************************************************************** 
def deleteSIBMQLink ( bus, messagingEngine, mqLink):

	global AdminTask
	print '\n====== Delete SIB MQ Link ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -mqLink '+mqLink

	try:
		_excp_ = 0
		result = AdminTask.deleteSIBMQLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error deleting SIB MQ Link "+mqLink
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:  	listSCAExports
# Description:	List all SCA exports for a given module name and application name.
#
# Note:  	
#****************************************************************************** 
def listSCAExports ( moduleName, appName):

	#-----------------------------------------------------------------------------
	# List SCA Exports
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== List SCA Exports ====='

	parms = '-moduleName '+moduleName

	if (appName != ""):

		parms += ' -applicationName '+appName
	#endIf
	
	try:
		_excp_ = 0
		result = AdminTask.listSCAExports(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SCA exports for module "+moduleName 
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
	else:
		return result
	#endElse


#endDef


#******************************************************************************
# Procedure:  	listSCAImports
# Description:	List all SCA imports for a given module name and application name.
#
# Note:  	
#****************************************************************************** 
def listSCAImports ( moduleName, appName):

	#-----------------------------------------------------------------------------
	# List SCA Imports
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== List SCA Imports ====='

	parms = '-moduleName '+moduleName

	if (appName != ""):

		parms += ' -applicationName '+appName
	#endIf
	
	try:
		_excp_ = 0
		result = AdminTask.listSCAImports(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SCA imports for module "+moduleName
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
	else:
		return result
	#endElse

#endDef


#******************************************************************************
# Procedure:  	listSCAModules
# Description:	List all installed SCA modules.
#
# Note:  	
#****************************************************************************** 
def listSCAModules ( ):

	#-----------------------------------------------------------------------------
	# List SCA Modules
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== List SCA Modules ====='

	
	try:
		_excp_ = 0
		member = AdminTask.listSCAModules()
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		member = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SCA modules"
		print "Error = "+member
		return	
	#endIf
	else:
		return member
	#endElse
		
#endDef




#******************************************************************************
# Procedure:   	listSIBEngines
# Description:	List messaging engines
#****************************************************************************** 
def listSIBEngines ( bus, node, server, cluster):

	global AdminTask
	print '\n====== List SIB Engines ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus

	if (clusterName != ""):
		parms += ' -cluster '+cluster
	#endIf

	else:
		parms += '-node '+node+' -server '+server		
	#endElse

	
	try:
		_excp_ = 0
		result = AdminTask.listSIBEngines(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SIB engines for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef





#******************************************************************************
# Procedure:   	listSIBForeignBuses
# Description:	List all SIB foreign buses
#****************************************************************************** 
def listSIBForeignBuses ( busName, type, routingType):

	global AdminTask
	print '\n====== List SIB Foreign Buses ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName

	if (type != ""):
		parms += ' -type '+type
	#endIf

	if (routingType != ""):
		parms += ' -routingType '+routingType
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.listSIBForeignBuses(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SIB foreign buses for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef





#******************************************************************************
# Procedure:   	listSIBLinks
# Description:	List all SIB Links
#****************************************************************************** 
def listSIBLinks ( bus, node, server, cluster, messagingEngine):

	global AdminTask
	print '\n====== List SIB Links ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName

	if (type != ""):
		parms += ' -type '+type
	#endIf

	if (routingType != ""):
		parms += ' -routingType '+routingType
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.listSIBLinks(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SIB links for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef





#******************************************************************************
# Procedure:   	listSIBMQLinks
# Description:	List all SIB MQ Links
#****************************************************************************** 
def listSIBMQLinks ( bus, node, server, cluster, messagingEngine):

	global AdminTask
	print '\n====== List SIB MQ Links ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName

	if (node != ""):
		parms += ' -node '+node
	#endIf

	if (server != ""):
		parms += ' -server '+server
	#endIf

	if (cluster != ""):
		parms += ' -cluster '+cluster
	#endIf

	if (messagingEngine != ""):
		parms += ' -messagingEngine '+messagingEngine
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.listSIBMQLinks(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error listing SIB MQ Links for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	modifySIBForeignBus
# Description:	Modify a SIB foreign bus
#****************************************************************************** 
def modifySIBForeignBus ( bus, name, routingType, type, description, sendAllowed, inboundUserid, outboundUserid, nextHopBus, destinationDefaults, topicSpaceMappings):

	global AdminTask
	print '\n====== Modify SIB Foreign Bus ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName+' -name '+name+' -routingType '+routingType

	if (type != ""):
		parms += ' -type '+type
	#endIf

	if (description != ""):
		parms += ' -description '+description
	#endIf

	if (sendAllowed != ""):
		parms += ' -sendAllowed '+sendAllowed
	#endIf

	if (inboundUserid != ""):
		parms += ' -inboundUserid '+inboundUserid
	#endIf

	if (outboundUserid != ""):
		parms += ' -outboundUserid '+outboundUserid
	#endIf

	if (nextHopBus != ""):
		parms += ' -nextHopBus '+nextHopBus
	#endIf

	if (destinationDefaults != ""):
		parms += ' -destinationDefaults '+destinationDefaults
	#endIf

	if (topicSpaceMappings != ""):
		parms += ' -topicSpaceMappings '+topicSpaceMappings
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.modifySIBForeignBus(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error modifying SIB foreign buses for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	modifySIBLink
# Description:	Modify a SIB Link
#****************************************************************************** 
def modifySIBLink ( propertyFileName):

	global AdminTask
	print '\n====== Modify SIB Link ====='


	readProperties(propertyFileName)

	bus = 		getProperty("BUS")
	messagingEngine = getProperty("MESSAGINGENGINE")
	name = 		getProperty("SIBLINKNAME")
	bootstrapEndpoints = getProperty("BOOTSTRAPENDPOINTS")
	remoteMessagingEngineName = getProperty("REMOTEMESSAGINGENGINENAME")
	description = getProperty("DESCRIPTION")
	protocolName = getProperty("PROTOCOLNAME")
	authAlias = getProperty("AUTHALIAS")
	initialState = getProperty("INITIALSTATE")
	

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor


	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -name '+name

	if (bootstrapEndpoints != ""):
		parms += ' -bootstrapEndpoints '+bootstrapEndpoints
	#endIf

	if (remoteMessagingEngineName != ""):
		parms += ' -remoteMessagingEngineName '+remoteMessagingEngineName
	#endIf

	if (description != ""):
		parms += ' -description '+description
	#endIf

	if (protocolName != ""):
		parms += ' -protocolName '+protocolName
	#endIf

	if (authAlias != ""):
		parms += ' -authAlias '+authAlias
	#endIf

	if (initialState != ""):
		parms += ' -initialState '+initialState
	#endIf

	
	try:
		_excp_ = 0
		result = AdminTask.createSIBLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error modifying SIB link for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	modifySIBMQLink
# Description:	Modify a SIB MQ Link
#****************************************************************************** 
def modifySIBMQLink ( propertyFileName ):

	global AdminTask
	print '\n====== Modify SIB MQ Link ====='


	readProperties(propertyFileName)

	bus = 		getProperty("BUS")
	messagingEngine = getProperty("MESSAGINGENGINE")
	mqLink = 		getProperty("MQLINK")
	name = 		getProperty("NAME")
	queueManagerName = getProperty("QUEUEMANAGERNAME")
	desc =		getProperty("DESCRIPTION")
	batchSize =		getProperty("BATCHSIZE")
	maxMsgSize =	getProperty("MAXMSGSIZE")
	heartBeat =		getProperty("HEARTBEAT")
	sequenceWrap =	getProperty("SEQUENCEWRAP")
	nonPersistentMessageSpeed =	getProperty("NONPERSISTENTMSGSPEED")
	adoptable = 	getProperty("ADOPTABLE")
	initialState =	getProperty("INITIALSTATE")
	senderChannelName =	getProperty("SENDERCHANNELNAME")
	hostName =		getProperty("HOSTNAME")
	port =		getProperty("PORT")
	discInterval = 	getProperty("DISCINTERVAL")
	shortRetryCount = getProperty("SHORTRETRYCOUNT")
	shortRetryInterval =	getProperty("SHORTRETRYINTERVAL")
	longRetryCount =	getProperty("LONGRETRYCOUNT")
	longRetryInterval =	getProperty("LONGRETRYINTERVAL")
	senderChannelInitialState =	getProperty("SENDERCHANNELINITIALSTATE")
	receiverChannelName =	getProperty("RECEIVERCHANNELNAME")
	inboundNonPersistentReliability =	getProperty("INBOUNDNONPERSISTENTRELIABILITY")
	inboundPersistentReliability =	getProperty("INBOUNDPERSISTENTRELIABILITY")
	receiverChannelInitialState = getProperty("RECEIVERCHANNELINITIALSTATE")
	
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor


	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -mqLink '+mqLink

	if (name != ""):
		parms += ' -name '+name
	#endIf

	if (queueManagerName != ""):
		parms += ' -queueManagerName '+queueManagerName
	#endIf

	if (desc != ""):
		parms += ' -desc '+desc
	#endIf

	if (batchSize != ""):
		parms += ' -batchSize '+batchSize
	#endIf

	if (maxMsgSize != ""):
		parms += ' -maxMsgSize '+maxMsgSize
	#endIf

	if (heartBeat != ""):
		parms += ' -heartBeat '+heartBeat
	#endIf

	if (sequenceWrap != ""):
		parms += ' -sequenceWrap '+sequenceWrap
	#endIf

	if (nonPersistentMessageSpeed != ""):
		parms += ' -nonPersistentMessageSpeed '+nonPersistentMessageSpeed
	#endIf

	if (adoptable != ""):
		parms += ' -adoptable '+adoptable
	#endIf

	if (initialState != ""):
		parms += ' -initialState '+initialState
	#endIf

	if (senderChannelName != ""):
		parms += ' -senderChannelName '+senderChannelName
	#endIf

	if (hostName != ""):
		parms += ' -hostName '+hostName
	#endIf

	if (discInterval != ""):
		parms += ' -discInterval '+discInterval
	#endIf

	if (shortRetryCount != ""):
		parms += ' -shortRetryCount '+shortRetryCount
	#endIf


	if (shortRetryInterval != ""):
		parms += ' -shortRetryInterval '+shortRetryInterval
	#endIf

	if (longRetryCount != ""):
		parms += ' -longRetryCount '+longRetryCount
	#endIf


	if (longRetryInterval != ""):
		parms += ' -longRetryInterval '+longRetryInterval
	#endIf


	if (senderChannelInitialState != ""):
		parms += ' -senderChannelInitialState '+senderChannelInitialState
	#endIf


	if (receiverChannelName != ""):
		parms += ' -receiverChannelName '+receiverChannelName
	#endIf

	if (inboundNonPersistentReliability != ""):
		parms += ' -inboundNonPersistentReliability '+inboundNonPersistentReliability
	#endIf


	if (receiverChannelInitialState != ""):
		parms += ' -receiverChannelInitialState '+receiverChannelInitialState
	#endIf


	
	try:
		_excp_ = 0
		result = AdminTask.modifySIBMQLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error modifying SIB MQ links for bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef



#******************************************************************************
# Procedure:  	modifySCAImportSCABinding
#
# Note:  	
#****************************************************************************** 
def modifySCAImportSCABinding ( moduleName, importName, targetModule, targetExport, appName):

	#-----------------------------------------------------------------------------
	# Modify SCA Import Binding
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Modify SCA Import Binding ====='

	parms = '-moduleName '+moduleName+' -import '+importName+' -targetModule '+targetModule+' -targetExport '+targetExport
	
	if (appName != ""):
		parms += ' -applicationName '+appName
	#endIf

	
		try:
			_excp_ = 0
			result = AdminTask.modifySCAImportSCABinding(parms)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			print "Error modifying import binding for SCA module "+moduleName+", import "+ImportName+", target module "+targetModule+", target export "+targetExport
			if (appName != ""):
				print " and application "+appName
			#endIf

			print "Error = "+result
			return
		#endIf 
				
#endDef






#******************************************************************************
# Procedure:   	removeUserOrGroupFromForeignBus
# Description:	Remove users or groups from foreign bus roles
#****************************************************************************** 
def removeUserOrGroupFromForeignBus ( busName, foreignBusName, roleName, userName, groupName ):

	global AdminTask
	print '\n====== Remove User or Group from Foreign Bus ====='

	
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			print ""+busName+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+busName+ ' -foreignBus ' + foreignBusName + ' -role ' + roleName

	if (userName != ""):
		parms += ' -user '+userName
	#endIf

	else:
		parms += ' -group '+groupName
	#endElse

	
	try:
		_excp_ = 0
		result = AdminTask.removeUserFromForeignBusRole(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error removing "+userName+" to "+foreignBusName+ "connector roles"
		print "Error = "+result
		return
	#endIf 

	print "Removing from foreign bus roles was successful."
	
#endDef



#******************************************************************************
# Procedure:  	showSCAExport
# Description:	Show details for an SCA export.
#
# Note:  	
#****************************************************************************** 
def showSCAExport ( moduleName, exportName, appName):

	#-----------------------------------------------------------------------------
	# Show SCA Export
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Show SCA Export ====='

	parms = '-moduleName '+moduleName+' -export '+exportName

	if (appName != ""):
		parms += ' -applicationName '+appName
	#endIf

	try:
		_excp_ = 0
		result = AdminTask.showSCAExport(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing export for SCA module "+moduleName+", export "+exportName
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
				
#endDef



#******************************************************************************
# Procedure:  	showSCAExportBinding
# Description:	Show details for an SCA export binding.
#
# Note:  	
#****************************************************************************** 
def showSCAExportBinding ( moduleName, exportName, appName):

	#-----------------------------------------------------------------------------
	# Show SCA Export
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Show SCA Export Binding ====='

	parms = '-moduleName '+moduleName+' -export '+exportName

	if (appName != ""):
		parms += ' -applicationName '+appName
	#endIf

	try:
		_excp_ = 0
		result = AdminTask.showSCAExportBinding(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing export binding for SCA module "+moduleName+", export "+exportName
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
				
#endDef


#******************************************************************************
# Procedure:  	showSCAImport
# Description:	Show details for an SCA import.
#
# Note:  	
#****************************************************************************** 
def showSCAImport ( moduleName, importName, appName):

	#-----------------------------------------------------------------------------
	# Show SCA Import
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Show SCA Import ====='

	parms = '-moduleName '+moduleName+' -import '+importName
	
	if (appName != ""):
		parms += ' -applicationName '+appName
	#endIf
	
	try:
		_excp_ = 0
		result = AdminTask.showSCAImport(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing import for SCA module "+moduleName+", import "+importName
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
				
#endDef



#******************************************************************************
# Procedure:  	showSCAImportBinding
# Description:	Show details for an SCA import binding.
#
# Note:  	
#****************************************************************************** 
def showSCAImportBinding ( moduleName, importName, appName):

	#-----------------------------------------------------------------------------
	# Show SCA Import Binding
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Show SCA Import Binding ====='

	
	parms = '-moduleName '+moduleName+' -import '+importName

	if (appName != ""):
			parms += ' -applicationName '+appName
	#endIf

	try:
			_excp_ = 0
			result = AdminTask.showSCAImportBinding(parms)
	except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing import binding for SCA module "+moduleName+", import "+importName
		if (appName != ""):
				print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
				
#endDef


#******************************************************************************
# Procedure:  	showSCAModule
# Description:	Show details for an SCA module.
#
# Note:  	
#****************************************************************************** 
def showSCAModule ( moduleName, appName):

	#-----------------------------------------------------------------------------
	# Show SCA Module
	#-----------------------------------------------------------------------------
	global AdminTask
	print '\n====== Show SCA Module ====='

	parms = '-moduleName '+moduleName

	if (appName != ""):
		parms += ' -applicationName '+appName
	#endIf

	try:
		_excp_ = 0
		result = AdminTask.showSCAModule(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing SCA module "+moduleName
		if (appName != ""):
			print " and application "+appName
		#endIf
		print "Error = "+result
		return
	#endIf 
	else:
		return result
	#endElse
				
#endDef





#******************************************************************************
# Procedure:   	showSIBForeignBus
# Description:	Show details of a SIB foreign bus
#****************************************************************************** 
def showSIBForeignBus ( bus, name):

	global AdminTask
	print '\n====== Show SIB Foreign Bus ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+name+' -name '+name
	
	try:
		_excp_ = 0
		result = AdminTask.showSIBForeignBus(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing SIB foreign bus "+bus
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	showSIBLink
# Description:	Show details of a SIB foreign bus
#****************************************************************************** 
def showSIBLink ( bus, messagingEngine, sibLink):

	global AdminTask
	print '\n====== Show SIB Links ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -sibLink '+sibLink
	
	try:
		_excp_ = 0
		result = AdminTask.showSIBLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing SIB Link "+sibLink
		print "Error = "+result
		return
	#endIf 
		
#endDef




#******************************************************************************
# Procedure:   	showSIBMQLink
# Description:	Show details of a SIB foreign bus
#****************************************************************************** 
def showSIBMQLink ( bus, messagingEngine, mqLink):

	global AdminTask
	print '\n====== Show SIB MQ Link ====='


	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(bus) < 0):
			print ""+bus+ " does not exist."
			return
		#endIf
	#endFor

	parms = '-bus '+bus+' -messagingEngine '+messagingEngine+' -mqLink '+mqLink
	
	try:
		_excp_ = 0
		result = AdminTask.showSIBMQLink(parms)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error showing SIB MQ Link "+mqLink
		print "Error = "+result
		return
	#endIf 
		
#endDef



#******************************************************************************
# Procedure:	updateMEDataStore
# Description:	Updates Message Engine Data Store information. 
#****************************************************************************** 
def updateMEDataStore ( bus, cluster, node, server, schemaName, createTables, authAlias):

	#---------------------------------------------------------------------------------
	# Create server cluster with one member if it does not exist
	#---------------------------------------------------------------------------------

	global AdminTask, AdminConfig

	print '\n====== Update ME Data Store ======'

	parms = ' -bus '+bus

	if (cluster != ""):
		target = ' -cluster '+cluster

	#endIf

	else:
		target = ' -node '+node + ' -server '+server
	#endElse

	parms += target

	engine = AdminTask.listSIBEngines(parms)

	dataStore = AdminConfig.showAttribute(engine, "dataStore")
		
	dsattrs = [["schemaName", schemaName],["createTables",createTables],["authAlias",authAlias]]	

	try:
		_excp_ = 0
		updateDS = AdminConfig.modify(dataStore,dsattrs)
		result = updateDS
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception updating data store"
		print result
		return
	#endIf 

	print "Data store "+dataStore+" updated successfully"
	
#endDef









