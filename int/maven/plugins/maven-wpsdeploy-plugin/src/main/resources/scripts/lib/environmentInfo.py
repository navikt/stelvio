import re

import lib.logUtil as log
l = log.getLogger(__name__)

def getBusObject(busType):
	siBuses = AdminTask.listSIBuses().splitlines()

	if len(siBuses) == 1:
		return siBuses[0]
	else:
		for siBus in siBuses:
			if re.search(busType, siBus):
				return siBus
		else:
			l.error('There is more than one bus but none of them has "%s" in the name. Ending Application!' % busType)

def getBusName(busType='APPLICATION'):
	busObject = getBusObject(busType)
	return AdminConfig.showAttribute(busObject, 'name')

