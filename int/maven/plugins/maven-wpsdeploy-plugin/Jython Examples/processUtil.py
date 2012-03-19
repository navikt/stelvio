import time

False, True = 0,1 # Define False, True

def run(cmd):
	''' Use Java exec command to run a script due to Jython 2.1 limitations'''
	process = java.lang.Runtime.getRuntime().exec(cmd)
	stdoutstream = '' 
	errorstream = ''
	while True: 
		while process.getInputStream().available():
			stdoutstream += chr(process.getInputStream().read())
		while process.getErrorStream().available():
			errorstream += chr(process.getErrorStream().read())
		try: 
			exitValue = process.exitValue()
			return Process(stdoutstream.splitlines(), errorstream.splitlines(), exitValue)
		except java.lang.IllegalThreadStateException, e: 
			# This exception means the process is still running
			pass
		time.sleep(0.1)

class Process:
	def __init__(self, stdout, stderr, exitValue):
		self.stdout = stdout
		self.stderr = stderr
		self.exitValue = exitValue
	def __nonzero__(self):
		return self.exitValue == 0
	def exitOK(self):
		return self.__nonzero__()