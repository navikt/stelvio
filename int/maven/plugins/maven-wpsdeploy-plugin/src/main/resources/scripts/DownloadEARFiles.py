import sys, os
from lib.downloadUtil import downloadFile, getPage
from lib.pomUtil import Module
import lib.javaXMLUtil as XML
import lib.logUtil as log
l = log.getLogger(__name__)
	
EAR_FILES = sys.argv[1]
POMS = sys.argv[2]
MVN_LOCAL_REPO = sys.argv[3]
MVN_REPO = sys.argv[4]
MVN_SNAPSHOT_REPO = sys.argv[5]

def pomDependenciesToModuleList(pom):
	modules = []
	
	xml = XML.parseFile(pom)
	dependencyElements = XML.getElementsByTagName(xml, "dependency")
	
	for d in dependencyElements:
		artifactId = XML.getChildNodeValue(d, 'artifactId')
		groupId = XML.getChildNodeValue(d, 'groupId')
		version = XML.getChildNodeValue(d, 'version')
		type = XML.getChildNodeValue(d, 'type')
	
		modules.append(Module(artifactId=artifactId, groupId=groupId, version=version,type=type, localRepo=MVN_LOCAL_REPO, repo=MVN_REPO, snapshotRepo=MVN_SNAPSHOT_REPO))
	return modules

def main():
	if not os.path.exists(EAR_FILES): 
		l.info("Created directory", EAR_FILES)
		os.mkdir(EAR_FILES)
		
	for pom in POMS.split(','):
		l.info("Parsing", pom, "and downloading dependencies into %s:" % EAR_FILES)
		dependenciesModules = pomDependenciesToModuleList(pom)
		for m in dependenciesModules:
			EJBUrl = "%s/%s-%s.ear"% (m.getUrl(), m.artifactId, m.version)
			l.info("Downloading", EJBUrl)
			downloadFile(EJBUrl, EAR_FILES)
		
if __name__ == "__main__": main()