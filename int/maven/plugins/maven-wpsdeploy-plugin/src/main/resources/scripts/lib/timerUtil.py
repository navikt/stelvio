import time
class Timer:
	def __init__(self):
		self.startTime = time.time()
		
	def reset(self):
		self.startTime = time.time()
		return self.__str__()
	
	def __str__(self):
		return self.__secondsToHMS(self.seconds())
		
	def HMS(self):
		return self.__secondsToHMS(self.seconds())	
	
	def seconds(self):
		return int(time.time() - self.startTime)
				
	def __secondsToHMS(self, secondsPased):
		minutes, seconds = divmod(secondsPased, 60)
		hours, minutes = divmod(minutes, 60)
		return "%d:%02d:%02d" % (hours, minutes, seconds)



