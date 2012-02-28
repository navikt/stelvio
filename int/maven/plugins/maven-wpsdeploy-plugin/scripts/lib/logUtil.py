from time import strftime
import re, sys, os

scriptName = re.search("-f\s+\S+/(\S+)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)
__availableLogLevels = {'DEBUG': 3, 'INFO': 2, 'WARNING':1, 'ERROR': 0}
__currentLogLevel = 3

def __time(): return strftime("%y.%m.%d-%H:%M:%S")

def level(logLevel):
	'''	level("info") will hide all debug log messages, 
		level("warning") will make logUtil only display 
		warning and error messages, and so on...'''
	global __currentLogLevel
	__currentLogLevel = __availableLogLevels[logLevel.upper()]

def info(*args):
	__log("INFO", *args)

def warning(*args):
	__log("WARNING", *args)

def debug(*args):
	__log("DEBUG", *args)

def error(*args):
	__log("ERROR", *args)
	sys.exit(1)

def __log(level, *args):
	if __currentLogLevel < __availableLogLevels[level]: return
	print '[%s %s %s] '%(level, __time(), scriptName) + ' '.join([str(x) for x in args])