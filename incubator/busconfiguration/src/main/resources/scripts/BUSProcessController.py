import time

execfile( "./src/main/scripts/scripts/utils6.py" )

if len(sys.argv) != 1:
        print time.strftime("[%d/%m %H:%M:%S]") + " [ERROR] (BUSProcessOperations.py): Syntax: wsadmin -lang jython -f BUSProcessOperations.py <operation>"
        sys.exit()

operation=sys.argv[0]
if(operation != 'stop' and operation != 'start'):
	print time.strftime("[%d/%m %H:%M:%S]") + " [ERROR] (BUSProcessOperations.py): The operation parameter must be \"stop\" or \"start\""
	sys.exit(1)

WPSMember1 = ""
WPSMember2 = ""
WPSMember1Node = ""
WPSMember2Node = ""
MEMember1 = ""
MEMember2 = ""
MEMember1Node = ""
MEMember2Node = ""
SupportMember1 = ""
SupportMember2 = ""
SupportMember1Node = ""
SupportMember2Node = ""

def findServers():
        global WPSMember1
        global WPSMember2
	global WPSMember1Node
        global WPSMember2Node
        global MEMember1
	global MEMember2
        global MEMember1Node
	global MEMember2Node	
	global SupportMember1
	global SupportMember2
	global SupportMember1Node
	global SupportMember2Node
	clusters = AdminConfig.list("ServerCluster").split(java.lang.System.getProperty('line.separator'))
	for cluster in clusters:
       		members = AdminConfig.showAttribute(cluster, "members").split(java.lang.System.getProperty(' '))
		for member in members:
			cool_member=member.replace('[','')
			cool_member=cool_member.replace(']','')
			serverName = AdminConfig.showAttribute(cool_member, "memberName")
			nodeName = AdminConfig.showAttribute(cool_member, "nodeName")
			print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Application Server: " + serverName + " is on node " + nodeName
			if(serverName.find("WPS") >= 0 and serverName.find("01") >= 0):
	                        WPSMember1 = serverName
	                        WPSMember1Node = nodeName
			if(serverName.find("WPS") >= 0 and serverName.find("02") >= 0):
                	        WPSMember2 = serverName
	                        WPSMember2Node = nodeName
	                if(serverName.find("ME") >= 0 and serverName.find("01") >= 0):
                	        MEMember1 = serverName
	                        MEMember1Node = nodeName
	                if(serverName.find("ME") >= 0 and serverName.find("02") >= 0):
                	        MEMember2 = serverName
	                        MEMember2Node = nodeName
	                if(serverName.find("Support") >= 0 and serverName.find("01") >= 0):
                	        SupportMember1 = serverName
	                        SupportMember1Node = nodeName
	                if(serverName.find("Support") >= 0 and serverName.find("02") >= 0):
                	        SupportMember2 = serverName
	                        SupportMember2Node = nodeName                                                                                        
#endDef

def doServerOperation ( appServer, appServerNode, operation ):
	if(operation == 'stop'):
		waitingForState = 'STOPPED'
		stopServer ( appServerNode , appServer )
	if(operation == 'start'):
		waitingForState = 'STARTED'
		startServer ( appServerNode, appServer )
	while 1:
		server  = AdminControl.completeObjectName("type=Server,name="+appServer+",*")
		if(server != ""):
			serverState = AdminControl.getAttribute(server, "state")
		else:
			serverState = "STOPPED"

		if(serverState.find(waitingForState) >=  0):
			print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] State " + waitingForState + " is reached."
			break

		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Application server " + appServer + " is: " + serverState + ". Waiting for this websphere stuff to be " + waitingForState + ". Sleeping 60 seconds"
		sleep(60)
#endDef

def waitForMessagingEnginesStarted():
	started = 'false'
	engines = AdminControl.queryNames("type=SIBMessagingEngine,*").splitlines()
	while 1:
		sleep(60)
		for engine in engines:
			try:
        			started = AdminControl.invoke(engine, "isStarted")
        		except:
        			print time.strftime("[%d/%m %H:%M:%S]") + "[WARNING] Could not reach messaging engine, trying again..."
			if(started == 'false' ):
				print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] All messaging engines are not started. Sleeping 60 seconds..."
				break
		if(started == 'true' ):
			print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] All messaging engines are started"
			break
#endDef

findServers()

nodes=0
nodeagents = AdminControl.queryNames("type=NodeAgent,*").splitlines()
for nodeagent in nodeagents:
	nodes=nodes+1

cluster="false"
if(nodes==1):
	print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Only one node agent is active"
	cluster="false"
else:
	print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] More than one nodeagent is active"
	cluster="true"

if(operation == 'stop'):
	print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stop is initiated for BUS"
	if(cluster == "true"):
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + WPSMember1
		doServerOperation(WPSMember1, WPSMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + WPSMember2
		doServerOperation(WPSMember2, WPSMember2Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember1
		doServerOperation(SupportMember1, SupportMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember2
		doServerOperation(SupportMember2, SupportMember2Node,operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember1
		doServerOperation(MEMember1, MEMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember2
		doServerOperation(MEMember2, MEMember2Node, operation)			
	else:
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + WPSMember1
		doServerOperation(WPSMember1, WPSMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember1
		doServerOperation(SupportMember1, SupportMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember1
		doServerOperation(MEMember1, MEMember1Node, operation)

if(operation == 'start'):
	print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Start is initiated for BUS"
	if(cluster == "true"):
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember1
		doServerOperation(MEMember1, MEMember1Node, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember2
		doServerOperation(MEMember2, MEMember2Node, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Waiting for all message engines to start"
		waitForMessagingEnginesStarted()	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember1
		doServerOperation(SupportMember1, SupportMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember2
		doServerOperation(SupportMember2, SupportMember2Node,operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember1
		doServerOperation(WPSMember1, WPSMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember2
		doServerOperation(WPSMember2, WPSMember2Node, operation)			
	else:
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember1
		doServerOperation(MEMember1, MEMember1Node, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Waiting for all message engines to start"
		waitForMessagingEnginesStarted()
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember1
		doServerOperation(SupportMember1, SupportMember1Node, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember1
		doServerOperation(WPSMember1, WPSMember1Node, operation)
