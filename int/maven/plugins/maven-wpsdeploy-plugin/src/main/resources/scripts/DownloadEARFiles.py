import sys, os
from lib.downloadUtil import downloadFile, getPage
from lib.pomUtil import Module, MavenRepository
from lib.XmlToUtil import pomXmlDependensiesToPomModules
import lib.logUtil as log
l = log.getLogger(__name__)
	
EAR_FILES = sys.argv[1]
POMS = sys.argv[2]
MVN_LOCAL_REPO = sys.argv[3]
MVN_REPO = sys.argv[4]
MVN_SNAPSHOT_REPO = sys.argv[5]

repo = MavenRepository(repository=MVN_REPO, snapshotRepository=MVN_SNAPSHOT_REPO, localRepository=MVN_LOCAL_REPO)

def main():
	if not os.path.exists(EAR_FILES): 
		l.info("Created directory", EAR_FILES)
		os.mkdir(EAR_FILES)
		
	for pom in POMS.split(','):
		l.info("Parsing", pom, "and downloading dependencies into %s:" % EAR_FILES)
		dependenciesModules = pomXmlDependensiesToPomModules(pom, repo)
		for m in dependenciesModules:
			EJBUrl = "%s/%s-%s.ear"% (m.getUrl(), m.artifactId, m.version)
			l.info("Downloading", EJBUrl)
			downloadFile(EJBUrl, EAR_FILES)
		
if __name__ == "__main__": main()