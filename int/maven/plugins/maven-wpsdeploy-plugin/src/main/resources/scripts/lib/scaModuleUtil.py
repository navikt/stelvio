import os, re

False, True = 0,1 #Define False, True
appREGEX = re.compile('_v\d+')
versionedModule = re.compile('.*-tjeneste-')
parseModuleNameREGEX = re.compile('^((?:.+(-tjeneste-))?.+)-(\d+\.\d+\.\d+.*).ear$')

def getInstalledModules():
	modules = []
	for line in AdminTask.listSCAModules().splitlines():
		moduleName, applicationName, version, shortName, cellId, empty = line.split(':')
		scaModule = ScaModule(shortName, version)
		modules.append(scaModule)
	return modules
	
def getModulesToBeInstalled(earFolder):
	earFiles = os.lisdir(earFolder)
	modules = []
	for earFile in earFiles:
		shortName, versioned, version = parseModuleNameREGEX.match(earFile)
		scaModule = ScaModule(shortName, version, versioned)
		modules.append(scaModule)
	return modules
	
class ScaModule:
	''' Example:
		shortName:       nav-tjeneste-sak
		version:         1.0.3
		moduleName:      nav-tjeneste-sak_v1
		applicationName: nav-tjeneste-sak_v1App
	'''
	def __init__(self, shortName, version, versioned=False):
		self.shortName = shortName
		self.version = version
		if versioned:
			majorVersion = version.split('.', 1)[0]
			self.moduleName = '%s_v%s' % (shortName, majorVersion)
		else:
			self.moduleName = shortName
		self.applicationName = self.moduleName+'App'
		
	def __eq__(self, other):
		return self.applicationName == str(other)
	def __hash__(self):
		return self.applicationName
	def __str__(self):
		return self.applicationName
	def __add__(self, x):
		return self.__str__() + x
	def __radd__(self, x):
		return x + self.__str__()