###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:   	setupMQLink.py
# Description: 	Used to setup an MQ Link.
#******************************************************************************
import sys
import java

execfile( WSADMIN_SCRIPTS_HOME+"/utils6.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/applications.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/environment.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/monitor.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/reports.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/resources.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/security.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/serverConfig.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/serverControl.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/serverSetup.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/serviceIntegration.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/troubleshoot.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/WPS.py" )

#===========================================================================================
#  Set up an MQ Link
#===========================================================================================
print "\n** Creating the SIBus **"
# USAGE:  createSIBus( propertyFileName )
#createSIBus(PROPS_HOME+"SIBus_create.props")

print "\n** Creating the Foreign Bus **"
# USAGE:  createSIBus( propertyFileName )
createSIBForeignBus(PROPS_HOME+"SIBForeignBus_create.props")

print "\n** Creating the MQ Link **"
# USAGE:  createSIBus( propertyFileName )
createSIBMQLink(PROPS_HOME+"SIBMQLink_create.props" )


	
#===================================================================================
#  Save Configuration
#===================================================================================
print "\n====== Saving configuration  ======"
AdminConfig.save()
#syncNodesToMaster( nodeName )


print 'Done ...'