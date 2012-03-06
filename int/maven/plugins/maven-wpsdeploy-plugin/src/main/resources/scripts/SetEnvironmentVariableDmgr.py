import sys
import java

from lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

VARIABLE_NAME = sys.argv[1]
VARIABLE_VALUE = sys.argv[2]

from lib.environment import createWebSphereVariable

dmgrProps = AdminControl.queryNames("processType=DeploymentManager,*" ).split(',')

if (len(dmgrProps)>1):
	l.info("(setEnvironmentVariableOnDmgr): Is managed")
	dmgrName = "";
	for prop in dmgrProps:
		if (prop.startswith("node")):
			dmgrName = prop.split('=')
			dmgrName = dmgrName[1]
	l.info("(setEnvironmentVariableOnDmgr): Deployment manager is "+dmgrName+".")
	createWebSphereVariable("node", dmgrName, "", VARIABLE_NAME, VARIABLE_VALUE)
else:
	nodeList = AdminTask.listNodes().splitlines()
	node = nodeList[0]
	
	l.info("(setEnvironmentVariableOnDmgr): Is unmanaged")
	l.info("(setEnvironmentVariableOnDmgr): Node is "+node+".")
	createWebSphereVariable("node", node, "", VARIABLE_NAME, VARIABLE_VALUE)

l.info("(setEnvironmentVariableOnDmgr): Saving configuration")
save()