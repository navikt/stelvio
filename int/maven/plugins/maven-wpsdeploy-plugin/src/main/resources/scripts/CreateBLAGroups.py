import sys, os, re
from lib.XmlToUtil import blaGroupXmlToStringList
from lib.scaModuleUtil import getInstalledModules
import lib.logUtil as log
l = log.getLogger(__name__)

False, True = 0,1 #Define False, True
BLA_GROUPS_DIR = sys.argv[1]
BLA_NAME_REGEX = re.compile('WebSphere:blaname=(.*)App')

def main():
	blaGroupFiles = os.listdir(BLA_GROUPS_DIR)
	l.debug('Found these BLA group files:', listToCSV(blaGroupFiles))
	for blaGroupFile in blaGroupFiles:
		blaGroupName = blaGroupFile.replace('.xml','')
		l.info('Creating new BLA group:', blaGroupName)
		blaGroupId = createNewBLAGroup(blaGroupName)
		
		l.info('Parsing', blaGroupFile)
		blaModules = blaGroupXmlToStringList(blaGroupFile)
		l.debug('Found these modules in %s: %s' % (blaGroupFile, listToCSV(blaModules)))
		
		l.info('Getting a list of all installed modules.')
		scaModules = getInstalledModules()
		
		toBlaGroup = []		
		for blaModule in blaModules:
			for scaModule in scaModules:
				#blaModule will match both v1 and v2 of a module
				if blaModule == scaModule.shortName:
					toBlaGroup.append(scaModule)
					
		
		for scaModule in toBlaGroup:
			if addUnitToGroup(scaModule, blaGroupId):
				l.info('Added', scaModule, 'to', blaGroupId)
			else:
				l.error('Could not add', scaModule, 'to', blaGroupId)
		

def addUnitToGroup(scaModule, blaGroupId):
	blaName = blaGroupId.replace('WebSphere:blaname=','')
	cmd = '[-blaID %(blaId)s -cuSourceID %(appId)s -CUOptions [[%(blaId)s %(appId)s %(blaName)_%(appName)s "" 1]]]' % {
			'appId': scaModule.applicationName,
			'appName': scaModule.moduleName,
			'blaId': blaGroupId,
			'blaName': blaName
		}
	l.debug('AdminTask.addCompUnit('+cmd+')')
	if AdminTask.addCompUnit(cmd):
		return True
	else:
		return False

def createNewBLAGroup(name):
	id = AdminTask.createEmptyBLA('[-name %(name)s-group -description "Applications that must be restarted when a new version of %(name)s is deployed" ]'% {'name':name})
	return id

def listToCSV(list):
	return ', '.join([str(x) for x in list])
	
if __name__ == '__main__': main()