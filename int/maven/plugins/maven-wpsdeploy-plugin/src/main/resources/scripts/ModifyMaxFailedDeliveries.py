#******************************************************************************
# File Name:	ModifyMaxFailedDeliveries.py
#
# Description:	Modify "Max Failed Deliveries" on the all destinations for given BUS
#
# Syntax: 	wsadmin -lang jython -f ModifyMaxFailedDeliveries.py
#******************************************************************************
from lib.environmentInfo import getBusName

import lib.logUtil as log
l = log.getLogger(__name__)

busName = getBusName('SYSTEM')
sibDestinations = AdminTask.listSIBDestinations('-bus ' + busName).splitlines()
counter = 1
numberOfSibDestinations = len(sibDestinations)

for sibDestination in sibDestinations:
	sibDestinationIdentifier = AdminConfig.showAttribute(sibDestination, "identifier")

	if sibDestinationIdentifier.find("WBI.FailedEvent") >= 0:
		maxFailedDeliveries = 5
	elif sibDestinationIdentifier.find("BFMIF") >= 0:
		maxFailedDeliveries = 5
	elif sibDestinationIdentifier.find("HTMIF") >= 0:
		maxFailedDeliveries = 5
	else:
		maxFailedDeliveries = 2

		parmsmodify = '-bus %s -name "%s" -maxFailedDeliveries %s' % (busName, sibDestinationIdentifier, maxFailedDeliveries)

	try:
		l.debug("AdminTask.modifySIBDestination('%s')" % parmsmodify)
		result = AdminTask.modifySIBDestination(parmsmodify)
		l.info("[ %s of %s ] - Successfully set maxFailedDeliveries to %s on destination %s." % (
			counter,
			numberOfSibDestinations,
			maxFailedDeliveries,
			sibDestinationIdentifier
		))
		counter += 1
	except:
		l.exception("(ModifyMaxFailedDeliveries): Error modifying SIB Destination for maxFailedDeliveries!")

AdminConfig.save()
