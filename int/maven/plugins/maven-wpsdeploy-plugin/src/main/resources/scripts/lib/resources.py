###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	resources.py
# Description:	This file contains the following resources procedures:
#		
#			addDataSourceCustomProperties
#			createCFConnectionPool
#			createCMPConnectionFactory
#			createDataSource
#			createJ2EEResourceProperty
#			createJ2CActivationSpec
#			createJDBCProvider
#			createJMSActivationSpec
#			createJMSConnectionFactory
#			createJMSProvider
#			createJMSQueue
#			createJMSTopic
#			createMailSession
#			createMQConnectionFactory
#			createMQDestination
#			createScheduler
#			createURL
#			createURLCustomProperty
#			createURLProvider
#			createWorkManager
#			modifyResourceProperty
#			removeDataSource
#			removeJDBCProvider
#			setDBPoolMaxConnections
#			installResourceAdapter
#			createRAconnectionFactory
#			createSharedLibrary 
#
#			
#******************************************************************************
from lib.IBM.utils6 import createJAASAuthAlias, findConfigTarget, findConfigTargetWithScope, findDataSourceWithScope, findJDBCProviderWithScope, findScopeEntry, getConfigId, getConfigItemId
from lib.IBM.environment import createSharedLibrary
from lib.javaPropertiesUtil import PropertiesReader
from lib.environmentInfo import getBusName, isLocalBus

import lib.logUtil as logLib
log = logLib.getLogger(__name__)

def addDataSourceCustomProperties ( scope, scopeName, dataSourceName, propName, propValue, propValueType, propDesc ):

	#------------------------------------------------------------------------------
	# Add DataSource Custom Property
	#------------------------------------------------------------------------------

	global AdminConfig

	log.info('====== Add Custom Property '+propName+' to '+dataSourceName+' ======')

	dataSource = findDataSourceWithScope(scope, scopeName, dataSourceName )
	if (dataSource == 0):
		log.info("DataSource doesn't exist")
        	return
	propSet = AdminConfig.showAttribute(dataSource, "propertySet" )

	if (propSet == " "):
		try:
			propSet = AdminConfig.create("J2EEResourcePropertySet", dataSource, [] )
			log.info("Created J2EEResourcePropertySet for DataSource ")
		except:
			log.exception("Caught Exception creating Property Set")

	attrs = [["name", propName], ["type", propValueType], ["value", propValue], ["description", propDesc]]

	# Check to see if property already exists on this DataSource - CMM 05/23/2007
	resourceProps = AdminConfig.showAttribute(propSet,"resourceProperties") 
	I = resourceProps.replace("[","")
	resourceProps = I
	I = resourceProps.replace("]","")
	resourceProps = I
	resourcePropsList = resourceProps.split(" ")

	props = 0
	modifiedOne = 0

	for entry in resourcePropsList:
		if ( entry.find(propName) >= 0):
			modifiedOne = 1
			log.info("Modifying "+propName+" values")
			try:
				result = AdminConfig.modify(entry, [["type", propValueType], ["value", propValue], ["description", propDesc]] )
			except:
				log.exception("Caught Exception modifying property")
			break 

	if ( not modifiedOne ):
		try:
			result = AdminConfig.create("J2EEResourceProperty", propSet, attrs )
		except:
			log.exception("Caught Exception creating Custom Property")
		else:
			log.info("Added DataSource Property "+propName)


#******************************************************************************
# Procedure:  	createCFConnectionPool
# Description:	Create a new Connection Factory Connection Pool
#****************************************************************************** 
def createCFConnectionPool ( cf, type, propertiesPath ):

	# Create Connection Pool

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	cpConnTimeOut =		propReader.get("CP_CONN_TIMEOUT")
	cpMaxConn =		propReader.get("CP_MAX_CONN")
	cpMinConn = 		propReader.get("CP_MIN_CONN")
	cpReapTime =		propReader.get("CP_REAP_TIME")
	cpUnusedTimeOut =	propReader.get("CP_UNUSED_TIMEOUT")
	cpAgedTimeOut =		propReader.get("CP_AGED_TIMEOUT")
	cpPurgePolicy =		propReader.get("CP_PURGE_POLICY")
	
	cpSharedParts =		propReader.get("CP_SHARED_PARTITIONS")
	cpFreePoolParts =	propReader.get("CP_FREE_POOL_PARTITIONS")
	cpFreeTableSize =	propReader.get("CP_FREE_TABLE_SIZE")
	cpSurgeThreshold =	propReader.get("CP_SURGE_THRESHOLD")
	cpSurgeInterval =	propReader.get("CP_SURGE_INTERVAL")
	cpStuckTimer =		propReader.get("CP_STUCK_TIMER")
	cpStuckTime = 		propReader.get("CP_STUCK_TIME")
	cpStuckThreshold =	propReader.get("CP_STUCK_THRESHOLD")

	if (type == "MQ"):
		cpConnectPool =	propReader.get("CP_CONNECT_POOL")
		cpSessionPool = propReader.get("CP_SESSION_POOL")

	attrs = []
	attrs.append(["connectionTimeout", cpConnTimeOut])
	attrs.append(["maxConnections", cpMaxConn])
	attrs.append(["minConnections", cpMinConn])
	attrs.append(["reapTime", cpReapTime])
	attrs.append(["unusedTimeout", cpUnusedTimeOut])
	attrs.append(["agedTimeout", cpAgedTimeOut])
	attrs.append(["purgePolicy", cpPurgePolicy])
	attrs.append(["numberOfSharedPoolPartitions", cpSharedParts])
	attrs.append(["numberOfFreePoolPartitions", cpFreePoolParts])
	#attrs.append(["freePoolDistributionTableSize", cpFreeTableSize])
	attrs.append(["surgeThreshold", cpSurgeThreshold])
	attrs.append(["surgeCreationInterval", cpSurgeInterval])
	attrs.append(["stuckTimerTime", cpStuckTimer])
	attrs.append(["stuckTime", cpStuckTime])
	attrs.append(["stuckThreshold", cpStuckThreshold])
	
	
	
	try:
		if (type == "JMS"):
			result = AdminConfig.create('ConnectionPool', cf, attrs)			
		elif (type == "MQ") and (cpConnectPool == "true"):
			result = AdminConfig.create('ConnectionPool', cf, attrs, 'connectionPool')			
		elif (type == "MQ") and (cpSessionPool == "true"):
			result = AdminConfig.create('ConnectionPool', cf, attrs, 'sessionPool')
			
	except:
		log.exception("(createCFConnectionPool): Caught Exception creating connection pool")

	log.info("(createCFConnectionPool): created Connection Pool.")
	return

#******************************************************************************
# Procedure:  	createJ2CConnectionPool
# Description:	Create a new J2C Connection Pool
#****************************************************************************** 
def createJ2CConnectionPool ( cf, type, propertiesPath ):

	# Create J2C Connection Pool

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	stuckTimerTime =		propReader.get("STUCK_TIMER_TIME")
	stuckTime = 		propReader.get("STUCK_TIME")
	stuckThreshold =	propReader.get("STUCK_THRESHOLD")

	attrs = []
	attrs.append(["stuckTimerTime", stuckTimerTime])
	attrs.append(["stuckTime", stuckTime])
	attrs.append(["stuckThreshold", stuckThreshold])
	
	try:
		result = AdminConfig.create('ConnectionPool', cf, attrs)			
	except:
		log.exception("(createJ2CConnectionPool): Caught Exception creating J2C Connection pool")

	log.info("(createJ2CConnectionPool): created J2C Connection Pool.")
	return

#******************************************************************************
# Procedure:  	installResourceAdapter
# Description:	Install a new Resource Adapter
#****************************************************************************** 
def installResourceAdapter (propertiesPath):
	
	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")
	archivePath =		propReader.get("ARCHIVE_PATH")
	name =			propReader.get("NAME")
	log.info('(installResourceAdapter): Install a new Resource Adapter '+name)
	
	# Check if Resource Adapter already exists
	ra = getConfigItemId(scope, nodeName, "", "J2CResourceAdapter", name)
	
	if (len(ra) != 0):
		log.info("(installResourceAdapter): Resource Adapter "+name+" already exists.")
		return
		
	try:
		result = AdminConfig.installResourceAdapter(archivePath, nodeName, "[-rar.desc 'Resource Adapter']")       		     
		
	except:
		log.exception("(installResourceAdapter): Caught Exception creating Resource Adapter")
	
	log.info("(installResourceAdapter): Install of Resource Adapter "+name+" was successful.")

#******************************************************************************
# Procedure:  	createJ2CconnectionFactory
# Description:	Create a new J2C Resource Adapter Connection Factory
#****************************************************************************** 
def createJ2CConnectionFactory ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	
	scope =		propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")
	name 	 =		propReader.get("NAME")
	jndiName =		propReader.get("JNDI_NAME")
	desc  	 =		propReader.get("DESCRIPTION")
	raName =		propReader.get("RA_NAME")
	
	connectionUrl 	=	propReader.get("CONNECTION_URL")
	portNumber 		=	propReader.get("PORT_NUMBER")
	serverName  		=	propReader.get("SERVER_NAME")
	socketConnectTimeOut	=	propReader.get("SOCKET_CONNECT_TIMEOUT")
	connectionTimeout	=	propReader.get("CONNECTION_TIMEOUT")
	applIdQualifier      =      propReader.get("APPL_ID_QUALIFIER")
	applId	              = 	propReader.get("APPL_ID")
	tpnName				=		propReader.get("TPN_NAME")
	requestExits	       =      propReader.get("REQUEST_EXITS")
	tranName			=		propReader.get("TRAN_NAME")
	xaSupport			=		propReader.get("XA_SUPPORT")
	
	log.info('(createJ2CConnectionFactory): Create a new J2C Connection Factory  '+name)
	if (nodeName == ""):
		ra = getConfigItemId(scope, scopeName, "", "J2CResourceAdapter", raName)
	else:
		ra = getConfigItemId(scope, scopeName, nodeName, "J2CResourceAdapter", raName)
		
	if (len(ra) == 0):
		log.error("(createJ2CConnectionFactory): Resource Adapter "+raName+" does not exists. Aborting creation of J2C Connection Factory. Please install J2C adapter.")
		return 1
		
	
	attrs = []
	attrs.append(["name", name])
	attrs.append(["jndiName", jndiName])
	attrs.append(["description", desc])
	
	# If using the old CICS adapters use upper case for first character in custom properties
	if (raName == "ECIResourceAdapter" or raName == "ECIXAResourceAdapter"):
		connAttr = [["name", "ConnectionURL"], ["type", "java.lang.String"], ["value", connectionUrl]] 
		portAttr = [["name", "PortNumber"], ["type", "java.lang.String"], ["value", portNumber]] 
		servAttr = [["name", "ServerName"], ["type", "java.lang.String"], ["value", serverName]] 
		timeoutAttr = [["name", "SocketConnectTimeout"], ["type", "java.lang.String"], ["value", socketConnectTimeOut]] 
		applIdQualifierAttr = [["name", "ApplidQualifier"], ["type", "java.lang.String"], ["value", applIdQualifier]]
		applIdAttr = [["name", "Applid"], ["type", "java.lang.String"], ["value", applId]]
		tpnNameAttr = [["name", "TPNName"], ["type", "java.lang.String"], ["value", tpnName]]
		requestExitsAttr= [["name", "RequestExits"], ["type", "java.lang.String"], ["value", requestExits]]
		tranNameAttr = [["name", "TranName"], ["type", "java.lang.String"], ["value", tranName]]
		xaSupportAttr = [["name", "xaSupport"], ["type", "java.lang.String"], ["value", xaSupport]]
	# USE lover case for first character in custum properties for CICS adapter v9.(CICSECIResourceAdapter)
	else:
		connAttr = [["name", "connectionURL"], ["type", "java.lang.String"], ["value", connectionUrl]] 
		portAttr = [["name", "portNumber"], ["type", "java.lang.String"], ["value", portNumber]] 
		servAttr = [["name", "serverName"], ["type", "java.lang.String"], ["value", serverName]] 
		timeoutAttr = [["name", "socketConnectTimeout"], ["type", "java.lang.String"], ["value", socketConnectTimeOut]] 
		applIdQualifierAttr = [["name", "applidQualifier"], ["type", "java.lang.String"], ["value", applIdQualifier]]
		applIdAttr = [["name", "applid"], ["type", "java.lang.String"], ["value", applId]]
		tpnNameAttr = [["name", "tPNName"], ["type", "java.lang.String"], ["value", tpnName]]
		requestExitsAttr= [["name", "requestExits"], ["type", "java.lang.String"], ["value", requestExits]]
		tranNameAttr = [["name", "tranName"], ["type", "java.lang.String"], ["value", tranName]]
		xaSupportAttr = [["name", "xaSupport"], ["type", "java.lang.String"], ["value", xaSupport]]
		
   	newprops = []
   	newprops.append(connAttr)
   	newprops.append(portAttr)
   	newprops.append(servAttr)
	if(socketConnectTimeOut != None and socketConnectTimeOut != "" ):
   		newprops.append(timeoutAttr)

	if(applIdQualifier != None and applIdQualifier != "" ):
   		newprops.append(applIdQualifierAttr )

	newprops.append(tpnNameAttr)
	
	if(applId != None and applId != "" ):
   		newprops.append(applIdAttr )

	if(requestExits != None and requestExits != "" ):
   		newprops.append(requestExitsAttr)

	newprops.append(tranNameAttr)
	
	if(xaSupport != None and xaSupport != "" ):
		newprops.append(xaSupportAttr)
	
  	psAttr = ["propertySet", [["resourceProperties", newprops]]]
  	
  	attrs.append(psAttr)
   
	if (nodeName == ""):
		j2cjndi = getConfigItemId(scope, scopeName, "", "J2CResourceAdapter", raName + "/J2CConnectionFactory:" + name )
	else:
		j2cjndi = getConfigItemId(scope, scopeName, nodeName, "J2CResourceAdapter", raName + "/J2CConnectionFactory:" + name )
		
	if (len(j2cjndi) != 0):
			log.info("(createJ2CConnectionFactory): J2C Connection Factory "+name+" already exist. Removing J2C Connection Factory...")
			
			try:
				result = AdminConfig.remove(j2cjndi)
			except:
				log.exception("(createJ2CConnectionFactory): Caught Exception removing J2C Connection Factory ")
	
	try:
		result = AdminConfig.create("J2CConnectionFactory", ra, attrs)
	except:
		log.exception("(createJ2CConnectionFactory): Caught Exception creating J2C Connection Factory ")
		
	createJ2CConnectionPool(result, "J2C", propertiesPath)
	
	#Setting the connection timeout
	if(connectionTimeout != None and connectionTimeout != "" ):
		thePool=AdminConfig.showAttribute(result, "connectionPool")
		AdminConfig.modify(thePool, [["connectionTimeout", connectionTimeout ]])

	log.info("(createJ2CConnectionFactory): Create of J2C Connection Factory "+name+" was successful.")
	return 0
#******************************************************************************
# Procedure:  	deleteJ2CconnectionFactory
# Description:	Delete J2C Resource Adapter Connection Factory
# RETURNS:
#    0    Success
#    1    Failure
#****************************************************************************** 
def deleteJ2CConnectionFactory ( propertiesPath ):
	retval = 1
	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	
	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")
	name 	 =		propReader.get("NAME")
	jndiName =		propReader.get("JNDI_NAME")
	desc  	 =		propReader.get("DESCRIPTION")
	raName =		propReader.get("RA_NAME")
	
	connectionUrl 	=	propReader.get("CONNECTION_URL")
	portNumber 	=	propReader.get("PORT_NUMBER")
	serverName  	=	propReader.get("SERVER_NAME")
	
	log.info('(deleteJ2CConnectionFactory): Delete J2C Connection Factory  '+name)
	if (nodeName == ""):
		ra = getConfigItemId(scope, scopeName, "", "J2CResourceAdapter", raName)
	else:
		ra = getConfigItemId(scope, scopeName, nodeName, "J2CResourceAdapter", raName)
		
	if (len(ra) == 0):
		log.error("(deleteJ2CConnectionFactory): Resource Adapter "+raName+" does not exists. Aborting... Please install J2C adapter.")
		return retval
		
   
	if (nodeName == ""):
		j2cjndi = getConfigItemId(scope, scopeName, "", "J2CResourceAdapter", raName + "/J2CConnectionFactory:" + name )
	else:
		j2cjndi = getConfigItemId(scope, scopeName, nodeName, "J2CResourceAdapter", raName + "/J2CConnectionFactory:" + name )
		
	if (len(j2cjndi) != 0):
						
		try:
			result = AdminConfig.remove(j2cjndi)
			log.info("(deleteJ2CConnectionFactory): Delete of J2C Connection Factory "+name+" was successful.")
			return 0
		except:
			log.exception("(deleteJ2CConnectionFactory): Caught Exception removing J2C Connection Factory ")
			
	else:
		log.error("(deleteJ2CConnectionFactory): J2CConnectionFactory "+name+" does not exists.")

#******************************************************************************
# Procedure:  	createCMPConnectionFactory
# Description:	Create a new CMP Connection Factory 
#****************************************************************************** 
def createCMPConnectionFactory ( dsId, propertiesPath ):  

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

 	scope 		=	propReader.get("SCOPE")
	scopeName	=	propReader.get("SCOPE_NAME")
	nodeName 	=	propReader.get("NODE_NAME")

	dataSourceName 	=	propReader.get("DATASOURCE_NAME")
	dbType 		=	propReader.get("DATABASE_TYPE" )
	providerName 	=	propReader.get("PROVIDER_NAME" )
	authAliasName 	= 	propReader.get("ALIAS_NAME" )

	cfName = dataSourceName+"_CF"
	cfAuthMech = "BASIC_PASSWORD"
	rraName = "WebSphere Relational Resource Adapter"
	
	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.info("Unable to find "+scopeName )
		return

	# Check if the connection factory already exists
	objType = "J2CResourceAdapter:"+rraName+"/CMPConnectorFactory"
	cfId = getConfigItemId(scope, scopeName, nodeName, objType, cfName)
	if (cfId != ""):
		log.info(""+cfName+" already exists on "+scope+" "+scopeName)
		return
	else:
		rraId = getConfigItemId(scope, scopeName, nodeName, "J2CResourceAdapter", rraName)

	# Create connection factory using default RRA
	nameAttr	= ["name", cfName]
	authMechAttr 	= ["authMechanismPreference", cfAuthMech]
	cmpDSAttr 	= ["cmpDatasource", dsId]
	attrs 		= [nameAttr, authMechAttr, cmpDSAttr]

	try:
		cf = AdminConfig.create("CMPConnectorFactory", rraId,  attrs)
	except:
		log.exception("Caught Exception creating CMP connection factory")

	# Mapping Module
	 
	mapAuthAttr = ["authDataAlias", authAliasName]
	mapConfigAttr = ["mappingConfigAlias", "DefaultPrincipalMapping"]
	attrs1 = [mapAuthAttr, mapConfigAttr]

	try:
		map = AdminConfig.create("MappingModule", cf, attrs1)
	except:
		log.exception("Caught Exception creating CMP mapping")

#******************************************************************************
# Procedure:   	createDataSource
# Description:	Create Data Source	
#****************************************************************************** 
def createDataSource ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName=		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")

	datasourceName =	propReader.get("DATASOURCE_NAME")
	dbType =		propReader.get("DATABASE_TYPE" )
	providerName =		propReader.get("PROVIDER_NAME" )
	datasourceDesc = 	propReader.get("DATASOURCE_DESC" )
	dsJNDIName = 		propReader.get("JNDI_NAME" )
	cmPersist =		propReader.get("CONTAINER_MANAGED_PERSIST")
	dataSourceHelper = 	propReader.get("DATASOURCE_HELPER_CLASSNAME" )
	cmpManagedAuthAlias =	propReader.get("CMP_MANAGED_AUTH_ALIAS")
	xaRecAuthAlias =	propReader.get("XA_RECOVERY_AUTH_ALIAS")	
	statementCache = 	propReader.get("STATEMENT_CACHE_SIZE" )
	enableAccess =		propReader.get("ENABLE_ACCESS_DETECTION")
	enableDBReauth =	propReader.get("ENABLE_DB_REAUTH")
	enableJMSOpt =		propReader.get("ENABLE_JMS_OPT")
	manageHandles =		propReader.get("MANAGE_CACHED_HANDLES")
	logContext =		propReader.get("LOG_MISSING_CONTEXT")
	pretestConn =		propReader.get("PRETEST_CONNECTIONS")
	pretestInt =		propReader.get("PRETEST_CONN_INTERVAL")
	pretestSQL =		propReader.get("PRETEST_SQL_STRING")
		
	connectionTimeout =	propReader.get("CONNECTION_TIMEOUT" )
	maxConnections = 	propReader.get("MAX_CONNECTIONS" )
	minConnections = 	propReader.get("MIN_CONNECTIONS" )
	reapTime = 		propReader.get("REAP_TIME" )
	unusedTimeout = 	propReader.get("UNUSED_TIMEOUT" )
	agedTimeout = 		propReader.get("AGED_TIMEOUT" )
	purgePolicy = 		propReader.get("PURGE_POLICY" )
	authAliasName = 	propReader.get("ALIAS_NAME" )
	user = 			propReader.get("USER" )
	password = 		propReader.get("PASSWORD" )
	desc = 			propReader.get("DESCRIPTION" )

	#---------------------------------------------------------------------------------
	# Create Data Source 
	#---------------------------------------------------------------------------------

	global AdminApp, AdminConfig

	log.info('(createDataSource): Create Data Source '+datasourceName+', if it does not exist')

	# DB2 Only CMMisuraca
	databaseName =		propReader.get("DATABASE_NAME" )
	driverType =		propReader.get("DRIVERTYPE" )	
	serverName = 		propReader.get("SERVERNAME" )
	portNumber =		propReader.get("PORTNUMBER" )

	# ORACLE Only
	oracleDBHost = propReader.get("ORACLE_DBHOST" )
	oracleId = propReader.get("ORACLE_ID" )
	
	ds1 = findDataSourceWithScope(scope, scopeName, datasourceName )
	log.info(ds1)
	if (ds1 == 0):
		attrs2 = [["name", datasourceName], ["description", datasourceDesc]]
		provider = getConfigItemId(scope, scopeName, nodeName, "JDBCProvider", providerName)
		if (provider == ""):
			log.error("(createDataSource): JDBC Provider does not exist.")
			return 1

		log.info("(createDataSource): Creating the data source "+datasourceName)

		try:
			datasource = AdminConfig.create("DataSource", provider, attrs2 )
		except:
			log.exception("(createDataSource): Caught Exception creating datasource")

		# Set the properties for the data source
		try:
			propSet1 = AdminConfig.create("J2EEResourcePropertySet", datasource, [] )
		except:
			log.exception("(createDataSource): Caught Exception creating data source properties")

		if (dbType == "ORACLE"):
			oraString = "jdbc:oracle:thin:@"+oracleDBHost+":1521:"+oracleId
			attrs3 = [["name", "URL"], ["type", "java.lang.String"], ["value", oraString]]
		else:
			attrs3 = [["name", "databaseName"], ["type", "java.lang.String"], ["value", databaseName]] 
		log.info("(createDataSource): Create J2EEResourceProperty "+databaseName)
		try:
			result = AdminConfig.create("J2EEResourceProperty", propSet1, attrs3 )
		except:
			log.exception("(createDataSource): Caught Exception creating J2EE ResourceProperty")

		log.info("(createDataSource): Create J2EEResourceProperty "+driverType)
		try:
			createJ2EEResourceProperty(propSet1, "driverType", driverType, "java.lang.Integer") 
		except:
			log.exception("(createDataSource): Caught Exception creating J2EE ResourceProperty")
		log.info("(createDataSource): Create J2EEResourceProperty "+serverName)
		try:
			createJ2EEResourceProperty(propSet1, "serverName", serverName, "java.lang.String") 
		except:
			log.exception("(createDataSource): Caught Exception creating J2EE ResourceProperty")

		attrs3 = [["name", "portNumber"], ["type", "java.lang.Integer"], ["value", portNumber]]


		log.info("(createDataSource): Create J2EEResourceProperty ")
		try:
			result = AdminConfig.create("J2EEResourceProperty", propSet1, attrs3 )
		except:
			log.exception("(createDataSource): Caught Exception creating J2EE ResourceProperty")

		log.info("(createDataSource): Create additional J2EEResourceProperties")
		createJ2EEResourceProperty(propSet1, "enableMultithreadedAccessDetection", enableAccess, "java.lang.Boolean") 
		createJ2EEResourceProperty(propSet1, "reauthentication", enableDBReauth, "java.lang.Boolean") 
		createJ2EEResourceProperty(propSet1, "jmsOnePhaseOptimization", enableJMSOpt, "java.lang.Boolean") 
		createJ2EEResourceProperty(propSet1, "preTestSQLString", pretestSQL, "java.lang.String") 

		attrs4 = []
		attrs4.append(['jndiName', dsJNDIName])
		attrs4.append(['statementCacheSize', statementCache])
		attrs4.append(['datasourceHelperClassname', dataSourceHelper])
		attrs4.append(['authMechanismPreference', 'BASIC_PASSWORD'])
		attrs4.append(['manageCachedHandles', manageHandles])
		attrs4.append(['logMissingTransactionContext', logContext])
		
		if (xaRecAuthAlias != ""):  #providerName.find("XA") >= 0: 
			attrs4.append(['xaRecoveryAuthAlias', xaRecAuthAlias])
		else:
			attrs4.append(['authDataAlias', cmpManagedAuthAlias])
#			attrs4.append(['authDataAlias', authAliasName])


		try:
			result = AdminConfig.modify(datasource, attrs4 )
		except:
			log.exception("Caught Exception modifying datasource")

		log.info("(createDataSource): Create connection pool")

		attrs5 = []
		attrs5.append(['connectionTimeout', connectionTimeout])
		attrs5.append(['maxConnections', maxConnections])
		attrs5.append(['minConnections', minConnections])
		attrs5.append(['reapTime', reapTime])
		attrs5.append(['unusedTimeout', unusedTimeout])
		attrs5.append(['agedTimeout', agedTimeout])
		attrs5.append(['purgePolicy', purgePolicy])
		attrs5.append(['testConnection', pretestConn]) 
		attrs5.append(['testConnectionInterval', pretestInt]) 
		try:
			result = AdminConfig.create('ConnectionPool', datasource, attrs5)
		except:
			log.exception("(createDataSource): Caught Exception creating connection pool")

		# Check if Container-managed authentication being used
		if (authAliasName != ""):
			log.info("(createDataSource): Create JAAS Auth Alias")
			authDataAlias = createJAASAuthAlias(cellName, authAliasName, user, password, desc )

			# Set the default principal mapping properties to the datasource
			log.info("Create Container Managed Alias")
			map_auth_attr = ["authDataAlias", authAliasName]
			map_configalias_attr = ["mappingConfigAlias", "DefaultPrincipalMapping"]
			map_attrs = [map_auth_attr, map_configalias_attr]
			mapping_attr = ["mapping", map_attrs]
			attrs = ["authDataAlias", authDataAlias], mapping_attr

			try:
				result = AdminConfig.modify(datasource, attrs )
			except:
				log.exception("(createDataSource): Caught Exception creating container managed alias")

		# Create CMP Connection Factory if necessary
		if (cmPersist == "true"):
			log.info('(createDataSource):  Create CMP Connection Factory '+datasourceName)
			createCMPConnectionFactory(datasource, propertiesPath)

	else:
		log.info("(createDataSource): "+datasourceName+" already exists on "+scope+" "+scopeName)
		return 0
	#endElse 

#******************************************************************************
# Procedure:	removeDataSource
# Description:	Remove a data source
#****************************************************************************** 
def removeDataSource ( propertiesPath):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	scope 		=	propReader.get("SCOPE")
	scopeName	=	propReader.get("SCOPE_NAME")
	dsName 		=	propReader.get("DATASOURCE_NAME")
	
	
	#------------------------------------------------------------------------------
	# Remove Data Source if it exists
	#------------------------------------------------------------------------------

	global AdminConfig

	log.info('(removeDataSource): Remove datasource '+dsName+', if it exists')

	dataSourceId = findDataSourceWithScope(scope, scopeName, dsName)
	if (dataSourceId == 0):
		log.info("(removeDataSource): DataSource "+dsName+" doesn't exist on "+scopeName)
		return 0

	try:
		error = AdminConfig.remove(dataSourceId )
	except:
		log.exception("(removeDataSource): Error removing dataSource "+dsName)

	log.info("(removeDataSource): Removed DataSource "+dsName+" from "+scopeName)



#******************************************************************************
# Procedure:   modifyResourceProperty
# Description:	Create a datasource property (ex: WAS data source property)
#******************************************************************************
def modifyResourceProperty (propSet, propName, propValue, propType):

	global AdminConfig

	attrs = []
	attrs.append(["name", propName])
	attrs.append(["value", propValue])
	attrs.append(["type", propType])	

	try:
		result = AdminConfig.modify(propSet, attrs )
	except:
		log.exception("Caught Exception modifying ResourceProperty")

#******************************************************************************
# Procedure:   createJ2EEResourceProperty
# Description:	Create a datasource property (ex: WAS data source property)
#******************************************************************************
def createJ2EEResourceProperty (propSet, propName, propValue, propType):

	global AdminConfig

	attrs = []
	attrs.append(["name", propName])
	attrs.append(["value", propValue])
	attrs.append(["type", propType])	

	try:
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs )
	except:
		log.exception("Caught Exception creating J2EE ResourceProperty")


#******************************************************************************
# Procedure:   createJDBCProvider
# Description:	Create JDBC provider
#****************************************************************************** 
def createJDBCProvider ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName = 		propReader.get("NODE_NAME")

	providerName =		propReader.get("JDBC_NAME")
	providerDesc =		propReader.get("JDBC_DESC")
	driverClassPath =	propReader.get("JDBC_CLASSPATH")
	impClassName =		propReader.get("JDBC_IMPCLASSNAME")
#CMM 03132007
	nativepath = 		propReader.get("JDBC_NATIVEPATH")
	providerType = 		propReader.get("JDBC_PROVIDERTYPE")
	xa = 			propReader.get("JDBC_XA")

	#----------------------------------------------------------------------------
	# Create JDBC Provider
	#----------------------------------------------------------------------------

	global AdminApp, AdminConfig

	log.info("(createJDBCProvider): Create JDBC Provider "+providerName+", if it does not exist ======")
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.error("(createJDBCProvider):"+scopeName+" does not exist.")
		return 1 

	objType = "JDBCProvider"
	jdbcId = getConfigItemId(scope, scopeName, nodeName, objType, providerName)
	if (jdbcId != ""):
		log.info("(createJDBCProvider): "+providerName+" already exists on "+scope+" "+scopeName)
		## Do nothing JDBC provider exists. This is ok.
		return 0

	else:
		attrs1 = []
		attrs1.append(['classpath', driverClassPath])
		attrs1.append(['implementationClassName', impClassName])
		attrs1.append(['name', providerName])
		attrs1.append(['description', providerDesc])
#CMM 03132007
		attrs1.append(['providerType', providerType])
		attrs1.append(['nativepath', nativepath])
		attrs1.append(['xa', xa])

		try:
			provider1 = AdminConfig.create("JDBCProvider", scopeEntry, attrs1 )
		except:
			log.exception("(createJDBCProvider): Caught Exception creating JDBC Provider")

	log.info("(createJDBCProvider): Created "+providerName+" successfully.")
	


#******************************************************************************
# Procedure:	 	
# Description:	
#****************************************************************************** 
def removeJDBCProvider ( propertiesPath):
	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName = 		propReader.get("NODE_NAME")

	providerName =		propReader.get("JDBC_NAME")

	#------------------------------------------------------------------------------
	# Remove JDBC Provider if it exists
	#------------------------------------------------------------------------------

	global AdminConfig
	log.info('(removeJDBCProvider): Remove JDBC Provider '+providerName+', if it exists.')

	provider = findJDBCProviderWithScope(scope, scopeName, providerName )
	if provider == 0:
		log.error("(removeJDBCProvider): "+providerName+" does not exist on "+scopeName)
		return 0 

	try:
		error = AdminConfig.remove(provider )
	except:
		log.exception("(removeJDBCProvider): Error removing JDBCProvider "+providerName)

	log.info("(removeJDBCProvider): Removed JDBCProvider "+providerName+" from "+scopeName)
	
#******************************************************************************
# Procedure:   	createJ2CActivationSpec
# Description:	Create a new J2C Activation Specification
# History:	
#****************************************************************************** 
def createJ2CActivationSpec ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName = 	propReader.get("SCOPE_NAME")
	name = 		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")
	desc = 		propReader.get("DESC")
	authAlias =	propReader.get("AUTH_ALIAS")
	msgListType =	propReader.get("MSG_LISTENER_TYPE")

	busName = 	getBusName()
	maxBatch = 	propReader.get("MAX_BATCH")
	maxEndpts =	propReader.get("MAX_ENDPTS")
	destType =	propReader.get("DEST_TYPE")
	destName =	propReader.get("DEST_NAME")

	durSubHome =	propReader.get("DUR_SUB_HOME")
	msgSelector = 	propReader.get("MSG_SELECTOR")
	delMode = 	propReader.get("MSG_DEL_MODE")

	subName =	propReader.get("SUB_NAME")
	shareDurSub =	propReader.get("SHARE_DUR_SUB")
	userName = 	propReader.get("USERNAME")
	password =	propReader.get("PASSWORD")
	discrim  =	propReader.get("DISCRIM")

	#------------------------------------------------------------------------------
	# Create a J2C Activation Specification
	#------------------------------------------------------------------------------
	global AdminTask , AdminConfig

	log.info('(createJ2CActivationSpec): Create '+name+' on the '+busName+' SIBus')

	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(createJ2CActivationSpec): Unable to find "+scopeName )
		return 1

	# Check if the activation specification already exists
        asList = AdminConfig.list('J2CResourceAdapter').splitlines()


	parms  = ' -name '+name 
	parms += ' -jndiName '+jndiName
	parms += ' -messageListenerType '+msgListType
	parms += ' -authenticationAlias '+authAlias
	parms += ' -description '+desc

	log.info('INFO: parms '+parms)

	for item in asList:
		if (item.find(scopeName) >= 0 and item.find('SPI') >= 0 ):
			break

	try:
		as = AdminTask.createJ2CActivationSpec(item, parms)
	except:
		log.exception("ERROR: Error creating J2C Activation Spec")

	log.info("INFO: J2C Activation Specification successfully created.")


	# Set the properties for the data source

	Tattrbs = AdminConfig.showAttribute(as,'resourceProperties')
	attrbs = Tattrbs.replace("[","")
	Tattrbs = attrbs.replace("]","")
	attrbs = Tattrbs.split(' ')

	for attrbID in attrbs:
		if (attrbID.find('busName') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "busName", busName, "java.lang.String") 
			except:
				log.exception("ERROR: Caught Exception modifying Act Spec")

		if (attrbID.find('destinationName') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "destinationName", destName, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('destinationType') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "destinationType", destType, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('durableSubscriptionHome') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "durableSubscriptionHome", durSubHome, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('maxBatchSize') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "maxBatchSize", maxBatch, "java.lang.Integer") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('maxConcurrency') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "maxConcurrency", maxEndpts, "java.lang.Integer") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('messageSelector') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "messageSelector", msgSelector, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('password') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "password", password, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('subscriptionName') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "subscriptionName", subName, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('messageDeletionMode') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "messageDeletionMode", delMode, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('shareDurableSubscriptions') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "shareDurableSubscriptions", shareDurSub, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('userName') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "userName", userName, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")

		if (attrbID.find('discriminator') >= 0 ):
			try:
				modifyResourceProperty(attrbID, "discriminator", discrim, "java.lang.String") 
			except:
				log.exception("Caught Exception modifying Act Spec")
	
	log.info("INFO: J2C Activation Specification Custom Properties successfully created.")



#******************************************************************************
# Procedure:   	createJMSActivationSpec
# Description:	Create a new JMS Activation Specification	
#****************************************************************************** 
def createJMSActivationSpec ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName = 	propReader.get("SCOPE_NAME")

	name = 		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")
	destJndiName=	propReader.get("DEST_JNDI_NAME")
	busName = 	getBusName()

	desc = 		propReader.get("DESC")
	ackMode =	propReader.get("ACK_MODE")
	authAlias =	propReader.get("AUTH_ALIAS")
	targetChain =	propReader.get("TARGET_TRANS_CHAIN")
	maxBatch = 	propReader.get("MAX_BATCH")
	maxEndpts =	propReader.get("MAX_ENDPTS")
	clientId =	propReader.get("CLIENT_ID")
	destType =	propReader.get("DEST_TYPE")
	durSubHome =	propReader.get("DUR_SUB_HOME")
	msgSelector = 	propReader.get("MSG_SELECTOR")
	subDur =	propReader.get("SUB_DUR")
	subName =	propReader.get("SUB_NAME")
	shareDurSub =	propReader.get("SHARE_DUR_SUB")
	shareDSCMP =	propReader.get("SHARE_DS_CMP")
	userName = 	propReader.get("USERNAME")
	password =	propReader.get("PASSWORD")

	#------------------------------------------------------------------------------
	# Create a JMS Activation Specification
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(createJMSActivationSpec): Create '+name+' on the '+busName+' SIBus')

	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(createJMSActivationSpec): Unable to find "+scopeName )
		return

	parms  = '-name "'+name+'" -jndiName '+jndiName+' -destinationJndiName '+destJndiName
	parms += ' -busName "'+busName+'"'
	parms += ' -description "'+desc+'" -acknowledgeMode '+ackMode
	parms += ' -clientId "'+clientId+'" -destinationType '+destType
	parms += ' -durableSubscriptionHome "'+durSubHome+'" -authenticationAlias "'+authAlias+'"'
	parms += ' -targetTransportChain '+targetChain+' -shareDataSourceWithCMP '+shareDSCMP
	parms += ' -maxBatchSize '+maxBatch+' -maxConcurrency '+maxEndpts
	parms += ' -messageSelector "'+msgSelector+'" -password "'+password+'"'
	parms += ' -subscriptionDurability "'+subDur+'"'
	parms += ' -subscriptionName "'+subName+'"'
	parms += ' -shareDurableSubscriptions "'+shareDurSub+'" -userName "'+userName+'"'
	
	# Check if the activation specification already exists
	asList = AdminTask.listSIBJMSActivationSpecs(scopeId).splitlines()

	for item in asList:
		if (item.find(name) >= 0):
			log.info("(createJMSActivationSpec): "+name+" already exists.")
			j2cActivationSpec = findConfigTarget(name,"J2CActivationSpec")
			return
			try:
				print "Dead code"
				#TODO: delete or modify SIBJMSActivationSpec
				#as = AdminTask.deleteSIBJMSActivationSpec('[-name '+name+']')
			except:
				log.exception("(createJMSActivationSpec): Error deleting JMS Activation Spec")
	
	try:
		as = AdminTask.createSIBJMSActivationSpec(scopeId, parms)
	except:
		log.exception("(createJMSActivationSpec): Error creating JMS Activation Spec")

	log.info("(createJMSActivationSpec): JMS Activation Specification successfully created.")
	return 0

#******************************************************************************
# Procedure:   	deleteJMSActivationSpec
# Description:	Delete JMS Activation Specification	
#****************************************************************************** 
def deleteJMSActivationSpec ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName = 	propReader.get("SCOPE_NAME")

	name = 		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")
	destJndiName=	propReader.get("DEST_JNDI_NAME")
	busName = 	getBusName()


	#------------------------------------------------------------------------------
	# Create a JMS Activation Specification
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(deleteJMSActivationSpec): Delete '+name+' on the '+busName+' SIBus')

	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(deleteJMSActivationSpec): Unable to find "+scopeName )
		return 1
	
	idList  = AdminTask.listSIBJMSActivationSpecs(scopeId)
	qList = idList.splitlines()
	if not idList:
		print("INFO (deleteJMSActivationSpec): No SIB JMSA ctivation Specs defined.")
		return 0
	try:
		for q in qList:
			nameAttr = AdminConfig.showAttribute(q, 'name')
			if nameAttr == name:
				print("INFO (deleteJMSActivationSpec): Running command: AdminTask.deleteSIBJMSActivationSpec(%s)")
				AdminTask.deleteSIBJMSActivationSpec(q)
	except:
		log.exception("(deleteJMSActivationSpec): Error deleting JMS Activation Spec")
	
	log.info("(deleteJMSActivationSpec): JMS Activation Specification successfully deleted.")
	return 0



#******************************************************************************
# Procedure:  	createJMSConnectionFactory
# Description:	Create a new SIB JMS Connection Factory on the specified SIBus
#****************************************************************************** 
def createJMSConnectionFactory ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName = 		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")

	cfName =		propReader.get("CF_NAME")
	cfJndiName = 		propReader.get("CF_JNDI_NAME")
	busName = 		getBusName()

	cfType =		propReader.get("CF_TYPE")
	cfAuthAlias =		propReader.get("CF_AUTH_ALIAS")
	cfXARecAuthAlias =	propReader.get("CF_XA_REC_AUTH_ALIAS")
	cfCategory =		propReader.get("CF_CATEGORY")
	cfDesc =		propReader.get("CF_DESCRIPTION")
	cfLogMissing =		propReader.get("CF_LOG_MISSING")
	cfManageCached =	propReader.get("CF_MANAGE_CACHED")
	cfClientId =		propReader.get("CF_CLIENTID")
	cfUserName = 		propReader.get("CF_USERNAME")
	cfPassword =		propReader.get("CF_PASSWORD")
	cfDurSubHome =		propReader.get("CF_DURABLE_SUB_HOME")
	cfNonPersistMap =	propReader.get("CF_NONPERSIST_MAP")
	cfPersistMap =		propReader.get("CF_PERSIST_MAP")
	cfReadAhead =		propReader.get("CF_READ_AHEAD")
	cfTarget =		propReader.get("CF_TARGET")
	cfTargetType =		propReader.get("CF_TARGET_TYPE")
	cfTargetSig =		propReader.get("CF_TARGET_SIG")
	cfTargetChain =		propReader.get("CF_TARGET_TRANS_CHAIN")
	cfProviderEndpts =	propReader.get("CF_PROVIDER_ENDPTS")
	cfConnectProx =		propReader.get("CF_CONNECT_PROX")
	cfTempQueuePrefix =	propReader.get("CF_TEMP_QUEUENAME_PREFIX")
	cfTempTopicPrefix =	propReader.get("CF_TEMP_TOPICNAME_PREFIX")
	cfShareDSCmp =		propReader.get("CF_SHARE_DS_CMP")
	cfShareDurSub =		propReader.get("CF_SHARE_DUR_SUB")


	#------------------------------------------------------------------------------
	# Create a SIB JMS Connection Factory on the given SIBus
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(createJMSConnectionFactory) Create '+cfName+' on the '+busName+' SIBus')


	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(createJMSConnectionFactory): Unable to find "+scopeName )
		return 1

	# Check if the connection factory already exists
	
	objType = "J2CResourceAdapter:SIB JMS Resource Adapter/J2CConnectionFactory"
	cfId = getConfigItemId(scope, scopeName, nodeName, objType, cfName)
	if (cfId != ""):
		log.info("(createJMSConnectionFactory): "+cfName+" already exists on "+scope+" "+scopeName + ". Skipping creation.")
		return 0

	parms  = '-name "'+cfName+'" -jndiName '+cfJndiName+' -busName "'+busName+'"' 
	parms += ' -xaRecoveryAuthAlias "'+cfXARecAuthAlias+'" -category "'+cfCategory+'"'  
	parms += ' -description "'+cfDesc+'" -logMissingTransactionContext '+cfLogMissing+' -manageCachedHandles '+cfManageCached 
	parms += ' -clientID "'+cfClientId+'" -userName "'+cfUserName+'" -password "'+cfPassword+'" -durableSubscriptionHome "'+cfDurSubHome+'"'
	parms += ' -nonPersistentMapping "'+cfNonPersistMap+'" -persistentMapping "'+cfPersistMap+'"'
	parms += ' -readAhead '+cfReadAhead+' -target '+cfTarget+' -targetType "'+cfTargetType+'" -targetSignificance '+cfTargetSig 
	parms += ' -targetTransportChain "'+cfTargetChain+'" -providerEndPoints "'+cfProviderEndpts+'" -connectionProximity '+cfConnectProx 
	parms += ' -tempQueueNamePrefix "'+cfTempQueuePrefix+'" -tempTopicNamePrefix "'+cfTempTopicPrefix+'"' 
	parms += ' -shareDataSourceWithCMP '+cfShareDSCmp+' -shareDurableSubscriptions "'+cfShareDurSub+'"'
	
	# Case for Queue/Topic JMS Connection Factory 
	if ( not(cfType == "") ):
		parms += ' -type '+cfType

	if ( not(cfAuthAlias == "") ):
		parms += ' -authDataAlias "'+cfAuthAlias+'"'
	log.info( parms)
	try:
		cf = AdminTask.createSIBJMSConnectionFactory(scopeId, parms)
	except:
		log.exception("(createJMSConnectionFactory): ERROR creating JMS Connection Factory")

	createCFConnectionPool(cf, "JMS", propertiesPath)

	log.info("(createJMSConnectionFactory): SIB JMS Connection Factory successfully created.")
	


#******************************************************************************
# Procedure:  	deleteJMSConnectionFactory
# Description:	Delete SIB JMS Connection Factory on the specified SIBus
#****************************************************************************** 
def deleteJMSConnectionFactory ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName = 		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")

	cfName =		propReader.get("CF_NAME")
	cfJndiName = 		propReader.get("CF_JNDI_NAME")
	busName = 		getBusName()
	global AdminTask

	log.info('(deleteJMSConnectionFactory) Delete '+cfName+' on the '+busName+' SIBus')

	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(deleteJMSConnectionFactory): Unable to find "+scopeName )
		return 1

	# Check if the connection factory already exists
	try:
		objType = "J2CResourceAdapter:SIB JMS Resource Adapter/J2CConnectionFactory"
		cfId = getConfigItemId(scope, scopeName, nodeName, objType, cfName)
		if (cfId != ""):
			jmsCF = AdminConfig.remove(cfId)
			log.info("(deleteJMSConnectionFactory): SIB JMSConnectionFactory  successfully deleted.")
			return 0
	except:
		log.exception("(deleteJMSConnectionFactory): Error creating JMS Connection Factory")


#******************************************************************************
# Procedure:	createJMSProvider
# Description:	Create a Generic JMS Provider
# 
#****************************************************************************** 
def createJMSProvider ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")   
	nodeName = 		propReader.get("NODE_NAME")
	jmsName = 		propReader.get("JMS_NAME")
	icf =			propReader.get("INITIAL_CF")
	url = 			propReader.get("URL_EXT")
	jmsDesc =		propReader.get("DESC")
	jmsClass =		propReader.get("CLASSPATH")
	jmsNative = 		propReader.get("NATIVE_LIB_PATH")
		
	#------------------------------------------------------------------------------
	# Create Generic JMS Provider
	#------------------------------------------------------------------------------
	global AdminConfig

	log.info('====== Create Generic JMS Provider '+jmsName+', if it does not exist ======')

	scopeEntry = findScopeEntry(scope, scopeName)
	if (scopeEntry == 0):
		log.info("Unable to find "+scopeName )
		return

	objType = "JMSProvider"
	jmsId = getConfigItemId(scope, scopeName, nodeName, objType, jmsName)
	if (jmsId != ""):
		log.info(""+jmsName+" already exists on "+scope+" "+scopeName)
		return

	name = ['name', jmsName]
	desc = ['description', jmsDesc]
	extICF = ['externalInitialContextFactory', icf]
	extPURL = ['externalProviderURL', url]
	jClass   = ['classpath', jmsClass]
	native = ['nativepath', jmsNative]
	attrs = [name, desc, extICF, extPURL, jClass, native]

	try:
		jmsProvider = AdminConfig.create('JMSProvider', scopeEntry, attrs)
	except:
		log.exception("Error creating JMS Provider")

	log.info("Create Generic JMS Provider was successful.")
	

#******************************************************************************
# Procedure:  	createJMSQueue
# Description:	Create a new SIB JMS Queue for the default messaging provider
#****************************************************************************** 
def createJMSQueue ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName =	propReader.get("SCOPE_NAME")

	qName =		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")
	cQueue =	propReader.get("CONNECT_QUEUE_NAME")
	desc = 		propReader.get("DESC")
	delMode = 	propReader.get("DELIVERY_MODE")
	time_to_liv =	propReader.get("TIME_TO_LIVE")
	priority = 	propReader.get("PRIORITY")
	readAhead =	propReader.get("READ_AHEAD")
	busName =	propReader.get("BUSNAME")

	if isLocalBus(busName):
		busName = getBusName()

	#------------------------------------------------------------------------------
	# Create a SIB JMS Queue on the given SIBus
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(createJMSQueue): Create '+qName+' for default messaging provider on '+scopeName)
	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(createJMSQueue): Unable to find scope "+scopeName )
		return 1

	
	
	parms  = '-name "'+qName+'" -jndiName '+jndiName+' -queueName "'+cQueue+'" -description "'+desc+'"' 
	parms += ' -deliveryMode '+delMode
	
	if ( not(time_to_liv == "") ):
		parms += ' -timeToLive '+time_to_liv

	if ( not(priority == "") ):
		parms += ' -priority '+priority
	
	parms += ' -readAhead '+readAhead+' -busName "'+busName+'"'
	
	# Check for existence of SIB JMS Queue
	qList = AdminTask.listSIBJMSQueues(scopeId).splitlines()
	for queue in qList:
		if (queue.find(qName) >= 0):
			log.info("(createJMSQueue): "+qName+" on "+scopeName+" already exists. Modifying JMS Queue destination....")
			j2cadminobj = findConfigTarget(qName,"J2CAdminObject")
			
			try:
				ret = AdminTask.modifySIBJMSQueue(j2cadminobj, parms)
				log.info("(createJMSQueue): SIB JMS Queue successfully modified.")
				return 0
			except:
				log.exception("(createJMSQueue): Error modifying SIB JMS Queue")
	
	try:
		newQueue = AdminTask.createSIBJMSQueue(scopeId, parms)
	except:
		log.exception("(createJMSQueue): Error creating SIB JMS Queue")

	log.info("(createJMSQueue): SIB JMS Queue successfully created.")
	

#******************************************************************************
# Procedure:  	deleteJMSQueue
# Description:	delete SIB JMS Queue for the default messaging provider
#****************************************************************************** 
def deleteJMSQueue ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName =	propReader.get("SCOPE_NAME")

	qName =		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")
	cQueue =	propReader.get("CONNECT_QUEUE_NAME")
	desc = 		propReader.get("DESC")
	delMode = 	propReader.get("DELIVERY_MODE")
	time_to_liv =	propReader.get("TIME_TO_LIVE")
	priority = 	propReader.get("PRIORITY")
	readAhead =	propReader.get("READ_AHEAD")
	busName =	propReader.get("BUSNAME")

	if isLocalBus(busName):
		busName = getBusName()

	global AdminTask

	log.info('(deleteJMSQueue): Delete '+qName+' for default messaging provider on '+scopeName)
	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(deleteJMSQueue): Unable to find scope "+scopeName )
		return 1

	# Check for existence of SIB JMS Queue
	qList = AdminTask.listSIBJMSQueues(scopeId).splitlines()
	for queue in qList:
		if (queue.find(qName) >= 0):
			j2cadminobj = findConfigTarget(qName,"J2CAdminObject")
			
			try:
				ret = AdminTask.deleteSIBJMSQueue(j2cadminobj)
				log.info("(deleteJMSQueue): SIB JMS Queue successfully deleted.")
				return 0
			except:
				log.exception("(deleteJMSQueue): Error deleting SIB JMS Queue")


#******************************************************************************
# Proceduree: 	createJMSTopic
# Description:	Create a new SIB JMS Topic for the default messaging provider
#****************************************************************************** 
def createJMSTopic ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName = 	propReader.get("SCOPE_NAME")

	name =		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")

	tDesc =		propReader.get("DESC")
	tName =		propReader.get("TOPIC_NAME")
	tSpace = 	propReader.get("TOPIC_SPACE")
	delMode = 	propReader.get("DELIVERY_MODE")
	time =		propReader.get("TIME_TO_LIVE")
	priority =	propReader.get("PRIORITY")
	readAhead =	propReader.get("READ_AHEAD")
	busName =	getBusName()

	#------------------------------------------------------------------------------
	# Create a SIB JMS TOPIC 
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(createJMSTopic): Create '+name+' for default messaging provider on '+scopeName)
	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(createJMSTopic): Unable to find scope "+scopeName )
		return 1
	#endIF

	parms  = '-name "'+name+'" -jndiName '+jndiName+' -description "'+tDesc+'"'
	parms += ' -topicName "'+tName+'" -topicSpace '+tSpace+' -deliveryMode '+delMode
	parms += ' -readAhead '+readAhead 
	parms += ' -busName "'+busName+'"'
	
	if ( not(time == "") ):
		parms += ' -timeToLive '+time

	if ( not(priority == "") ):
		parms += ' -priority '+priority

	# Check for existence of SIB JMS Topic
	tList = AdminTask.listSIBJMSTopics(scopeId).splitlines()
	for topic in tList:
		if (topic.find(name) >= 0):
			log.info("(createJMSTopic): "+name+" on "+scopeName+" already exists. Modifying JMS Topic destination...")
			j2cadminobj = findConfigTarget(tName,"J2CAdminObject")
			try:
				ret = AdminTask.modifySIBJMSTopic(j2cadminobj, parms)
				log.info("(createJMSTopic): SIB JMS Topic successfully modified.")
				return 0
				
			except:
				log.exception("(createJMSTopic): Error removing SIB JMS Topic")
	
		
	try:
		newTopic = AdminTask.createSIBJMSTopic(scopeId, parms)
	except:
		log.exception("(createJMSTopic): Error creating SIB JMS Topic")

	log.info("(createJMSTopic): SIB JMS Topic successfully created.")
	return 0
	


#******************************************************************************
# Proceduree: 	deleteJMSTopic
# Description:	Create a new SIB JMS Topic for the default messaging provider
#****************************************************************************** 
def deleteJMSTopic ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName = 	propReader.get("SCOPE_NAME")

	name =		propReader.get("NAME")
	jndiName = 	propReader.get("JNDI_NAME")

	tDesc =		propReader.get("DESC")
	tName =		propReader.get("TOPIC_NAME")
	tSpace = 	propReader.get("TOPIC_SPACE")
	delMode = 	propReader.get("DELIVERY_MODE")
	time =		propReader.get("TIME_TO_LIVE")
	priority =	propReader.get("PRIORITY")
	readAhead =	propReader.get("READ_AHEAD")
	busName =	getBusName()

	#------------------------------------------------------------------------------
	# Create a SIB JMS TOPIC 
	#------------------------------------------------------------------------------
	global AdminTask

	log.info('(deleteJMSTopic): Delete '+name+' for default messaging provider on '+scopeName)
	scopeId = findScopeEntry(scope, scopeName)
	if (scopeId == 0):
		log.error("(deleteJMSTopic): Unable to find scope "+scopeName )
		return 1
	#endIF
	
	# Check for existence of SIB JMS Topic
	tList = AdminTask.listSIBJMSTopics(scopeId).splitlines()
	for topic in tList:
		if (topic.find(name) >= 0):
			j2cadminobj = findConfigTarget(tName,"J2CAdminObject")
			try:
				ret = AdminTask.deleteSIBJMSTopic(j2cadminobj)
				log.info("(deleteJMSTopic): SIB JMS Topic successfully deleted.")
				return 0
				
			except:
				log.exception("(deleteJMSTopic): Error deleting SIB JMS Topic")


#******************************************************************************
# Procedure:  	createMailSession
# Description:	Create a Mail Session of the Built-in-Mail Provider
#****************************************************************************** 
def createMailSession ( propertiesPath):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	name =			propReader.get("NAME")
	jndiName =		propReader.get("JNDI_NAME")
	desc =			propReader.get("DESC")
	category =		propReader.get("CATEGORY")
	mailTransHost =		propReader.get("MAIL_TRANS_HOST")
	mailTransProto =	propReader.get("MAIL_TRANS_PROTOCOL")
	mailTransUserId =	propReader.get("MAIL_TRANS_USERID")
	mailTransPasswd = 	propReader.get("MAIL_TRANS_PASSWD")
	enableParse =		propReader.get("ENABLE_INET_PARSING")
	mailFrom =		propReader.get("MAIL_FROM")
	mailStoreHost = 	propReader.get("MAIL_STORE_HOST")
	mailStoreProto =	propReader.get("MAIL_STORE_PROTOCOL")
	mailStoreUserId =	propReader.get("MAIL_STORE_USERID")
	mailStorePasswd =	propReader.get("MAIL_STORE_PASSWD")
	enableDebug =		propReader.get("ENABLE_DEBUG_MODE")

	mailSession = findConfigTargetWithScope("cell", cellName, name, "MailSession")
	if (mailSession != 0):
		log.info(""+name+" already exists")
		return 

	mailProvider = getConfigItemId("cell", cellName, "", "MailProvider", "Built-in Mail Provider")
	protoProvider = AdminConfig.list("ProtocolProvider", mailProvider).splitlines()

	# Get the protocol provider objects
	for proto1 in protoProvider:
		if proto1.find(mailTransProto) >= 0:
			newmtp = proto1
			break
	#endFor

	for proto2 in protoProvider:
		if proto2.find(mailStoreProto) >= 0:
			newmsp = proto2
			break
	#endFor

	attrs = []
	attrs.append(["name", name])
	attrs.append(["jndiName", jndiName])
	attrs.append(["description", desc])
	attrs.append(["category", category])
	attrs.append(["mailTransportHost", mailTransHost])
	attrs.append(["mailTransportProtocol", newmtp])
	attrs.append(["mailTransportUser", mailTransUserId])
	attrs.append(["mailTransportPassword", mailTransPasswd])
	attrs.append(["strict", enableParse])
	attrs.append(["mailFrom", mailFrom])
	attrs.append(["mailStoreHost", mailStoreHost])
	attrs.append(["mailStoreProtocol", newmsp])
	attrs.append(["mailStoreUser", mailStoreUserId])
	attrs.append(["mailStorePassword", mailStorePasswd])
	attrs.append(["debug", enableDebug])
	
	try:
		mSession = AdminConfig.create("MailSession", mailProvider, attrs)
	except:
		log.exception("Caught Exception creating mail session")

	log.info("Creation of mail session "+name+" was successful.")
	

#******************************************************************************
# Procedure:  	createMQConnectionFactory
# Description:	Create a MQ connection factory for creating JMS connections
#		to both queue and topic destinations
#****************************************************************************** 
def createMQConnectionFactory ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	cfScope =		propReader.get("CF_SCOPE")
	cfScopeName =		propReader.get("CF_SCOPE_NAME")
	cfNodeName =		propReader.get("CF_NODE_NAME")
	cfName =		propReader.get("CF_NAME")
	cfJndiName =		propReader.get("CF_JNDI_NAME")
	cfDesc =		propReader.get("CF_DESC")
	cfCategory =		propReader.get("CF_CATEGORY")
	cfCMPAuthAlias =	propReader.get("CF_CMP_AUTH_ALIAS")
	cfConAuthAlias =	propReader.get("CF_CON_AUTH_ALIAS")
	cfMapAuthAlias =	propReader.get("CF_MAP_AUTH_ALIAS")
	cfQueueMgr =		propReader.get("CF_QUEUE_MANAGER")
 	cfHost =		propReader.get("CF_HOST")
 	cfPort =		propReader.get("CF_PORT")
 	cfChannel = 		propReader.get("CF_CHANNEL")
 	cfTransType =		propReader.get("CF_TRANS_TYPE")
 	cfModelQueueDef =	propReader.get("CF_MODEL_QUEUE_DEF")
 	cfClientId =		propReader.get("CF_CLIENT_ID")
 	cfCCSID =		propReader.get("CF_CCSID")
 	cfEnableMsgRet =	propReader.get("CF_ENABLE_MSG_RET")
 	cfXAEnabled =		propReader.get("CF_XA_ENABLED")
 	cfEnableRetShut =	propReader.get("CF_ENABLE_RET_SHUTDOWN")
 	cfLocalServerAddr =	propReader.get("CF_LOCAL_SERVER_ADDR")
 	cfPollingInt =		propReader.get("CF_POLLING_INT")
 	cfRescanInt =		propReader.get("CF_RESCAN_INT")
 	cfSSLCipher = 		propReader.get("CF_SSL_CIPHER")
 	cfSSLCRL=		propReader.get("CF_SSL_CRL")
 	cfSSLPeerName =		propReader.get("CF_SSL_PEER_NAME")
 	cfTempQueuePre =	propReader.get("CF_TEMP_QUEUE_PREFIX")
 	cfEnableMQCP = 		propReader.get("CF_ENABLE_MQ_CONN_POOL")

	cfBrokerCQ =		propReader.get("CF_BROKER_CONTROL_QUEUE")
	cfBrokerQMgr = 		propReader.get("CF_BROKER_QUEUE_MGR")
 	cfBrokerPubQ =		propReader.get("CF_BROKER_PUB_QUEUE")
 	cfBrokerSubQ =		propReader.get("CF_BROKER_SUB_QUEUE")
 	cfBrokerCCSubQ =	propReader.get("CF_BROKER_CC_SUB_QUEUE")
 	cfBrokerVer =		propReader.get("CF_BROKER_VERSION")
 	cfPubSubCleanLvl =	propReader.get("CF_PUBSUB_CLEANUP_LEVEL")
 	cfPubSubCleanInt =	propReader.get("CF_PUBSUB_CLEANUP_INT")
 	cfBrokerMsgSel =	propReader.get("CF_BROKER_MSG_SEL")
 	cfPubAckInt =		propReader.get("CF_PUB_ACK_INT")
 	cfEnableSpBrokerSubs =	propReader.get("CF_ENABLE_SPARSE_BROKER_SUBS")
 	cfPubSubStatInt =	propReader.get("CF_PUBSUB_STAT_INT")
 	cfPersistSubStore =	propReader.get("CF_PERSIST_SUB_STORE")
 	cfEnableMultiTrans =	propReader.get("CF_ENABLE_MULTI_TRANS")
 	cfEnableCloneSup =	propReader.get("CF_ENABLE_CLONE_SUPPORT")
 	cfDirBrokerAuthType =	propReader.get("CF_DIRECT_BROKER_AUTH_TYPE")
 	#cfProxyHostName =	propReader.get("CF_PROXY_HOST_NAME")
 	#cfProxyPort = 		propReader.get("CF_PROXY_PORT")

	MQJMSProvider = getConfigItemId(cfScope, cfScopeName, cfNodeName, "JMSProvider", "WebSphere MQ JMS Provider")
	MQCF = getConfigItemId(cfScope, cfScopeName, cfNodeName, "JMSProvider:WebSphere MQ JMS Provider/MQConnectionFactory", cfName)
	
	
	attrs = []
	attrs.append(["name", cfName])
	attrs.append(["jndiName", cfJndiName])
	attrs.append(["description", cfDesc])
	attrs.append(["category", cfCategory])
	attrs.append(["authDataAlias", cfCMPAuthAlias])
	attrs.append(["authMechanismPreference", cfConAuthAlias])
	attrs.append(["mapping", cfMapAuthAlias])
	attrs.append(["queueManager", cfQueueMgr])
	attrs.append(["host", cfHost])
	attrs.append(["port", cfPort])
	attrs.append(["channel", cfChannel])
	attrs.append(["transportType", cfTransType])
	attrs.append(["tempModel", cfModelQueueDef])
	attrs.append(["clientID", cfClientId])
	attrs.append(["CCSID", cfCCSID])
	attrs.append(["msgRetention", cfEnableMsgRet])
	attrs.append(["XAEnabled", cfXAEnabled])
	attrs.append(["failIfQuiesce", cfEnableRetShut])
	attrs.append(["localAddress", cfLocalServerAddr])
	attrs.append(["pollingInterval", cfPollingInt])
	attrs.append(["rescanInterval", cfRescanInt])
	attrs.append(["sslCipherSuite", cfSSLCipher])
	attrs.append(["sslCRL", cfSSLCRL])
	attrs.append(["sslPeerName", cfSSLPeerName])
	attrs.append(["tempQueuePrefix", cfTempQueuePre])
	attrs.append(["useConnectionPooling", cfEnableMQCP])

	attrs.append(["brokerControlQueue", cfBrokerCQ])
	attrs.append(["brokerQueueManager", cfBrokerQMgr])
	attrs.append(["brokerPubQueue", cfBrokerPubQ])
	attrs.append(["brokerSubQueue", cfBrokerSubQ])
	attrs.append(["brokerCCSubQ", cfBrokerCCSubQ])
	attrs.append(["brokerVersion", cfBrokerVer])
	attrs.append(["pubSubCleanup", cfPubSubCleanLvl])
	attrs.append(["pubSubCleanupInterval", cfPubSubCleanInt])
	attrs.append(["msgSelection", cfBrokerMsgSel])
	attrs.append(["publishAckInterval", cfPubAckInt])
	attrs.append(["sparseSubscriptions", cfEnableSpBrokerSubs])
	attrs.append(["statRefreshInterval", cfPubSubStatInt])
	attrs.append(["substore", cfPersistSubStore])
	attrs.append(["multicast", cfEnableMultiTrans])
	attrs.append(["cloneSupport", cfEnableCloneSup])
	attrs.append(["directAuth", cfDirBrokerAuthType])
	#attrs.append(["proxyHostName", cfProxyHostName])
	#attrs.append(["proxyPort", cfProxyPort])

	if (len(MQCF) == 0):
		# Create MQ Connection Factory
		try:	
			log.info("(createMQConnectionFactory): Creating MQCF "+cfName)
			MQCF = AdminConfig.create("MQConnectionFactory", MQJMSProvider , attrs)	
		except:
			log.exception("(createMQConnectionFactory): Caught Exception creating MQ Connection Factory")
	else:
		# Modify MQ Connection Factory
		try:
			log.info("(createMQConnectionFactory): Modifying MQCF "+cfName)
			cf = AdminConfig.modify(MQCF, attrs)
		except:
			log.exception("(createMQConnectionFactory):Caught Exception modifying MQ Connection Factory")

	createCFConnectionPool(MQCF, "MQ", propertiesPath)
	createWebSphereMQCFCustomProperty(MQCF)
	log.info("(createMQConnectionFactory): MQ Connection Factory was successful created/modified")
	return 0
	

#******************************************************************************
# Procedure:  	deleteMQConnectionFactory
# Description:	Delete MQ connection factory
#****************************************************************************** 
def deleteMQConnectionFactory ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	cfScope =		propReader.get("CF_SCOPE")
	cfScopeName =		propReader.get("CF_SCOPE_NAME")
	cfNodeName =		propReader.get("CF_NODE_NAME")
	cfName =		propReader.get("CF_NAME")

	MQJMSProvider = getConfigItemId(cfScope, cfScopeName, cfNodeName, "JMSProvider", "WebSphere MQ JMS Provider")
	MQCF = getConfigItemId(cfScope, cfScopeName, cfNodeName, "JMSProvider:WebSphere MQ JMS Provider/MQConnectionFactory", cfName)
	

	if (len(MQCF) == 0):
		return 0
	else:
		# Modify MQ Connection Factory
		try:
			cf = AdminConfig.remove(MQCF)
		except:
			log.exception("(deleteMQConnectionFactory):Caught Exception modifying MQ Connection Factory")
	
	log.info("(deleteMQConnectionFactory): Deleation of MQ Connection Factory was successful.")
	return 0
	



#******************************************************************************
# Procedure:   createWebSphereMQCFCustomProperty
# Description:	
#******************************************************************************
def createWebSphereMQCFCustomProperty (MQCF):
	
	try:
		propSet = AdminConfig.create("J2EEResourcePropertySet", MQCF, [])
	except:
		log.exception("(createWebSphereMQCFCustomProperty): Caught Exception creating property set")
	
	attrs1 = []
	attrs1.append(["name", "SENDEXIT"])
	attrs1.append(["value", "com.ibm.ws.sca.internal.mq.exit.MQInternalSendExitImpl"])
	attrs1.append(["type", "java.lang.String"])
	attrs2 = []
	attrs2.append(["name", "RECEXIT"])
	attrs2.append(["value", "com.ibm.ws.sca.internal.mq.exit.MQInternalReceiveExitImpl"])
	attrs2.append(["type", "java.lang.String"])
	attrs3 = []
	attrs3.append(["name", "SENDEXITINIT"])
	attrs3.append(["value", "MQ Bindings Send Exit"])
	attrs3.append(["type", "java.lang.String"])
	attrs4 = []
	attrs4.append(["name", "RECEXITINIT"])
	attrs4.append(["value", "MQ Bindings Receive Exit"])
	attrs4.append(["type", "java.lang.String"])	
	
	try:
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs1)
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs2)
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs3)
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs4)
	except:
		log.exception("(createWebSphereMQCFCustomProperty): Caught Exception creating WebSphere MQ_CF CustomP Property")

#******************************************************************************
# Procedure:  	createMQDestination
# Description:	Create a MQ destination
#****************************************************************************** 
def createMQDestination ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	
	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")
	
	name =			propReader.get("NAME")
	jndiName =		propReader.get("JNDI_NAME")
	desc =			propReader.get("DESC")
	category =		propReader.get("CATEGORY")
	baseQueueName  =	propReader.get("BASE_QUEUE_NAME")
	baseQueueManager =  	propReader.get("BASE_QUEUE_MANAGER")
	ccsid =			propReader.get("CCSID")
	targetClient = 		propReader.get("TARGET_CLIENT")
	
	attrs = []
	attrs.append(["name", name])
	attrs.append(["jndiName", jndiName])
	attrs.append(["description", desc])
	attrs.append(["baseQueueName", baseQueueName])
	attrs.append(["targetClient", targetClient])
		
	MQJMSProvider = getConfigItemId(scope, scopeName, nodeName, "JMSProvider", "WebSphere MQ JMS Provider")
	MQDES = getConfigItemId(scope, scopeName, nodeName, "JMSProvider:WebSphere MQ JMS Provider/MQQueue", name)
	
	if (len(MQDES) == 0):
		try:
			log.info("(createMQDestination): Creating MQ destination "+name)
			MQQUEUE = AdminConfig.create("MQQueue", MQJMSProvider , attrs)
			
		except:
			log.exception("(createMQDestination): Caught Exception creating WebSphere MQ Destination")
	else:
		# Modify MQ Destination
		try:
			log.info("(createMQDestination): Modifying MQ destination "+name)
			cf = AdminConfig.modify(MQDES, attrs)
		except:
			log.exception("(createMQDestination): Caught Exception modifying MQ Queue Destination")
		
	log.info("(createMQDestination): MQ Destination was successful created/modified.")
	return 0
	
#******************************************************************************
# Procedure:  	deleteMQDestination
# Description:	Delete a MQ destination
#****************************************************************************** 
def deleteMQDestination ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)
	
	scope =			propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")
	
	name =			propReader.get("NAME")
		
	MQJMSProvider = getConfigItemId(scope, scopeName, nodeName, "JMSProvider", "WebSphere MQ JMS Provider")
	MQDES = getConfigItemId(scope, scopeName, nodeName, "JMSProvider:WebSphere MQ JMS Provider/MQQueue", name)
	
	if (len(MQDES) == 0):
		return 0	
	else:
		try:
			cf = AdminConfig.remove(MQDES)
		except:
			log.exception("(deleteMQDestination): Caught Exception modifying MQ Queue Destination")
		
	log.info("(deleteMQDestination): Delation of  MQ Destination was successful.")
	return 0
	


#******************************************************************************
# Procedure:  	createScheduler
# Description:	Create a Scheduler
#****************************************************************************** 
def createScheduler ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName = 		propReader.get("NODE_NAME")

	sName =		propReader.get("NAME")
	jndiName =		propReader.get("JNDI_NAME")
	dsJndiName =	propReader.get("DS_JNDI_NAME")
	tablePrefix =	propReader.get("TABLE_PREFIX")
	pInterval =		propReader.get("POLL_INTERVAL")
	workMgr =		propReader.get("WORK_MGR")

	desc =		propReader.get("DESC")
	category =		propReader.get("CATEGORY")
	dsAlias =		propReader.get("DS_ALIAS")
	useAdminRoles=	propReader.get("USE_ADMIN_ROLES")

	#---------------------------------------------------------------------------------
	# Create a Scheduler
	#---------------------------------------------------------------------------------

	global AdminConfig

	log.info('====== Create scheduler '+sName+', if it does not exist ======')

	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.info(""+scopeName+" does not exist.")
		return 

	schedConf = findConfigTargetWithScope(scope, scopeName, sName, "SchedulerConfiguration")
	if (schedConf != 0):
		log.info(""+sName+" already exists on "+scopeName)
		return 

	# Get the scheduler provider
	schedProv = getConfigId(scope, scopeName, nodeName, "SchedulerProvider")
	if (schedProv == ""):
		log.info("The scheduler provider could not be found.")
		return

	# Check if the work manager for the scheduler exists
	workManager = findConfigTargetWithScope(scope, scopeName, workMgr, "WorkManagerInfo")
	if (workManager == 0):
		log.info("Work manager "+workMgr+" could not be found.")
		return

	# Create the scheduler
	attrs = []
	attrs.append(['name', sName])
	attrs.append(['jndiName', jndiName])
	attrs.append(['datasourceJNDIName', dsJndiName])
	attrs.append(['tablePrefix', tablePrefix])
	attrs.append(['pollInterval', pInterval])
	attrs.append(['workManagerInfoJNDIName', workMgr])
	attrs.append(['description', desc])
	attrs.append(['category', category])
	attrs.append(['datasourceAlias', dsAlias])
	attrs.append(['useAdminRoles', useAdminRoles])

	try:
		newScheduler = AdminConfig.create("SchedulerConfiguration", schedProv, attrs)
	except:
		log.exception("Caught Exception creating scheduler")

	log.info("Creation of scheduler "+sName+" on "+scopeName+" was successful.")
	

#******************************************************************************
# Procedure:   	createURL
# Description:	Create URL if it doesn't exist, otherwise modify it.
#****************************************************************************** 
def createURL ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName=		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")

	urlName =		propReader.get("URL_NAME")
	providerName =		propReader.get("URL_PROVIDER")
	urlJNDIName = 		propReader.get("URL_JNDI_NAME")
	urlSpec = 		propReader.get("URL_SPECIFICATION")
	urlDesc =		propReader.get("URL_DESC")
	urlCategory =		propReader.get("URL_CATEGORY")

	#---------------------------------------------------------------------------------
	# Create URL
	#---------------------------------------------------------------------------------

	global AdminConfig

	log.info('====== Create URL '+urlName+', if it does not exist ======')

	
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.info(""+scopeName+" does not exist.")
		return 

	# Make sure URL Provider exists
	urlId = getConfigItemId(scope, scopeName, nodeName, "URLProvider", providerName)
	if (urlId == ""):
		log.info(""+providerName+" does not exist on "+scope+" "+scopeName)
		return

	nameAttr = ["name", urlName]
	jndiAttr = ["jndiName", urlJNDIName]
	specAttr = ["spec", urlSpec]
	descAttr = ["description", urlDesc]
	catAttr  = ["category", urlCategory]

	# Make sure URL doesn't already exist, but if so, will modify 
	theURL = findConfigTargetWithScope(scope, scopeName, urlName, "URL")

	if (theURL == 0):
		attrs = [nameAttr, jndiAttr, specAttr, descAttr, catAttr]
		log.info("Creating new URL")

		try:
			url = AdminConfig.create("URL", urlId, attrs)
		except:
			log.exception("Caught Exception creating URL")
	else:
		
		attrs = [jndiAttr, specAttr, descAttr, catAttr]
		log.info("Modifying existing URL")

		try:
			url = AdminConfig.modify(theURL, attrs )
		except:
			log.exception("Caught Exception modifying URL")
	
	log.info("Created/Modified "+urlName+" successfully.")
	

#******************************************************************************
# Procedure:   	createURLCustomProperty
# Description:	Create URL Custom Property if it doesn't exist, otherwise modify it.
#****************************************************************************** 
def createURLCustomProperty ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =			propReader.get("SCOPE")
	scopeName=		propReader.get("SCOPE_NAME")
	nodeName =		propReader.get("NODE_NAME")

	providerName =	propReader.get("URL_PROVIDER")
	propertyName = 	propReader.get("PROP_NAME")
	propertyValue= 	propReader.get("PROP_VALUE")
	propertyReq = 	propReader.get("PROP_REQUIRED")
	propertyType =	propReader.get("PROP_TYPE")
	propertyDesc =	propReader.get("PROP_DESC")

	#---------------------------------------------------------------------------------
	# Create URL Custom Property
	#---------------------------------------------------------------------------------

	global AdminConfig

	log.info('====== Create URL '+propertyName+', if it does not exist ======')

	
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.info(""+scopeName+" does not exist.")
		return 

	# Make sure URL Provider exists
	urlId = getConfigItemId(scope, scopeName, nodeName, "URLProvider", providerName)
	if (urlId == ""):
		log.info(""+providerName+" does not exist on "+scope+" "+scopeName)
		return

	# Create a property set if it doesn't exist
	propSet = getConfigItemId(scope, scopeName, nodeName, "J2EEResourcePropertySet", providerName)
	if (propSet == ""):
		try:
			propSet = AdminConfig.create("J2EEResourcePropertySet", urlId, [])
		except:
			log.exception("Caught Exception creating property set")

	# set up attributes
	nameAttr =  ["name", propertyName]
	valueAttr = ["value", propertyValue]
	reqAttr =   ["required", propertyReq]
	typeAttr =  ["type", propertyType]
	descAttr =  ["description", propertyDesc]

	# If custom property already exists, it will just overwrite it  
	attrs = [nameAttr, valueAttr, reqAttr, typeAttr, descAttr]
	try:
		result = AdminConfig.create("J2EEResourceProperty", propSet, attrs )
	except:
		log.exception("Caught Exception creating Custom URL Property")
	else:
		log.info("Added URL Custom Property "+propertyName) 

	log.info("Created/Modified "+propertyName+" successfully.")
	

#******************************************************************************
# Procedure:   	createURLProvider
# Description:	Create URL provider
#****************************************************************************** 
def createURLProvider ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =		propReader.get("SCOPE")
	scopeName =		propReader.get("SCOPE_NAME")
	nodeName = 		propReader.get("NODE_NAME")

	providerName =	propReader.get("URL_NAME")
	providerDesc =	propReader.get("URL_DESC")
	urlClassPath =	propReader.get("URL_CLASSPATH")
	urlStreamHand =	propReader.get("URL_STREAM_HANDLER")
	urlProtocol =	propReader.get("URL_PROTOCOL")
	#----------------------------------------------------------------------------
	# Create URL Provider
	#----------------------------------------------------------------------------

	global AdminApp, AdminConfig

	log.info('====== Create URL Provider '+providerName+', if it does not exist ======')
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.info(""+scopeName+" does not exist.")
		return 

	objType = "URLProvider"
	urlId = getConfigItemId(scope, scopeName, nodeName, objType, providerName)
	if (urlId != ""):
		log.info(""+providerName+" already exists on "+scope+" "+scopeName)
		return

	else:
		attrs1 = []
		attrs1.append(['name', providerName])
		attrs1.append(['streamHandlerClassName', urlStreamHand])
		attrs1.append(['protocol', urlProtocol])
		attrs1.append(['classpath', urlClassPath])
		attrs1.append(['description', providerDesc])

		try:
			provider1 = AdminConfig.create("URLProvider", scopeEntry, attrs1 )
		except:
			log.exception("Caught Exception creating URL Provider")

	log.info("Created "+providerName+" successfully.")
	

#******************************************************************************
# Proceduree: 	createWorkManager
# Description:	Create a WorkManager
#****************************************************************************** 
def createWorkManager ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope =				propReader.get("SCOPE")
	scopeName =			propReader.get("SCOPE_NAME")
	nodeName = 			propReader.get("NODE_NAME")

	wmName =			propReader.get("NAME")
	wmJNDIName =			propReader.get("JNDI_NAME")
	wmMaxThreads =			propReader.get("MAX_THREADS")
	wmMinThreads =			propReader.get("MIN_THREADS")  
	wmNoAlarmThreads=		propReader.get("ALARM_THREADS")
	wmThreadPriority =		propReader.get("THREAD_PRIORITY")

	wmDescription =			propReader.get("DESCRIPTION")
	wmServiceNames =		propReader.get("SVC_NAMES")
	wmIsGrowable =			propReader.get("IS_GROWABLE")
	wmCategory =			propReader.get("CATEGORY")


	#---------------------------------------------------------------------------------
	# Create a Work Manager
	#---------------------------------------------------------------------------------

	global AdminConfig

	log.info('====== Create work manager '+wmName+', if it does not exist ======')

	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.info(""+scopeName+" does not exist.")
		return 

	# Check if work manager already exists
	wrkMgr = findConfigTargetWithScope(scope, scopeName, wmName, "WorkManagerInfo")
	if (wrkMgr != 0):
		log.info(""+wmName+" already exists on "+scopeName)
		return 

	# Get the work manager provider
	wmProv = getConfigId(scope, scopeName, nodeName, "WorkManagerProvider")
	if (wmProv == ""):
		log.info("The work manager provider could not be found.")
		return

	# Create the work manager 
	attrs = []
	attrs.append(['name', wmName])
	attrs.append(['jndiName', wmJNDIName])
	attrs.append(['isGrowable', wmIsGrowable])
	attrs.append(['maxThreads', wmMaxThreads])
	attrs.append(['minThreads', wmMinThreads])
	attrs.append(['numAlarmThreads', wmNoAlarmThreads])
	attrs.append(['serviceNames', wmServiceNames])
	attrs.append(['threadPriority', wmThreadPriority])
	attrs.append(['description', wmDescription])
	attrs.append(['category', wmCategory])

	try:
		newMgr = AdminConfig.create("WorkManagerInfo", wmProv, attrs)
	except:
		log.exception("Caught Exception creating work manager")

	log.info("Creation of work manager "+wmName+" was successful.")
	


#******************************************************************************
# Procedure:	setDBPoolMaxConnections
# Description:	Set the database connection pool maximum connections
#****************************************************************************** 
def setDBPoolMaxConnections ( scope, scopeName, dataSourceName, value ):

	#------------------------------------------------------------------------------
	# Set database pool maximum connections
	#------------------------------------------------------------------------------

	global AdminApp, AdminConfig

	log.info('====== Set the maximum db pool connections with value '+value+' ======')

	# Too easy to enter the wrong case at command line
	scope = scope.title()

	dataSource = findDataSourceWithScope(scope, scopeName, dataSourceName )
	if (dataSource == 0):
		log.info("DataSource doesn't exist")
		return 

	connPool = AdminConfig.showAttribute(dataSource, "connectionPool" )

	if (connPool == 0):
		try:
			connPool = AdminConfig.create("ConnectionPool", dataSource, [] )
			result = connPool
		except:
			log.exception("Caught Exception creating Connection Pool")

	try:
		maxConn = AdminConfig.modify(connPool, [["maxConnections", value]] )
		result = maxConn
	except:
		log.info("Caught Exception modifying Max Connections")

	log.info("Max database connections has been set to "+value+" successfully")
	
#******************************************************************************
# Procedure:   	createSharedLibrary
# Description:	Create Shared Library
#****************************************************************************** 
def createSharedLibrary ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope 		=	propReader.get("SCOPE")
	scopeName 	=	propReader.get("SCOPE_NAME")
	nodeName 	= 	propReader.get("NODE_NAME")
	server 		=	propReader.get("SERVER")
	
	name 		= 	propReader.get("NAME")
	classPath	= 	propReader.get("CLASS_PATH")
	description	=	propReader.get("DESCRIPTION")

	global AdminApp, AdminConfig
	log.info("(createSharedLibrary): Creating Shared Library " + name)
	
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.error("(createSharedLibrary): Scope name "+scopeName+" does not exist. Check properties file!")
		return 
	attrs = []
	attrs.append(["name", name])
	attrs.append(["classPath", classPath])
	attrs.append(["description", description])
	lib = getConfigItemId(scope, scopeName, "", "Library", name)
			
	if (len(lib) != 0):
		log.info("(createSharedLibrary): Shared Library "+name+" already exists. Removing...")
		
		try:		
			librefList = AdminConfig.getid("/Deployment:"+APPLICATION_NAME+"/ApplicationDeployment:/Classloader:/LibraryRef:/" )
			
			if(librefList == ""):
				pass
			else:
				arrayLibRef = librefList.splitlines()
				for libref in arrayLibRef:
						removeT = AdminConfig.remove(libref)
		except:
			log.exception("(createSharedLibrary): Caught Exception removing Library Ref. for application "+APPLICATION_NAME)
				
		try:
			library = AdminConfig.remove(lib)
		except:
			log.exception("(createSharedLibrary): Caught Exception removing Shared Library "+ name)
	
	try:
		library = AdminConfig.create("Library", scopeEntry, attrs )
		
	except:
		log.exception("(createSharedLibrary): Caught Exception creating Shared Library "+ name +" for application " + APPLICATION_NAME)
	
	try:	
		deployment = AdminConfig.getid("/Deployment:"+APPLICATION_NAME+"/" )
		appDeploy  = AdminConfig.showAttribute(deployment, 'deployedObject')
		classLoad1 = AdminConfig.showAttribute(appDeploy, 'classloader')
		libraryRef = AdminConfig.create('LibraryRef', classLoad1, [['libraryName', name],  ['sharedClassloader', 'true']])
	except:		
		log.exception("(createSharedLibrary): Caught Exception associate shared library with  application "+APPLICATION_NAME)

	log.info("(createSharedLibrary): Created shared libary "+name+" and associate with application "+APPLICATION_NAME+" successfully.")
	return 0
#******************************************************************************
# Procedure:   	deleteSharedLibrary
# Description:	Delete Shared Library
#****************************************************************************** 
def deleteSharedLibrary ( propertiesPath ):

	propReader = PropertiesReader()
	propReader.load(propertiesPath)

	scope 		=	propReader.get("SCOPE")
	scopeName 	=	propReader.get("SCOPE_NAME")
	nodeName 	= 	propReader.get("NODE_NAME")
	server 		=	propReader.get("SERVER")
	
	name 		= 	propReader.get("NAME")
	classPath	= 	propReader.get("CLASS_PATH")
	description	=	propReader.get("DESCRIPTION")

	global AdminApp, AdminConfig
	log.info("(deleteSharedLibrary): Deleting Shared Library " + name)
	
	scopeEntry = findScopeEntry(scope, scopeName )
	if (scopeEntry == 0):
		log.error("(deleteSharedLibrary): Scope name "+scopeName+" does not exist. Check properties file!")
		return 
	attrs = []
	attrs.append(["name", name])
	attrs.append(["classPath", classPath])
	attrs.append(["description", description])
	lib = getConfigItemId(scope, scopeName, "", "Library", name)
			
	if (len(lib) != 0):
		
		try:		
			librefList = AdminConfig.getid("/Deployment:"+APPLICATION_NAME+"/ApplicationDeployment:/Classloader:/LibraryRef:/" )
			arrayLibRef = librefList.splitlines()
			for libref in arrayLibRef:
				removeT = AdminConfig.remove(libref)
		except:
			log.exception("(createSharedLibrary): Caught Exception removing Library Ref. for application "+APPLICATION_NAME)
				
		try:
			library = AdminConfig.remove(lib)
		except:
			log.exception("(createSharedLibrary): Caught Exception removing Shared Library "+ name)
	
	log.info("(deleteSharedLibrary): Deleted shared libary "+name+" and associate with application "+APPLICATION_NAME+" successfully.")
	return 0
