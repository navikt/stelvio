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

def isVersioned(moduleName):
	if versionedREGEX.search(moduleName):
		return True
	else:
		return False
		
def isProcess(moduleName):
	if processREGEX.search(moduleName):
		return True
	else:
		return False
		
def parseEarFileName(earFileName):
	shortName, versioned, version, majorVersion = parseEarModuleNameREGEX.match(earFileName).groups()
	return shortName, version, majorVersion, versioned