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
#       10 = datasource        		= Delete Datasource	
#		11 = mqcf			= Delete WebSphere MQ Connection Factory
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
import sys, re

from lib.saveUtil import save
import lib.scaModuleUtil as sca
import lib.logUtil as log
l = log.getLogger(__name__)

from lib.environment import createNameSpaceBinding, createSharedLibrary
from lib.resources import installResourceAdapter, createJ2CConnectionFactory, createDataSource, createJDBCProvider, createJMSActivationSpec, createJMSConnectionFactory, createJMSQueue, createJMSTopic, createMQConnectionFactory, createMQDestination, createSharedLibrary
from lib.serviceIntegration import createSIBDestination

ENVIRONMENT 	 	 = sys.argv[1]
APP_PROPS_HOME 		 = sys.argv[2]


############### Main Section ###############################################



configInfo = {}
	
for scaModule in sca.getModulesToBeInstalled():
	if not scaModule.deployResources:
		l.debug('Skipping %s because "deployResources" is false in the scaModule' % scaModule)
		continue
	APPLICATION_NAME=scaModule.shortName
	try:
		# Use Java to load it, it is a properties file
		fileprop = Properties()
		fileStream = FileInputStream(APP_PROPS_HOME+APPLICATION_NAME +".properties")
		
		fileprop.load(fileStream)
		configNames = fileprop.propertyNames()
		for configName in configNames:
			configInfo[configName] = fileprop.getProperty(configName)
	except:
		l.info('Application '+ APPLICATION_NAME + ' contains no resources, since property file was not defined.')
		continue
	
	l.info('================================================================================')
	l.info(('INFO: Deploying resources for ' +  APPLICATION_NAME).center(80))
	l.info('============================================================================')
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
					l.error("createNameSpaceBinding() returned 1")
			#endIf
			#===================================================================================
			#  Create J2C Connection Factory
			#===================================================================================
			if (whatToCreate == "j2cqcf"):
				# USAGE:  createJ2CConnectionFactory ( <"properties file name"> ):
				retval =  createJ2CConnectionFactory(APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createJ2CConnectionFactory() returned 1")
			#endIf	
			#===================================================================================
			#  Create Sibus destination
			#===================================================================================
			if (whatToCreate == "sibdestination"):
				# USAGE:  createSIBDestination ( <"properties file name"> ):
				retval = createSIBDestination (APP_PROPS_HOME + whereIsProperties)
				if(retval == 1):
					l.error("createSIBDestination() returned 1")
			#endIf
			#===================================================================================
			#  Create JMS queue
			#===================================================================================
			if (whatToCreate == "jmsqueue"):
				# USAGE:  createSIBDestination ( <"properties file name"> ):
				retval = createJMSQueue (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					l.error("createJMSQueue() returned 1")
			#endIf
			#===================================================================================
			#  Create JMS topic
			#===================================================================================
			if (whatToCreate == "jmstopic"):
				# USAGE:  createJMSTopic ( <"properties file name"> ):
				retval = createJMSTopic (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					l.error("createJMSTopic() returned 1")
			#endIf
			#===================================================================================
			#  Create J2CActivationSpec
			#===================================================================================
			if (whatToCreate == "jmsactivationspec"):
				# USAGE:  createJMSActivationSpec ( <"properties file name"> ):
				retval = createJMSActivationSpec (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					l.error("createJMSActivationSpec() returned 1")
			#endIf
			#===================================================================================
			#  Create JMS Connection Factory
			#===================================================================================
			if (whatToCreate == "jmscf"):
				# USAGE:  createJMSConnectionFactory ( <"properties file name"> ):
				retval = createJMSConnectionFactory (APP_PROPS_HOME + whereIsProperties ) 
				if(retval == 1):
					l.error("createJMSConnectionFactory() returned 1")
			#endIf
			#===================================================================================
			#  Install J2C Resource Adapter
			#===================================================================================
			if (whatToCreate == "racreate"):
				# USAGE:  installResourceAdapter ( <"properties file name"> ):
				retval = installResourceAdapter (APP_PROPS_HOME+whereIsProperties) 
				if(retval == 1):
					l.error("installResourceAdapter() returned 1")
			#endIf
			#===================================================================================
			#  Create Datasource
			#===================================================================================
			if (whatToCreate == "datasource"):
				# USAGE:  installResourceAdapter ( <"properties file name"> ):
				retval = createDataSource ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createDataSource() returned 1")
			#endIf
			#===================================================================================
			#  Create JDBC Provider
			#===================================================================================
			if (whatToCreate == "jdbcprovider"):
				# USAGE:  createJDBCProvider ( <"properties file name"> ):
				retval = createJDBCProvider ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createJDBCProvider() returned 1")
				
			#endIf
			#===================================================================================
			#  create MQ Connection Factory
			#===================================================================================
			if (whatToCreate == "mqcf"):
				# USAGE:  createMQConnectionFactory ( <"properties file name"> ):
				retval = createMQConnectionFactory ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createMQConnectionFactory() returned 1")
			#endIf
			#===================================================================================
			#  create MQ Queue Destination
			#===================================================================================
			if (whatToCreate == "mqqueuedes"):
				# USAGE:  createMQDestination ( <"properties file name"> ):
				retval = createMQDestination ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createMQDestination() returned 1")
			#endIf
			#===================================================================================
			#  Create a shared library and associate it with an application
			#===================================================================================
			if (whatToCreate == "sharedlib"):
				# USAGE:  createSharedLibrary ( <"properties file name"> ):
				retval = createSharedLibrary ( APP_PROPS_HOME+whereIsProperties)
				if(retval == 1):
					l.error("createSharedLibrary() returned 1")
			#endIf
			
			
save()

############### End Main Section ###############################################					

