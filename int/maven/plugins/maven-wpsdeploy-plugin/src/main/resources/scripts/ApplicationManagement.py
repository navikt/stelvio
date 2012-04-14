import sys, re
import lib.scaModuleUtil as sca
from lib.timerUtil import Timer
import lib.applicationUtil as app
import lib.logUtil as log
from lib.saveUtil import save

l = log.getLogger(__name__)

def main():
	myTimer = Timer()
	
	l.info('Getting modules to be installed')
	modulesToBeInstalled = sca.getModulesToBeInstalled()
	l.info('It took', myTimer.reset(), 'to get', len(modulesToBeInstalled), 'modules.')
	
	for scaModule in modulesToBeInstalled:
		if scaModule.uninstallOldVersion:
			app.uninstall(scaModule)
		if scaModule.doInstall:
			app.install(scaModule)
			
	save()
	
if __name__ == '__main__': main()