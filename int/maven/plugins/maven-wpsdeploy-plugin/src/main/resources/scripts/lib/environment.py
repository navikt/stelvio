###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	environment.py
# Description:	This file contains the following environment procedures:
#		
#			createNameSpaceBinding
#			createReplicationDomain
#			createReplicator
#			createSharedLibrary
#			createWebSphereVariable
#			removeWebSphereVariable
#
#
# History:		
#		
#******************************************************************************
#****************************************************************************** 
# Procedure:  	createNameSpaceBinding
# Description:	Create a name space binding. If it already exists, modify it.
#****************************************************************************** 
import sys

from lib.utils6 import findConfigTargetWithScope, findNode, findScopeEntry, findServer, findServerOnNode, getConfigId, getConfigItemId, getProperty, readProperties, wsadminToList
from lib.Utils import readProperties, getProperty

import lib.logUtil as log
l = log.getLogger(__name__)

def createNameSpaceBinding ( propertyFileName ):
	
	readProperties( propertyFileName )

	scope =			getProperty("SCOPE")
	scopeName =		getProperty("SCOPE_NAME")
	nodeName =		getProperty("NODE_NAME")
	
	bindingType =		getProperty("BINDING_TYPE")
	bindingId =		getProperty("BINDING_IDENTIFIER")
	nameSpace =		getProperty("NAME_IN_NAMESPACE")
	
	stringVal =		getProperty("STRING_VALUE")

	jndiName =		getProperty("JNDI_NAME")
	ejbLocation =		getProperty("EJB_LOCATION")
	serverName =		getProperty("SERVER_NAME")
	serverNode =		getProperty("SERVER_NODE")

	corbaURL =		getProperty("CORBA_URL")
	fedContext =		getProperty("FEDERATED_CONTEXT")

	providerURL =		getProperty("PROVIDER_URL")

	global AdminConfig

	l.info('(createNameSpaceBinding): Create Name Space Binding '+bindingId)

	scopeEntry = findScopeEntry(scope, scopeName )
	
	if (scopeEntry == 0):
		l.error("(createNameSpaceBinding): Scope "+scopeName+" does not exist. Namespace Binding will not be created.")
	#endIf 

	attrs = []
	attrs.append(["name", bindingId])
	attrs.append(["nameInNameSpace", nameSpace])
	
	
	if (bindingType.lower() == "string"):
		attrs.append(["stringToBind", stringVal])
		stringConf= findConfigTargetWithScope(scope, scopeName, bindingId, "StringNameSpaceBinding")
		if (stringConf == 0):
			try:
				_excp_ = 0
				result = AdminConfig.create("StringNameSpaceBinding", scopeEntry, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception creating string name space binding:\n"+result)
        		#endIf 
		else:
			# Modify existing string name space binding
			
			try:
				_excp_ = 0
				result = AdminConfig.modify(stringConf, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception modifying string name space binding:\n"+result)
        		#endIf 
		#endIf

	elif (bindingType.lower() == "ejb"):
		attrs.append(["applicationServerName", serverName])
		attrs.append(["bindingLocation", ejbLocation])
		attrs.append(["ejbJndiName", jndiName])
		if (ejbLocation.lower() == "singleserver"):
			attrs.append(["applicationNodeName", serverNode]) 
		#endIf

		ejbConf= findConfigTargetWithScope(scope, scopeName, bindingId, "EjbNameSpaceBinding")
		if (ejbConf == 0):
			try:
				_excp_ = 0
				result = AdminConfig.create("EjbNameSpaceBinding", scopeEntry, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception creating EJB name space binding:\n"+result)
        		#endIf 
		else:

			# Modify existing EJB name space binding
			try:
				_excp_ = 0
				result = AdminConfig.modify(ejbConf, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception modifying EJB name space binding:\n"+result)
        		#endIf 
		#endIf

	elif (bindingType.lower() == "corba"):
		attrs.append(["corbanameUrl", corbaURL])
		attrs.append(["federatedContext", fedContext])

		corbaConf= findConfigTargetWithScope(scope, scopeName, bindingId, "CORBAObjectNameSpaceBinding")
		if (corbaConf == 0):
			try:
				_excp_ = 0
				result = AdminConfig.create("CORBAObjectNameSpaceBinding", scopeEntry, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception creating CORBA Object name space binding:\n"+result)
        		#endIf 
		else:

			# Modify existing CORBA object name space binding
			try:
				_excp_ = 0
				result = AdminConfig.modify(corbaConf, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception modifying CORBA Object name space binding:\n"+result)
        		#endIf 
		#endIf

	elif (bindingType.lower() == "indirect"):
		attrs.append(["jndiName", jndiName])
		attrs.append(["providerURL", providerURL])

		indConf= findConfigTargetWithScope(scope, scopeName, bindingId, "IndirectLookupNameSpaceBinding")
		if (indConf == 0):
			try:
				_excp_ = 0
				result = AdminConfig.create("IndirectLookupNameSpaceBinding", scopeEntry, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception creating Indirect name space binding:\n"+result)
        		#endIf 
		else:

			# Modify existing indirect name space binding
			try:
				_excp_ = 0
				result = AdminConfig.modify(indConf, attrs)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(createNameSpaceBinding): Caught Exception modifying Indirect name space binding:\n"+result)
        		#endIf 
		#endIf
	#endIf

	l.info("(createNameSpaceBinding): Create/Modify of Name Space Binding "+bindingId+" was successful.")
	
#endDef

#****************************************************************************** 
# Procedure:  	deleteNameSpaceBinding
# Description:	Create a name space binding. If it already exists, modify it.
#****************************************************************************** 
def deleteNameSpaceBinding ( propertyFileName ):
	
	readProperties( propertyFileName )

	scope =			getProperty("SCOPE")
	scopeName =		getProperty("SCOPE_NAME")
	nodeName =		getProperty("NODE_NAME")
	
	bindingType =		getProperty("BINDING_TYPE")
	bindingId =		getProperty("BINDING_IDENTIFIER")
	nameSpace =		getProperty("NAME_IN_NAMESPACE")
	
	stringVal =		getProperty("STRING_VALUE")

	jndiName =		getProperty("JNDI_NAME")
	ejbLocation =		getProperty("EJB_LOCATION")
	serverName =		getProperty("SERVER_NAME")
	serverNode =		getProperty("SERVER_NODE")

	corbaURL =		getProperty("CORBA_URL")
	fedContext =		getProperty("FEDERATED_CONTEXT")

	providerURL =		getProperty("PROVIDER_URL")

	global AdminConfig
	l.info('(deleteNameSpaceBinding): Delete Name Space Binding '+bindingId)
	scopeEntry = findScopeEntry(scope, scopeName )	
	if (scopeEntry == 0):
		l.error("(deleteNameSpaceBinding): Scope "+scopeName+" does not exist. Namespace Binding will not be deleted.")
	#endIf 
	
	if (bindingType.lower() == "string"):
		
		stringConf= findConfigTargetWithScope(scope, scopeName, bindingId, "StringNameSpaceBinding")
		if (stringConf == 0):
			l.warning("(deleteNameSpaceBinding): No Namespace Binding defined with name: "+bindingId)
			return 0 #ok if does not exists, keep going and no exit form wsadmin
		else:
			# Deleting existing string name space binding		
			try:
				_excp_ = 0
				result = AdminConfig.remove(stringConf)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(deleteNameSpaceBinding): Caught Exception deleting string name space binding:\n"+result)
        		#endIf 
		#endIf
	elif (bindingType.lower() == "ejb"):
		ejbConf= findConfigTargetWithScope(scope, scopeName, bindingId, "EjbNameSpaceBinding")
		if (ejbConf == 0):
			l.warning("(deleteNameSpaceBinding): No Namespace Binding defined with name: "+bindingId)
			return 0 #ok if does not exists, keep going and no exit form wsadmin
		else:

			# Deleting existing EJB name space binding
			try:
				_excp_ = 0
				result = AdminConfig.remove(ejbConf)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(deleteNameSpaceBinding): Caught Exception deleting EJB name space binding:\n"+result)
        		#endIf 
		#endIf

	elif (bindingType.lower() == "corba"):
		
		corbaConf= findConfigTargetWithScope(scope, scopeName, bindingId, "CORBAObjectNameSpaceBinding")
		if (corbaConf == 0):
			l.warning("(deleteNameSpaceBinding): No Namespace Binding defined with name :\n"+bindingId)
			return 0 #ok if does not exists, keep going and no exit form wsadmin
		else:

			# Deleting existing CORBA object name space binding
			try:
				_excp_ = 0
				result = AdminConfig.remove(corbaConf)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(deleteNameSpaceBinding): Caught Exception deleting CORBA Object name space binding:\n"+result)
        		#endIf 
		#endIf

	elif (bindingType.lower() == "indirect"):
		
		indConf= findConfigTargetWithScope(scope, scopeName, bindingId, "IndirectLookupNameSpaceBinding")
		if (indConf == 0):
			l.warning("(deleteNameSpaceBinding): No Namespace Binding defined with name :\n"+bindingId)
			return 0 #ok if does not exists, keep going and no exit form wsadmin
		else:

			# Deleting existing indirect name space binding
			try:
				_excp_ = 0
				result = AdminConfig.remove(indConf)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				l.error("(deleteNameSpaceBinding): Caught Exception deleting Indirect name space binding:\n"+result)
        		#endIf 
		#endIf
	#endIf

	l.info("(deleteNameSpaceBinding): Delete of Name Space Binding "+bindingId+" was successful.")
#endDef


#****************************************************************************** 

# Procedure:  	createReplicationDomain
# Description:	Create a Replication Domain
#****************************************************************************** 
def createReplicationDomain ( propertyFileName ):

	readProperties(propertyFileName)
	
	repName =		getProperty("REP_NAME")
	requestTimeout =	getProperty("REQUEST_TIMEOUT")
	encryptType =		getProperty("ENCRYPT_TYPE")
	numReplicas =		getProperty("NO_OF_REPLICAS")
	drsSize = 		getProperty("DRS_SIZE")
	drsPartition =		getProperty("DRS_PARTITION_ON_ENTRY")
	entrySerial =		getProperty("ENTRY_SERIAL_KIND")
	propertySerial =	getProperty("PROPERTY_SERIAL_KIND")
	poolConn =		getProperty("POOL_CONNECTIONS")
	poolSize =		getProperty("POOL_SIZE") 

	l.info('===== Create Replication Domain '+repName+' if it does not exist  =====')

	cellId = AdminConfig.getid("/Cell:"+cellName+"/")

	# Check if replication domain already exists
	repdom = getConfigItemId("cell", cellName, "", "MultibrokerDomain", repName)
	if (len(repdom) != 0):
		l.error("Replication domain "+repName+" already exists.")
	#endIf

	nameAttr = ["name", repName]

	drsSizeAttr = ["size", drsSize]
	drsPartAttr = ["partitionOnEntry", drsPartition]
	drsAttr = ["partition", [drsSizeAttr, drsPartAttr]]

	poolConnAttr = ["poolConnections", poolConn]
	poolSizeAttr = ["size", poolSize]
	poolAttr = ["pooling", [poolConnAttr, poolSizeAttr]]
	
	encryptTypeAttr  = ["encryptionType", encryptType]
	encryptTimeAttr  = ["requestTimeout", requestTimeout]
	replicaAttr = ["numberOfReplicas", numReplicas]

	entrySerialAttr = ["entrySerializationKind", entrySerial]
	propSerialAttr =  ["propertySerializationKind", propertySerial]
	serialAttr = ["serialization", [entrySerialAttr, propSerialAttr]]

	repAttr = ["defaultDataReplicationSettings", [encryptTypeAttr, encryptTimeAttr, replicaAttr, drsAttr, serialAttr, poolAttr]]
	attrs = [nameAttr, repAttr]

	# Create a replication domain
	try:
		_excp_ = 0
		result = AdminConfig.create("MultibrokerDomain", cellId, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
       	#endTry 
	if (_excp_ ):
		l.error("Caught Exception creating a replication domain")
       	#endIf 

	l.info("Create of Replication Domain "+repName+" was successful.")
	
#endDef


#****************************************************************************** 
# Procedure:  	createReplicator
# Description:	Create a Replicator
#****************************************************************************** 
def createReplicator ( propertyFileName ):

	readProperties(propertyFileName)

	domainName =	getProperty("REP_DOMAIN_NAME")
	brokerName =	getProperty("BROKER_NAME")
	clientHost =	getProperty("CLIENT_HOST")
	clientPort =	getProperty("CLIENT_PORT")
	brokerHost =	getProperty("BROKER_HOST")
	brokerPort =	getProperty("BROKER_PORT")
	nodeName =		getProperty("NODE_NAME")
	serverName =	getProperty("SERVER_NAME")
	enable = 		getProperty("ENABLE")

	l.info('===== Create Replicator '+brokerName+' if it does not exist  =====')

	# Make sure the node exists
	oNodeToUse = findNode(nodeName )
	if (oNodeToUse == 0):
		l.error("Node "+nodeName+" does not exist")
	#endIf
	
	# Make sure the server exists
	oServerToUse = findServer(serverName )
	if (oServerToUse == 0):
		l.error("Server "+serverName+" does not exist")
	#endIf 

	# Check if replicator already exists
	repConf = getConfigItemId("cell", cellName, "", "MultibrokerDomain", domainName)
	rep = AdminConfig.showAttribute(repConf, "entries") 
	if (rep != '[]'):
		for repEntry in rep:
			if (AdminConfig.showAttribute(repEntry, "brokerName") == brokerName):
				l.error("Replicator already exists")
			#endIf
		#endFor
	#endIf

	# create a replicator
	nameAttr = ["brokerName", brokerName]
	clientHostAttr = ["host", clientHost]
	clientPortAttr = ["port", clientPort]
	brokerHostAttr = ["host", brokerHost]
	brokerPortAttr = ["port", brokerPort]
	clientAttr = ["clientEndPoint", [clientHostAttr, clientPortAttr]]
	brokerAttr = ["brokerEndPoint", [brokerHostAttr, brokerPortAttr]]
	attrs = [nameAttr, clientAttr, brokerAttr]
	
	try:
		_excp_ = 0
		result = AdminConfig.create("MultiBrokerRoutingEntry", repConf, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
        #endTry 
	if (_excp_ ):
		l.error("Caught Exception creating replicator:\n"+result)
        #endIf 

	# configure input server to define this replicator entry
	attrs1 = []
	attrs1.append(["domainName", domainName])
	attrs1.append(["brokerName", brokerName])
	attrs1.append(["enable", enable])
	
	serverId = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/")
	try:
		_excp_ = 0
		result = AdminConfig.create("SystemMessageServer", serverId, attrs1)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
       	#endTry 
	if (_excp_ ):
		l.error("Caught Exception creating system message server:\n"+result)
       	#endIf 

	l.info("Create of Replicator "+brokerName+" was successful.")
	
#endDef

#****************************************************************************** 
# Procedure:  	createSharedLibary
# Description:	Create a Shared Library, if it exists already, modify it.
#****************************************************************************** 
def createSharedLibrary ( propertyFileName ):

	readProperties(propertyFileName)
	
	scope = 		getProperty("SCOPE")
	scopeName =		getProperty("SCOPE_NAME")
	nodeName =		getProperty("NODE_NAME")
	
	libName =		getProperty("LIB_NAME")
	libClassPath =	getProperty("LIB_CLASSPATH")

	libDesc =		getProperty("LIB_DESCRIPTION")
	nativeLibPath =	getProperty("NATIVE_LIBPATH")

	global AdminConfig

	l.info('===== Create Shared Library '+libName+' if it does not exist  =====')


	if (scope.lower() == "server"):
		scopeEntry = findServerOnNode(scopeName, nodeName)
	else:
		scopeEntry = findScopeEntry(scope, scopeName )
	#endIf

	if (scopeEntry == 0):
		l.error(""+scopeName+" does not exist.")
	#endIf 

	nameAttr = ["name", libName]
	classAttr = ["classPath", libClassPath]	
	descAttr = ["description", libDesc]

	if (nativeLibPath == "_NULL_"):
		natAttr = ["nativePath", ""]
	else:
		natAttr = ["nativePath", nativeLibPath]
	#endIf

	libConf = findConfigTargetWithScope(scope, scopeName, libName, "Library")

	if (libConf == 0):
		attrs = [nameAttr, classAttr, descAttr, natAttr]
		try:
			_excp_ = 0
			shLib = AdminConfig.create("Library", scopeEntry, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			shLib= `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			l.error("Caught Exception creating Shared Library"+shLib)
		#endIf
	else:
		# Since the classPath and nativePath fields are type String* they will
		# append to when modified.  We want to replace here, so we must modify
		# with an empty string first

		attrs = []
		attrs.append(["classPath", ""])
		attrs.append(["nativePath", ""])

		try:
			_excp_ = 0
			shLib = AdminConfig.modify(libConf, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			shLib= `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			l.error("Caught Exception modifying Shared Library"+shLib)
		#endIf

		attrs1 = [classAttr, descAttr, natAttr]
		try:
			_excp_ = 0
			shLib = AdminConfig.modify(libConf, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			shLib= `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			l.error("Caught Exception modifying Shared Library"+shLib)
		#endIf
	#endIf

	l.info("Create/Modify of Shared Library "+libName+" on "+scopeName+" was successful.")
	
#endDef

#******************************************************************************
# Procedure:   	createWebSphereVariable
# Description:	Create a new variable in the variable map.  If it already 
#			exists, it will modify the value. 
#****************************************************************************** 
def createWebSphereVariable ( scope, scopeName, nodeName, name, value ):

	#------------------------------------------------------------------------------
	# Create variable if it does not exist.  
	#------------------------------------------------------------------------------

	global AdminConfig

	l.info('====== Create variable '+name+' with value '+value+', if it does not exist ======')

	scopeEntry = findScopeEntry(scope, scopeName)
	if (scopeEntry == 0):
		l.error(""+scopeName+" does not exist.")
	#endIf

	varMap = getConfigId(scope, scopeName, nodeName, "VariableMap")

	nameattr = ["symbolicName", name]
	valattr = ["value", value]
	attrs = [nameattr, valattr]

	# Check to see if variable already exists
	entries = AdminConfig.showAttribute(varMap, "entries")
	entryList = wsadminToList(entries)
	modifiedOne = 0
	if (entries != '[]'):
		for entry in entryList:
			entryName = AdminConfig.showAttribute(entry, "symbolicName" )
			if entryName.find(name) >= 0:
        			modifiedOne = 1
				l.info("Modifying value of "+name+" to "+value)
				try:
					_excp_ = 0
					result = AdminConfig.modify(entry, [["value", value]] )
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
        			#endTry 
				if (_excp_ ):
					l.error("Caught Exception modifying variable map"+result)
        			#endIf 
				break 
         		#endIf 
		#endFor 
	#endIf	
	if ( not modifiedOne):
		l.info("Attempting to create the variable "+name+" on "+scopeName)
		try:
			_excp_ = 0
			vmap = AdminConfig.create("VariableSubstitutionEntry", varMap, attrs )
			result = vmap
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			l.error("Caught Exception creating variable map")
		#endIf 
	#endIf 

	l.info("Create/modify variable successful.")
	
#endDef

#******************************************************************************
# Procedure:   	removeWebSphereVariable
# Description:	Remove a variable in the variable map.  If it does not exist, 
#		      it will report that the value does not exist. 
#****************************************************************************** 
def removeWebSphereVariable ( scope, scopeName, nodeName, name ):

        #------------------------------------------------------------------------------
	# Remove variable if it exists.  
	#------------------------------------------------------------------------------

	global AdminConfig

	l.info('====== Remove variable '+name+' if it exists ======')

	if (scope.lower() == "server"):
		scopeEntry = findServerOnNode(scopeName, nodeName)
	else:	scopeEntry = findScopeEntry(scope, scopeName )

	if (scopeEntry == 0):
		l.error(""+scopeName+" does not exist.")
	#endIf

	varMap = getConfigId(scope, scopeName, nodeName, "VariableMap")
	
	# Check to see if variable already exists
	entries = AdminConfig.showAttribute(varMap, "entries")
	entryList = wsadminToList(entries)
	modifiedOne = 0
	if (entries != '[]'):
		for entry in entryList:
			entryName = AdminConfig.showAttribute(entry, "symbolicName" )
			if entryName.find(name) >= 0:
        			modifiedOne = 1
				l.info("Found Variable "+name)
				try:
					_excp_ = 0
					result = AdminConfig.remove(entry)
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
        			#endTry 
				if (_excp_ ):
					l.error("Caught Exception removing variable"+result)
        			#endIf 
				break 
         		#endIf 
		#endFor 
	#endIf	
	if ( not modifiedOne):
		l.info("No matches found for name "+name+" on "+scopeName)
	#endIf 
	
	l.info("Remove variable successful.")
	
#endDef

