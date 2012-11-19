import re, os
import lib.deployEnviromentUtil as env
import lib.XMLUtil as XML
import lib.logUtil as log
l = log.getLogger(__name__)

def blaGroupXmlToStringList(blaGroupXmlFile):
	xml = XML.parseXML(blaGroupXmlFile)
	return [module.get() for module in xml.findAll('module')]
	
def parseAuthorizationConfiguration(roleXmlPath):
	xml = XML.parseXML(roleXmlPath)
	rolesNode = xml.findFirst('roles')
	
	def getRolesData(node):
		userNames = groupNames = runas = ""
	
		roleName = node.getChildValue('name')
		
		usersNode = node.findFirst('users')
		if usersNode:
			userNames = [x.get() for x in usersNode.findAll('name')]
			
		groupsNode = node.findFirst('groups')
		if groupsNode:
			groupNames = [x.get() for x in groupsNode.findAll('name')]
			
		runasNode = node.findFirst('runas')
		if runasNode:
			runas = {'username': runasNode.getChildValue('username'), 'password': runasNode.getChildValue('password')}
	
		return roleName, {'users': userNames, 'groups': groupNames, 'runas': runas}

	roles = dict(rolesNode.each(getRolesData))
	
	return roles
		
def readPolicySetBindingConfig(xmlPath):
	xml = XML.parseXML(xmlPath)
	config = []
	for policySetBindingXML in xml.findAll('policySetBinding'):
		name = policySetBindingXML.attr('name')
		user = policySetBindingXML.getChildValue('user')
		password = policySetBindingXML.getChildValue('password')
		config.append([name,user,password])
		
	return config
	
def parseEndpoints(applicationEndpointsFolderPath):
	moduleEndpoints = {}
	for xmlFile in getAllXmlFiles(applicationEndpointsFolderPath):
		xml = XML.parseXML(xmlFile)
		
		for module in xml.findAll('module'):
			moduleName = module.attr('name')
			endpoints = {}
			for scaImport in module.findAll('scaImport'):
				scaImportName = scaImport.attr('name')
				value = scaImport.get()
				endpoints[scaImportName] = value
			moduleEndpoints[moduleName] = endpoints
			
	return moduleEndpoints

def parseActivationspecifications(activationspecificationsPath):
	xml = XML.parseXML(activationspecificationsPath)
	
	moduleActivationspecifications = {}
	for module in xml.findAll('module'):
		moduleName = module.get()
		maxconcurrency = module.attr('maxconcurrency')
		moduleActivationspecifications[moduleName] = maxconcurrency
	
	return moduleActivationspecifications

def getAllXmlFiles(folder):
	xmlFiles = []
	for fileName in os.listdir(folder):
		if fileName.lower().endswith('.xml'):
			xmlFiles.append(folder + '/' + fileName)
	return xmlFiles

fileNameAndExtensionREGEX = re.compile('(.*)\.([^\.]+)$')
def stripExtension(fileName):
	name, extension = fileNameAndExtensionREGEX.match(fileName).groups()
	return name