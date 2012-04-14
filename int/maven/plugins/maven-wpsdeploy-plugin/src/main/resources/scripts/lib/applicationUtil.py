import re
import lib.logUtil as log

l = log.getLogger(__name__)
clusterName = ""

def uninstall(scaModule):
	l.info('Uninstalling %s!' % scaModule)
	AdminApp.uninstall(scaModule.applicationName)
	l.debug('Uninstall complete')
		
def install(scaModule, verbose=False):
	if verbose:
		options = "[ -verbose -cluster %s -distributeApp ]" % getClusterName()
	else:
		options = "[ -cluster %s -distributeApp ]" % getClusterName()

	l.info("Installing %s with options [%s] on %s." % (scaModule, options, getClusterName()))
	AdminApp.install(scaModule.earPath, options)
	l.debug('Installation complete')
	
def getClusterName():
	global clusterName
	if clusterName:
		return clusterName
	else:	
		clusters = AdminConfig.list('ServerCluster', AdminConfig.list('Cell')).splitlines()
		for cluster in clusters:
			if re.search('AppTarget', cluster):
				clusterName = AdminConfig.showAttribute(cluster, 'name')
		if not clusterName:
			raise Exception, "No cluster name found"
		else:
			return clusterName