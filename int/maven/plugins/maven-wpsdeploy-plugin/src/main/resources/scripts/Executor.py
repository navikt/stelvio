import sys, os, re
import traceback

False,True = 0,1 # Define False, True for executed script, imported libs still need to spesify them manually

def main():
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
	from lib.timer import Timer
	import lib.logUtil as log
	l = log.getLogger("Executor")
	l.info("Executing", scriptName)
	try:
		l.info("Starting", scriptName + "...")
		myTimer = Timer
		execfile(scriptPath)
		l.info(scriptName, "took", myTimer, "to complete.")
	except:
		l.exception("Received an exception while executing script:", scriptName)

if __name__ == "__main__": main()
