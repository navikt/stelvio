import re

versionedModuleNames = '|'.join([
	'tjeneste',
	'produsent',
	'konsument',
	'prosess'
])
versionedREGEX = re.compile('-(%s)-' % versionedModuleNames)
processREGEX = re.compile('-prosess-|-microflow-|-bproc-|-bsrv-frg-hentinstitusjonsoppholdliste')
parseEarModuleNameREGEX = re.compile('^((?:.+-(%s)-)?.+)-((\d+)\.\d+\.\d+.*).ear$' % versionedModuleNames)

def isVersioned(moduleShortName):
	if versionedREGEX.search(moduleShortName):
		return True
	else:
		return False
		
def isProcess(moduleShortName):
	if processREGEX.search(moduleShortName):
		return True
	else:
		return False
		
def parseEarFileName(earFileName):
	shortName, versioned, version, majorVersion = parseEarModuleNameREGEX.match(earFileName).groups()
	return shortName, version, majorVersion, versioned