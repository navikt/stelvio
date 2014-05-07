import sys, re
import lib.scaModuleUtil as sca
import lib.applicationUtil as app
from  lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

def main():
	microflowREGEX = re.compile('-microflow-')
	bpelREGEX = re.compile('nav-bsrv-frg-hentinstitusjonsoppholdliste')
	scaModules = sca.getModulesToBeInstalled()
	for scaModule in scaModules:
		if microflowREGEX.search(scaModule.shortName):
			l.info('Checking whether older version of microflow application %s exists...' % scaModule.shortName)
			setUninstallOldVersion(scaModule)
		elif bpelREGEX.search(scaModule.shortName):
			l.info('Checking whether older version of short lived BPEL application %s exists...' % scaModule.shortName)
			setUninstallOldVersion(scaModule)
	sca.save(scaModules)
	save()

def setUninstallOldVersion(scaModule):
	if scaModule.uninstallOldVersion:
		app.uninstall(scaModule)
		scaModule.uninstallOldVersion = False
	else:
		l.info('Same version or not installed, continuing without uninstalling.')

if __name__ == '__main__': main()