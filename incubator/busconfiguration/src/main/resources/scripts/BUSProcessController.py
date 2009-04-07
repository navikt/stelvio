import time

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/utils6.py" )

if len(sys.argv) != 1:
        print time.strftime("[%d/%m %H:%M:%S]") + " [ERROR] (BUSProcessOperations.py): Syntax: wsadmin -lang jython -f BUSProcessOperations.py <operation>"
        sys.exit()

operation=sys.argv[0]
if(operation != 'stop' and operation != 'start'):
	print time.strftime("[%d/%m %H:%M:%S]") + " [ERROR] (BUSProcessOperations.py): The operation parameter must be \"stop\" or \"start\""
	sys.exit(1)

WPSMember1 = ""
WPSMember2 = ""
MEMember1 = ""
MEMember2 = ""
SupportMember1 = ""
SupportMember2 = ""

def findServers():
        global WPSMember1
        global WPSMember2
        global MEMember1
	global MEMember2
	global SupportMember1
	global SupportMember2
        servers = AdminConfig.list("Server").split(java.lang.System.getProperty('line.separator'))
        for server in servers:
                serverName = AdminConfig.showAttribute(server, "name")
                if(serverName.find("WPS") >= 0 and serverName.find("01") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found WPSMember 1: " + serverName
                        WPSMember1 = serverName
                if(serverName.find("WPS") >= 0 and serverName.find("02") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found WPSMember 2: " + serverName
                        WPSMember2 = serverName
                if(serverName.find("ME") >= 0 and serverName.find("01") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found MEMember 1: " + serverName
                        MEMember1 = serverName
                if(serverName.find("ME") >= 0 and serverName.find("02") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found MEMember 2: " + serverName
                        MEMember2 = serverName
                if(serverName.find("Support") >= 0 and serverName.find("01") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found SupportMember 1: " + serverName
                        SupportMember1 = serverName
                if(serverName.find("Support") >= 0 and serverName.find("02") >= 0):
                        print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Found SupportMember 2: " + serverName
                        SupportMember2 = serverName                                                                                        
#endDef

def findNodeName():
	nodes = AdminConfig.list("Node").split(java.lang.System.getProperty('line.separator'))
	for node in nodes:
        	nodeName = AdminConfig.showAttribute(node, "name")
		if(nodeName.find("Node01") >= 0):
			print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Using node name: " + nodeName
			return nodeName
	print time.strftime("[%d/%m %H:%M:%S]") + " [FATAL] Did not find a node ending with Node01. Bailing out..."
	sys.exit(1)
#endDef

def doServerOperation ( appServer, operation ):
	nodeName = findNodeName();
	if(operation == 'stop'):
		waitingForState = 'STOPPED'
		stopServer ( nodeName , appServer )
	if(operation == 'start'):
		waitingForState = 'STARTED'
		startServer ( nodeName, appServer)

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
		doServerOperation( WPSMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + WPSMember2
		doServerOperation( WPSMember2, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember1
		doServerOperation( SupportMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember2
		doServerOperation( SupportMember2, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember1
		doServerOperation( MEMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember2
		doServerOperation( MEMember2, operation)			
	else:
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + WPSMember1
		doServerOperation( WPSMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + SupportMember1
		doServerOperation( SupportMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Stopping application server: " + MEMember1
		doServerOperation( MEMember1, operation)

if(operation == 'start'):
	print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Start is initiated for BUS"
	if(cluster == "true"):
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember1
		doServerOperation( MEMember1, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember2
		doServerOperation( MEMember2, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Waiting for all message engines to start"
		waitForMessagingEnginesStarted()	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember1
		doServerOperation( SupportMember1, operation)	
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember2
		doServerOperation( SupportMember2, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember1
		doServerOperation( WPSMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember2
		doServerOperation( WPSMember2, operation)			
	else:
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + MEMember1
		doServerOperation( MEMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Waiting for all message engines to start"
		waitForMessagingEnginesStarted()
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + SupportMember1
		doServerOperation( SupportMember1, operation)
		print time.strftime("[%d/%m %H:%M:%S]") + "[INFO] Starting application server: " + WPSMember1
		doServerOperation( WPSMember1, operation)
