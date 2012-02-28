import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs
#******************************************************************************
# File Name:   	DeleteApplicationArtifacts.py
# Description: 	Used for script flow.
#******************************************************************************
#
##############################################################################
# WHAT = 
#		1 = nsbinding 			= Delete Name Space Binding
#		2 = j2cqcf				= Delete J2C Connection Factory
#		3 = sibdestination 	 	= Delete Sibus destination (Queue, Topic...)
#		4 = jmsqueue			= Delete JMS queue
#		5 = jmstopic			= Delete JMS topic
#		6 = jmsactivationspec	= Delete JMS Activation Specification
#		7 = jmscf				= Delete JMS Connection Factory
#		8 = racreate 			= DeInstall Resource adapter
#		9 = jdbcprovider		= Delete JDBC Provider
#       10 = datasource         = Delete Datasource	
#		11 = mqcf				= Delete WebSphere MQ Connection Factory
#		12 = mqqueuedes			= Delete WebSphere MQ queue destinations 
#		13 = sharedlib			= Delete a shared library and associate it with an application
#		
#		
#		= 13:
#	
#
##############################################################################
import java
from java.lang 	import System
from java.net 	import InetAddress
from java.util 	import Properties
from java.io 	import FileInputStream
import lib.logUtil as l

APPLICATION_NAME 	 = sys.argv[0]
ENVIRONMENT 	 	 = sys.argv[1]
WSADMIN_SCRIPTS_HOME	 = sys.argv[2]
APP_PROPS_HOME 		 = WSADMIN_SCRIPTS_HOME+"/app_props/"+ENVIRONMENT+"/"

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/utils6.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/environment.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/monitor.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/reports.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/resources.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/security.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/serverConfig.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/serverControl.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/serverSetup.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/serviceIntegration.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/troubleshoot.py" )
execfile( WSADMIN_SCRIPTS_HOME+"/scripts/WPS.py" )


############### Main Section ###############################################

global progInfo
global configInfo


configInfo = {}


try:
	# Use Java to load it, it is a properties file
	fileprop = Properties()
	fileStream = FileInputStream(APP_PROPS_HOME+APPLICATION_NAME +".properties")
	
	fileprop.load(fileStream)
	configNames = fileprop.propertyNames()
	for configName in configNames:
		configInfo[configName] = fileprop.getProperty(configName)
except:
	l.error('Application '+ APPLICATION_NAME + ' contains no resources, since property file was not defined.')
			
for jidx in range(int(configInfo["app.count"])):
		whatToDelete	 	= configInfo["app.%d.WHAT" % (jidx)]
		whereIsProperties	= configInfo["app.%d.WHERE" % (jidx)]
		#===================================================================================
		#  Delete Name Space Binding
		#===================================================================================
		if (whatToDelete == "nsbinding"):
			# USAGE:  deleteNameSpaceBinding ( <"properties file name"> ):
			retval = deleteNameSpaceBinding (APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("deleteNameSpaceBinding() returned 1")
		#endIf
		#===================================================================================
		#  Delete J2C Connection Factory
		#===================================================================================
		if (whatToDelete == "j2cqcf"):
			# USAGE:  deleteJ2CConnectionFactory ( <"properties file name"> ):
			retval =  deleteJ2CConnectionFactory(APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("deleteJ2CConnectionFactory() returned 1")
		#endIf	
		#===================================================================================
		#  Delete Sibus destination
		#===================================================================================
		if (whatToDelete == "sibdestination"):
			# USAGE:  deleteSIBDestination ( <"properties file name"> ):
			retval = deleteSIBDestination (APP_PROPS_HOME + whereIsProperties)
			if(retval == 1):
				l.error("deleteSIBDestination() returned 1")
		#endIf
		#endIf
		#===================================================================================
		#  Delete JMS queue
		#===================================================================================
		if (whatToDelete == "jmsqueue"):
			# USAGE:  deleteSIBDestination ( <"properties file name"> ):
			retval = deleteJMSQueue (APP_PROPS_HOME + whereIsProperties ) 
			if(retval == 1):
				l.error("deleteJMSQueue() returned 1")
		#endIf
		#===================================================================================
		#  Delete JMS topic
		#===================================================================================
		if (whatToDelete == "jmstopic"):
			# USAGE:  deleteJMSTopic ( <"properties file name"> ):
			retval = deleteJMSTopic (APP_PROPS_HOME + whereIsProperties ) 
			if(retval == 1):
				l.error("deleteJMSTopic() returned 1")
		#endIf
		#===================================================================================
		#  Delete J2CActivationSpec
		#===================================================================================
		if (whatToDelete == "jmsactivationspec"):
			# USAGE:  deleteJMSActivationSpec ( <"properties file name"> ):
			retval = deleteJMSActivationSpec (APP_PROPS_HOME + whereIsProperties ) 
			if(retval == 1):
				l.error("deleteJMSActivationSpec() returned 1")
		#endIf
		#===================================================================================
		#  Delete JMS Connection Factory
		#===================================================================================
		if (whatToDelete == "jmscf"):
			# USAGE:  deleteJMSConnectionFactory ( <"properties file name"> ):
			retval = deleteJMSConnectionFactory (APP_PROPS_HOME + whereIsProperties ) 
			if(retval == 1):
				l.error("deleteJMSConnectionFactory() returned 1")
		#endIf
		#===================================================================================
		#  Delete Datasource
		#===================================================================================
		if (whatToDelete == "datasource"):
			# USAGE:  removeDataSource ( <"properties file name"> ):
			retval = removeDataSource ( APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("removeDataSource() returned 1")
		#endIf
		#===================================================================================
		#  Delete JDBC Provider
		#===================================================================================
		if (whatToDelete == "jdbcprovider"):
			# USAGE:  removeJDBCProvider ( <"properties file name"> ):
			retval = removeJDBCProvider ( APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("removeJDBCProvider() returned 1")
		#endIf
		#===================================================================================
		#  Delete MQ Connection Factory
		#===================================================================================
		if (whatToDelete == "mqcf"):
			# USAGE:  deleteMQConnectionFactory ( <"properties file name"> ):
			retval = deleteMQConnectionFactory ( APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("deleteMQConnectionFactory() returned 1")
		#endIf
		#===================================================================================
		#  Delete MQ Queue Destination
		#===================================================================================
		if (whatToDelete == "mqqueuedes"):
			# USAGE:  deleteMQDestination ( <"properties file name"> ):
			retval = deleteMQDestination ( APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("deleteMQDestination() returned 1")
		#endIf
		#===================================================================================
		#  Delete a shared library and associate it with an application
		#===================================================================================
		if (whatToDelete == "sharedlib"):
			# USAGE:  deleteSharedLibrary ( <"properties file name"> ):
			retval = deleteSharedLibrary ( APP_PROPS_HOME+whereIsProperties)
			if(retval == 1):
				l.error("deleteSharedLibrary() returned 1")
		#endIf
		
		
		
save()
############### End Main Section ###############################################					

