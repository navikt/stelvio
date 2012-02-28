###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:   serverControl.py
# Description: This file contains procedures to control the state of a server 
#              in a WebSphere 6.0.x ND environment:
#
#			goRestartServers
#			goStartCluster
#			goStartClusters
#			goStartServer
#			goStartServers
#			goStopCluster
#			goStopClusters
#			goStopServer
#			goStopServers
#			
#
# History:
#******************************************************************************
#--------------------------------------------------------------------
# Procedure:   goRestartServers
# Description: Restarts the given servers
#             
# Parameters:  server list
#--------------------------------------------------------------------
def goRestartServers ( serverList ):

	print "Executing Restart Servers"
	print "Server List: "+serverList
	restartServers (getConfigObjectValues ( serverList, "Server", ["name","node"] ))

#endDef


#--------------------------------------------------------------------
# Procedure:   goStartCluster
# Description: Starts a given cluster
#             
# Parameters:  cluster name
#--------------------------------------------------------------------
def goStartCluster ( clusterName ):
	
	print "Executing Start Cluster"
	print "Cluster: "+clusterName+" in Cell: "+cellName
	startCluster ( cellName, clusterName )

#endDef


#--------------------------------------------------------------------
# Procedure:   goStartClusters
# Description: Starts the given clusters
#             
# Parameters:  cluster list
#--------------------------------------------------------------------
def goStartClusters ( clusterList ):

	print "Executing Start Clusters"
	print "Cluster List: "+clusterList
	startClusters ( clusterList )
	
#endDef


#--------------------------------------------------------------------
# Procedure:   goStartServer
# Description: Starts a given server
#             
# Parameters:  node name, server name
#--------------------------------------------------------------------
def goStartServer ( nodeName, serverName ):
	
	print "Executing Start Server"
	print "Server: "+serverName+" on Node: "+nodeName
	startServer ( nodeName, serverName )

#endDef


#--------------------------------------------------------------------
# Procedure:   goStartServers
# Description: Starts a list of servers
#             
# Parameters:  server list
#--------------------------------------------------------------------
def goStartServers ( serverList ):
	
	print "Executing Start Servers"
	print "Server List: "+serverList
	startServers(getConfigObjectValues(serverList, "Server", ["name","node"]))

#endDef


#--------------------------------------------------------------------
# Procedure:   goStopCluster
# Description: Stops a given cluster
#             
# Parameters:  cluster name
#--------------------------------------------------------------------
def goStopCluster ( clusterName ):
	
	print "Executing Stop Cluster"
	print "Cluster: "+clusterName+" in Cell: "+cellName
	stopCluster ( cellName, clusterName )

#endDef

#--------------------------------------------------------------------
# Procedure:   goStopClusters
# Description: Stops the given clusters
#             
# Parameters:  cluster list
#--------------------------------------------------------------------
def goStopClusters ( clusterList ):

	print "Executing Stop Clusters"
	print "Cluster List: "+clusterList
	stopClusters ( clusterList )
	
#endDef


#--------------------------------------------------------------------
# Procedure:   goStopServer
# Description: Stops a given server
#             
# Parameters:  node name, server name
#--------------------------------------------------------------------
def goStopServer ( nodeName, serverName ):
	
	print "Executing Stop Server"
	print "Server: "+serverName+" on Node: "+nodeName
	stopServer ( nodeName, serverName )

#endDef


#--------------------------------------------------------------------
# Procedure:   goStopServers
# Description: Stops a list of servers
#             
# Parameters:  server list
#--------------------------------------------------------------------
def goStopServers ( serverList ):
	
	print "Executing Stop Servers"
	print "Server List: "+serverList
	stopServers(getConfigObjectValues(serverList, "Server", ["name","node"]))

#endDef

