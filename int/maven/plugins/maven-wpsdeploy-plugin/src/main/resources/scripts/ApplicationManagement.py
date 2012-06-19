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
			if (numberOfappsInstalled % 15) == 0:
				save()
	
	save()
	sync()
	
	if app.isApplicationClusterRunning():
		myTimer.reset()
		for scaModule in modulesToBeInstalled:
			if scaModule.doInstall:
				appIsRunning = app.isApplicationRunning(scaModule)
				l.debug(scaModule, '== running:', appIsRunning)
				if not appIsRunning:
					try:
						l.info('Starting %s!' % scaModule.moduleName)
						app.startApplication(scaModule)
					except AdminException, msg:
						l.warning('Got exception while trying to start', scaModule)
						l.debug(msg)
		l.info('It took', myTimer.reset(), 'to check all modules and start those that was not running.')
					
	l.info('Finished installing %s applications!' % numberOfappsInstalled)
	
if __name__ == '__main__': main()