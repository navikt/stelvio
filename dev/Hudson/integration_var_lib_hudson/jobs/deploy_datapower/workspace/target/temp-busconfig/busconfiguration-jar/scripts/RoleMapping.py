#****************************************************************************
#
# File name: RoleMapping.py
#
# Description: This script adds users and groups to a given role for a given
#              module, both provided as arguments.
#
#              Used by the plugin:
#              maven-websphere-plugin: no.nav.maven.plugin.websphere.plugin
#
# Author: test@example.com
#
#****************************************************************************

import sys
import os
import time
import re

WSADMIN_SCRIPTS_HOME    = sys.argv[0]
WSADMIN_SCRIPTS_HOME    = WSADMIN_SCRIPTS_HOME.replace('\t','\\t')
ARG_LIST = sys.argv[1:]

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/Log.py" )

class SCAModule:
        def __init__(self, module_string):
                attributes = module_string.split(":")
                self.name = attributes[0]
                self.app_name = attributes[1]
                self.sca_version = attributes[2]
                self.short_name = attributes[3]

        def __repr__(self):
                return self.name

class SCAModuleList:
        def __init__(self, module_list_string):
                modules = module_list_string.split("\n")
                self.module_list = []
                for module in modules:
                        self.module_list.append(SCAModule(module))

        def __repr__(self):
                return str(self.module_list)

def main():
        start = time.clock()

        roles = {}
        for arg in ARG_LIST:
                temp = arg.split("::")
                role = temp[0]
                roles[role] = temp[1:]

        ears = os.listdir( WSADMIN_SCRIPTS_HOME + "/../EARFilesToDeploy/" )
        installed_modules = SCAModuleList(AdminTask.listSCAModules())

        modules = []

        for module in installed_modules.module_list:
                for app in ears:
                        if (app.startswith(module.short_name)):
                                modules.append(module)
                                break

        log("DEBUG", "After comparing installed SCA modules and .ear files, decided to work with modules: " + str(modules))

        for module in modules:
                ear = module.app_name
                appRoles = getRolesToUsers(ear)
                if (appRoles):
                        for role in appRoles:
                                if (roles.has_key(role)):
                                        if (roles[role]):
                                                users = ""
                                                groups = ""
                                                runas_user = ""
                                                runas_pwd = ""

                                                if (roles[role].count("_user") == 1 ):
                                                        temp_index = roles[role].index("_user")
                                                        users = roles[role][temp_index+1]
                                                        log("INFO", "Mapping role [" + role + "] to user(s) [" + users + "] for " + ear + ".")
                                                # end if
                                                if (roles[role].count("_groups") == 1):
                                                        temp_index = roles[role].index("_groups")
                                                        groups = roles[role][temp_index+1]
                                                        log("INFO", "Mapping role [" + role + "] to group(s) [" + groups + "] for " + ear + ".")
                                                # end if

                                                result = mapRolesToUsers(ear, role, users, groups)
                                        # end if

                                        if (roles[role].count("_runas") == 1 and getRunAsRolesToUsers(ear)):
                                                temp_index = roles[role].index("_runas")
                                                runas = roles[role][temp_index+1]
                                                runas = roles[role][temp_index+1].split("|")
                                                runas_user = runas[0]
                                                runas_pwd = runas[1]
                                                mapRunAsRolesToUsers(ear, role, runas_user, runas_pwd)
                                                log("INFO", "Set Run As user [" + runas_user + "] with pwd [*****] for [" + ear + "].")
                                        # end if

                                # end if
                                else:
                                        log("ERROR", "No configuration found for role [" + role + "] in [" + ear + "]. Please check configuration.")
                                        return 
                                # end else
                        # end for
                # end if
        # end for

        AdminConfig.save()
#endDef

def getRolesToUsers(ear):
        view_info = AdminApp.view(ear, ['-MapRolesToUsers'])
        roles = []
        for item in view_info.splitlines():
                if (item.startswith("Role:")):
                        roles.append(item.replace("Role:","").strip())

        return roles
# end def getRolesToUsers


def getRunAsRolesToUsers(ear):
        view_info = AdminApp.view(ear, ['-MapRunAsRolesToUsers'])
        roles = []
        for item in view_info.splitlines():
                if (item.startswith("Role:")):
                        roles.append(item.replace("Role:","").strip())

        return roles
# end def getRunAsRolesToUsers

def mapRolesToUsers(ear, role, users, groups):
        r_value = AdminApp.edit(ear, '[ -MapRolesToUsers [[' + role + ' AppDeploymentOption.No AppDeploymentOption.No \"' + users + '\" \"' + groups + '\"]]]')

        return r_value
# end def mapRolesToUsers

def mapRunAsRolesToUsers(ear, role, username, password):
        r_value = AdminApp.edit(ear, '[  -MapRunAsRolesToUsers [[ ' + role + ' ' + username + ' ' + password + ' ]]]' )

        return r_value
# end def mapRunAsRolesToUsers

def deleteUsersAndGroupEntries(ear):
        return AdminApp.deleteUserAndGroupEntries(ear)
# end def deleteUsersAndGroupEntries

main()