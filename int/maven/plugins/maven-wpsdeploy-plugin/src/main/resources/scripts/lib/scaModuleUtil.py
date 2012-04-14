import os, re
import lib.namingConventionLogic as naming
import lib.fileMap as fileMap
import lib.logUtil as log
l = log.getLogger(__name__)

headlineRow = ""
APPLICATIONS_INSTALL_CSV_PATH = fileMap.get('deployDependencies')

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
	f = open(APPLICATIONS_INSTALL_CSV_PATH)
	
	global headlineRow
	headlineRow = f.readline().strip()
	
	modules = []
	for line in f.readlines():
		shortName, version, earPath, doInstall, deployResources = line.strip().split(',')
		if naming.isVersioned(shortName):
			if naming.isProcess(shortName):
				scaVersion = version
			else:
				scaVersion = getMajorVersion(version)
		else:
			scaVersion = None
		scaModule = ScaModule(shortName, version, scaVersion)
		scaModule.doInstall = doInstall == "True"
		scaModule.deployResources = deployResources == "True"
		scaModule.earPath = earPath
		modules.append(scaModule)
	return modules
	
def setModulesToBeInstalled(applicationList):
	f = open(APPLICATIONS_INSTALL_CSV_PATH, 'w')
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
	def __init__(self, shortName, version, scaVersion):
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
		if self.doInstall:
			doInstall = "True"
		else: 
			doInstall = "False"
		if self.deployResources:
			deployResources = "True"
		else: 
			deployResources = "False"
			
		return ','.join((self.shortName, self.version, self.earPath, doInstall, deployResources))
		
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