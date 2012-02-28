import time
import java
import re, sys, os
import traceback

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
		self.currentLogLevel = getLogLevelId()
		self.__timers = {}
		if scriptName == "__main__":
			self.scriptName = sys.argv[0]
		else:
			self.scriptName = scriptName + ".py"
		
	def timer(self, name=None, reset=False):
		if self.__timers.has_key(name):
			secondsPased = time.time() - self.__timers[name]
			
			if reset: self.__timers[name] = time.time()
			
			minutes, seconds = divmod(secondsPased, 60)
			hours, minutes = divmod(minutes, 60)
			return "%d:%02d:%02d" % (hours, minutes, seconds)
		else:
			self.__timers[name] = time.time()
			return 0
	
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
		self.__log(EXCEPTION, "Stacktrace:")
		traceback.print_tb(sys.exc_info()[2])
		self.__log(EXCEPTION, "Exception:")
		raise
		
	def println(self, *args):
		print ' '.join([str(x) for x in args])

	def __log(self, level, *args):
		if self.currentLogLevel < __availableLogLevels[level]: return
		print '[%s %s %s]'%(level, self.__time(), self.scriptName), ' '.join([str(x) for x in args])
		
	def __time(self): return time.strftime("%y.%m.%d-%H:%M:%S")

def getLogLevelId():
	logLevel = java.lang.System.getProperty('logging.level')
	if logLevel:
		for key, value in __availableLogLevels.items():
			if key == logLevel:
				return value
	return __defaultLogLevel
