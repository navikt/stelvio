from lib.saveUtil import save
from lib.timerUtil import Timer
import lib.logUtil as log
l = log.getLogger(__name__)

def main():
	myTimer = Timer()
	l.info('Getting BLA-groups...')
	blaGroups = getBLAGrups()
	l.info('It took', myTimer, 'to get', len(blaGroups), 'BLA-groups.')
	
	if blaGroups:
		for blaGroupId in blaGroups:
			l.info('Deleting CompUnits in', blaGroupId)
			for unitId in getCompUnitsInGroup(blaGroupId):
				if deleteCompUnit(blaGroupId, unitId):
					l.info('Deleted', unitId)
				else:
					l.error('Could not delete', unitId)
					
			if deleteEmptyBlaGroup(blaGroupId):
				l.info('Deleted', blaGroupId)
			else:
				l.error('An error ocured when deleting', blaGroupId)
		
		save()
	else:
		l.info('Found no BLA-groups, exiting!')

def getBLAGrups():
	allBLAs = AdminTask.listBLAs().splitlines()
	return [bla for bla in allBLAs if bla.endswith('-group')]
	
def getCompUnitsInGroup(blaGroupId):
	return AdminTask.listCompUnits('-blaID %s' % blaGroupId).splitlines()
	
def deleteCompUnit(blaGroupId, unitId):
	cmd = '[-blaID %s -cuID %s]' % (blaGroupId, unitId)
	l.debug('AdminTask.deleteCompUnit("'+cmd+'")')
	if AdminTask.deleteCompUnit(cmd):
		return True
	else:
		return False

def deleteEmptyBlaGroup(blaGroupId):
	cmd = '-blaID %s' % blaGroupId
	l.debug('AdminTask.deleteBLA("'+cmd+'")')
	if AdminTask.deleteBLA(cmd):
		return True
	else:
		return False
	
if __name__ == '__main__': main()