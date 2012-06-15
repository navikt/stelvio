import re
import lib.logUtil as log

l = log.getLogger(__name__)
clusterName = ""

def uninstall(scaModule):
	l.info('Uninstalling %s!' % scaModule.shortName)
	AdminApp.uninstall(scaModule.applicationName)
	l.debug('Uninstall complete')
		
def install(scaModule, verbose=False):
	clusterName = getApplicationClusterName()
	if verbose:
		options = "[ -verbose -cluster %s -distributeApp ]" % clusterName
	else:
		options = "[ -cluster %s -distributeApp ]" % clusterName

	l.info("Installing %s (%s) with options [%s] on %s." % (scaModule.shortName, scaModule.version, options, clusterName))
	AdminApp.install(scaModule.earPath, options)
	l.debug('Installation complete')

def isApplicationClusterRunning():
	cellName = getName(getCell())
	cluster = AdminControl.completeObjectName('cell=%s,type=Cluster,name=%s,*' % (cellName, getApplicationClusterName()))
	status = AdminControl.getAttribute(cluster, 'state')
	return status == 'websphere.cluster.running'

def getApplicationClusterName():
	targetCluster = 'AppTarget'
	global clusterName
	if clusterName:
		return clusterName
	else:	
		clusters = AdminConfig.list('ServerCluster', getCell()).splitlines()
		for cluster in clusters:
			if re.search(targetCluster, cluster):
				return getName(cluster)
		else:
			raise Exception, targetCluster + " cluster not found!"

def getApplicationManager(nodeName):
	cellName = getName(getCell())
	serverName = getName(getAppServer(nodeName))
	return AdminControl.queryNames('cell=%s,node=%s,type=ApplicationManager,process=%s,*' % (cellName, nodeName, serverName))

def getCell():
	cell = AdminConfig.list('Cell')
	if '\n' in cell:
		raise ValueError('The deployscript only handles one cell!')
	return cell

def getName(configElement):
	return AdminConfig.showAttribute(configElement,'name')

def getNodes():
	cell = getCell()
	return [n for n in AdminConfig.list('Node',cell).splitlines() if not re.search('CellManager', n)]

def getAppServer(nodeName):
	for server in AdminConfig.getid('/Node:'+ nodeName +'/Server:/').splitlines():
		if re.search('AppTarget', server):
			return server

def isApplicationRunning(scaModule):
	status = AdminControl.completeObjectName('type=Application,name=%s,*' % scaModule.applicationName)
	if status:
		return True
	else:
		return False
		
def stopApplication(scaModule):
	applicationInvoke(scaModule, 'stopApplication')

def startApplication(scaModule):
	applicationInvoke(scaModule, 'startApplication')

def applicationInvoke(scaModule, command):
	nodes = [getName(n) for n in getNodes()]
	for nodeName in nodes:
		appManager = getApplicationManager(nodeName)
		l.debug("AdminControl.invoke('"+appManager+"', '"+command+"', '"+scaModule.applicationName+"')")
		AdminControl.invoke(appManager, command, scaModule.applicationName)
