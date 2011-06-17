import re
import os
import time

def createJ2CConnectionFactory (raName, clusterName, attrs):
	raName = raName
	ra = AdminConfig.getid("/ServerCluster:" + clusterName + "/J2CResourceAdapter:" + raName)

	if (len(ra) == 0):
		log("ERROR", "Resource Adapter "+raName+" does not exists. Aborting creation of J2C Connection Factory. Please install J2C adapter.")
		return 1
	#endIf
	try:
		_excp_ = 0
		result = AdminConfig.create("J2CConnectionFactory", ra, attrs)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry
	if (_excp_ ):
		log("ERROR", "Caught Exception creating J2C Connection Factory." + ra)
		log("ERROR", result)
		return 1
	#endIf
	log("INFO", "Creation of J2C Connection Factory " + attrs[0][1] + " for " +raName+" was successful.")
	return 0
#endDef

def installAdapterOnNode(nodeName, fileName, adapterName, adapterDesc):
	options = '[-rar.name "' + adapterName  + '" -rar.desc "' + adapterDesc + '"]'
	try:
		_excp_ = 0
		result = AdminConfig.installResourceAdapter(fileName, nodeName, options)
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry
	if (_excp_ ):
		log("ERROR", "Caught Exception while installing the adapter.")
		log("ERROR", result)
		return 1
	#endIf

	log("INFO", "Installation was succesful.")
	return 0
#endDef

def configureAdapters(props):
	adapters = ["adldap", "cicseciXA", "cicseciNoTrans", "email"]
	for a in adapters:
		os.system("cd /tmp && wget " + props.getProperty(a + ".file"))
		time.sleep(1)
	#endFor

	dmgr = getDmgrNodeName()
	cell_name = getCellName()
	nodes = AdminConfig.list('Node').splitlines()
	clusters = AdminConfig.list('ServerCluster').splitlines()

	for node in nodes:
		nodeName = getNodeName(node)
		if (nodeName == dmgr):
			log("INFO", "Not installing on " + nodeName + '.')
		else:
			hostname = getNodeHostname(nodeName)
			for a in adapters:
				fileName = props.getProperty(a + '.file')
				fileName = '/tmp/' + re.split('\/', fileName)[-1]
				adapterName = props.getProperty(a + '.name')
				adapterDesc = props.getProperty(a + '.description')
				log("INFO", "Copying " + adapterName + " to " + nodeName + ".")
				os.system('scp ' + fileName + ' wasadm@' + hostname + ':/tmp')
				log("INFO", "Installing " + adapterName + " on " + nodeName + ".")
				result = installAdapterOnNode(nodeName, fileName, adapterName, adapterDesc)
				if (result == 1):
					log("ERROR", "Installation failed. Please verify that nodeagent for node " + nodeName + " is running.")
				
	#AdminConfig.save()

	appTarget = ''
	for cluster in clusters:
		if (cluster.count("AppTarget") > 0 or cluster.count("WPSCluster") > 0):
			appTarget = cluster
	
	clusterName = AdminConfig.showAttribute(appTarget, "name")
	adapter_list = AdminConfig.list('J2CResourceAdapter', AdminConfig.getid( '/Cell:' + cell_name + '/')).splitlines()

	for a in adapter_list:
		raName = AdminConfig.showAttribute(a, "name")
		if (a.count('ADLDAPAdapter') > 0 or a.count('ECIXAResourceAdapter') > 0 or a.count('ECIResourceAdapter') > 0 or a.count('IBM WebSphere Adapter for Email') > 0):
			ra = AdminConfig.getid("/ServerCluster:" + clusterName + "/J2CResourceAdapter:" + raName)
			if (len(ra) == 0):
				adapter_cluster_scope = AdminTask.copyResourceAdapter(a, '[-scope ' + appTarget + ' -name "' + AdminConfig.showAttribute(a, "name") + '" -useDeepCopy false]')
				log("INFO", "Copying adapter " + a + " to " + appTarget + " scope was succesful.")
				if (a.count('ADLDAPAdapter') > 0):
					attrs = props.getPropertiesForAdapter('adldap')
					createJ2CConnectionFactory('ADLDAPAdapter', clusterName, attrs)
					attrs = props.getPropertiesForAdapter('minsideldap')
					createJ2CConnectionFactory('ADLDAPAdapter', clusterName, attrs)
				elif (a.count('IBM WebSphere Adapter for Email') > 0):
					attrs = props.getPropertiesForAdapter('email')
					createJ2CConnectionFactory('IBM WebSphere Adapter for Email', clusterName, attrs)
					attrs = props.getPropertiesForAdapter('minsideemail')
					createJ2CConnectionFactory('IBM WebSphere Adapter for Email', clusterName, attrs)
				#endIf
			#endIf
		#endIf
	#AdminConfig.save()
#endDef

