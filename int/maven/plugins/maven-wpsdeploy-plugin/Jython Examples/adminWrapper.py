
print "adminWrapper dir():", dir()

from lib.adminObjects import *
global AdminTask, AdminApp, AdminApplication, AdminAuthorizations, AdminBLA, AdminClusterManagement, AdminConfig, AdminControl, AdminJ2C, AdminJDBC, AdminJMS, AdminLibHelp, AdminNodeGroupManagement, AdminNodeManagement, AdminResources, AdminServerManagement, AdminTask, AdminUtilities

AdminTask = MyAdminTask()
AdminApp = MyAdminApp()
AdminApplication = MyAdminApplication()
AdminAuthorizations = MyAdminAuthorizations()
AdminBLA = MyAdminBLA()
AdminClusterManagement = MyAdminClusterManagement()
AdminConfig = MyAdminConfig()
AdminControl = MyAdminControl()
AdminJ2C = MyAdminJ2C()
AdminJDBC = MyAdminJDBC()
AdminJMS = MyAdminJMS()
AdminLibHelp = MyAdminLibHelp()
AdminNodeGroupManagement = MyAdminNodeGroupManagement()
AdminNodeManagement = MyAdminNodeManagement()
AdminResources = MyAdminResources()
AdminServerManagement = MyAdminServerManagement()
AdminTask = MyAdminTask()
AdminUtilities = MyAdminUtilities()