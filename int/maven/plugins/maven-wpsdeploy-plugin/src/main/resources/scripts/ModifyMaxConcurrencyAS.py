import sys
from time import strftime
from lib.saveUtil import save
from lib.parseConfiguration import parseActivationspecifications
import lib.logUtil as log
l = log.getLogger(__name__)

def main():
	activationspecificationsPath = sys.argv[1]
	activationspecConfig = parseActivationspecifications(activationspecificationsPath)
	listenMap = getJ2CActivationSpecs()

	for moduleName, maxConcurrency in activationspecConfig.items():		
		J2CActivationSpec = listenMap[moduleName]
		
		resourceProperties = AdminConfig.showAttribute(J2CActivationSpec, 'resourceProperties').split()
		for resourcePropertie in resourceProperties:
			if resourcePropertie.startswith('maxConcurrency'):
				try:
					l.info("Changed the max concurrency resource property for", moduleName, "to", maxConcurrency)
					modifyResourceProperty(resourcePropertie, "maxConcurrency", maxConcurrency, "java.lang.Integer")
					break
				except:
					l.exception("Error modifying maxConcurrency resource property for activation record!")

	save()

def getJ2CActivationSpecs():
	J2CActivationSpecs = AdminConfig.list('J2CActivationSpec').splitlines()
	out = {}
	for J2CActivationSpec in J2CActivationSpecs:
		moduleName = J2CActivationSpec.split("(")[0]
		out[moduleName] = J2CActivationSpec
		
	return out


def modifyResourceProperty (propSet, propName, propValue, propType):
	attrs = []
	attrs.append(["name", propName])
	attrs.append(["value", propValue])
	attrs.append(["type", propType])

	try:
		AdminConfig.modify(propSet, attrs)
	except:
		l.exception("Caught Exception modifying ResourceProperty!")

if __name__ == '__main__': main()