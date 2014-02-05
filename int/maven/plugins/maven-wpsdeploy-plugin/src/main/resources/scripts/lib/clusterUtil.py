import re
from time import sleep
import lib.logUtil
import lib.exceptionUtil
from lib.IBM.wsadminlib import nodeIsDmgr
from lib.stringUtil import strip

log = lib.logUtil.getLogger(__name__)

GET_NAME_REGEX = re.compile('[^\(]+')
GET_NODE_NAME_REGEX = re.compile('(?<=node=)[^,]+')
IS_APPTARGET_REGEX = re.compile("AppTarget")
IS_MESSAGING_REGEX = re.compile("Messaging")
IS_SUPPORT_REGEX = re.compile("Support")

CLUSTER_STOP_OPERATION = "stop"
CLUSTER_STOPPED_STATE = "websphere.cluster.stopped"
SERVER_STOPPED_STATE = "STOPPED"

CLUSTER_START_OPERATION = "start"
CLUSTER_STARTED_STATE = "websphere.cluster.running"
SERVER_STARTED_STATE = "STARTED"

SECONDS_TO_RETRY = 900  # 900 seconds is 15 minutes
SECONDS_BETWEEN_RECHECKS = 10
SECONDS_BETWEEN_RETRIES = 600  # 600 seconds is 10 minutes

def startCluster(cellName, clusterRef):
	doClusterOperation(cellName, clusterRef, CLUSTER_START_OPERATION, CLUSTER_STARTED_STATE, SERVER_STARTED_STATE)

def stopCluster(cellName, clusterRef):
	doClusterOperation(cellName, clusterRef, CLUSTER_STOP_OPERATION, CLUSTER_STOPPED_STATE, SERVER_STOPPED_STATE)

def doClusterOperation(cellName, clusterRef, clusterOperation, clusterEndState, serverEndState):
	log.info("Will wait for the cluster to %s for %s seconds and retry the operation after %s seconds." % (clusterOperation, SECONDS_TO_RETRY, SECONDS_BETWEEN_RETRIES))
	clusterName = getStuffBeforeParentheses(clusterRef)
	resolvedCluster = resolveCluster(cellName, clusterName)

	invokeClusterOperation(resolvedCluster, clusterOperation)

	if isAllNodesActive():
		log.info("All nodes are running, checking individual servers if %sed!" % clusterOperation)
		__startStopCluster(resolvedCluster, clusterOperation, clusterName, clusterEndState)
	else:
		log.warning("Not all nodes are running, checking individual servers if %sed!" % clusterOperation)
		__startStopServer(resolvedCluster, clusterOperation, clusterRef, serverEndState)

def getStuffBeforeParentheses(resource):
	return GET_NAME_REGEX.match(resource).group(0)

def resolveCluster(cellName, clusterName):
	log.debug('AdminControl.completeObjectName("cell=%s,type=Cluster,name=%s,*")' % (cellName, clusterName))
	return AdminControl.completeObjectName("cell=%s,type=Cluster,name=%s,*" % (cellName, clusterName))

def invokeClusterOperation(resolvedCluster, clusterOperation):
	log.debug('AdminControl.invoke("%s", "%s")' % (resolvedCluster, clusterOperation))
	AdminControl.invoke(resolvedCluster, clusterOperation)

def __startStopCluster(resolvedCluster, clusterOperation, clusterName, clusterEndState):
	for i in xrange(1, SECONDS_TO_RETRY):
		if i % SECONDS_BETWEEN_RECHECKS == 0:
			clusterState = getClusterState(resolvedCluster)
			if clusterEndState == clusterState:
				log.info("The cluster is now %sed and has state [%s]" % (clusterOperation, clusterState))
				break
			elif i % SECONDS_BETWEEN_RETRIES == 0:
				retryClusterOperation(resolvedCluster, clusterOperation)
			log.info("The cluster %s is still in state [%s], sleeping for %s seconds." % (
				clusterName, clusterState, SECONDS_BETWEEN_RECHECKS))
		sleep(1)

def __startStopServer(resolvedCluster, clusterOperation, clusterRef, serverEndState):
	activeNodes = getRunningNodes()
	for nodeMember in getClusterMembers(clusterRef):
		serverName = getServerName(nodeMember)
		if isOnActiveNode(activeNodes, nodeMember):
			for i in xrange(1, SECONDS_TO_RETRY):
				if i % SECONDS_BETWEEN_RECHECKS == 0:
					serverState = getServerState(serverName)
					if serverEndState == serverState:
						log.info("The server is now %sed and has state [%s]" % (clusterOperation, serverState))
						break
					elif i % SECONDS_BETWEEN_RETRIES == 0:
						retryClusterOperation(resolvedCluster, clusterOperation)
					log.info("The server %s is still in state [%s], sleeping for %s seconds." % (
						serverName, serverState, SECONDS_BETWEEN_RECHECKS))
				sleep(1)

def isAllNodesActive():
	active_nodes = getRunningNodes()
	for node in getAllNodes():
		if not nodeIsDmgr(node):
			if not node in active_nodes:
				return False

	return True

def getRunningNodes():
	nodes = AdminControl.queryNames("type=NodeAgent,*").splitlines()
	return [getNodeName(node) for node in nodes]

def getNodeName(node):
	return GET_NODE_NAME_REGEX.search(node).group(0)

def getAllNodes():
	return [getStuffBeforeParentheses(node) for node in AdminConfig.list('Node').splitlines()]

def getClusterState(clusterRef):
	return __getState(clusterRef)

def __getState(ref):
	log.debug('__getState(): AdminControl.getAttribute("%s", "state")' % ref)
	return AdminControl.getAttribute(ref, "state")

def getServerName(nodeMember):
	log.debug('getServerName(): AdminConfig.showAttribute("%s", "memberName")' % nodeMember)
	return AdminConfig.showAttribute(nodeMember, "memberName")

def getServerState(serverName):
	serverRef = getServerRef(serverName)
	if not serverRef:
		log.debug("getServerState(): There was no serverRef, assuming the server is stopped, returning state:", SERVER_STOPPED_STATE)
		return SERVER_STOPPED_STATE
	return __getState(serverRef)

def getServerRef(serverName):
	log.debug('getServerRef(): AdminControl.completeObjectName("type=Server,name=%s,*")' % serverName)
	return AdminControl.completeObjectName("type=Server,name=%s,*" % serverName)

def getClusterMembers(clusterName):
	log.debug('getClusterMembers(): AdminConfig.showAttribute("%s", "members")' % clusterName)
	return [stripBrackets(x) for x in AdminConfig.showAttribute(clusterName, "members").split(' ')]

def stripBrackets(string):
	return strip(string, '[]')

def isOnActiveNode(activeNodes, nodeMember):
	log.debug('isOnActiveNode(): AdminConfig.showAttribute("%s", "nodeName")' % nodeMember)
	nodeName = AdminConfig.showAttribute(nodeMember, "nodeName")
	return nodeName in activeNodes

def retryClusterOperation(resolvedCluster, clusterOperation):
	log.info("Retrying the %s operation!" % clusterOperation)
	try:
		invokeClusterOperation(resolvedCluster, clusterOperation)
	except:
		log.info("Retrying resulted in an exception. Won't retry for another %s seconds" % SECONDS_BETWEEN_RETRIES)

def getCell():
	cellRef = AdminConfig.list("Cell")
	cellName = getStuffBeforeParentheses(cellRef)
	return cellName, cellRef

def getClusterRefs():
	clusters = AdminConfig.list("ServerCluster").splitlines()
	for cluster in clusters:
		if IS_APPTARGET_REGEX.search(cluster):
			appTargetRef = cluster
		elif IS_MESSAGING_REGEX.search(cluster):
			messagingRef = cluster
		elif IS_SUPPORT_REGEX.search(cluster):
			supportRef = cluster
		else:
			log.error("Could not identify cluster:", cluster)
	return [appTargetRef, messagingRef, supportRef]

