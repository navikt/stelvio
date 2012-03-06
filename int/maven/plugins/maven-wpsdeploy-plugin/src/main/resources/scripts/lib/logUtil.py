import time
import java
import re, sys, os
import traceback
import exceptions

False,True = 0,1 # Define False, True
DEBUGGING = "DEBUG"
INFOMATION = "INFO"
WARNING = "WARNING"
ERROR = "ERROR"
EXCEPTION = "EXCEPTION"

__loggers = {}
__availableLogLevels = {DEBUGGING: 4, INFOMATION: 3, WARNING:2, ERROR: 1, EXCEPTION: 0}
__defaultLogLevel = 3

def getLogger(scriptName):
	'''Usage: l = getLogger(__name__)'''
	if not __loggers.has_key(scriptName):
		__loggers[scriptName] = __Logger(scriptName)
	return __loggers[scriptName]

class __Logger:
	def __init__(self, scriptName):
		self.currentLogLevel = __getLogLevelId()
		if scriptName == "__main__":
			self.scriptName = sys.argv[0]
		else:
			self.scriptName = scriptName + ".py"
	
	def info(self, *args):
		self.__log(INFOMATION, *args)

	def warning(self, *args):
		self.__log(WARNING, *args)

	def debug(self, *args):
		self.__log(DEBUGGING, *args)

	def error(self, *args):
		self.__log(ERROR, *args)
		sys.exit(1)

	def exception(self, *args):
		self.__log(EXCEPTION, *args)
		traceback.print_exc()
		sys.exit(105)  #default wsadmin exit code
			
	def println(self, *args):
		print ' '.join([str(x) for x in args])

	def __log(self, level, *args):
		if self.currentLogLevel < __availableLogLevels[level]: return
		print '[%s %s %s]'%(level, self.__time(), self.scriptName), ' '.join([str(x) for x in args])
		
	def __time(self): return time.strftime("%y.%m.%d-%H:%M:%S")

	def __getLogLevelId():
		logLevel = java.lang.System.getProperty('logging.level')
		if logLevel:
			for key, value in __availableLogLevels.items():
				if key == logLevel.upper():
					self.info("[[Logging.level=%s]]" % key)
					return value
		return __defaultLogLevel
