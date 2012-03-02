import sys, os, re
import traceback
import exceptions

#False,True = 0,1 # Define False, True for executed script, imported libs still need to spesify them manually
glob= globals()
loc = locals()
glob['False'] = 0
glob['True'] = 1
def main_funnye():
	scriptDir = getScriptDir()	
	sys.path.append(scriptDir) #Essential to get the import statement to work as expected

	scriptPath = scriptDir + getScriptName()

	executeScript(scriptPath)

def getScriptDir():
	IBM_JAVA_COMMAND_LINE = os.environ.get("IBM_JAVA_COMMAND_LINE")
	match = re.search("-f\s+(/?\S+/)?Executor.py", IBM_JAVA_COMMAND_LINE)
	if not match:
			raise Exception, "Could not find the folder where the scripts are located!"
	else:
			return match.group(1) or "./"

def getScriptName():
	if not sys.argv:
		raise ValueError, "This script needs to have the script it is supposed to execute as an argument!"
	return sys.argv[0]

def executeScript(scriptPath):
	scriptName = getScriptName()
	from lib.timerUtil import Timer
	import lib.logUtil as log
	l = log.getLogger("Executor")
	l.info("Executing", scriptName)
	myTimer = Timer
	try:
		execfile(scriptPath, glob, loc)
	except:
		exceptionClass = sys.exc_info()[0]
		if exceptionClass != exceptions.SystemExit:
			l.exception("Received an exception while executing script:", scriptName)
	l.info(scriptName, "took", myTimer, "to complete.")

main_funnye()
