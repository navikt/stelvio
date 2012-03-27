from __future__ import nested_scopes

class AdminWrapper:
	'''Wrapper class for the wsadmin Admin* objects that logs the admin-commands.'''
	def __init__(self, adminObj, name):
		self.adminObj = adminObj
		self.name = name
		
	def __getattr__(self, attr):
		adminFunction = getattr(self.adminObj, attr)
		def wrapper(*args):
			argsString = ""
			if args: argsString = "'%s'" % "', '".join(args)
			print("[TEST] %s(%s)" % (self.name+'.'+attr,argsString))
			return adminFunction(*args)
		return wrapper
		
		
def AdminTask(logInstance):
	return AdminWrapper(AdminTask,'AdminTask')
def AdminApp(logInstance):
	return AdminWrapper(AdminApp,'AdminApp')
def AdminConfig(logInstance):
	return AdminWrapper(AdminConfig,'AdminConfig')
def AdminControl(logInstance):
	return AdminWrapper(AdminControl,'AdminControl')