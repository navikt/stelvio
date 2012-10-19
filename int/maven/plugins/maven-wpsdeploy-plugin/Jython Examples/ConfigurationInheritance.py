import sys, os, re
from glob import glob
import lib.deployEnviromentUtil as env

def readProperties(dir):
	allLinesFromAllPropertiesFilesInFolder = []
	if os.path.exists(dir):
		lines = []
		for propertiesFile in glob(dir + "/*.properties"):
			file = open(propertiesFile)
			allLinesFromAllPropertiesFilesInFolder += file.read().splitlines()
			file.close()
		
	return allLinesFromAllPropertiesFilesInFolder
			
def extractProperties(lines, properties):
	for line in lines:
		if line.count('='):
			key, value = line.split('=',1)
			properties[key] = value

def writeProperties(properties, file):
	f = open(file, 'a+')
	for key, value in properties.items():
		f.write('%s=%s\n' % (key, value))
	f.close()

def main():
	inputDir = sys.argv[1]
	outputFile = sys.argv[2]
	allProperties = {}
	
	extractProperties(readProperties( inputDir ), allProperties)
	extractProperties(readProperties( inputDir +'/'+ env.getEnvClass() ), allProperties)
	extractProperties(readProperties( inputDir +'/'+ env.getEnvClass() +'/'+ env.getEnviroment() ), allProperties)
	
	writeProperties(allProperties, outputFile)

if __name__ == "__main__": main()