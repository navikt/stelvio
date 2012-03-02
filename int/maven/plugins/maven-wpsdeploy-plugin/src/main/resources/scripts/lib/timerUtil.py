import time
class Timer:
	'''
		Usage:
			myTimer = timer()
			doStuff()
			print "It tok", myTimer, "seconds to finish doing stuff"
	'''
	def __init__(self):
		self.time = time.time()
		
	def reset(self):
		self.time = time.time()
		return self.__str__()
	
	def __str__(self):
		return __secondsToHMS(self.seconds)
	
	def seconds(self):
		return time.time() - self.time()
				
	def __secondsToHMS(secondsPased):
		minutes, seconds = divmod(secondsPased, 60)
		hours, minutes = divmod(minutes, 60)
		return "%d:%02d:%02d" % (hours, minutes, seconds)