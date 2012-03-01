import os, re, sys
import traceback

False,True = 0,1 # Define False, True for executed script, imported libs still need to spesify them manually

def main():
	scriptDir = getScriptDir()	
	sys.path.append(scriptDir) #Essential to get the import statement to work as expected

	script = scriptDir + getScriptName()

	executeScript(script)

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

def executeScript(script):
	scriptName = getScriptName()
	from lib.timer import Timer
	import lib.logUtil as log
	l = log.getLogger("Executor")
	l.info("Executing", scriptName)
	try:
		myTimer = Timer
		execfile(script)
		l.info(getScriptName(), "took", myTimer, "to complete")
	except:
		l.exception("Received an exception while executing script:", scriptName)
	l.info("Finished executing", scriptName)

if __name__ == "__main__": main()
