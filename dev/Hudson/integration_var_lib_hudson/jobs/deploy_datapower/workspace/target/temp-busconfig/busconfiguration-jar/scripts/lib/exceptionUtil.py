from __future__ import nested_scopes
def debug(*args, **kwargs): print "[DEBUG]", ', '.join(args)

class ExceptionHandlingWrapper:
	def __init__(self, admin):
		self.admin = admin
	def __getattr__(self, name):
		adminObjFunction = getattr(self.admin, name)
		callName = self.admin.__class__.__name__+'.'+name
		def tryWrapper(*args, **kwargs):
			debug("%s(%s)" % 
				(
					callName,
					', '.join([
						', '.join(args), 
						', '.join([k+'='+v for k,v in kwargs.items()])
					])
				)
			)
			try:
				return adminObjFunction(*args, **kwargs)
			except:
				type, value, tracebackObj = sys.exc_info()
				raise Exception, "Wrapped Exception type: [%s]\nException value:\n%s" % (type, value), tracebackObj
		return tryWrapper

if __name__ == "__main__":
	#Example usage:
	AdminControl = ExceptionHandlingWrapper(AdminControl)
	try:
		print AdminControl.invoke('WebSphere:name=nodeSync,process=nodeagent,platform=common,node=26apvl041Node01,diagnosticProvider=true,version=7.0.0.13,type=NodeSync,mbeanIdentifier=nodeSync,cell=U2Cell,spec=1.0', 'isNodeSynchronized')
	except Exception, e:
		print "my exception handled:\n%s" % e