import java.io.File as File
import java.io.StringReader as StringReader
import org.xml.sax.InputSource as InputSource
import javax.xml.parsers.DocumentBuilderFactory as DocumentBuilderFactory

def makeFileObject(string):
	file = InputSource();
	file.setCharacterStream(StringReader(string));
	return file
	
def parseString(xmlString): return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(makeFileObject(xmlString))

def parseFile(xmlFile): return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
	
def getChildNodeValue(element, nodeName):
	elements = element.getChildNodes()
	for i in range(elements.getLength()):
		e = elements.item(i)
		if e.getNodeName() == nodeName: 
			return e.getChildNodes().item(0).getNodeValue()

def getElementsByTagName(xml, tagName):
	tagsObj = xml.getElementsByTagName(tagName)
	tagList = []
	for i in range(tagsObj.getLength()): tagList.append(tagsObj.item(i))
	return tagList