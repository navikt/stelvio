import re, os
import lib.moduleConfigPath as moduleConfigPath
import lib.deployEnviromentUtil as env
import lib.XMLUtil as XML
	
def getEndpoints():
	moduleEndpoints = {}
	for moduleName, configPath in getAllModulesWithConfig().items():
		xml = XML.parseXML(configPath)
		
		endpointNodes = xml.findAll('endpoint')
		if not endpointNodes:
			continue
		
		importNameREGEX = re.compile('(?:/?sca/import/|)([^/]+)$')
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

def getActivationspecifications():
	moduleActivationspecifications = {}
	for moduleName, configPath in getAllModulesWithConfig().items():
		xml = XML.parseXML(configPath)
		
		activationspecifications = xml.findAll('activationspecification')
		if not activationspecifications:
			continue
			
		for activationspecification in activationspecifications:
			name = activationspecification.getChildValue('name')
			maxconcurrency = activationspecification.getChildValue('maxconcurrency')
			moduleActivationspecifications[name] = maxconcurrency
			
	return moduleActivationspecifications

def getAllModulesWithConfig():
	modules = {}
	path = moduleConfigPath.getPath()
	extractXmlsFilePath(path, modules)
	path = path +'/'+ env.getEnvClass()
	extractXmlsFilePath(path, modules)
	path = path +'/'+ env.getEnviroment()
	extractXmlsFilePath(path, modules)
	return modules
			
def extractXmlsFilePath(path, modulesDict):
	for fileName in os.listdir(path):
		if fileName.lower().endswith('.xml'):
			moduleName = stripExtension(fileName)
			modulesDict[moduleName] = path+'/'+fileName
	
fileNameAndExtensionREGEX = re.compile('(.*)\.([^\.]+)$')
def stripExtension(fileName):
	name, extension = fileNameAndExtensionREGEX.match(fileName).groups()
	return name