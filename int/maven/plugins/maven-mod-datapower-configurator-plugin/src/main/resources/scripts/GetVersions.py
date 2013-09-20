#****************************************************************************
#
# File name: GetVersions.py
#
# Description: This script is used to get the versions of modules deployed on 
#				a specific WPS, and then writing them to a file.  The modules 
#				are specified in the datapower-deploy pom.xml.  
#
#              Used by the plugin:
#              maven-datapower-config-generator-plugin: no.nav.maven.plugins
#
# Author: test@example.com
#
#****************************************************************************

variableNames	= sys.argv[0]

varNameList = variableNames.split(",")

filename = "target/temp.txt"
FILE = open(filename,"w")

AdminOperations = AdminControl.completeObjectName('WebSphere:name=AdminOperations,process=dmgr,*')
for varName in varNameList:
#	AdminOperations = AdminControl.completeObjectName('WebSphere:*,type=AdminOperations')
	version = AdminControl.invoke(AdminOperations, 'expandVariable', '${'+varName+'}')
	FILE.writelines(varName+"="+version+"\n")

FILE.close()