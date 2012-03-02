import sys, re
from lib.syncUtil import sync
import lib.logUtil as log
from lib.timeUtil import Timer

l = log.getLogger(__name__)
OPERATION = sys.argv[1]

if len(sys.argv) != 2:
	l.error("Syntax: wsadmin -lang jython -f <scriptName>.py <scripts_home> <operation>")
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
			l.warning(""Could not reach messaging engine, trying again...")
			l.warning(""Return value is ["+started+"]")
			l.warning(""Retry #"+str(max_failed))
		
		if (started == 'true'):
			l.info(str(re.split(',', re.split('\=', engine)[1])[0] + ' is started'))
			engines.pop(i)
			count=len(engines)
		else:
			l.info('Waiting for ' + str(re.split(',', re.split('\=', engine)[1])[0] + ' to be started'))
			sleep(10)
						
		i = i+1
		if(i==count and i!=0):
			i=0
	l.info("All messaging engines are started")

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
				return True
	
	return False
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
		result = AdminControl.invoke(cluster, operation )
	except:
		l.warning("Caught Exception during cluster operation. Retrying...")
		return 1
	else:
		if (allNodesActive() == 0):
			if (operation == 'start'):
				waiting_for_state = "websphere.cluster.running"
			elif (operation == 'stop'):
				waiting_for_state = "websphere.cluster.stopped"

			while 1:
				current_state = getClusterState(cluster)
				if(current_state == waiting_for_state):
					l.info(re.split('\(', clusterName)[0] + " is in state: [" + current_state + "].")
					break
				else:
					l.info("Waiting for " + re.split('\(', clusterName)[0] + " to " + operation + ". Current state is: [" + current_state + "].")
					sleep(10)
		else:
			if (operation == 'start'):
				waiting_for_state = "STARTED"
			elif (operation == 'stop'):
				waiting_for_state = "STOPPED"

			l.info("Not all nodes are running, we are entering the Matrix.")
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
							l.info(serverName + " is in state: [" + serverState + "].")
							break
						else:
							l.info("Waiting for " + serverName + " to " + operation + ". Current state is: [" + serverState + "].")
							sleep(10)

def main():
	myTimer = Timer()

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
			l.error("Could not identify " + cluster + ".")
			
	if (OPERATION == "start"):
		l.info("Starting cluster " + re.split('\(', messaging)[0])
		result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		while (result_me == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		waitForMessagingEnginesStarted()
		
		l.info("Starting cluster " + re.split('\(', support)[0])
		result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		while (result_support == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
			
		l.info("Starting cluster " + re.split('\(', appTarget)[0])
		result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)	
		while (result_app == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
			
		l.info("All clusters are started")
		
	elif (OPERATION == "stop"):
		l.info("Stopping cluster " + re.split('\(', appTarget)[0])
		result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
		while (result_app == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_app = doClusterOperation(re.split('\(', cell)[0], appTarget, OPERATION)
		
		l.info("Stopping cluster " + re.split('\(', support)[0])
		result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		while (result_support == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_support = doClusterOperation(re.split('\(', cell)[0], support, OPERATION)
		
		l.info("Stopping cluster " + re.split('\(', messaging)[0])
		result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		while (result_me == 1):
			sleep(10)
			l.info("Exception detected. Retrying...")
			result_me = doClusterOperation(re.split('\(', cell)[0], messaging, OPERATION)
		
		l.info("All clusters are stopped")
		
	elif (OPERATION == "synch"):
		sync()
	else:
		l.error("Bad operation: [" + OPERATION + "]")

	l.info("Time elapsed:", myTimer)

main()

