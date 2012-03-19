from lib.XMLUtil import parseXML
from lib.pomUtil import PomModule
def pomXmlDependensiesToPomModules(xml, repository):
	modules = []
	
	xml = parseXML(pom)
	
	for d in xml.findAll("dependency"):
		artifactId = d.getChild('artifactId').get()
		groupId = d.getChild('groupId').get()
		version = d.getChild('version').get()
		type = d.getChild('type').get()
	
		modules.append(PomModule(artifactId=artifactId, groupId=groupId, version=version, type=type, repository=repository))
	return modules
	
def blaGroupXmlToStringList(xml):
	xml = parseXML(blaGroupFile)
	return [module.get() for module in xml.findAll('module')]