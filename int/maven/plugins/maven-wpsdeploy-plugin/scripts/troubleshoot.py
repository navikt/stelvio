###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	troubleshoot.py
# Description:	This file contains the following troubleshooting procedures:
#		
#			activateServerTrace
#			deactivateServerTrace
#			dumpThreads 
#
#
#******************************************************************************
#******************************************************************************
# Procedure:   	activateServerTrace
# Description:	Activate a server trace
#****************************************************************************** 
def activateServerTrace (nodeName, serverName, isRuntime, tracefile, traceString):

	#---------------------------------------------------------------------------------
	# Activate the trace
	#---------------------------------------------------------------------------------
	print "\n ====== Activate the trace for "+serverName+" on "+nodeName+" ======"

	if (isRuntime == "true"):
		pass
	else:
		# enable tracing
		aTrace = toggleService("TraceService", serverName, nodeName, "true")
		if (aTrace != 0):
			print "Enable Trace failed."
		else:
			print "Enable Trace successful."
		#endIf

	#endIf

	# set trace 
	sTrace = setTrace(serverName, nodeName, isRuntime, traceString, traceFile)	
	if (sTrace != 0):
		print "Set Trace failed."
	else:
		print "Set Trace successful."
	#endIf

#endDef


#******************************************************************************
# Procedure:  	deactivateServerTrace
# Description:	Deactivate a server trace
#****************************************************************************** 
def deactivateServerTrace ( nodeName, serverName, isRuntime, traceFile, traceString ):

	#---------------------------------------------------------------------------------
	# Deactivate the trace
	#---------------------------------------------------------------------------------
	print "\n ====== Deactivate the trace for "+serverName+" on "+nodeName+" ======"

	if (isRuntime == "true"):
		sTrace = setTrace(serverName, nodeName, isRuntime, "*=all=disabled", "")

	else:
		# disable tracing
		dTrace = toggleService("TraceService", serverName, nodeName, "false")
		if (dTrace != 0):
			print "Disable Trace failed."
		else:
			AdminConfig.save()
			print "Disable Trace successful."
			syncNodesToMaster(nodeList)
		#endIf

	#endIf

#endDef


#******************************************************************************
# Procedure:  	dumpThreads
# Description:	Invoke a JVM thread dump
#****************************************************************************** 
def dumpThreads ( nodeName, serverName ):

	#---------------------------------------------------------------------------------
	# Invoke JVM dump
	#---------------------------------------------------------------------------------

	global AdminControl

	print "\n ====== Invoke JVM dump for "+serverName+" on "+nodeName+" ======"

	# Get the JVM MBean we want to have dump threads
	try:
		_excp_ = 0
		jvm = AdminControl.completeObjectName("type=JVM,node="+nodeName+",process="+serverName+",*" )
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		jvm = `_value_`
		_excp_ = 1
	#endTry 
	if (_excp_ ):
		print "Error getting JVM object"
		print "JVM = "+jvm
		sys.exit()
	#endIf 

	# Invoke the thread dump
	AdminControl.invoke(jvm, "dumpThreads" )

#endDef


