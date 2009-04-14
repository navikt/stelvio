#******************************************************************************
# File Name:   	CreateApplicationArtifacts.py
# Description: 	Used for script flow.
#******************************************************************************
#
##############################################################################
# WHAT = 
#		1 = nsbinding 			= Delete Name Space Binding
#		2 = j2cqcf			= Delete J2C Connection Factory
#		3 = sibdestination 	 	= Delete Sibus destination (Queue, Topic...)
#		4 = jmsqueue			= Delete JMS queue
#		5 = jmstopic			= Delete JMS topic
#		6 = jmsactivationspec		= Delete JMS Activation Specification
#		7 = jmscf			= Delete JMS Connection Factory
#		8 = racreate 			= DeInstall Resource adapter
#		9 = jdbcprovider		= Delete JDBC Provider
#       	10 = datasource        		= Delete Datasource	
#		11 = mqcf			= Delete WebSphere MQ Connection Factory
#		12 = mqqueuedes			= Delete WebSphere MQ queue destinations 
#		13 = sharedlib			= Delete a shared library and associate it with an application
#		
#		
#		= 13:
#	
#
##############################################################################
import sys
import java
from java.lang 	import System
from java.net 	import InetAddress
from java.util 	import Properties
from java.io 	import FileInputStream

APPLICATIONS_FOLDER 	 = sys.argv[0]
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

ears = readDistributionDirectory(APPLICATIONS_FOLDER)
appNames = parseApplicationNames(ears)	 
	
for appName in appNames: 
	APPLICATION_NAME=appName
	try:
		# Use Java to load it, it is a properties file
		fileprop = Properties()
		fileStream = FileInputStream(APP_PROPS_HOME+APPLICATION_NAME +".properties")
		
		fileprop.load(fileStream)
		configNames = fileprop.propertyNames()
		for configName in configNames:
			configInfo[configName] = fileprop.getProperty(configName)
	except:
		print 'WARNING: Application '+ APPLICATION_NAME + ' contains no resources, since property file was not defined.'
		continue
	
	print '============================================================================'
	print 'INFO: Deploying resources for ' +  APPLICATION_NAME
	print '============================================================================'
	for jidx in range(int(configInfo["app.count"])):
			whatToCreate	 	= configInfo["app.%d.WHAT" % (jidx)]
			whereIsProperties	= configInfo["app.%d.WHERE" % (jidx)]
			#===================================================================================
			#  Create Name Space Binding
			#===================================================================================
			if (whatToCreate == "nsbinding"):
				# USAGE:  createNameSpaceBinding ( <"properties file name"> ):
				retval = createNameSpaceBinding (APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create J2C Connection Factory
			#===================================================================================
			if (whatToCreate == "j2cqcf"):
				# USAGE:  createJ2CConnectionFactory ( <"properties file name"> ):
				retval =  createJ2CConnectionFactory(APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf	
			#===================================================================================
			#  Create Sibus destination
			#===================================================================================
			if (whatToCreate == "sibdestination"):
				# USAGE:  createSIBDestination ( <"properties file name"> ):
				retval = createSIBDestination (APP_PROPS_HOME + whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create JMS queue
			#===================================================================================
			if (whatToCreate == "jmsqueue"):
				# USAGE:  createSIBDestination ( <"properties file name"> ):
				retval = createJMSQueue (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create JMS topic
			#===================================================================================
			if (whatToCreate == "jmstopic"):
				# USAGE:  createJMSTopic ( <"properties file name"> ):
				retval = createJMSTopic (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create J2CActivationSpec
			#===================================================================================
			if (whatToCreate == "jmsactivationspec"):
				# USAGE:  createJMSActivationSpec ( <"properties file name"> ):
				retval = createJMSActivationSpec (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create JMS Connection Factory
			#===================================================================================
			if (whatToCreate == "jmscf"):
				# USAGE:  createJMSConnectionFactory ( <"properties file name"> ):
				retval = createJMSConnectionFactory (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Install J2C Resource Adapter
			#===================================================================================
			if (whatToCreate == "racreate"):
				# USAGE:  installResourceAdapter ( <"properties file name"> ):
				retval = installResourceAdapter (APP_PROPS_HOME+whereIsProperties) 
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create Datasource
			#===================================================================================
			if (whatToCreate == "datasource"):
				# USAGE:  installResourceAdapter ( <"properties file name"> ):
				retval = createDataSource ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create JDBC Provider
			#===================================================================================
			if (whatToCreate == "jdbcprovider"):
				# USAGE:  createJDBCProvider ( <"properties file name"> ):
				retval = createJDBCProvider ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
				
			#endIf
			#===================================================================================
			#  create MQ Connection Factory
			#===================================================================================
			if (whatToCreate == "mqcf"):
				# USAGE:  createMQConnectionFactory ( <"properties file name"> ):
				retval = createMQConnectionFactory ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  create MQ Queue Destination
			#===================================================================================
			if (whatToCreate == "mqqueuedes"):
				# USAGE:  createMQDestination ( <"properties file name"> ):
				retval = createMQDestination ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			#===================================================================================
			#  Create a shared library and associate it with an application
			#===================================================================================
			if (whatToCreate == "sharedlib"):
				# USAGE:  createSharedLibrary ( <"properties file name"> ):
				retval = createSharedLibrary ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					sys.exit(1)
			#endIf
			
			
save()

############### End Main Section ###############################################					

