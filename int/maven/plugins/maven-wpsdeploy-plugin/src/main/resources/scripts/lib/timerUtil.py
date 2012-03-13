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
		return "%dh %2dm %02ds" % (hours, minutes, seconds)



