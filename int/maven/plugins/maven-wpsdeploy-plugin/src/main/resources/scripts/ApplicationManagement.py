import sys, re
import lib.scaModuleUtil as sca
import lib.logUtil as log
from lib.timerUtil import Timer

l = log.getLogger(__name__)
installedModules = []
clusterName = ""

def main():
	myTimer = Timer()
	
	global installedModules, clusterName
	l.info('Getting installed modules...')
	installedModules = sca.getInstalledModules()
	l.info('It took', myTimer.reset(), 'to get', len(installedModules), 'modules.')
	
	l.info('Getting modules to be installed')
	modulesToBeInstalled = sca.getModulesToBeInstalled()
	l.info('It took', myTimer.reset(), 'to get', len(installedModules), 'modules.')
	
	clusterName = getClusterName()
	
	for installModule in modulesToBeInstalled:
		if installModule.doInstall:
			
			uninstallModule = getExistingScaModule(installModule)
			if uninstallModule: uninstall(uninstallModule)
			
			install(installModule)
			
def getExistingScaModule(scaModule):
	for installed in installedModules:
		if installed == scaModule:
			return installed

def uninstall(scaModule):
	AdminApp.uninstall(scaModule.applicationName)
		
def install(scaModule):
	options = "[ -cluster %s -distributeApp ]" % clusterName

	l.info("Installing %s with options [%s] on %s." % (scaModule, options, clusterName))
	l.info(AdminApp.install(scaModule.earPath, options))
	
def getClusterName():
	clusters = AdminConfig.list('ServerCluster', AdminConfig.list('Cell')).splitlines()
	for cluster in clusters:
		if re.search('AppTarget', cluster):
			clusterName = AdminConfig.showAttribute(cluster, 'name')
	if not clusterName:
		raise Exception, "No cluster name found"
	else:
		return clusterName
	
	
if __name__ == '__main__': main()