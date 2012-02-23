import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs

from lib.downloadUtil import downloadFile, getPage
import lib.javaXMLUtil as XML
import lib.logUtil as l
from lib.pomUtil import Module
	
EARFFILES = sys.argv[0]
POMS = sys.argv[1]
MVNLOCALREPO = sys.argv[2]
MVNREPO = sys.argv[3]
MVNSNAPSHOTREPO = sys.argv[4]

def pomDependenciesToModuleList(pom):
	modules = []
	
	xml = XML.parseFile(pom)
	dependencyElements = XML.getElementsByTagName(xml, "dependency")
	
	for d in dependencyElements:
		artifactId = XML.getChildNodeValue(d, 'artifactId')
		groupId = XML.getChildNodeValue(d, 'groupId')
		version = XML.getChildNodeValue(d, 'version')
		type = XML.getChildNodeValue(d, 'type')
	
		modules.append(Module(artifactId=artifactId, groupId=groupId, version=version,type=type, localRepo=MVNLOCALREPO, repo=MVNREPO, snapshotRepo=MVNSNAPSHOTREPO))
	return modules

def main():
	if not os.path.exists(EARFFILES): 
		l.info("Created directory", EARFFILES)
		os.mkdir(EARFFILES)
		
	for pom in POMS.split(','):
		l.info("Parsing", pom, "and downloading dependencies into %s:" % EARFFILES)
		dependenciesModules = pomDependenciesToModuleList(pom)
		for m in dependenciesModules:
			EJBUrl = "%s/%s-%s.ear"% (m.getUrl(), m.artifactId, m.version)
			l.info("Downloading", EJBUrl)
			downloadFile(EJBUrl, EARFFILES)
		
if __name__ == "__main__": 
	if len(sys.argv) < 4: sys.exit('Minimum 4 arguments: downloadDir pomsString mvnRepo mvnSnapshotRepo')
	main()