###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	reports.py
# Description:	This file contains the following report procedures:
#		
#				showAllDataSources
#				showAllEnvVars
#				showAppState
#				showClusterInfo
#				showRunningStatus
#				showServerInfo
#
#
# History:		
#******************************************************************************
#******************************************************************************
# Procedure:  	showAllDataSources
# Description:	Show the DataSources defined at all scope levels
#****************************************************************************** 
def showAllDataSources ():

	print "\n=========================================================================="
	print "%-16s %-22s %-30s" % ("Scope","Scope Name","DataSource Name")
	print "%-16s %-22s %-30s" % ("-----","----------","---------------")

	# Cell Level
	cName = findCell(cellName)
	elements = AdminConfig.list("DataSource", cName)
	elementList = elements.split(lineSeparator)
	mCounter = 0
	for element in elementList:
		if (element.find(cellName+"|") >= 0):
			if (len(element) > 0 ):
				name = AdminConfig.showAttribute(element, "name")
				if (mCounter == 0):
					print "%-16s %-22s %-30s" % ("CELL",cellName,name)
					mCounter += 1
				else:
					print "%-16s %-22s %-30s" % ("","", name)
				#endIf
			#endIf
		#endIf
	#endFor

	# Cluster Level
	clusters = AdminConfig.list("ServerCluster", cName).split(lineSeparator)

	for cluster in clusters:
		clusterName = AdminConfig.showAttribute(cluster, "name")
		clName = findServerCluster(clusterName)
		elements = AdminConfig.list("DataSource", clName)
		elementList = elements.split(lineSeparator)
		mCounter = 0
		if (elementList != ['']):
			for element in elementList:
				if (element.find(clusterName+"|") >= 0):
					if (len(element) > 0 ):
						name= AdminConfig.showAttribute(element, "name")
						if (mCounter == 0):
							print "\n--------------------------------------------------------------------------"
					
							print "%-16s %-22s %-30s" % ("CLUSTER",clusterName, name)
							mCounter += 1
						else:
							print "%-16s %-22s %-30s" % ("","",name)
						#endIf
					#endIf
				#endIf
			#endFor
		# endIf
	#endFor

	# Node Level
	nodes = AdminConfig.list("Node", cName).split(lineSeparator)

	for node in nodes:
		nodeName = AdminConfig.showAttribute(node, "name")
		nName = findNode(nodeName)
		elements = AdminConfig.list("DataSource", nName)
		elementList = elements.split(lineSeparator)
		mCounter = 0
		if (elementList != ['']):
			for element in elementList:
				if (element.find(nodeName+"|") >= 0):
					if (len(element) > 0 ):
						name= AdminConfig.showAttribute(element, "name")
						if (mCounter == 0):
							print "\n--------------------------------------------------------------------------"
					
							print "%-16s %-22s %-30s" % ("NODE",nodeName, name)
							mCounter += 1
						else:
							print "%-16s %-22s %-30s" % ("","",name)
						#endIf
					#endIf
				#endIf
			#endFor
		# endIf

		# Server Level
		srvrs = AdminConfig.list("Server", nName).split(lineSeparator)
		for server in srvrs:
			serverName = AdminConfig.showAttribute(server, "name")
			sName = findServer(serverName)
			elements = AdminConfig.list("DataSource", sName)
			elementList = elements.split(lineSeparator)
			mCounter = 0
			if (elementList != ['']):
				for element in elementList:
					if (element.find(serverName+"|") >= 0):
						if (len(element) > 0 ):
							name = AdminConfig.showAttribute(element, "name")
							if (mCounter == 0):
							        print "\n"
								print "%-7s %-9s %-21s %-30s" % ("","SERVER", serverName, name)
								mCounter += 1
							else:
								print "%-16s %-22s %-30s" % ("","",name)
							#endIf
						# endIf
					#endIf
				#endFor	
			#endIf
		#endFor
	#endFor

	print "\n=========================================================================="
#endDef

#******************************************************************************
# Procedure:  	showAllEnvVars
# Description:	Show the WebSphere Environment Variables defined at all 
#			scope levels
#****************************************************************************** 
def showAllEnvVars ():

	print "\n==========================================================================================================================="
	print "%-16s %-22s %-40s %-35s" % ("Scope","Scope Name","Variable Name","Value")
	print "%-16s %-22s %-40s %-35s" % ("-----","----------","-------------","-----")

	# Cell Level
	cName = findCell(cellName)
	elements = AdminConfig.list("VariableSubstitutionEntry", cName)
	elementList = elements.split(lineSeparator)
	mCounter = 0
	for element in elementList:
		if (element.find(cellName+"|") >= 0):
			if (len(element) > 0 ):
				name = AdminConfig.showAttribute(element, "symbolicName")
				value = AdminConfig.showAttribute(element, "value")
				if (mCounter == 0):
					print "%-16s %-22s %-40s %-35s" % ("CELL",cellName,name, value)
					mCounter += 1
				else:
					print "%-16s %-22s %-40s %-35s" % ("","", name, value)
				#endIf
			#endIf
		#endIf
	#endFor


	# Cluster Level
	clusters  = AdminConfig.list("ServerCluster", cName).split(lineSeparator)
	for cluster in clusters:
		clusterName = AdminConfig.showAttribute(cluster, "name")
		clName = findServerCluster(clusterName)
		elements = AdminConfig.list("VariableSubstitutionEntry", clName)
		elementList = elements.split(lineSeparator)
		mCounter = 0
		if (elementList != ['']):
			for element in elementList:
				if (element.find(clusterName+"|") >= 0):
					if (len(element) > 0 ):
						name= AdminConfig.showAttribute(element, "symbolicName")
						value = AdminConfig.showAttribute(element, "value")
						if (mCounter == 0):
							print "\n---------------------------------------------------------------------------------------------------------------------------"
					
							print "%-16s %-22s %-40s %-35s" % ("CLUSTER",clusterName, name, value)
							mCounter += 1
						else:
							print "%-16s %-22s %-40s %-35s" % ("","",name, value)
						#endIf
					#endIf
				#endIf
			#endFor
		# endIf
	# endFor

	# Node Level
	nodes = AdminConfig.list("Node", cName).split(lineSeparator)

	for node in nodes:
		nodeName = AdminConfig.showAttribute(node, "name")
		nName = findNode(nodeName)
		elements = AdminConfig.list("VariableSubstitutionEntry", nName)
		elementList = elements.split(lineSeparator)
		mCounter = 0
		if (elementList != ['']):
			for element in elementList:
				if (element.find(nodeName+"|") >= 0):
					if (len(element) > 0 ):
						name= AdminConfig.showAttribute(element, "symbolicName")
						value = AdminConfig.showAttribute(element, "value")
						if (mCounter == 0):
							print "\n---------------------------------------------------------------------------------------------------------------------------"
					
							print "%-16s %-22s %-40s %-35s" % ("NODE",nodeName, name, value)
							mCounter += 1
						else:
							print "%-16s %-22s %-40s %-35s" % ("","",name, value)
						#endIf
					#endIf
				#endIf
			#endFor
		# endIf

		# Server Level
		srvrs = AdminConfig.list("Server", nName).split(lineSeparator)
		for server in srvrs:
			serverName = AdminConfig.showAttribute(server, "name")
			sName = findServer(serverName)
			elements = AdminConfig.list("VariableSubstitutionEntry", sName)
			elementList = elements.split(lineSeparator)
			mCounter = 0
			if (elementList != ['']):
				for element in elementList:
					if (element.find(serverName+"|") >= 0):
						if (len(element) > 0 ):
							name = AdminConfig.showAttribute(element, "symbolicName")
							value = AdminConfig.showAttribute(element, "value")
							if (mCounter == 0):
							        #print "\n"
								print "%-7s %-9s %-21s %-40s %-35s" % ("","SERVER", serverName, name, value)
								mCounter += 1
							else:
								print "%-16s %-22s %-40s %-35s" % ("","",name,value)
							#endIf
						# endIf
					#endIf
				#endFor	
			#endIf
		#endFor
	#endFor

	print "\n==========================================================================================================================="
#endDef

#******************************************************************************
# Procedure:  	showAppState
# Description:	Show the state of applications - enabled or disabled.
#****************************************************************************** 
def showAppState ():

	#-------------------------------------------------------------------------
	print "\n=================================="
	print "%-25s %-8s" % ("Application","State")
	print  "%-25s %-8s" % ("-----------","-----")

	# Get a list of the apps
	apps  = AdminApp.list().split(lineSeparator)
	if (apps == ['']):
		print "No Applications to report on."
		return
	#endIf

	for app in apps:
		app1 = AdminConfig.getid("/Deployment:"+app+"/")
		do  = AdminConfig.showAttribute(app1, 'deployedObject')
		tm  = AdminConfig.showAttribute(do, 'targetMappings')
		tm = tm [1:len(tm)-1].split(" ")
		status = AdminConfig.showAttribute(tm[0], "enable")
		if (status  == "true"):
			state = "ENABLED"
		else:
			state = "DISABLED"
		#endIf

		print "%-25s %-8s" % (app,state)
	#endFor

	print "\n=================================="

#endDef


#******************************************************************************
# Procedure:  	showClusterInfo
# Description:	Show the members of each cluster and their associated node, 
#			state, and weight.
#****************************************************************************** 
def showClusterInfo ():

	#------------------------------------------------------------------------------

	print "\n=========================================================================="
	print "%-16s %-16s %-16s %-16s %-8s" % ("Cluster Name","Member Name","Node Name","State","Weight")
	print "%-16s %-16s %-16s %-16s %-8s" % ("------------","-----------","---------","-----","------")

	cName  = findCell(cellName)
	cname  = AdminConfig.showAttribute(cName, "name")

	# Get a list of the server clusters
	clusters  = AdminConfig.list("ServerCluster", cName).split(lineSeparator)
	for cluster in clusters:
		clname  = AdminConfig.showAttribute(cluster, "name")
		memberlist = AdminConfig.showAttribute(cluster, "members")
		if (memberlist != "[]"):
			memberlist = memberlist [1:len(memberlist)-1].split(" ")
			mCounter = 0
			for member in memberlist:
				mCounter += 1
				mname = AdminConfig.showAttribute(member, "memberName")
				node = AdminConfig.showAttribute(member, "nodeName")
				weight = AdminConfig.showAttribute(member, "weight")
				server = AdminConfig.getid("/Node:"+node+"/Server:"+mname)
				runserv = AdminConfig.getObjectName(server)
				if (len(runserv) > 0):
					state  = "STARTED"
				else:
					state  = "STOPPED"
				#endIf

				# Logic necessary because Jython doesn't have "no-linefeed option"
				if (mCounter > 1):
					print "%-16s %-16s %-16s %-16s %-8s" % ("",mname,node,state,weight)
				else:
					print "%-16s %-16s %-16s %-16s %-8s" % (clname,mname,node,state,weight)
				#endIf
			#endFor
		else:
			print "%-16s" % (clname)
		#endIf
	#endFor
	print "\n=========================================================================="

#endDef

#******************************************************************************
# Procedure:  	showRunningStatus
# Description:	Show the status of all running servers and their applications
#****************************************************************************** 
def showRunningStatus ():

	#-----------------------------------------------------------------------------
	# Determine servers and applications running on each node
	#-----------------------------------------------------------------------------
	cName = findCell(cellName)
	cname = AdminConfig.showAttribute(cName, "name")
	nodes = AdminConfig.list("Node", cName).split(lineSeparator)

	print "\n===================================================================================="
	print "%-18s %-18s %-8s %-14s %-18s" % ("Node Name", "Server Name", "PID","State","Running Application(s)")
	print "%-18s %-18s %-8s %-14s %-18s" % ("---------","-----------","---","-----","----------------------")

	for node in nodes:
		nodeName = AdminConfig.showAttribute(node,"name")
 		srvsName = AdminControl.queryNames("WebSphere:type=Server,cell="+cname+",node="+nodeName+",*").split(lineSeparator)
		if (srvsName == ['']):
			continue
		#endIf
		nCount = 0
		for server in srvsName:
			nCount += 1
			#------------------------------------------
			# Get attributes from the server to display
			#------------------------------------------

			sname = AdminControl.getAttribute(server,"name")
			pid =	AdminControl.getAttribute(server,"pid")
			state = AdminControl.getAttribute(server,"state")

			# Workaround for "no-linefeed" option missing in Jython
			if (nCount > 1):
				print "%-18s %-18s %-8s %-15s" % ("",sname,pid,state)
			else:
				print "%-18s %-18s %-8s %-15s" % (nodeName,sname,pid,state)
			#endIF

			#-----------------------------------------
			# Find applications running on this server
			#-----------------------------------------

			apps = AdminControl.queryNames("WebSphere:type=Application,cell="+cname+",node="+nodeName+",process="+sname+",*").split(lineSeparator)
			if (apps != ['']):
				for app in apps:
					aname  = AdminControl.getAttribute(app,"name")
					print "%-62s %-18s" % ("",aname)
				#endFor
			#endIf
			print "\n------------------------------------------------------------------------------------"

		#endFor
	#endFor

	print "\n===================================================================================="
#endDef


#******************************************************************************
# Procedure:  	showServerInfo
# Description:	Show the status of all application servers
#****************************************************************************** 
def showServerInfo ():

	print "\n========================================="
	print "%-16s %-16s %-8s" % ("Node Name","Server Name","State")
	print "%-16s %-16s %-8s" % ("---------","-----------","-----")

	cName = findCell(cellName)
	cname = AdminConfig.showAttribute(cName, "name")
	nodes = AdminConfig.list("Node", cName).split(lineSeparator)

	# Get a list of the nodes in this cell, and the name of each

	for node in nodes:
		nname = AdminConfig.showAttribute(node, "name")
  		srvrs = AdminConfig.list("Server", node).split(lineSeparator)
		nCount = 0
  		for server in srvrs:
			nCount += 1
    			sname  = AdminConfig.showAttribute(server, "name")
     			runserv = AdminConfig.getObjectName(server)
    			if (len(runserv) > 0):
    				state = "STARTED"
	 		else:
				state = "STOPPED"	
			#endIf

			# Workaround for "no linefeed" option missing in Jython
			if (nCount > 1):
				print "%-16s %-16s %-8s" % ("", sname, state)
			else:
				print "%-16s %-16s %-8s" % (nname, sname, state)
			#endIf
		#endFor
		print ""
	#endFor


	print "\n========================================="

#endDef

