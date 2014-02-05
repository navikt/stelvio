import sys
from lib.IBM.environment import createWebSphereVariable
from lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

argumentStrings = sys.argv[1].split(',')


def main():
	dmgrProps = AdminControl.queryNames("processType=DeploymentManager,*" ).split(',')
	
	for variableName, variableValue in [x.split('=') for x in argumentStrings]:
		if (len(dmgrProps)>1):
			l.info("(setEnvironmentVariableOnDmgr): Is managed")
			dmgrName = "";
			for prop in dmgrProps:
				if (prop.startswith("node")):
					dmgrName = prop.split('=')
					dmgrName = dmgrName[1]
			l.info("(setEnvironmentVariableOnDmgr): Deployment manager is "+dmgrName+".")
			createWebSphereVariable("node", dmgrName, "", variableName, variableValue)
		else:
			nodeList = AdminTask.listNodes().splitlines()
			node = nodeList[0]
			
			l.info("(setEnvironmentVariableOnDmgr): Is unmanaged")
			l.info("(setEnvironmentVariableOnDmgr): Node is "+node+".")
			createWebSphereVariable("node", node, "", variableName, variableValue)

		l.info("(setEnvironmentVariableOnDmgr): Saving configuration")
		
	save()
	
if __name__ == '__main__': main()