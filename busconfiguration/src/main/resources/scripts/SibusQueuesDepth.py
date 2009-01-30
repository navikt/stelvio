#******************************************************************************
# File Name:	SibusQueuesDepth.py
#
# Description:	List current depth if it is greater than zero for all QUEUE destinations in the cell
#
# Syntax: 	wsadmin -lang jython -f SibusQueuesDepth.py
#****************************************************************************** 

lineSeparator = java.lang.System.getProperty('line.separator')
SIBusList = AdminTask.listSIBuses().split(lineSeparator)	

for bus in SIBusList:
	busName = AdminConfig.showAttribute(bus, "name")
	print "%-40s %-40s %-30s" % ("=======================================",busName,"================================")
	SIDestList = AdminTask.listSIBDestinations('-bus '+busName + " -type QUEUE").split(lineSeparator)
	print "%-100s %-20s" % ("Queue Name","Current Depth")
	print "%-100s %-20s" % ("----------","-------------")
	
	for dest in SIDestList:
		destName  =  AdminConfig.showAttribute(dest, "identifier")
		qp = AdminControl.queryNames("WebSphere:type=SIBQueuePoint,name="+destName+",*")
		queueDepth = AdminControl.getAttribute(qp,"depth")
		
		if(int(queueDepth) > 0 ):
			print "%-100s %-20s" % (destName,queueDepth)
			
	print "\n"
