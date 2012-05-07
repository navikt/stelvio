#****************************************************************************
#
# File name: RoleMapping.py
#
# Description: This script adds users and groups to a given role for a given
#	      module
#
#	      Used by the plugin:
#	      maven-websphere-plugin: no.nav.maven.plugin.websphere.plugin
#
#
#****************************************************************************

import sys, os, re
from lib.saveUtil import save
import lib.scaModuleUtil as sca
import lib.logUtil as log
import lib.getConfiguration as getConfiguration
l = log.getLogger(__name__)

def main():
	roles = getConfiguration.getRoles()
	l.debug('Got roles:', roles)
	scaModules = sca.getModulesToBeInstalled()
	
	for scaModule in scaModules:
		if not scaModule.deployResources:
			continue
			
		applicationName = scaModule.applicationName
		appRoles = getRolesToUsers(applicationName)
		if (appRoles):
			for roleName in appRoles:
				if (roles.has_key(roleName)):
					role = roles[roleName]
					
					users = '|'.join(role['users'])
					groups = '|'.join(role['groups'])

					if users:
						l.info("Mapping role [" + roleName + "] to user(s) [" + users + "] for " + applicationName + ".")
					
					if groups:
						l.info("Mapping role [" + roleName + "] to group(s) [" + groups + "] for " + applicationName + ".")
					
					result = mapRolesToUsers(applicationName, roleName, users, groups)
				

					if (role['runas'] and getRunAsRolesToUsers(applicationName)):
						username = role['runas']['username']
						password = role['runas']['password']
						mapRunAsRolesToUsers(applicationName, roleName, username, password)
						l.info("Set Run As user [" + username + "] with pwd [*****] for [" + applicationName + "].")
					
				else:
					l.error("No configuration found for role [" + roleName + "] in [" + applicationName + "]. Please check configuration.")
	save()

def getRolesToUsers(applicationName):
	view_info = AdminApp.view(applicationName, ['-MapRolesToUsers'])
	roles = []
	for item in view_info.splitlines():
		if (item.startswith("Role:")):
			roles.append(item.replace("Role:","").strip())

	return roles

def getRunAsRolesToUsers(applicationName):
	view_info = AdminApp.view(applicationName, ['-MapRunAsRolesToUsers'])
	roles = []
	for item in view_info.splitlines():
		if (item.startswith("Role:")):
			roles.append(item.replace("Role:","").strip())

	return roles

def mapRolesToUsers(applicationName, roleName, users, groups):
	r_value = AdminApp.edit(applicationName, '[ -MapRolesToUsers [[' + roleName + ' AppDeploymentOption.No AppDeploymentOption.No \"' + users + '\" \"' + groups + '\"]]]')

	return r_value

def mapRunAsRolesToUsers(applicationName, roleName, username, password):
	r_value = AdminApp.edit(applicationName, '[  -MapRunAsRolesToUsers [[ ' + roleName + ' ' + username + ' ' + password + ' ]]]' )

	return r_value

def deleteUsersAndGroupEntries(applicationName):
	return AdminApp.deleteUserAndGroupEntries(applicationName)


if __name__ == '__main__': main()