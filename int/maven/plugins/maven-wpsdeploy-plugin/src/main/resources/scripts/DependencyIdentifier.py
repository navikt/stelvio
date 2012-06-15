import sys
import lib.scaModuleUtil as sca
import lib.logUtil as log
from lib.tableUtil import Table

l = log.getLogger(__name__)
installedModules = []
yes, no = "Yes", "No"

def main():
	deployAllResources = sys.argv[1] == "deployAllResources=True"
	myTimer = Timer()
	
	global installedModules
	
	l.info('Getting installed modules...')
	installedModules = sca.getInstalledModules()
	l.info('It took', myTimer.reset(), 'to get', len(installedModules), 'modules.')
	
	l.info('Getting modules to be installed...')
	modulesToBeInstalled = sca.getModulesToBeInstalled()
	l.info('It took', myTimer.reset(), 'to get', len(modulesToBeInstalled), 'modules.')
	
	
	table = Table(['Module', 'Install', 'Uninstall current', 'Deploy resources', 'Old version', 'New version'])
	for installModule in modulesToBeInstalled:
		doInstall = doUninstall = deployResources = False
		
		existingModule = getExistingScaModule(installModule)
		if not existingModule:
			doInstall = deployResources = True
			existingModuleVersion = 'N/A'
			
		elif installModule.majorVersion == existingModule.majorVersion:
			existingModuleVersion = existingModule.version
			
			if existingModule.version != installModule.version or installModule.version.endswith('-SNAPSHOT'):
				doInstall = deployResources = True
				if existingModule == installModule:
					doUninstall = True
			else:
				if deployAllResources:
					deployResources = True
					
		row = [installModule.shortName, boolToYesNo(doInstall), boolToYesNo(doUninstall), boolToYesNo(deployResources), existingModuleVersion, installModule.version]
		
		table.addRow(row)
		installModule.doInstall = doInstall
		installModule.uninstallOldVersion = doUninstall
		installModule.deployResources = deployResources
	table.sort(columb=1, reverse=True)
	l.println(table)
	sca.save(modulesToBeInstalled)

def boolToYesNo(b):
	if b: return 'Yes'
	else: return 'No'
				
def getExistingScaModule(scaModule):
	for installed in installedModules:
		if installed == scaModule:
			return installed
	
if __name__ == '__main__': main()