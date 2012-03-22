from __future__ import nested_scopes 
import lib.logUtil as log
l = log.getLogger(__name__)
print "adminObjects.py dir():", dir()
global AdminTask, AdminApp, AdminApplication, AdminAuthorizations, AdminBLA, AdminClusterManagement, AdminConfig, AdminControl, AdminJ2C, AdminJDBC, AdminJMS, AdminLibHelp, AdminNodeGroupManagement, AdminNodeManagement, AdminResources, AdminServerManagement, AdminTask, AdminUtilities
class Extender:
	'''Wrapper class for the wsadmin Admin* objects that logs the admin-commands.
	Usage:
	from lib.myAdminObjects import myAdminTask
	AdminTask = myAdminTask()
	'''
	def __init__(self):
		global AdminTask, AdminApp, AdminApplication, AdminAuthorizations, AdminBLA, AdminClusterManagement, AdminConfig, AdminControl, AdminJ2C, AdminJDBC, AdminJMS, AdminLibHelp, AdminNodeGroupManagement, AdminNodeManagement, AdminResources, AdminServerManagement, AdminTask, AdminUtilities
		print "Extender class dir():",dir()
		self.__dict__['className'] = self.__class__.__name__.replace('My','',1)
		print 'self.adminObj = '+self.__dict__['className']
		exec('self.adminObj = '+self.__dict__['className'])
		
	def __getattr__(self, name):
		adminFunction = getattr(self.adminObj, name)
		def wrapper(*args):
			l.fine("%s('%s')" % (
					self.__dict__['className']+'.'+name,
					"', '".join(args)
				))
			return adminFunction(*args)
		return wrapper

class MyAdminTask(Extender): pass
class MyAdminApp(Extender): pass
class MyAdminApplication(Extender): pass
class MyAdminAuthorizations(Extender): pass
class MyAdminBLA(Extender): pass
class MyAdminClusterManagement(Extender): pass
class MyAdminConfig(Extender): pass
class MyAdminControl(Extender): pass
class MyAdminJ2C(Extender): pass
class MyAdminJDBC(Extender): pass
class MyAdminJMS(Extender): pass
class MyAdminLibHelp(Extender): pass
class MyAdminNodeGroupManagement(Extender): pass
class MyAdminNodeManagement(Extender): pass
class MyAdminResources(Extender): pass
class MyAdminServerManagement(Extender): pass
class MyAdminTask(Extender): pass
class MyAdminUtilities(Extender): pass