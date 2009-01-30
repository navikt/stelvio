##############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:   serverConfig.py
# Description: This file contains procedures to configure a server in a 
#              WebSphere 6.0.x ND environment:
#
#			setInitialState
#			setJVMArguments
#			setJVMClasspath
#			setJVMDebugArguments
#			setJVMDebugMode
#			setJVMHeapSizes
#			setJVMVerboseGC
#			setMaxInMemorySessionCount
#			setORBRequestTimeout
#			setPassByRef
#			setWebContainerServletCaching
#			setWebContainerThreadPoolIsGrowable
#			setWebContainerThreadPoolSizes
#			
#
#
# History:
#		
#******************************************************************************
#--------------------------------------------------------------------
# Procedure:   setInitialState
# Description: Set the Initial state of the application server
#              
# Parameters:  server list, init state
#--------------------------------------------------------------------
def setInitialState ( serverList, initialState ):
	
	print "Executing Set initial server state"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "stateManagement", [["initialState", initialState]] )
#endDef


#--------------------------------------------------------------------
# Procedure:   setJVMArguments
# Description: Set the JVM arguments
#              
# Parameters:  server list, jvm args
#--------------------------------------------------------------------
def setJVMArguments ( serverList, jvmArguments ):
	
	print "Executing JVM Arguments Settings"
	print "Server List: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["genericJvmArguments", jvmArguments]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setJVMClasspth
# Description: Set the JVM Classpath
#              
# Parameters:  server list, jvm classpath
#--------------------------------------------------------------------
def setJVMClasspath ( serverList, jvmClasspath ):

	print "Executing JVM Classpath Settings"
	print "ServerList: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["Classpath", jvmClasspath]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setJVMDebugArguments
# Description: Set the JVM debug arguments
#              
# Parameters:  server list, jvm debug args
#--------------------------------------------------------------------
def setJVMDebugArguments ( serverList, jvmDebugArguments ):
	
	print "Executing JVM Debug Arguments Settings"
	print "Server List: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["debugArguments", jvmDebugArguments]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setJVMDebugMode
# Description: Set the JVM debug mode
#              
# Parameters:  server list, boolean value
#--------------------------------------------------------------------
def setJVMDebugMode ( serverList, jvmDebugMode ):
	
	print "Executing JVM Debug Mode Settings"
	print "Server List: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["DebugMode", jvmDebugMode]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setJVMHeapSizes
# Description: Set the min and max JVM Heap Sizes
#              
# Parameters:  server list, heap min, heap max
#--------------------------------------------------------------------
def setJVMHeapSizes ( serverList, min, max ):

	print "Executing JVM Heap Size Settings"
	print "Server List: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["HeapMin", min], ["HeapMax", max]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setJVMVerboseGC
# Description: Enable the JVM Verbose Garbage Collection
#              
# Parameters:  server list, boolean value
#--------------------------------------------------------------------
def setJVMVerboseGC ( serverList, gcFlag ):

	print "Executing JVM Verbose GC Enabling"
	print "Server List: "+serverList
	setConfigObjectValues( [serverList], "JavaVirtualMachine", [["VerboseGC", gcFlag]] )

#endDef

#--------------------------------------------------------------------
# Procedure:   setMaxInMemorySessionCount
# Description: Set the maximum in memory session count
#              
# Parameters:  server list, size
#--------------------------------------------------------------------
def setMaxInMemorySessionCount ( serverList, maxInMemorySessionCount ):

	print "Executing Max in Memory Session Count"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "SessionManager", [["maxInMemorySessionCount", maxInMemorySessionCount]] )
	
#endDef

#--------------------------------------------------------------------
# Procedure:   setORBRequestTimeout
# Description: Set the ORB request timeout
#              
# Parameters:  server list, value
#--------------------------------------------------------------------
def setORBRequestTimeout ( serverList, requestTimeout ):

	print "Executing ORB Request Timeout"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "ObjectRequestBroker", [["requestTimeout", requestTimeout]] )

#endDef


#--------------------------------------------------------------------
# Procedure:   setPassByRef
# Description: Set ORB Pass By Reference
#              
# Parameters:  server list, boolean value
#--------------------------------------------------------------------
def setPassByRef ( serverList, noLocalCopies ):

	print "Executing ORB Pass By Reference"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "ObjectRequestBroker", [["noLocalCopies", noLocalCopies]] )
	
#endDef


#--------------------------------------------------------------------
# Procedure:   setWebContainerServletCaching
# Description: Set Servlet Caching for the web container
#              
# Parameters:  server list, boolean value
#--------------------------------------------------------------------
def setWebContainerServletCaching ( serverList, webContainerServletCaching ):

	print "Executing Web Container Servlet Caching"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "WebContainer", [["enableServletCaching", webContainerServletCaching]])

#endDef


#--------------------------------------------------------------------
# Procedure:   setWebContainerThreadPoolIsGrowable
# Description: Set thread pool is growable for the web container
#              
# Parameters:  server list, boolean value
#--------------------------------------------------------------------
def setWebContainerThreadPoolIsGrowable ( serverList, webPoolIsGrowable ):

	print "Executing Web Pool is Growable"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "ThreadPool", [["IsGrowable", webPoolIsGrowable]] )

#endDef


#--------------------------------------------------------------------
# Procedure:   setWebContainerThreadPoolSizes
# Description: Set thread pool sizes for the web container
#              
# Parameters:  server list, min, max
#--------------------------------------------------------------------
def setWebContainerThreadPoolSizes ( serverList, webPoolSizeMin, webPoolSizeMax ):

	print "Executing Web Container Thread Pool Sizes"
	print "Server List: "+serverList
	setConfigObjectValues([serverList], "ThreadPool", [["ThreadPoolMin", webPoolSizeMin], ["ThreadPoolMax", webPoolSizeMax]] )
                
#endDef

