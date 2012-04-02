from lib.XMLUtil import parseXML
	
def blaGroupXmlToStringList(blaGroupXmlFile):
	xml = parseXML(blaGroupXmlFile)
	return [module.get() for module in xml.findAll('module')]