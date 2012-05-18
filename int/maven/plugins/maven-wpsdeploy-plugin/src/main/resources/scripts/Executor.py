import sys, os, re
import exceptions
from lib.timerUtil import Timer
import lib.logUtil as log
import lib.exceptionUtil
import lib.ibmfixes

glo = globals()
loc = locals()
def Executor_main():
	scriptDir = Executor_getScriptDir()	
	scriptPath = scriptDir + Executor_getScriptName()
	Executor_executeScript(scriptPath)

def Executor_getScriptDir():
	IBM_JAVA_COMMAND_LINE = os.environ.get("IBM_JAVA_COMMAND_LINE")
	match = re.search("-f\s+(/?\S+/)?Executor.py", IBM_JAVA_COMMAND_LINE)
	if not match:
			raise Exception, "Could not find the folder where the scripts are located!"
	else:
			return match.group(1) or "./"

def Executor_getScriptName():
	if not sys.argv:
		raise ValueError, "This script needs to have the script it is supposed to execute as an argument!"
	return sys.argv[0]

def Executor_executeScript(scriptPath):
	scriptName = Executor_getScriptName()
	l = log.getLogger("Executor")
	l.info("Executing", scriptName)
	myTimer = Timer()
	
	try:
		execfile(scriptPath, glo, loc)
	except:
		ex = lib.exceptionUtil.getException()
		if ex.checkInstance(exceptions.SystemExit):
			exitCode = ex.getExitCode()
			l.println("Exitcode:", exitCode)
			sys.exit(exitCode)
		else:
			l.exception("Received an exception while executing script:", scriptName)
	l.info(scriptName, "took", myTimer, "to complete.")

Executor_main()
