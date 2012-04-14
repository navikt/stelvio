import sys
import lib.scaModuleUtil as sca
import lib.fileMap as fileMap
import lib.logUtil as log
from lib.tableUtil import Table

l = log.getLogger(__name__)
installedModules = []
modulesToBeInstalled = []
APPLICATIONS_INSTALL_CSV_PATH = fileMap.get('deployDependencies')
yes, no = "Yes", "No"

def main():
	deployAllResources = sys.argv[1] == "deployAllResources=True"
	myTimer = Timer()
	
	global installedModules, modulesToBeInstalled
	
	l.info('Getting installed modules...')
	installedModules = sca.getInstalledModules()
	l.info('It took', myTimer.reset(), 'to get', len(installedModules), 'modules.')
	
	l.info('Getting modules to be installed...')
	modulesToBeInstalled = sca.getModulesToBeInstalled()
	l.info('It took', myTimer.reset(), 'to get', len(modulesToBeInstalled), 'modules.')
	
	
	table = Table(['Module', 'Install', 'Deploy resources', 'Old version', 'New version'])
	for installModule in modulesToBeInstalled:
		deployResources = doInstall = False
		existingModule = getExistingScaModule(installModule)
		if not existingModule:
			row = [installModule.shortName, yes, yes, 'N/A', installModule.version]
			deployResources = doInstall = True
			
		elif installModule.majorVersion == existingModule.majorVersion:
			if existingModule.version != installModule.version:
				row = [installModule.shortName, yes, yes, existingModule.version, installModule.version]
				deployResources = doInstall = True
			else:
				if deployAllResources:
					deployResources = yes
				else:
					deployResources = no
				row = [installModule.shortName, no, deployResources, existingModule.version, installModule.version]
		
		table.addRow(row)
		installModule.doInstall = doInstall
		installModule.deployResources = deployResources
	table.sort(columb=1, reverse=True)
	l.println(table)
	sca.setModulesToBeInstalled(modulesToBeInstalled)
				
def getExistingScaModule(scaModule):
	for installed in installedModules:
		if installed == scaModule:
			return installed
	
if __name__ == '__main__': main()