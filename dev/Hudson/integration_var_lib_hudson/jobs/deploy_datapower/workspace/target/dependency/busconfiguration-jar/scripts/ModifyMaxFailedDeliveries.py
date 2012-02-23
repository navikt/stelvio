#******************************************************************************
# File Name:	ModifyMaxFailedDeliveries.py
#
# Description:	Modify "Max Failed Deliveries" on the all destinations for given BUS
#
# Syntax: 	wsadmin -lang jython -f ModifyMaxFailedDeliveries.py
#****************************************************************************** 

def findCellName():
	cells = AdminConfig.list("Cell").split(java.lang.System.getProperty('line.separator'))

	if(len(cells) > 1):
		print "[FATAL] More than one cells found. Bailing out..."
		sys.exit(1)

	cellName = AdminConfig.showAttribute(cells[0], "name")
	print "[INFO] Using cell name: " + cellName
	return cellName

BUS_NAME	 = "SCA.SYSTEM." + findCellName() + ".Bus"
SIDestList = AdminTask.listSIBDestinations('-bus '+BUS_NAME).split(lineSeparator)
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
		_excp_ = 0
		result = AdminTask.modifySIBDestination(parmsmodify)
		print "[ " + repr(COUNTER) + " of " + LENGTH + " ] - Successfully modified maxFailedDeliveries on destination " + ident
		COUNTER = COUNTER + 1	
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	if (_excp_ ):
		print "ERROR (ModifyMaxFailedDeliveries): Error modifying SIB Destination for maxFailedDeliveries "
		print "ERROR (ModifyMaxFailedDeliveries): "+result
		sys.exit()
AdminConfig.save()