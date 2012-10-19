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
	
def parseEndpoints(moduleConfigPath):
	moduleEndpoints = {}
	importNameREGEX = re.compile('(?:/?sca/import/|)([^/]+)$')
	for moduleName, configPath in getAllModuleConfigFiles(moduleConfigPath).items():
		xml = XML.parseXML(configPath)
		
		endpointNodes = xml.findAll('endpoint')
		if not endpointNodes:
			continue
		
		endpoints = {}
		for endpointNode in endpointNodes:
			fullName = endpointNode.getChildValue('name')
			m = importNameREGEX.match(fullName)
			if not m:
				raise ValueError('Ugyldig formatering på import navnet i XMLen!')
			name = m.group(1)
			value = endpointNode.getChildValue('value')
			endpoints[name] = value
		
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
	
def parseSamhandlerEndpoints(samhandlerSpesificationPath):
	xml = XML.parseXML(samhandlerSpesificationPath)
	defaultNSBs = []
	tilkoblingsListeNSBs = []
	for nsb in xml.findAll('nameSpaceBindings'):
		resources = [r.val() for r in nsb.findAll('resource')]
		samhandlers = []
		if nsb.attr('type') == 'default':
			for samhandler in nsb.findAll('samhandler'):
					samhandlers.append('^'.join([
						samhandler.childValue('version'),
						samhandler.attr('tpnr'),
						samhandler.childValue('endpoint')
						]))
			defaultNSBs.append('|'.join(samhandlers))
		elif nsb.attr('type') == 'tilkoblingsListe':
			for samhandler in nsb.findAll('samhandler'):
				samhandlers.append('^'.join(
					[samhandler.attr('eksternTSSId')] + 
					[x.get() for x in samhandler.getChild('commonProperties').getChildren()] +
					[nsb.getChildValue('aktivertaktivertFom')]
				))
			tilkoblingsListeNSBs.append('|'.join(samhandlers))
		
		for resource in resources:
			pass
		

def getAllModuleConfigFiles(moduleConfigPath):
	modules = {}
	path = moduleConfigPath +'/'+ env.getEnvClass() +'/'+ env.getEnviroment()
	extractXmlsFilePath(path, modules)
	path = moduleConfigPath +'/'+ env.getEnvClass()
	extractXmlsFilePath(path, modules)
	path = moduleConfigPath
	extractXmlsFilePath(path, modules)
	return modules
			
def extractXmlsFilePath(path, modulesDict):
	for fileName in os.listdir(path):
		if fileName.lower().endswith('.xml'):
			moduleName = stripExtension(fileName)
			if not modulesDict.has_key(moduleName):
				modulesDict[moduleName] = path+'/'+fileName
			else:
				l.debug(moduleName, 'is also defined on a higner level and therfore will %s/%s be ignored!' % (path, fileName))
	
fileNameAndExtensionREGEX = re.compile('(.*)\.([^\.]+)$')
def stripExtension(fileName):
	name, extension = fileNameAndExtensionREGEX.match(fileName).groups()
	return name