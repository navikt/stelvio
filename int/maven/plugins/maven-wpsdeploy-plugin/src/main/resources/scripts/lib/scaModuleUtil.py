import os, re
import lib.namingConventionLogic as naming

False, True = 0,1 #Define False, True
appREGEX = re.compile('_v\d+')

def getInstalledModules():
	modules = []
	for line in AdminTask.listSCAModules().splitlines():
		moduleName, applicationName, scaVersion, shortName, cellId, empty = line.split(':')
		if moduleName.endswith('.AppTarget'):
			continue
		version = getApplicationVersion(applicationName)
		scaModule = ScaModule(shortName, version, scaVersion.strip())
		modules.append(scaModule)
	return modules
	
def getModulesToBeInstalled(earFolder):
	earFiles = os.listdir(earFolder)
	modules = []
	for earFile in earFiles:
		shortName, version, majorVersion, versioned = naming.parseEarFileName(earFile)
		if versioned:
			if naming.isProcess(shortName):
				scaVersion = version
			else:
				scaVersion = majorVersion
		else:
			scaVersion = None
		scaModule = ScaModule(shortName, version, scaVersion)
		modules.append(scaModule)
	return modules
	
def getInstalledScaModule(scaModuleToBeDeployed):
	pass

def getApplicationVersion(applicationName):
	versionString = AdminApp.view(applicationName, '-buildVersion').strip()
	versionNumber = versionString.replace('Application Build ID:  ', '')
	return versionNumber
	
class ScaModule:
	''' Example:
		shortName:       nav-tjeneste-sak
		scaVersion:      1
		version:         1.1.2
		moduleName:      nav-tjeneste-sak_v1
		applicationName: nav-tjeneste-sak_v1App
	'''
	def __init__(self, shortName, version, scaVersion):
		self.shortName = shortName
		self.version = version
		self.majorVersion = version.split('.')[0]
		self.scaVersion = scaVersion
		
		if scaVersion:
			self.moduleName = '%s_v%s' % (shortName, str(scaVersion).replace('.','_'))
		else:
			self.moduleName = shortName
			
		self.applicationName = self.moduleName+'App'
		
	def __gt__(self, other):
		return self.applicationName > str(other)
	def __lt__(self, other):
		return self.applicationName < str(other)
	def __eq__(self, other):
		return self.applicationName == str(other)
	def __hash__(self):
		return self.applicationName
	def __add__(self, x):
		return self.__str__() + x
	def __radd__(self, x):
		return x + self.__str__()
	def __str__(self):
		return self.applicationName