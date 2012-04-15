#****************************************************************************
#
# File name: RoleMapping.py
#
# Description: This script adds users and groups to a given role for a given
#	      module, both provided as arguments.
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
l = log.getLogger(__name__)

ARG_LIST = sys.argv[1:]

def main():
	roles = {}
	for arg in ARG_LIST:
		temp = arg.split("::")
		roleName = temp[0]
		roles[roleName] = temp[1:]

	scaModules = sca.getModulesToBeInstalled()
	
	for scaModule in scaModules:
		applicationName = scaModule.applicationName
		appRoles = getRolesToUsers(applicationName)
		if (appRoles):
			for roleName in appRoles:
				if (roles.has_key(roleName)):
					if (roles[roleName]):
						users = ""
						groups = ""
						runas_user = ""
						runas_pwd = ""

						if (roles[roleName].count("_user") == 1 ):
							temp_index = roles[roleName].index("_user")
							users = roles[roleName][temp_index+1]
							l.info("Mapping role [" + roleName + "] to user(s) [" + users + "] for " + applicationName + ".")
						
						if (roles[roleName].count("_groups") == 1):
							temp_index = roles[roleName].index("_groups")
							groups = roles[roleName][temp_index+1]
							l.info("Mapping role [" + roleName + "] to group(s) [" + groups + "] for " + applicationName + ".")
						

						result = mapRolesToUsers(applicationName, roleName, users, groups)
					

					if (roles[roleName].count("_runas") == 1 and getRunAsRolesToUsers(applicationName)):
						temp_index = roles[roleName].index("_runas")
						runas = roles[roleName][temp_index+1]
						runas = roles[roleName][temp_index+1].split("|")
						runas_user = runas[0]
						runas_pwd = runas[1]
						mapRunAsRolesToUsers(applicationName, roleName, runas_user, runas_pwd)
						l.info("Set Run As user [" + runas_user + "] with pwd [*****] for [" + applicationName + "].")
					

				
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