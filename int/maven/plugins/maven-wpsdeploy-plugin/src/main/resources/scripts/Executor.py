import sys, os, re
import traceback
import exceptions

glob= globals()
loc = locals()
# Define False, True for executed script, imported libs still need to spesify them manually
glob['False'] = 0
glob['True'] = 1
def main_Executor():
	scriptDir = getScriptDir_Executor()	
	sys.path.append(scriptDir) #Essential to get the import statement to work as expected

	scriptPath = scriptDir + getScriptName_Executor()

	executeScript_Executor(scriptPath)

def getScriptDir_Executor():
	IBM_JAVA_COMMAND_LINE = os.environ.get("IBM_JAVA_COMMAND_LINE")
	match = re.search("-f\s+(/?\S+/)?Executor.py", IBM_JAVA_COMMAND_LINE)
	if not match:
			raise Exception, "Could not find the folder where the scripts are located!"
	else:
			return match.group(1) or "./"

def getScriptName_Executor():
	if not sys.argv:
		raise ValueError, "This script needs to have the script it is supposed to execute as an argument!"
	return sys.argv[0]

def executeScript_Executor(scriptPath):
	scriptName = getScriptName_Executor()
	from lib.timerUtil import Timer
	import lib.logUtil as log
	l = log.getLogger("Executor")
	l.info("Executing", scriptName)
	myTimer = Timer()
	try:
		execfile(scriptPath, glob, loc)
	except:
		exceptionInstance = sys.exc_info()[1]
		if isinstance(exceptionInstance, exceptions.SystemExit):
			exitCode = exceptionInstance.code
			l.println("Exitcode:", exitCode)
			sys.exit(exitCode)
		else:
			l.exception("Received an exception while executing script:", scriptName)
	l.info(scriptName, "took", myTimer, "to complete.")

main_Executor()
