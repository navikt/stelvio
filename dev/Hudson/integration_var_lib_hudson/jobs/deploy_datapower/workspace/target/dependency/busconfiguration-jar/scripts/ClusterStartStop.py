import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs

import time
from lib.adminUtil import sync

OPERATION				= sys.argv[1]
WSADMIN_SCRIPTS_HOME	= sys.argv[0]
WSADMIN_SCRIPTS_HOME	= WSADMIN_SCRIPTS_HOME.replace('\t','\\t')

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/Log.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/Utils.py" )

if len(sys.argv) != 2:
	log("ERROR", "Syntax: wsadmin -lang jython -f <scriptName>.py <scripts_home> <operation>")
	sys.exit()

def waitForMessagingEnginesStarted():
	engines = AdminControl.queryNames("type=SIBMessagingEngine,*").splitlines()
	
	count = len(engines)
	i = 0
	max_failed = 1
	
	while i < count:
		if (max_failed == 10):
			sys.exit(105)
		started = 'false'
		engine = engines[i]
		
		try:
			started = AdminControl.invoke(engine, "isStarted")
		except:
			max_failed = max_failed + 1
			log("WARNING", "Could not reach messaging engine, trying again...")
			log("WARNING", "Return value is ["+started+"]")
			log("WARNING", "Retry #"+str(max_failed))
		
		if (started == 'true'):
			log("INFO", str(re.split(',', re.split('\=', engine)[1])[0] + ' is started'))
			engines.pop(i)
			count=len(engines)
		else:
			log("INFO", 'Waiting for ' + str(re.split(',', re.split('\=', engine)[1])[0] + ' to be started'))
			sleep(10)
						
		i = i+1
		if(i==count and i!=0):
			i=0
	#endWhile
	log("INFO", "All messaging engines are started")
#endDef

def allNodesActive():
	nodes = AdminControl.queryNames("type=NodeAgent,*").split(java.lang.System.getProperty('line.separator'))
	active_nodes = []
	for node in nodes:
		active_nodes.append(node.split(',node=')[1].split(',')[0])
		
		
	nodes = AdminConfig.list('Node').split(java.lang.System.getProperty('line.separator'))
	all_nodes_in_cell = []
	for node in nodes:
		# Should use getDmgrNode from wsadminlib.py
		if (node.count('Manager') == 0):
			all_nodes_in_cell.append(node.split('(')[0])
			if (active_nodes.count(node.split('(')[0]) == 0):
				return 1
	
	return 0
#endDef

def getRunningNodes():
	nodes = AdminControl.queryNames("type=NodeAgent,*").split(java.lang.System.getProperty('line.separator'))
	active_nodes = []
	for node in nodes:
		active_nodes.append(node.split(',node=')[1].split(',')[0])
	return active_nodes
#endDef

def getClusterState(cluster):
	return AdminControl.getAttribute(cluster, 'state')
#endDef

def doClusterOperation(cellName, clusterName, operation):
	
	cluster = AdminControl.completeObjectName('cell='+cellName+',type=Cluster,name='+re.split('\(', clusterName)[0]+',*')
	
	try:
		_excp_ = 0
		result = AdminControl.invoke(cluster, operation )
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		log("WARNING", "Caught Exception during cluster operation")
		log("WARNING", result)
		return 1
	else:
		#endIf
		if (allNodesActive() == 0):
			if (operation == 'start'):
				waiting_for_state = "websphere.cluster.running"
			elif (operation == 'stop'):
				waiting_for_state = "websphere.cluster.stopped"
			#endIf
			while 1:
				current_state = getClusterState(cluster)
				if(current_state == waiting_for_state):
					log("INFO", re.split('\(', clusterName)[0] + " is in state: [" + current_state + "].")
					break
				else:
					log("INFO", "Waiting for " + re.split('\(', clusterName)[0] + " to " + operation + ". Current state is: [" + current_state + "].")
					sleep(10)
		else:
			if (operation == 'start'):
				waiting_for_state = "STARTED"
			elif (operation == 'stop'):
				waiting_for_state = "STOPPED"
			#endIf
			log("INFO", "Not all nodes are running, we are entering the Matrix.")
			members = AdminConfig.showAttribute(clusterName, "members").split(java.lang.System.getProperty(' '))
			active_nodes = getRunningNodes()
			
			for member in members:
				member=member.replace('[','')
				member=member.replace(']','')
				serverName = AdminConfig.showAttribute(member, "memberName")
				nodeName = AdminConfig.showAttribute(member, "nodeName")
				if (active_nodes.count(nodeName) > 0):
					while 1:
						serverState = 'STOPPED'
						server  = AdminControl.completeObjectName("type=Server,name="+serverName+",*")
						if(server != ""):
							serverState = AdminControl.getAttribute(server, 'state')
						if (serverState == waiting_for_state):
							log("INFO", serverName + " is in state: [" + serverState + "].")
							break
						else:
							log("INFO", "Waiting for " + serverName + " to " + operation + ". Current state is: [" + serverState + "].")
							sleep(10)
			#endIf
			
			
		#endWhile
	#endIf
#endDef 



def main():
	start = time.clock()

	appTarget = ""
	messaging = ""
	support = ""

	cell = AdminConfig.list("Cell")

	clusters = AdminConfig.list("ServerCluster").split(java.lang.System.getProperty('line.separator'))
	for cluster in clusters:
		if (cluster.find("AppTarget") >= 0 or cluster.find("WPSCluster") >= 0):
			appTarget = cluster
		elif (cluster.find("Messaging") >= 0 or cluster.find("MECluster") >= 0):
			messaging = cluster
		elif (cluster.find("Support") >= 0):
			support = cluster
		else:
			log("ERROR", "Could not identify " + cluster + ".")
			
	if (OPERATION == "start"):
		log("INFO", "Starting cluster " + re.split('\(', messaging)[0])
		result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		while (result_me == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		waitForMessagingEnginesStarted()
		
		log("INFO", "Starting cluster " + re.split('\(', support)[0])
		result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		while (result_support == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
			
		log("INFO", "Starting cluster " + re.split('\(', appTarget)[0])
		result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)	
		while (result_app == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
			
		log("INFO", "All clusters are started")
		
	elif (OPERATION == "stop"):
		log("INFO", "Stopping cluster " + re.split('\(', appTarget)[0])
		result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
		while (result_app == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
		
		log("INFO", "Stopping cluster " + re.split('\(', support)[0])
		result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		while (result_support == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		
		log("INFO", "Stopping cluster " + re.split('\(', messaging)[0])
		result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		while (result_me == 1):
			sleep(10)
			log("INFO", "Exception detected. Retrying...")
			result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		
		log("INFO", "All clusters are stopped")
		
	elif (OPERATION == "synch"):
		sync()
	else:
		log("ERROR", "Bad operation: [" + OPERATION + "]")
	#endIf	
	stop = time.clock()
	log("INFO", "Time elapsed: " + str(round(stop - start, 2)) + " seconds.")
#endDef


main()

