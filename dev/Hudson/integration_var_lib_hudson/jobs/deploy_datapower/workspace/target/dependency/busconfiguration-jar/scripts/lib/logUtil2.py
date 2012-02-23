from time import strftime
import re, sys, os

def getLogger(scriptName=None):
	return Logger(scriptName)

class Logger:
	'''Usage: l = getLogger(__name__)'''
	def __init__(self, scriptName=None):
		if scriptName:
			self.scriptName = scriptName + ".py"
		else:
			self.scriptName = re.search("-f\s+\S+/(\S+)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)

	__currentLogLevel = 3
	__availableLogLevels = {'DEBUG': 3, 'INFO': 2, 'WARNING':1, 'ERROR': 0}
	
	def __time(self): return strftime("%y.%m.%d-%H:%M:%S")

	def level(self,logLevel):
		'''	level("info") will hide all debug log messages, 
			level("warning") will make logUtil only display 
			warning and error messages, and so on...'''
		global __currentLogLevel
		__currentLogLevel = __availableLogLevels[logLevel.upper()]

	def info(self, *args):
		self.__log("INFO", *args)

	def warning(self, *args):
		self.__log("WARNING", *args)

	def debug(self, *args):
		self.__log("DEBUG", *args)

	def error(self, *args):
		self.__log("ERROR", *args)
		sys.exit(1)
		
	def print(self, *args):
		print ' '.join([str(x) for x in args])

	def __log(self, level, *args):
		if __currentLogLevel < __availableLogLevels[level]: return
		print '[%s %s %s] '%(level, self.__time(), scriptName) + ' '.join([str(x) for x in args])