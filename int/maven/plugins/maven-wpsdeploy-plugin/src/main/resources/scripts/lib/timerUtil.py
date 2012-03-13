import time
class Timer:
	def __init__(self):
		self.startTime = time.time()
		
	def reset(self):
		self.startTime = time.time()
		return self.__str__()
	
	def __str__(self):
		return self.__secondsToHMS(self.seconds())
		
	def seconds(self):
		return round(time.time() - self.startTime, 0)
				
	def __secondsToHMS(self, secondsPased):
		minutes, seconds = divmod(secondsPased, 60)
		hours, minutes = divmod(minutes, 60)
		if hours > 0:
			return "%d hours %d minutes %d seconds" % (hours, minutes, seconds)
		elif minutes > 0:
			return "%d minutes %d seconds" % (minutes, seconds)
		else:
			return "%d seconds" % (hours, minutes, seconds)