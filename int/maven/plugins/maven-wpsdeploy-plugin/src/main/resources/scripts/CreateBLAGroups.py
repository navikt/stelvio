import sys, os
from lib.saveUtil import save
from lib.XmlToUtil import blaGroupXmlToStringList
from lib.scaModuleUtil import getInstalledModules
import lib.logUtil as log
l = log.getLogger(__name__)

False, True = 0,1 #Define False, True

def main():
	BLA_GROUPS_DIR = sys.argv[1]
	allFiles = os.listdir(BLA_GROUPS_DIR)
	blaGroupFiles = filterOutNonGroupXmlFiles(allFiles)
	l.debug('Found these BLA group files:', listToCSV(blaGroupFiles))
	
	l.info('Getting a list of all installed modules.')
	installedModules = getInstalledModules()
	l.debug('Got these modules:', listToCSV(installedModules))
	
	for blaGroupFile in blaGroupFiles:
		blaGroupName = blaGroupFile.replace('.xml','')
		
		l.info('Parsing', blaGroupFile)
		blaModules = blaGroupXmlToStringList(BLA_GROUPS_DIR +'/'+ blaGroupFile)
		l.debug('Found these modules in %s: %s' % (blaGroupFile, listToCSV(blaModules)))
		
		if not blaModules:
			l.info('No modules in BLA group file', blaGroupFile)
			continue
		
		l.info('Creating new BLA group:', blaGroupName)
		blaGroupId = createNewBLAGroup(blaGroupName)
		
		scaModules = getIntersection(blaModules, installedModules)
		
		for scaModule in scaModules:
			if addUnitToGroup(scaModule, blaGroupId):
				l.info('Added', scaModule, 'to', blaGroupName)
			else:
				l.error('Could not add', scaModule, 'to', blaGroupName)
	
	save()
	
def filterOutNonGroupXmlFiles(list):
	return [x for x in list if x.endswith('-group.xml')]

def getIntersection(blaModules, installedModules):
	scaModules = []
	for blaModule in blaModules:
		for scaModule in installedModules:
			#blaModule will match both v1 and v2 of a scaModule's shortName
			if blaModule == scaModule.shortName:
				scaModules.append(scaModule)
	return scaModules

def addUnitToGroup(scaModule, blaGroupId):
	blaName = blaGroupId.replace('WebSphere:blaname=','')
	cmd = '[-blaID %(blaId)s -cuSourceID %(appId)s -CUOptions [[%(blaId)s %(appId)s %(blaName)s_%(appName)s "" 1]]]' % {
			'appId': 'WebSphere:blaname='+scaModule.applicationName,
			'appName': scaModule.moduleName,
			'blaId': blaGroupId,
			'blaName': blaName
		}
	l.debug('AdminTask.addCompUnit(\''+cmd+'\')')
	if AdminTask.addCompUnit(cmd):
		return True
	else:
		return False

def createNewBLAGroup(name):
	cmd = '[-name %(name)s -description "Applications that must be restarted when a new version of %(name)s is deployed" ]'% {'name':name}
	l.debug('AdminTask.createEmptyBLA("'+cmd+'")')
	blaId = AdminTask.createEmptyBLA(cmd)
	return blaId

def listToCSV(list):
	return ', '.join([str(x) for x in list])
	
if __name__ == '__main__': main()