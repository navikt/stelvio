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

def findCellName():
	cells = AdminConfig.list("Cell").split(java.lang.System.getProperty('line.separator'))

	if(len(cells) > 1):
		l.error("[FATAL] More than one cells found. Bailing out...")

	cellName = AdminConfig.showAttribute(cells[0], "name")
	l.info("Using cell name: " + cellName)
	return cellName

BUS_NAME = getBusName("SYSTEM")
SIDestList = AdminTask.listSIBDestinations('-bus '+BUS_NAME).splitlines()
COUNTER = 1
LENGTH = repr(len(SIDestList))

for dest in SIDestList:
	ident = AdminConfig.showAttribute(dest, "identifier")
		
	if(ident.find("WBI.FailedEvent") >= 0):
		parmsmodify = '-bus '+BUS_NAME+' -name "'+ident+ '" -maxFailedDeliveries 5'
	elif(ident.find("BFMIF") >= 0):
		parmsmodify = '-bus '+BUS_NAME+' -name "'+ident+ '" -maxFailedDeliveries 5'
	elif(ident.find("HTMIF") >= 0):
		parmsmodify = '-bus '+BUS_NAME+' -name "'+ident+ '" -maxFailedDeliveries 5'
	else:
		parmsmodify = '-bus '+BUS_NAME+' -name "'+ident+ '" -maxFailedDeliveries 2'
	try:
		result = AdminTask.modifySIBDestination(parmsmodify)
		l.info("[ " + repr(COUNTER) + " of " + LENGTH + " ] - Successfully modified maxFailedDeliveries on destination " + ident)
		COUNTER += 1
	except:
		l.exception("(ModifyMaxFailedDeliveries): Error modifying SIB Destination for maxFailedDeliveries!")
AdminConfig.save()
