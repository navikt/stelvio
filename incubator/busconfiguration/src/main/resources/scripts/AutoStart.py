if len(sys.argv) != 2:
        print("ERROR (AutoStart.py): Syntax: wsadmin -lang jython -f AutoStart.py <Application name> <true/false>")
        sys.exit()

APPLICATION = sys.argv[0]
AUTOSTART = sys.argv[1]

deployments=AdminConfig.getid('/Deployment:' + APPLICATION + '/')
deploymentObject=AdminConfig.showAttribute(deployments,'deployedObject')
targetMappings=AdminConfig.showAttribute(deploymentObject, 'targetMappings')
targetMappings=targetMappings[1:len(targetMappings)-1]
AdminConfig.modify(targetMappings, [["enable", AUTOSTART]] )
AdminConfig.save()