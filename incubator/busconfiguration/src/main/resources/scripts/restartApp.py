execfile("./Applications.py")

#******************************************************************************
# Method Name:   	stopApp
# Description:	Stops deployed application 
#
#****************************************************************************** 
def stopApp(appName):
       	global AdminApp, AdminConfig, AdminControl
        
        readProperties( APP_PROPS_HOME + whereIsProperties )
	scope			=	getProperty("SCOPE")
	clusterName	 	=	getProperty("CLUSTER")
	serverName	  	=	getProperty("SERVER")
	nodeName		= 	getProperty("NODE")
	print "Stopping " + appName + "..."
        if(scope == "cluster"):
		scopeId = findScopeEntry(scope, clusterName)
		if (scopeId == 0):
			print "ERROR (stopApp): Unable to find "+scope 
			return
		#endIf
        	
		clusterMembers = AdminConfig.list("ClusterMember",scopeId )
		clusterMemberList = clusterMembers.split(lineSeparator)
				
		for clusterMember in clusterMemberList:
			server = clusterMember.rstrip()
			serverName = AdminConfig.showAttribute( server, "memberName")
			nodeName = AdminConfig.showAttribute( server, "nodeName")
			
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (stopApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to stop application " +  appName
				continue	
			#print "Stoping Applicaiton "+appName+" on Cluster Member "+serverName
			
			rc = appControl("stopApplication", appName, serverName)
			
			if (rc):
				print "WARNING (stopApp): Stop Application "+appName+" failed on Cluster Member "+serverName
			else:
				print "INFO (stopApp):Stop Application "+appName+" was successful on Cluster Member "+serverName
			#endIf

	elif(scope == "server" or scope == "node"):
		if(scope == "server"):
			scopeId = findScopeEntry(scope, serverName)
		elif(scope == "node"):
			scopeId = findScopeEntry(scope, nodeName)
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (stopApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to stop application " +  appName
				return
		if (scopeId == 0):
			print "ERROR (stopApp): Unable to find "+scope 
			return
		#endIf
		#print "Stoping Applicaiton "+appName+" on Server "+scopeName
		
		rc = appControl("stopApplication", appName, serverName)
		if (rc):
			print "WARNING (stopApp): Stop Application "+appName+" failed."
		else:
			print "INFO (stopApp): Stop Application "+appName+" was successful."
		#endIf
	else:
		print "ERROR (stopApp): Method does not support the scope: "+scope 
		return

#endDef

#******************************************************************************
# Method Name:   	startApp
# Description:		Starts deployed application 
#
#****************************************************************************** 
def startApp(appName):
       	global AdminApp, AdminConfig, AdminControl
        readProperties( APP_PROPS_HOME + whereIsProperties )
	scope			=	getProperty("SCOPE")
	clusterName	 	=	getProperty("CLUSTER")
	serverName	  	=	getProperty("SERVER")
	nodeName		= 	getProperty("NODE")
	print "Starting " + appName + "..."
        if(scope == "cluster"):
		scopeId = findScopeEntry(scope, clusterName)
		if (scopeId == 0):
			print "ERROR (startApp): Unable to find "+scope 
			return
		#endIf
        	
		clusterMembers = AdminConfig.list("ClusterMember",scopeId )
		clusterMemberList = clusterMembers.split(lineSeparator)

		for clusterMember in clusterMemberList:
			server = clusterMember.rstrip()
			serverName = AdminConfig.showAttribute( server, "memberName")
			nodeName = AdminConfig.showAttribute( server, "nodeName")
			
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (startApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to start application " +  appName
				continue
			#print "Starting Applicaiton "+appName+" on Cluster Member "+serverName
			rc = appControl("startApplication", appName, serverName)
			if (rc):
				print "INFO (startApp): Start Application "+appName+" failed on Cluster Member" + serverName
			else:
				print "INFO (startApp): Start Application "+appName+" was successful Cluster Member " + serverName
			#endIf

	elif(scope == "server" or scope == "node"):
		if(scope == "server"):
			scopeId = findScopeEntry(scope, serverName)
		elif(scope == "node"):
			scopeId = findScopeEntry(scope, nodeName)
			if(checkServerStopped(nodeName,serverName)):
				print "INFO (startApp): Application Server is stopped on NODE="+nodeName+" SERVER " + serverName + ". Not able to start application " +  appName
				return
		if (scopeId == 0):
			print "ERROR (startApp):Unable to find "+scope 
			return
		#endIf
		#print "Starting Applicaiton "+appName+" on Server "+scopeName
		rc = appControl("startApplication", appName, serverName)
		if (rc):
			print "WARNING (startApp): Start Application "+appName+" failed."
		else:
			print "INFO (startpApp): Start Application "+appName+" was successful."
		#endIf
	else:
		print "ERROR (startApp): Method does not support the scope: "+scope 
		return

#endDef

#MAIN
APPLICATION_NAME 	 = sys.argv[0]

stopApp(APPLICATION_NAME)
startApp(APPLICATION_NAME)

