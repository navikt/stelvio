import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs

import lib.logUtil as l

###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	serviceIntegration.py
# Description:	This file contains the following service integration procedures:
#		
#			addServerOrClusterToSIB
#			removeServerOrClusterToSIB
#			addUserOrGroupToBus
#			createSIBDestination
#			createSIBus
#			removeUserOrGroupFromBus
#			toggleSIBService
#
#
# History:		
#******************************************************************************
#******************************************************************************
# Procedure:  	removeServerOrClusterToSIB
# Description:	Make server or cluster into a bus member.
# Author:     	Chuck Misuraca - test@example.com
#
# Note:  	Even though the documents indicate that description is an option, it 
#	 	is not available from wsadmin or the console.
#****************************************************************************** 
def removeServerOrClusterToSIB ( busName, serverName, nodeName):

	#-----------------------------------------------------------------------------
	# Add Server/cluster to the SIBus 
	#-----------------------------------------------------------------------------
	global AdminTask
	l.info('\n====== remove '+serverName+' to the '+busName+' SIB =====')

	SIBus = ['']
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) >= 0):
			SIBus = bus
			break

	if (SIBus == ['']):
		l.info(busName+ " does not exist.")
		return

	if ( nodeName != ""):
		parms = '-bus '+busName+' -node '+nodeName+' -server '+serverName
	else:
		parms = '-bus '+busName+' -cluster '+serverName

	try:
		member = AdminTask.removeSIBusMember(parms)
	except:
		type, value, tracebackObj = sys.exc_info()
		l.error("Error removing "+serverName+" to "+busName+":\n"+value)

	l.info(serverName+" is no longer a member of "+busName+".")
		

#******************************************************************************
# Procedure:  	addServerOrClusterToSIB
# Description:	Make server or cluster into a bus member.
#
# Note:  	Even though the documents indicate that description is an option, it 
#	 	is not available from wsadmin or the console.
#****************************************************************************** 
def addServerOrClusterToSIB ( busName, serverName, nodeName, dsName ):

	#-----------------------------------------------------------------------------
	# Add Server/cluster to the SIBus 
	#-----------------------------------------------------------------------------
	global AdminTask
	l.info('\n====== Add '+serverName+' to the '+busName+' SIB =====')

	SIBus = ''
	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) >= 0):
			SIBus = bus
			break

	if not SIBus:
		exit(busName+ " does not exist.")

	if ( nodeName != ""):
		parms = '-bus '+busName+' -node '+nodeName+' -server '+serverName
	else:
		parms = '-bus '+busName+' -cluster '+serverName+' -datasourceJndiName '+dsName

	try:
		member = AdminTask.addSIBusMember(parms)
	except:
		type, value, tracebackObj = sys.exc_info()
		l.error("Error adding "+serverName+" to "+busName+":\n"+value)

	l.info(serverName+" is now a member of "+busName+".")
		

#******************************************************************************
# Procedure:   	addUserOrGroupToBus
# Description:	Add users or groups to bus connector roles
#****************************************************************************** 
def addUserOrGroupToBus ( busName, userName, groupName ):

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			l.info(busName+ " does not exist.")
			return

	if (userName != ""):
		parms = '-bus '+busName+' -user '+userName
	
		try:
			result = AdminTask.addUserToBusConnectorRole(parms)
		except:
			type, value, tracebackObj = sys.exc_info()
			l.error("Error adding "+userName+" to "+busName+ "connector roles:\n"+value)
	else:

		parms = '-bus '+busName+' -group '+groupName
	
		try:
			result = AdminTask.addGroupToBusConnectorRole(parms)
		except:
			type, value, tracebackObj = sys.exc_info()
			l.error("Error adding "+groupName+" to "+busName+ "connector roles"+value)

	l.info("Adding to bus connector roles was successful.")
	

#******************************************************************************
# Procedure:   	createSIBDestination
# Description:	Create a new SIB Destination on the specified SIBus
# History:	
#****************************************************************************** 
def createSIBDestination( propertyFileName ):

	readProperties(propertyFileName)

	busName = 	getProperty("BUSNAME")
	destName = 	getProperty("NAME")
	type = 		getProperty("TYPE")

	clusterName = 	getProperty("CLUSTER")
	nodeName = 	getProperty("NODE")
	serverName =	getProperty("SERVER")
	aliasBus =	getProperty("ALIAS_BUS")
	targetBus =	getProperty("TARGET_BUS")
	targetName=	getProperty("TARGET_NAME")
	foreignBus =	getProperty("FOREIGN_BUS")
	desc =		getProperty("DESCRIPTION")
	reliability =	getProperty("RELIABILITY")
	maxRel =	getProperty("MAX_RELIABILITY")
	overRide =	getProperty("OVERRIDE")
	defPriority =	getProperty("DEFAULT_PRIORITY")
	maxFailedDel = 	getProperty("MAX_FAILED_DEL")
	exceptDest =	getProperty("EXCEPT_DEST")
	sendAllowed =	getProperty("SEND_ALLOWED")
	recvAllowed =	getProperty("RECV_ALLOWED")
	recvExcl =	getProperty("RECV_EXCL")
	topicAccess =	getProperty("TOPIC_ACCESS")
	replyDest =	getProperty("REPLY_DEST")
	replyDestBus = 	getProperty("REPLY_DEST_BUS")
	delAuth =	getProperty("DEL_AUTH")
	highMessThres =	getProperty("HIGH_MESSAGE_THRESHOLD")
               
	#-----------------------------------------------------------------------------
	# Create a SIB Destination on the given SIBus
	#-----------------------------------------------------------------------------
	global AdminTask
	global AdminConfig

	l.info('(createSIBDestination): Create '+destName+' on the '+busName+' SIBus')
	parms = '-bus '+busName+' -name "'+destName+'" -type '+type

	if (type != "Alias"):
		parms += ' -overrideOfQOSByProducerAllowed '+overRide
		parms += ' -receiveAllowed '+recvAllowed+' -sendAllowed '+sendAllowed

	if (type == "Queue" and nodeName == ""):
		parms += ' -cluster '+clusterName+' -receiveExclusive '+recvExcl
	elif (type == "Queue"):
		parms += ' -node '+nodeName+' -server '+serverName+' -receiveExclusive '+recvExcl
	elif (type == "TopicSpace"):
		parms += ' -topicAccessCheckRequired '+topicAccess
	
	if(type == "Foreign"):
		parmsmodify = '-bus '+busName+' -name "'+destName+ '" -reliability '+reliability+' -maxReliability '+maxRel +  ' -overrideOfQOSByProducerAllowed '+overRide + ' -defaultPriority '+defPriority

	parms += ' -aliasBus '+aliasBus+' -targetBus '+targetBus+' -targetName '+targetName
	parms += ' -foreignBus '+foreignBus+' -reliability '+reliability+' -maxReliability '+maxRel
	parms += ' -description "'+desc+'" -defaultPriority '+defPriority
	parms += ' -maxFailedDeliveries '+maxFailedDel+' -exceptionDestination '+exceptDest
	parms += ' -replyDestination '+replyDest
	parms += ' -replyDestinationBus '+replyDestBus+' -delegateAuthorizationCheckToTarget '+delAuth
	
	SIDestList = AdminTask.listSIBDestinations('-bus '+busName).split(lineSeparator)
	# Check for existence of SIB Destination, if yes delete
	for dest in SIDestList:
		ident = AdminConfig.showAttribute(dest, "identifier")
		if (ident == destName):
			l.info("(createSIBDestination): "+ destName+ " exists. Removing SIB destination...")
			try:
				foreignBus = getForeignBusName(dest)
				if type == "Foreign":
					if foreignBus:
						l.debug('Type is "Foreign" and name is:', foreignBus)
						AdminTask.deleteSIBDestination('-bus '+busName + ' -name '+destName + ' -foreignBus '+foreignBus)
				else:
					AdminTask.deleteSIBDestination('-bus '+busName + ' -name '+destName)
			except:
				type, value, tracebackObj = sys.exc_info()
				l.error("Error removing SIB Destination:\n" +value)
	
	try:
		result = AdminTask.createSIBDestination(parms)
		if(type == "Foreign"):
			result = AdminTask.modifySIBDestination(parmsmodify)
			
		#Set the highMessageThreshold on the QueuePoint for the destination
		#TODO:  Find a way to do a query instead of looping through all queuepoints.
		#	Fix this ".000-SCA stuff. It must be configurable.
		if (type == "Queue" and highMessThres != None and highMessThres != "" ):
			l.info("(createSIBDestination): Updating QueuePoint: HIGH_MESSAGE_THRESHOLD is set")
			allqp = AdminConfig.list('SIBQueueLocalizationPoint').split(lineSeparator)
			queuepointupdated=0
			for qp in allqp:
				identifier = AdminConfig.showAttribute(qp, 'identifier')
				if (identifier == destName + "@" + clusterName + ".000-" + busName):
					l.info("(createSIBDestination): Updating QueuePoint " + identifier + " with highMessageThreshold " + highMessThres)
					AdminConfig.modify(qp, [["highMessageThreshold", highMessThres]] )
					queuepointupdated=1
					break
		
			if(queuepointupdated==0):
				l.error("(createSIBDestination): Could not find queuepoint og det er litt rart: " + destName + "@" + clusterName + ".000-" + busName)

                
	except:
		type, value, tracebackObj = sys.exc_info()
		l.error("(createSIBDestination): Error creating SIB Destination:\n"+value)
	#endIf 

	l.info("(createSIBDestination): "+destName+ " SIB Destination successfully created.")
	
#******************************************************************************
# Procedure:   	deleteSIBDestination
# Description:	Delete SIB Destination on the specified SIBus
# History:	
#****************************************************************************** 
def deleteSIBDestination( propertyFileName ):

	readProperties(propertyFileName)

	busName = 	getProperty("BUSNAME")
	destName = 	getProperty("NAME")
	l.info('(deleteSIBDestination): Delete '+destName+' on the '+busName+' SIBus')
	SIDestList = AdminTask.listSIBDestinations('-bus '+busName).split(lineSeparator)
	# Check for existence of SIB Destination, if yes delete
	for dest in SIDestList:
		ident = AdminConfig.showAttribute(dest, "identifier")
		if (ident == destName):
			try:
				AdminTask.deleteSIBDestination('-bus '+busName + ' -name '+destName)
			except:
				type, value, tracebackObj = sys.exc_info()
				l.error("(deleteSIBDestination): Error removing SIB Destination:\n"+value)

	l.info("(deleteSIBDestination): "+destName+ " SIB Destination successfully deleted.")


#******************************************************************************
# Procedure:   	createSIBus
# Description:	Create new service integration bus
# History:	02/28/06:  Added inter-engine auth alias, configuration reload
#			   enabled, and high message threshold
#		03/13/06:  Added extra quotes around interAuth
#****************************************************************************** 
def createSIBus( propertyFileName ):

	readProperties(propertyFileName)

	name = 			getProperty("NAME")
	desc =			getProperty("DESC")
	secure = 		getProperty("SECURE")
	interAuth =		getProperty("IE_AUTH_ALIAS")
	medAuth =		getProperty("MED_AUTH_ALIAS")
	protocol =		getProperty("PROTOCOL")
	discard = 		getProperty("DISCARD_ON_DELETE")
	confReload =		getProperty("CONFIG_RELOAD")
	msgThreshold =		getProperty("HIGH_MSG_THRESHOLD")	

	#-------------------------------------------------------------------------
	# Create service integration bus if it does not exist. 
	#-------------------------------------------------------------------------
	global AdminTask

	l.info('====== Create SIBus '+name+', if it does not exist ======')

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Check for existence of SIBus
	for bus in SIBusList:
		if (bus.find(name) >= 0):
			l.info(name, "already exists.")
			return

	parms  = '-bus '+name+' -description "'+desc+'" -secure '+secure
	
	parms += ' -interEngineAuthAlias "'+interAuth+'" -mediationsAuthAlias '+medAuth
	parms += ' -protocol '+protocol+' -discardOnDelete '+discard
	parms += ' -configurationReloadEnabled '+confReload+' -highMessageThreshold '+msgThreshold


	try:
		result = AdminTask.createSIBus(parms)
	except:
		type, value, tracebackObj = sys.exc_info()
		l.error("Error creating SIBus:\n"+value)

	l.info("Creation of "+name+" was successful.")
	

#******************************************************************************
# Procedure:   	removeUserOrGroupFromBus
# Description:	Remove users or groups from bus connector roles
#****************************************************************************** 
def removeUserOrGroupFromBus ( busName, userName, groupName ):

	SIBusList = AdminTask.listSIBuses().split(lineSeparator)

	# Make sure the bus exists
	for bus in SIBusList:
		if (bus.find(busName) < 0):
			l.error(busName+ " does not exist.")

	if (userName != ""):
		parms = '-bus '+busName+' -user '+userName
	
		try:
			result = AdminTask.removeUserFromBusConnectorRole(parms)
		except:
			type, value, tracebackObj = sys.exc_info()
			l.error("Error removing "+userName+" from "+busName+ "connector roles:\n"+value)
	else:

		parms = '-bus '+busName+' -group '+groupName
	
		try:
			result = AdminTask.removeGroupFromBusConnectorRole(parms)
		except:
			type, value, tracebackObj = sys.exc_info()
			l.error("Error removing "+groupName+" from "+busName+ "connector roles"+value)

	l.info("Removing from bus connector roles was successful.")
	

#******************************************************************************
# Procedure:  	toggleSIBService
# Description:	Enable/Disable SIB Service for a server
#****************************************************************************** 
def toggleSIBService ( nodeName, serverName, flag ):

	#-----------------------------------------------------------------------------
	# Enable/Disable the SIB Service
	#-----------------------------------------------------------------------------
	l.info("\n ====== Toggle SIB Service for "+serverName+" on "+nodeName+" to "+flag+" ======")

	service = toggleService("SIBService", serverName, nodeName, flag)
	if (service == 0):
		l.info("SIB Service set to "+flag+" successfully")
	else:
		l.error("SIB Service toggling failed.")

def getAttibutesDict(SIBdestination):
	attributeParser = re.compile('\[(\S+)\s(.*)\]')
	out = {}
	for line in AdminConfig.show(SIBdestination).splitlines():
		key, value = attributeParser.match(line).groups()
		out[key] = value
	return out

def getForeignBusName(SIBdestination):
	SIBdestinationAttriutes = getAttibutesDict(SIBdestination)
	if SIBdestinationAttriutes.has_key("bus"): 
		return SIBdestinationAttriutes["bus"]
	else:
		return None