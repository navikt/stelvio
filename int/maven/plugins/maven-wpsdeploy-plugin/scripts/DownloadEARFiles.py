import sys, os
from lib.downloadUtil import downloadFile, getPage
from lib.pomUtil import Module
import lib.javaXMLUtil as XML
import lib.logUtil as log
l = log.getLogger(__name__)
	
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
	main()