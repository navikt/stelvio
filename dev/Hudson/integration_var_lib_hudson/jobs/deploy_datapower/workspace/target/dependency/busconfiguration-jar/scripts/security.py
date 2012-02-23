###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################


#******************************************************************************
# File Name:	security.py
# Description:	This file contains the following security procedures:
#		
#			configCustomUserRegistry
#			configGlobalSecurity
#			configLDAPUserRegistry
#			configureLTPA
#			configureSSL
#			configureSSO
#			createJ2CAuthData  
#			getLDAPFilters
#			modifyJ2CAuthData
#			setCURCustomProperties
#			setSecurityCustomProperty
#
#
# History:		
#			
#******************************************************************************
#******************************************************************************
# Procedure:  	configCustomUserRegistry
# Description:	Configure Custome User Registry
#****************************************************************************** 
def configCustomUserRegistry ( propertyFileName ):

	readProperties(propertyFileName)

 	serverId =		getProperty("CU_SERVER_ID")
	serverPwd =		getProperty("CU_SERVER_PASSWORD")
	className =		getProperty("CU_CLASSNAME")
	ignoreCase =	getProperty("CU_IGNORE_CASE")

	global AdminConfig
	
	sec = getConfigId("cell", cellName, "", "Security")
	uReg = AdminConfig.list("CustomUserRegistry", sec)
	if (len(uReg) != 0):
		attrs = []
		attrs.append(["serverId", serverId])
		attrs.append(["serverPassword", serverPwd])
		attrs.append(["customRegistryClassName", className])
		attrs.append(["ignoreCase", ignoreCase])
		try:
			_excp_ = 0
			result = AdminConfig.modify(uReg, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying custom user registry"
			print result
			return
        	#endIf 
	#endIf

	print "Custom User Registry Properties configured successfully."
	
#endDef
	
#******************************************************************************
# Procedure:  	configGlobalSecurity
# Description:	Configure Global Security
#
# History:	
#****************************************************************************** 
def configGlobalSecurity ( propertyFileName ):

	readProperties(propertyFileName)

	enabled = 		getProperty("ENABLE_GLOBAL_SECURITY")
	enforceJava2 =	getProperty("ENFORCE_JAVA2_SECURITY")
	enforceJCA =	getProperty("ENFORCE_JCA_SECURITY")
	domainQualified =	getProperty("USE_DOMAIN_QUALIFIED_USERIDS")
	cacheTimeout =	getProperty("CACHE_TIMEOUT")
	permission =	getProperty("PERMISSION_WARNING")
	activeProtocol =	getProperty("ACTIVE_PROTOCOL")
	enableFIPS =	getProperty("USE_FIPS")
	authMechanism = 	getProperty("ACTIVE_AUTH_MECHANISM")
	userRegistry =	getProperty("ACTIVE_USER_REGISTRY")

	#------------------------------------------------------------------------------
	# Configure global security general properties
	#------------------------------------------------------------------------------

	global AdminConfig

	print '\n====== Configure Global Security General Properties ======'

	security = AdminConfig.list('Security')

	attrs = []
	attrs.append(['enabled', enabled])
	attrs.append(['enforceJava2Security', enforceJava2])
	attrs.append(['enforceFineGrainedJCASecurity', enforceJCA])
	attrs.append(['useDomainQualifiedUserNames', domainQualified])
	attrs.append(['cacheTimeout', cacheTimeout])
	attrs.append(['issuePermissionWarning', permission])
	attrs.append(['activeProtocol', activeProtocol])

	try:
		_excp_ = 0
		result = AdminConfig.modify(security, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception configuring security"
		print result
		return
	#endIf 

	# Set FIPS
	setSecurityCustomProperty(security, "com.ibm.security.useFIPS", enableFIPS, "use FIPS")

	sec = getConfigId("cell", cellName, "", "Security")

	# Set active authentication mechanism
	activeMech = AdminConfig.list('AuthMechanism', sec)
	try:
		_excp_ = 0
		result = AdminConfig.modify(sec, [["activeAuthMechanism", activeMech]])
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception modifying authentication mechanism"
		print result
		return
    	#endIf 


	# Set active user registry
	userReg = AdminConfig.list("UserRegistry", sec).split(lineSeparator)
	for ureq in userReg:
		if ureq.find(userRegistry) >= 0:	
			newUserReg = ureq
			break
		#endIf
	#endFor

	try:
		_excp_ = 0
		result = AdminConfig.modify(sec, [["activeUserRegistry", newUserReg]])
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Caught Exception modifying active user registry"
		print result
		return
    	#endIf 


	print "Global Security General Properties configured successfully."
	
#endDef

#******************************************************************************
# Procedure:  	configLDAPUserRegistry
# Description:	Configure LDAP User Registry
#
# History:	
#****************************************************************************** 
def configLDAPUserRegistry ( propertyFileName ):

	readProperties(propertyFileName)

	serverId =		getProperty("LDAP_SERVER_ID")
	serverPwd =		getProperty("LDAP_SERVER_PASSWORD")
	type =		getProperty("LDAP_TYPE")
	hostName =		getProperty("LDAP_HOSTNAME")
	port =		getProperty("LDAP_PORT")
	baseDn =		getProperty("LDAP_BASEDN")
	bindDn =		getProperty("LDAP_BINDDN")
	bindPwd =		getProperty("LDAP_BIND_PASSWORD")
	searchTimeout=	getProperty("LDAP_SEARCH_TIMEOUT")
	reuseConn =		getProperty("LDAP_REUSE_CONNECTION")
	ignoreCase =	getProperty("LDAP_IGNORE_CASE")
	sslEnabled =	getProperty("LDAP_SSL_ENABLED")
	sslConfig =		getProperty("LDAP_SSL_CONFIG")

	certMapMode =	getProperty("LDAP_CERT_MAP_MODE")
	certFilter =	getProperty("LDAP_CERT_FILTER")

	nestGrpName =	getProperty("LDAP_NAME")
	nestGrpVal =	getProperty("LDAP_VALUE")
	nestGrpDesc =	getProperty("LDAP_DESCRIPTION")

	global AdminConfig
	
	sec = getConfigId("cell", cellName, "", "Security")
	uReg = AdminConfig.list("LDAPUserRegistry", sec)
	if (len(uReg) != 0):
		attrs = []
		attrs.append(["serverId", serverId])
		attrs.append(["serverPassword", serverPwd])
		attrs.append(["type", type])
		attrs.append(["baseDN", baseDn])
		attrs.append(["bindDN", bindDn])
		attrs.append(["bindPassword", bindPwd])
		attrs.append(["searchTimeout", searchTimeout])
		attrs.append(["reuseConnection", reuseConn])
		attrs.append(["ignoreCase", ignoreCase])
		attrs.append(["sslEnabled", sslEnabled])
		attrs.append(["sslConfig", sslConfig])

		try:
			_excp_ = 0
			result = AdminConfig.modify(uReg, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying LDAP user registry"
			print result
			return
        	#endIf 
	#endIf

	# Handle LDAP server host
	endpts = AdminConfig.showAttribute(uReg, "hosts") 
	endptList = wsadminToList(endpts)
	if (endpts != '[]'):
		for endpt in endptList:   
			if (AdminConfig.showAttribute(endpt, "port") == "389"):
				try:
					_excp_ = 0
					result = AdminConfig.modify(endpt, [["host", hostName]])
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
        			#endTry 
				if (_excp_ ):
					print "Caught Exception modifying LDAP server host"
					print result
					return
        			#endIf 
			#endIf
		#endFor
	#endIf

	# Handle Advanced LDAP Settings

	# Get the attributes dependent on LDAP type
	attrs1 = getLDAPFilters(type)
	attrs1.append(["certificateMapMode", certMapMode])
	attrs1.append(["certificateFilter", certFilter])

	sfiltr = AdminConfig.showAttribute(uReg, "searchFilter")	
	if (sfiltr != '[]'):
		try:
			_excp_ = 0
			result = AdminConfig.modify(sfiltr, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying LDAP User Registry Advanced Properties" 
			print result
			return
        	#endIf 

	#endIf

	# Handle Nested Group Search, which is really a custom property
	attrs2 = []
	attrs2.append(["name", nestGrpName]) 
	attrs2.append(["value", nestGrpVal])
	attrs2.append(["description", nestGrpDesc])

	try:
		_excp_ = 0
		result = AdminConfig.create("Property", uReg, attrs2)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
    	#endTry 
	if (_excp_ ):
		print "Caught Exception creating LDAP Custom Property: Recursive Search" 
		print result
		return
    	#endIf 

	print "LDAP User Registry Properties configured successfully."
		
#endDef


#******************************************************************************
# Procedure:  	configureLTPA
# Description:	Configure Lightweight Third Party Authentication configuration
#			settings.  
#******************************************************************************
def configureLTPA ( propertyFileName ):

	readProperties( propertyFileName )

	ltpaPwd =		getProperty("LTPA_PASSWORD")
	ltpaTimeout =	getProperty("LTPA_TIMEOUT")

	sec = getConfigId("cell", cellName, "", "Security")
	ltpa = AdminConfig.list("LTPA", sec)

	# Update the general properties

	passwdAttr = ["password", ltpaPwd]
	timeAttr = ["timeout", ltpaTimeout]
	attrs = [passwdAttr, timeAttr]
	AdminConfig.modify(ltpa, attrs)

	try:
		_excp_ = 0
		result = AdminConfig.modify(ltpa, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
       	#endTry 
	if (_excp_ ):
		print "Caught Exception modifying LTPA properties"
		print result
		return
       	#endIf 

	print "LTPA Properties configured successfully."
	
#endDef

#****************************************************************************** 
# Procedure:  	configureSSL
# Description:	Configure Secure Socket Layer.  A new repertoire is created
#			if it doesn't already exist, otherwise it is modified.
#
# History:	
#****************************************************************************** 
def configureSSL ( propertyFileName ):

	readProperties(propertyFileName)

	myAlias =			getProperty("SSL_ALIAS")
	keyFileName =		getProperty("SSL_KEYFILE_NAME")
	keyFilePwd =		getProperty("SSL_KEYFILE_PASSWD")
	keyFileFormat =		getProperty("SSL_KEYFILE_FORMAT")
	trustFileName =		getProperty("SSL_TRUST_FILE_NAME")
	trustFilePwd =		getProperty("SSL_TRUST_FILE_PASSWD")
	trustFileFormat=		getProperty("SSL_TRUST_FILE_FORMAT")
	clientAuth =		getProperty("SSL_CLIENT_AUTH")
	securityLevel =		getProperty("SSL_SECURITY_LEVEL")
	cryptoToken =		getProperty("SSL_CRYPTO_TOKEN")
	
	providerPropName =	getProperty("SSL_PROVIDER_PROP_NAME")
	providerPropValue =	getProperty("SSL_PROVIDER_PROP_VALUE")
	protocolPropName =	getProperty("SSL_PROTOCOL_PROP_NAME")
	protocolPropValue =	getProperty("SSL_PROTOCOL_PROP_VALUE")
	cipherSuiteName =		getProperty("SSL_CIPHER_SUITES_NAME")
	cipherSuiteValue =	getProperty("SSL_CIPHER_SUITES_VALUE")
	
	#---------------------------------------------------------------------------------
	# Configure SSL
	#---------------------------------------------------------------------------------
	global AdminConfig

	sec = getConfigId("cell", cellName, "", "Security")
	reps = AdminConfig.showAttribute(sec, "repertoire")
	repsList = wsadminToList(reps)
	foundSSL = "false"
	if (reps != '[]'):
		for rep in repsList:
			if (AdminConfig.showAttribute(rep, "alias") == myAlias):
				foundSSL = rep
			#endIf
		#endFor
	#endIf

	# create/modify a repertoire named myAlias
	aliasAttr=["alias", myAlias]
	attr1=["keyFileName", keyFileName]
	attr2=["keyFilePassword", keyFilePwd]
	attr3=["keyFileFormat", keyFileFormat]
	attr4=["trustFileName", trustFileName]
	attr5=["trustFilePassword", trustFilePwd]
	attr6=["trustFileFormat", trustFileFormat]
	attr7=["clientAuthentication", clientAuth]
	attr8=["securityLevel", securityLevel]
	attr9 = ["enableCryptoHardwareSupport", cryptoToken]
	attr10= ["setting", [attr1, attr2, attr3, attr4, attr5, attr6, attr7, attr8, attr9]]
	attrs = [aliasAttr, attr10]

	if (foundSSL == "false"):
		try:
			_excp_ = 0
			foundSSL = AdminConfig.create("SSLConfig", sec, attrs, "repertoire")
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			foundSSL = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating repertoire"
			print foundSSL
			return
        	#endIf 

	else:
		try:
			_excp_ = 0
			result = AdminConfig.modify(foundSSL, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
       		#endTry 
		if (_excp_ ):
			print "Caught Exception modifying repertoire"
			print result
			return
       		#endIf 
	#endIf

	settingId = AdminConfig.showAttribute(foundSSL, "setting")

	# Add Crypto token
	crypto = AdminConfig.showAttribute(settingId, "cryptoHardware")
	if (crypto != '[]'):
		attrs = []
		attrs.append(["tokenType", ""])
		attrs.append(["libraryFile", ""])
		attrs.append(["password", ""])

		try:
			_excp_ = 0
			result = AdminConfig.create("CryptoHardwareToken", settingId, attrs)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception creating crypto hardware token"
			print result
			return
        	#endIf 
	#endIf

	# Handle Provider, Protocol, and Cipher Suites
	setSecurityCustomProperty(settingId, providerPropName, providerPropValue, "Provider")
	setSecurityCustomProperty(settingId, protocolPropName, protocolPropValue, "Protocol")
	setSecurityCustomProperty(settingId, cipherSuiteName, cipherSuiteValue, "Cipher Suite")
 
	print "SSL Properties configured successfully."
	
#endDef


#****************************************************************************** 
# Procedure:  	configureSSO
# Description:	Configure Single Sign On
#******************************************************************************
def configureSSO ( propertyFileName ):

	readProperties(propertyFileName)

	enabled = 		getProperty("SSO_ENABLED")
	requiresSSL =	getProperty("SSO_REQUIRES_SSL")
	domainName =	getProperty("SSO_DOMAIN_NAME")
	interopMode =	getProperty("SSO_INTEROP_MODE")
	webInbound =	getProperty("SSO_WEB_INBOUND")
	
	sec = getConfigId("cell", cellName, "", "Security")
	ltpa = AdminConfig.list("LTPA", sec)

	attrs = []
	attrs.append(["enabled", enabled])
	attrs.append(["requiresSSL", requiresSSL])
	attrs.append(["domainName", domainName])
	
	signon = AdminConfig.showAttribute(ltpa, "singleSignon")
	
	try:
		_excp_ = 0
		result = AdminConfig.modify(signon, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
       	#endTry 
	if (_excp_ ):
		print "Caught Exception modifying single signon"
		print result
		return
       	#endIf 

	security = AdminConfig.list('Security')
	setSecurityCustomProperty(security, "com.ibm.ws.security.ssoInteropModeEnabled", interopMode, "SSO Interop Mode Enabled")
	setSecurityCustomProperty(security, "com.ibm.ws.security.webInboundPropagationEnabled", webInbound, "Web Inbound Security")

	print "SSO Properties configured successfully."
	
#endDef

#******************************************************************************
# Procedure:  	createJ2CAuthData
# Description:	Create JAAS Auth Alias information
#****************************************************************************** 
def createJ2CAuthData ( authAliasName, user, password, desc ):

	#---------------------------------------------------------------------------------
	# Create JAAS Auth Alias
	#---------------------------------------------------------------------------------
	print '\n====== Create JAAS Auth Alias '+authAliasName+', if it does not exist ======'

	rc =  createJAASAuthAlias(cellName, authAliasName, user, password, desc)
	if (rc == 0):
		print "createJAASAuthAlias failed."
		return
	else:

		print "Creation of "+authAliasName+" was successful."
	#endIf
		
#endDef

#******************************************************************************
# Procedure:	getLDAPFilters
# Description:	Determine the filter and map values based on the LDAP type
#
# Parameters:	LDAPType
#
# Returns:	attrs [list]
#
#******************************************************************************
def getLDAPFilters (LDAPType):

	fPath = 	PROPS_HOME
	attrs = []

	# Parse thru LdapConfig.properties file to acquire the appropriate
	# filters and ID maps for the given LDAP type

	type = LDAPType.lower()
	file = open(fPath+"/LdapConfig.properties", 'r')
	lines = file.readlines()
	for line in lines:
		line = line.strip()
		if (len(line)==0 or line[0:1]=="#"): continue
		if line.find(type+".") >= 0:
			index = line.find("=")
			name = line[0:index]
			value = line[index+1:]
			if name.find("user.filter") >= 0:
				attrs.append(["userFilter", value])
			elif name.find("group.filter") >= 0:
				attrs.append(["groupFilter", value])
			elif name.find("user.idmap") >= 0:
				attrs.append(["userIdMap", value])
			elif name.find("group.idmap") >= 0:
				attrs.append(["groupIdMap", value])
			elif name.find("groupmember.idmap") >= 0:
				attrs.append(["groupMemberIdMap", value])
			#endIf
		#endIf
	#endFor
	file.close()
	return attrs
#endDef

#******************************************************************************
# Procedure:  	modifyJ2CAuthData
# Description:	Modify JAAS Auth Alias information
#****************************************************************************** 
def modifyJ2CAuthData ( authAliasName, user, password, desc ):

	#---------------------------------------------------------------------------------
	# Modify JAAS Auth Alias
	#---------------------------------------------------------------------------------
	print '\n====== Modify JAAS Auth Alias '+authAliasName+', if it exists ======'

	attrs = []
	attrs.append(["userId", user])
	attrs.append(["password", password])
	attrs.append(["description", desc])

	# Check if alias exists	
	listJAAS = AdminConfig.list("JAASAuthData").split(lineSeparator)
	found = 0
	if (listJAAS != ['']):
		for eachJAAS in listJAAS:
			jaasname = AdminConfig.showAttribute(eachJAAS, "alias")
			if (jaasname == authAliasName):
				found = 1
				try:
					_excp_ = 0
					result = AdminConfig.modify(eachJAAS, attrs)
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
        			#endTry 
				if (_excp_ ):
					print "Caught Exception modifying JAAS Auth Alias"
					print result
					return
        			#endIf 
                        #endIf 
                #endFor 
        #endIf

	if (not found):
		print ""+authAliasName+" not found."
		print "Modification failed."
		return
	else:
		print "Modification of "+authAliasName+" was successful."
		
	#endIf
#endDef

#******************************************************************************
# Procedure:  	processLTPAKeys
# Description:	Generate, import, and export keys
#******************************************************************************
def processLTPAKeys ( ltpaPwd, ltpaKeyFile ):

	sec = getConfigId("cell", cellName, "", "Security")
	ltpa = AdminConfig.list("LTPA", sec)

	# This code is for generating, importing, and exporting keys
	if (len(ltpa) != 0):
		sharedKey = null
		privateKey = null
		publicKey = null

		# Network Deployment environment assumed
		runningSecMBean = AdminControl.queryNames("type=SecurityAdmin,process=dmgr,*")
		
		if (len(runningSecMBean) != 0):
			# Generate LTPA Keys
			try:
				_excp_ = 0
				result = AdminConfig.invoke(runningSecMBean, "generateKeys", ltpaPwd)
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				result = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				print "LTPA keys were not generated"
				print result
				return    
        		#endIf 
			print "LTPA Keys were generated"

			# Export LTPA Keys

			try:
				_excp_ = 0
				prop = AdminControl.invoke(runningSecMBean, "exportLTPAKeys")
			except:
				_type_, _value_, _tbck_ = sys.exc_info()
				prop = `_value_`
				_excp_ = 1
        		#endTry 
			if (_excp_ ):
				print "LTPA Keys were not exported"
				print prop
				return
        		#endIf 

			# Look for public, private, and shared keys
			for x in range (0,7):
				key = (prop[x]) [0]	
				value = (prop[x]) [1]
				if (key == "com.ibm.websphere.ltpa.DESKey"):
					sharedKey = value
				elif (key == "com.ibm.websphere.ltpa.PrivateKey"):
					privateKey = value
				elif (key == "com.ibm.websphere.ltpa.Publickey"):
					publicKey = value
				#endIf
			print key
			print value
			#endFor
		#endIf

		sharedAttr = ["shared", [["byteArray", sharedKey]]]
		privAttr = ["private", [["byteArray", privateKey]]]
		publicAttr = ["public", [["byteArray", publicKey]]]
		attrs1 = [sharedAttr, privAttr, publicAttr]

		try:
			_excp_ = 0
			result = AdminConfig.modify(ltpa, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
        	#endTry 
		if (_excp_ ):
			print "Caught Exception modifying LTPA keys"
			print result
			return
        	#endIf 
	
	#endIf
#endDef

#******************************************************************************
# Procedure:	setCURCustomProperties
# Description:	Create Custom User Registry Custom Properties or modify existing ones.
#******************************************************************************
def setCURCustomProperties ( cuName, cuValue, cuDesc ):

	#---------------------------------------------------------------------------------
	# Set Custom User Registry Properties
	#---------------------------------------------------------------------------------
	global AdminConfig

	nameAttr = 	["name", cuName]
	valueAttr = ["value", cuValue]
	descAttr = 	["description", cuDesc]

	sec = getConfigId("cell", cellName, "", "Security")
	uReg = AdminConfig.list("CustomUserRegistry", sec)
	props = AdminConfig.showAttribute(uReg, "properties")
	propsList = wsadminToList(props)
	modifiedOne = 0
	if (props != '[]'):
		for entry in propsList:
			entryName = AdminConfig.showAttribute(entry, "name")
			if entryName.find(cuName) >= 0:
				modifiedOne = 1
				print "\nModifying "+cuName+" values"
				try:
					_excp_ = 0
					result = AdminConfig.modify(entry, [valueAttr, descAttr])
				except:		
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
        			#endTry 
				if (_excp_ ):
					print "Caught Exception modifying custom properties"
					print result
					return
        			#endIf 
				break
			#endIf
		#endFor
	#endIf

	if ( not modifiedOne ):
		attrs = [nameAttr, valueAttr, descAttr]
		try:
			_excp_ = 0
			result = AdminConfig.create("Property", uReg, attrs )
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			print "Caught Exception creating Custom Property"
			print result
			return
		else:
			print "\nAdded Custom User Registry Property: "+cuName
		#endIf 
	#endIf

#endDef


#******************************************************************************
# Procedure:	setSecurityCustomProperty
# Description:	Set Security Custom Property
#******************************************************************************
def setSecurityCustomProperty (propType, propName, propValue, display):

	global AdminConfig

	props = AdminConfig.showAttribute(propType, "properties") 
	propsList = wsadminToList(props)
	foundIt = "false"
	if (props != '[]'):
		for property in propsList:
			if (AdminConfig.showAttribute(property, "name") == propName):
				try:
					_excp_ = 0
					result = AdminConfig.modify(property, [["value", propValue]])
				except:
					_type_, _value_, _tbck_ = sys.exc_info()
					result = `_value_`
					_excp_ = 1
				#endTry 
				if (_excp_ ):
					print "Caught Exception modifying "+display
					print result
					return
    				#endIf 

				foundIt = "true"
			#endIf
		#endFor
	#endIf

	if (foundIt == "false"):
		attrs1 = []
		attrs1.append(["name", propName])
		attrs1.append(["value", propValue])
		try:
			_excp_ = 0
			result = AdminConfig.create("Property", propType, attrs1)
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			print "Caught Exception creating "+display
			print result
			return
 		#endIf 
	#endIf
	return 
#endDef

