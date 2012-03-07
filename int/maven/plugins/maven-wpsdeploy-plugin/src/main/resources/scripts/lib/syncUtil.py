import re, time
from lib.saveUtil import save
import lib.exceptionUtil as ex
import lib.logUtil as log
l = log.getLogger(__name__)

# Define False, True
False, True = 0,1

def sync(pollTimer=10, retriesBeforeResync=30):
	l.info("Synchronizing and checking if the node is synchronized every %s seconds, and resyncronizing after every %s times the check commes back as not synchronized." % (pollTimer, retriesBeforeResync))
	save()
	nodes = getNodes()
	l.info("Synchronizing %s..." % ' and '.join(nodes))
	for node in nodes:
		syncNode(node)
		counter = 0
		while not isSync(node):
				l.info("%s not synchronized, checking again in %s seconds..." % (node, pollTimer))
				counter += 1
				if (counter % retriesBeforeResync) == 0: 
					l.info("Synchronizing node %s again..." % node)
					syncNode(node)
				else:
					time.sleep(pollTimer)
		l.info("%s is now synchronized!" % node)

def syncNode(node):
	DeploymentManager = getDeployentManagerDict()
	cmd = 'WebSphere:name=cellSync,process=dmgr,platform=common,node=%(node)s,version=%(version)s,type=CellSync,mbeanIdentifier=cellSync,cell=%(cell)s,spec=%(spec)s' % DeploymentManager
	isSynchronized = AdminControl.invoke(cmd, 'syncNode', '['+node+']', '[java.lang.String]')
	return isSynchronized == "true"

def isSync(node, retries = 5):
	l.debug("isSync(%s)"%node)
	for i in xrange(1, retries+1):
		try:
			l.debug('AdminControl.completeObjectName("type=NodeSync,node=" + '+node+' + ",*")')
			nodeSync = AdminControl.completeObjectName("type=NodeSync,node=" + node + ",*")
			l.debug('AdminControl.invoke("'+nodeSync+'", "isNodeSynchronized")')
			synched = AdminControl.invoke(nodeSync, "isNodeSynchronized")
			l.debug("isSync(%s)"%node, "method is finished!")
			return synched == 'true'
		except:
			l.warning('Encountered an exception while checking if node(%s) was synchronized!'%node)
			l.debug(ex.getException())
			l.info('Retrying %s more time(s)...'% (retries-i))
	else:
		l.error('Exceeded the maximum number for allowed retries!')
	
def getDeployentManagerDict():
	string = AdminControl.completeObjectName("type=DeploymentManager,process=dmgr,*" )
	dictionary = {}
	for cutOut in string.split(','):
		key, value = cutOut.split('=')
		dictionary[key] = value
	return dictionary
	
def getNodes():
	nodes = []
	nodeREGEX = re.compile('Node')
	lines = AdminTask.listNodes().splitlines()
	for line in lines:
		if nodeREGEX.search(line):
			nodes.append(line)
	return nodes