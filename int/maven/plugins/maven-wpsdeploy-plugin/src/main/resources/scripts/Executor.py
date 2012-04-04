import sys, os, re
from lib.timerUtil import Timer
import lib.logUtil as log
import lib.exceptionUtil
import exceptions


# Define False, True for all scripts
try:
    True and False
except NameError:
    class bool(type(1)):
        def __init__(self, val = 0):
            if val:
                type(1).__init__(self, 1)
            else:
                type(1).__init__(self, 0)
        def __repr__(self):
            if self:
                return 'True'
            else:
                return 'False'

        __str__ = __repr__

    __builtin__.bool = bool
    __builtin__.False = bool(0)
    __builtin__.True = bool(1)
# End fix #3

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
