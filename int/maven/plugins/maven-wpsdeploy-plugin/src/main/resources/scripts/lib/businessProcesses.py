import time, re
import lib.logUtil as log
from lib.timerUtil import Timer
l = log.getLogger(__name__)
def getUniqueBPList():
	l.info("(getUniqueBPList) Executing...")
	myTimer = Timer()
	processComponents = AdminConfig.list('ProcessComponent').splitlines()

	tempEARs = []

	for pc in processComponents:
		temp = re.split('\(', pc)[1]
		temp = re.split('\)', temp)[0]
		temp = re.split('\|', temp)[0]
		temp = re.split('\/', temp)[5]
		tempEARs.append(temp)

	if (tempEARs):
		tempEARs.sort()
		last = tempEARs[-1]
		for i in range(len(tempEARs)-2, -1, -1):
			if (last == tempEARs[i]):
				del tempEARs[i]
			else:
				last = tempEARs[i]
				
	processEARs = {}
	pattern = re.compile(r'\s+')
	for pc in tempEARs:
		buildVersion = re.sub(pattern, '', AdminApp.view(pc, '-buildVersion').replace('Application Build ID:  ', ''))
		if (pc.count("App") > 0):
			temp_name = re.split("App",pc)[0]
			processEARs[temp_name] = buildVersion
		else:
			match = re.search("-" + "(\d+\.)+\d+$", pc)
			if (match):
				processEARs[pc[:match.start()]] = buildVersion

	stop = time.clock()
	l.info(processEARs)
	l.info("(getUniqueBPList) Time elapsed:", myTimer, "sec")
	return processEARs
