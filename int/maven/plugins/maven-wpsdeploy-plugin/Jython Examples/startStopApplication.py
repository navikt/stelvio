
def stopApplication(scaModule):
	applicationInvoke(scaModule, 'stopApplication')

def startApplication(scaModule):
	applicationInvoke(scaModule, 'startApplication')

def applicationInvoke(scaModule, command):
	nodes = [getName(n) for n in getNodes()]
	for nodeName in nodes:
		appManager = getApplicationManager(nodeName)
		l.debug("AdminControl.invoke("+appManager+", "+command+", "+scaModule.applicationName+")")
		AdminControl.invoke(appManager, command, scaModule.applicationName)


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