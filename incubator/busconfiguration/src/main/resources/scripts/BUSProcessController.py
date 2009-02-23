execfile( WSADMIN_SCRIPTS_HOME+"/scripts/utils6.py" )

if len(sys.argv) != 2:
        print("[ERROR] (BUSProcessOperations.py): Syntax: wsadmin -lang jython -f BUSProcessOperations.py <operation> <for cluster?>")
        sys.exit()

operation=sys.argv[0]
cluster=sys.argv[1]

def findEnvPrefixName():
        servers = AdminConfig.list("Server").split(java.lang.System.getProperty('line.separator'))
        for server in servers:
                serverName = AdminConfig.showAttribute(server, "name")
                tokens = serverName.split("_")
                if(len(tokens) == 2):
                        print "[INFO] Using environment prefix name : " + tokens[0]
                        return tokens[0]
#endDef

def findNodeName():
        nodes = AdminConfig.list("Node").split(java.lang.System.getProperty('line.separator'))
        for node in nodes:
                nodeName = AdminConfig.showAttribute(node, "name")
                if(nodeName.find("Node01") >= 0):
                        print "[INFO] Using node name: " + nodeName
                        return nodeName
        print("[FATAL] Did not find a node ending with Node01. Bailing out...")
        sys.exit(1)
#endDef

def findCellName():
        cells = AdminConfig.list("Node").split(java.lang.System.getProperty('line.separator'))

        if(len(cells) > 1):
                print "[FATAL] More than one cells found. Bailing out..."
                sys.exit(1)

        for cell in cells:
                cellName = AdminConfig.showAttribute(node, "name")
                if(cellName.find("Cell") >= 0):
                        print "[INFO] Using cell name: " + cellName
                        return cellName
#endDef

def doClusterOperation ( clusterName, operation ):
        cellName = findCellName()
        if(operation == 'stop'):
                waitingForState = 'stopped'
                stopCluster ( cellName, clusterName )
        if(operation == 'start'):
                waitingForState = 'started'
                startCluster ( cellName, clusterName )

        while 1:
                cluster = AdminControl.completeObjectName("type=Cluster,name="+clusterName+",*")
                if(cluster != ""):
                        clusterState = AdminControl.getAttribute(cluster, "state")
                else:
                        clusterState = "stopped"

                if(clusterState.find(waitingForState) >=  0):
                        print "[INFO] State " + waitingForState + " is reached."
                        break

                print "[INFO] Cluster state is: " + clusterState + ". Waiting for this websphere stuff to be " + waitingForState + ". Sleeping 60 seconds"
                sleep(60)
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
                        print "[INFO] State " + waitingForState + " is reached."
                        break

                print "[INFO] Application server state is: " + serverState + ". Waiting for this websphere stuff to be " + waitingForState + ". Sleeping 60 seconds"
                sleep(60)
#endDef

def waitForMessagingEnginesStarted():
        engines = AdminControl.queryNames("type=SIBMessagingEngine,*").splitlines()
        while 1:
                sleep(60)
                for engine in engines:
                        started = AdminControl.invoke(engine, "isStarted")
                        if(started == 'false' ):
                                print "[INFO] All messaging engines are not started. Sleeping 60 seconds..."
                                break
                if(started == 'true' ):
                        print "[INFO] All messaging engines are started"
                        break
#endDef

envPrefixName=findEnvPrefixName()
print "[INFO] Setting environment prefix name to: " + envPrefixName
wpsClusterName=envPrefixName + "_WPSCluster"
print "[INFO] Setting WPS cluster name to: " + wpsClusterName
meClusterName=envPrefixName + "_MECluster"
print "[INFO] Setting ME cluster name to: " + meClusterName
supportClusterName=envPrefixName + "_SupportCluster"
print "[INFO] Setting Support cluster name to: " + supportClusterName

if(operation == 'stop'):
        print "Stop is initiated for BUS"
        if(cluster == "true"):
                print "[INFO] Stopping cluster: " + wpsClusterName
                doClusterOperation( wpsClusterName, operation)
                print "[INFO] Stopping cluster: " + supportClusterName
                doClusterOperation( supportClusterName, operation)
                print "[INFO] Stopping cluster: " + meClusterName
                doClusterOperation(  meClusterName, operation)
        else:
                print "[INFO] Stopping application server: " + wpsClusterName + "Member01"
                doServerOperation( wpsClusterName + "Member01", operation)
                print "[INFO] Stopping application server: " + supportClusterName + "Member01"
                doServerOperation( supportClusterName + "Member01", operation)
                print "[INFO] Stopping application server: " + meClusterName + "Member01"
                doServerOperation( meClusterName + "Member01", operation)

if(operation == 'start'):
        print "Start is initiated for BUS"
        if(cluster == "true"):
                print "[INFO] Starting cluster: " + meClusterName
                doClusterOperation( meClusterName, operation)
                print "[INFO] Waiting for all message engines to start"
                waitForMessagingEnginesStarted()
                print "[INFO] Starting cluster: " + supportClusterName
                doClusterOperation( supportClusterName, operation)
                print "[INFO] Starting cluster: " + wpsClusterName
                doClusterOperation( wpsClusterName, operation)
        else:
                print "[INFO] Starting application server: " + meClusterName  + "Member01"
                doServerOperation( meClusterName + "Member01", operation)
                print "[INFO] Waiting for all message engines to start"
                waitForMessagingEnginesStarted()
                print "[INFO] Starting application server: " + supportClusterName  + "Member01"
                doServerOperation( supportClusterName + "Member01", operation)
                print "[INFO] Starting application server: " + wpsClusterName  + "Member01"
                doServerOperation( wpsClusterName + "Member01", operation)
