import os, re
import lib.namingConventionLogic as naming
import lib.configurationPaths as configurationPaths
import lib.logUtil as log
l = log.getLogger(__name__)

headlineRow = ""

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
	
def getModulesToBeInstalled():
	f = open(configurationPaths.getDeployDependenciesPath())
	
	global headlineRow
	headlineRow = f.readline().strip()
	
	modules = []
	for line in f.readlines():
		shortName, version, earPath, doInstall, deployResources, uninstallOldVersion = line.strip().split(',')
		scaModule = ScaModule(shortName, version)
		
		scaModule.doInstall = doInstall == "True"
		scaModule.deployResources = deployResources == "True"
		scaModule.earPath = earPath
		scaModule.uninstallOldVersion = uninstallOldVersion == "True"
		
		modules.append(scaModule)
	return modules
	
def save(applicationList):
	f = open(configurationPaths.getDeployDependenciesPath(), 'w')
	f.write(headlineRow +'\n')
	for app in applicationList:
		f.write(app.toCsvLine() +'\n')
	f.close()

def getApplicationVersion(applicationName):
	versionString = AdminApp.view(applicationName, '-buildVersion').strip()
	versionNumber = versionString.replace('Application Build ID:  ', '')
	return versionNumber
	
def getMajorVersion(version):
	return version.split('.')[0]

class ScaModule:
	''' Example:
		shortName:       nav-tjeneste-sak
		scaVersion:      1
		version:         1.1.2
		moduleName:      nav-tjeneste-sak_v1
		applicationName: nav-tjeneste-sak_v1App
	'''
	def __init__(self, shortName, version, scaVersion=None):
		if not scaVersion:
			if naming.isVersioned(shortName):
				if naming.isProcess(shortName):
					scaVersion = version
				else:
					scaVersion = getMajorVersion(version)
		self.shortName = shortName
		self.version = version
		self.majorVersion = getMajorVersion(version)
		self.scaVersion = scaVersion
		
		if scaVersion:
			self.moduleName = '%s_v%s' % (shortName, str(scaVersion).replace('.','_'))
		else:
			self.moduleName = shortName
			
		self.applicationName = self.moduleName+'App'
		
	def toCsvLine(self):			
		return ','.join((
			self.shortName,
			self.version,
			self.earPath,
			self.__boolToStr(self.doInstall),
			self.__boolToStr(self.deployResources),
			self.__boolToStr(self.uninstallOldVersion)
		))
		
	def __boolToStr(self, b):
		if b: return "True"
		else: return "False"
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