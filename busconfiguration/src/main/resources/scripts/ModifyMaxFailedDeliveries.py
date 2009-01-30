#******************************************************************************
# File Name:	ModifyMaxFailedDeliveries.py
#
# Description:	Modify "Max Failed Deliveries" on the all destinations for given BUS
#
# Syntax: 	wsadmin -lang jython -f ModifyMaxFailedDeliveries.py <BUS_NAME> <new value maxFailedDeliveries>
#****************************************************************************** 

if len(sys.argv) != 2:
	print("ERROR (ModifyMaxFailedDeliveries): Syntax: wsadmin -lang jython -f ModifyMaxFailedDeliveries.py <BUS_NAME> <new value maxFailedDeliveries>")
	sys.exit()

BUS_NAME	 = sys.argv[0]
MAX_FAILED_DEL 	 = sys.argv[1]

SIDestList = AdminTask.listSIBDestinations('-bus '+BUS_NAME).split(lineSeparator)
for dest in SIDestList:
		ident = AdminConfig.showAttribute(dest, "identifier")
		parmsmodify = '-bus '+BUS_NAME+' -name "'+ident+ '" -maxFailedDeliveries '+MAX_FAILED_DEL
		
		try:
			_excp_ = 0
			result = AdminTask.modifySIBDestination(parmsmodify)
			AdminConfig.save()
			print "Successfully modified maxFailedDeliveries on destination " + ident
			
		except:
			_type_, _value_, _tbck_ = sys.exc_info()
			result = `_value_`
			_excp_ = 1
		#endTry 
		if (_excp_ ):
			print "ERROR (ModifyMaxFailedDeliveries): Error modifying SIB Destination for maxFailedDeliveries "
			print "ERROR (ModifyMaxFailedDeliveries): "+result
			sys.exit()
		#endIf 