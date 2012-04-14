import sys, re
import lib.scaModuleUtil as sca
import lib.applicationUtil as app
import lib.logUtil as log
l = log.getLogger(__name__)

microflowREGEX = re.compile('-microflow-')
for scaModule in sca.getModulesToBeInstalled():
	if microflowREGEX.search(scaModule.shortName):
		l.info('Checking whether older version of application %s exists...' % scaModule.shortName)
		if scaModule.uninstallOldVersion:
			app.uninstall(scaModule)
		else:
			l.info('Same version or not installed, continuing without uninstalling.')