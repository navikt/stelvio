import os.environ
import re
import time
from time import strftime

ARG_LIST		= sys.argv[1].split(";")

from lib.saveUtil import save
from lib.timerUtil import Timer
import lib.logUtil as log
l = log.getLogger(__name__)


def main():
	myTimer = Timer()
	listen = AdminConfig.list('J2CActivationSpec').split(java.lang.System.getProperty('line.separator'))


	listenMap = {}

	for ent in listen:
		key = ent.split("(")[0]
		listenMap[key] = ent;

	for arg in ARG_LIST:
		temp = arg.split("::");
		ACTIVATION_RECORD = temp[0]
		MAX_CONCURRENCY = temp[1]
		ent = listenMap[ACTIVATION_RECORD]
		identifier = AdminConfig.showAttribute(ent, 'name')
		res = AdminConfig.showAttribute(ent, 'resourceProperties').split(java.lang.System.getProperty(' '))
		for r in res:
			if(r.find('maxConcurrency') >= 0 ):
				try:
					l.info("Modified max concurrency resource property for " +ACTIVATION_RECORD)
					modifyResourceProperty(r, "maxConcurrency", MAX_CONCURRENCY, "java.lang.Integer")
					break
				except:
					l.exception("Error modifying maxConcurrency resource property for activation record!")


	save()
	l.info("Time elapsed:", myTimer)


def modifyResourceProperty (propSet, propName, propValue, propType):
	attrs = []
	attrs.append(["name", propName])
	attrs.append(["value", propValue])
	attrs.append(["type", propType])

	try:
		result = AdminConfig.modify(propSet, attrs )
	except:
		l.exception("Caught Exception modifying ResourceProperty!")

main()