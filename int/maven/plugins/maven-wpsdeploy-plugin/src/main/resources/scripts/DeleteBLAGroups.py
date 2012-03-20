from lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

False, True = 0,1 #Define False, True

def main():
	for blaGroupId in getBLAGrups():
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

def getBLAGrups():
	allBLAs = AdminTask.listBLAs().splitlines()
	return [bla for bla in allBLAs if bla.endswith('-group')]
	
def getCompUnitsInGroup(blaGroupId):
	return AdminTask.listCompUnits('-blaID %s' % blaGroupId).splitlines()
	
def deleteCompUnit(blaGroupId, unitId):
	if AdminTask.deleteCompUnit('[-blaID %s -cuID %s]' % (blaGroupId, unitId)):
		return True
	else:
		return False

def deleteEmptyBlaGroup(blaGroupId):
	if AdminTask.deleteBLA('-blaID %s' % blaGroupId):
		return True
	else:
		return False
	
if __name__ == '__main__': main()