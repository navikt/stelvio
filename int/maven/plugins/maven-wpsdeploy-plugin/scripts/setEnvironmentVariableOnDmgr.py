import sys
import java

from types import StringType

VARIABLE_NAME = sys.argv[0]
VARIABLE_VALUE = sys.argv[1]
WSADMIN_SCRIPTS_HOME	 = sys.argv[2]
WSADMIN_SCRIPTS_HOME      = WSADMIN_SCRIPTS_HOME.replace('\t','\\t')

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/utils6.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/environment.py" )

dmgrProps = AdminControl.queryNames("processType=DeploymentManager,*" ).split(',')

if (len(dmgrProps)>1):
	print ("INFO (setEnvironmentVariableOnDmgr): Is managed")
	dmgrName = "";
	for prop in dmgrProps:
		if (prop.startswith("node")):
			dmgrName = prop.split('=')
			dmgrName = dmgrName[1]
		#endIf
	#endFor
	print "INFO (setEnvironmentVariableOnDmgr): Deployment manager is "+dmgrName+"."
	createWebSphereVariable ("node", dmgrName, "", VARIABLE_NAME, VARIABLE_VALUE)
else:
	lineSeparator = java.lang.System.getProperty('line.separator')
	nodeList = AdminTask.listNodes().split(lineSeparator)
	node = nodeList[0]
	
	print "INFO (setEnvironmentVariableOnDmgr): Is unmanaged"
	print "INFO (setEnvironmentVariableOnDmgr): Node is "+node+"."
	createWebSphereVariable ("node", node, "", VARIABLE_NAME, VARIABLE_VALUE)
#endElse

print "\nINFO (setEnvironmentVariableOnDmgr): Saving configuration"
AdminConfig.save()