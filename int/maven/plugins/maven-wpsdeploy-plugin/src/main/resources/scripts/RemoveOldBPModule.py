import sys, re
import lib.scaModuleUtil as sca
import lib.applicationUtil as app
from  lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

microflowREGEX = re.compile('-microflow-')
scaModules = sca.getModulesToBeInstalled()
for scaModule in scaModules:
	if microflowREGEX.search(scaModule.shortName):
		l.info('Checking whether older version of application %s exists...' % scaModule.shortName)
		if scaModule.uninstallOldVersion:
			app.uninstall(scaModule)
			scaModule.uninstallOldVersion = False
		else:
			l.info('Same version or not installed, continuing without uninstalling.')

sca.save(scaModules)
save()