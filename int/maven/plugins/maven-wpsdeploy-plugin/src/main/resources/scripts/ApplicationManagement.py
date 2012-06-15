import sys, re
import lib.scaModuleUtil as sca
from lib.timerUtil import Timer
import lib.applicationUtil as app
import lib.logUtil as log
from lib.saveUtil import save
from lib.exceptionUtil import getException
from com.ibm.websphere.management.exception import AdminException
from lib.syncUtil import sync

l = log.getLogger(__name__)

def main():
	numberOfappsInstalled = 0
	myTimer = Timer()
	
	l.info('Getting modules to be installed...')
	modulesToBeInstalled = sca.getModulesToBeInstalled()
	l.info('It took', myTimer.reset(), 'to get', len(modulesToBeInstalled), 'modules.')
	
	for scaModule in modulesToBeInstalled:
		if scaModule.uninstallOldVersion:
			app.uninstall(scaModule)
			
	save()
	sync()
	
	for scaModule in modulesToBeInstalled:
		if scaModule.doInstall:
			app.install(scaModule)
			numberOfappsInstalled += 1
			if (numberOfappsInstalled % 25) == 0:
				save()
	
	save()
	sync()
	
	if app.isApplicationClusterRunning():
		for scaModule in modulesToBeInstalled:
			if scaModule.doInstall:
				l.debug(scaModule, '== running:', app.isApplicationRunning(scaModule))
				if not app.isApplicationRunning(scaModule):
					try:
						l.info('Starting %s!' % scaModule.moduleName)
						app.startApplication(scaModule)
					except AdminException, msg:
						l.warning('Got exception while trying to start', scaModule)
						l.debug(msg)
					
	l.info('Finished installing %s applications!' % numberOfappsInstalled)
	
if __name__ == '__main__': main()