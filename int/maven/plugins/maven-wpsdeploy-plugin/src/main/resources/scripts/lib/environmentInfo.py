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

def isLocalBus(busName):
	"""
		When BPM8.5 is in production and you want to change the busConfig to the new BPM8.5 bus names(starts with BPM.) I
		would suggest to remove the BUS_NAME as a property for all JMS Queues that doesn't require a foreign bus. Then you
		can use ´busName = getProperty('BUSNAME') || getBusName()´ so that it will use the config for the foreign buses
		and return a valid dynamic busName when you need a local bus. You can also remove all BUSNAME properties form the
		template files because it has been replaced with getBusName() for all but the foreign bus references.
	"""
	return busName.startswith('SCA.APPLICATION.') or busName.startswith('BPM.')
