import sys
import traceback

def getException():
	return myException()

class myException:
	def __init__(self):
		self.type, self.instance, self.traceback = sys.exc_info()
		
	def __str__(self):
		return ' '.join(traceback.format_exception(self.type, self.instance, self.traceback))
		
	def printException(self):
		print self.__str__()
		
	def getExitCode(self):
		if hasattr(self.instance, 'code'):
			return self.instance.code
		else:
			return 105 #default wsadmin error exit code
				
	def checkInstance(self, instanceType):
		return isinstance(self.instance, instanceType)
