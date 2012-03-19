import types
import os

import java.io.File as File
import java.io.StringReader as StringReader
import org.xml.sax.InputSource as InputSource
import javax.xml.parsers.DocumentBuilderFactory as DocumentBuilderFactory
import org.w3c.dom.Node as Node
# ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE, ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, DOCUMENT_FRAGMENT_NODE, NOTATION_NODE

False, True = 0,1 #Define False, True


def parseXML(xml):
	if isinstance(xml, Node):
		return XMLNode(xml, xml)
	elif isinstance(xml, types.StringType):
		if os.path.exists(xml):
			dom = __parseFile(xml)
		else:
			dom = __parseString(xml)
		return XMLNode(dom, dom)

class XMLNode:
	def __init__(self, xml, document):
		self.dom = xml
		self.document = document
		
	def getChild(self, nodeName):
		for node in self.getChildren():
			if node.getName() == nodeName:
				return node
		else:
			raise NodeNotFoundException('Could not find the "%s" child node you where looking for' % nodeName)
	def getChildren(self, path=""):
		'''Get direce descendants or get descendants by path:
			xml.getChildren('library/books/book') or
			library.getChildren('books/book') or
			books.getChildren()
			all gives [book1, book2, book3] as result
		'''
		if not path:
			return self.__javaToJythonList(self.dom.getChildNodes(), filterOutTextNodes=True)
		else:
			nodePaths = path.split('/')
			if not nodePaths:
				return None

			if nodePaths[0] == '':
				traveler = self.__makeInstance(self.document)
				nodePaths.pop(0)
			else:
				traveler = self
				
			for nodePath in nodePaths[:-1]:
				if not nodePath:
					traveler = traveler.fc()
				else:
					traveler = traveler.getChild(nodePath)

			return traveler.findAll(nodePaths[-1])

	def set(self, data):
		self.dom.getChildNodes().item(0).setNodeValue(data)

	def get(self): 
		if self.dom:
			return self.dom.item(0).getNodeValue()

	def attr(self, attr): 
		if self.dom:
			return self.dom.getAttributes().getNamedItem(attr)

	def fc(self):
		for c in self.getChildren():
			return c

	def append(self, data):
		self.set(self.get() + data)

	def findAll(self, tag):
		return self.__javaToJythonList(self.dom.getElementsByTagName(tag))
	
	def findFirst(self, tag): 
		for node in self.__javaToJythonList(self.dom.getElementsByTagName(tag)):
			return node

	def getName(self):
		return self.dom.getNodeName() 
	
	def getDom(self):
		return self.dom

	def each(self, function):
		out = []
		for node in self.getChildren():
			result = function(node)
			if result: 
				out.append(result)
		return out

	def getNS(self, id):
		return self.document.getAttributes().getNamedItem('xmlns:'+id)

	def __makeInstance(self, xml):
		return self.__class__(xml, self.document)

	def __javaToJythonList(self, stupidList, filterOutTextNodes=False):
		smartList = []
		for i in range(stupidList.getLength()):
			node = stupidList.item(i)
			if filterOutTextNodes:
				if node.getNodeType() == Node.TEXT_NODE:
					continue
			smartList.append(self.__makeInstance(node))
		return smartList
		
	def __add__(self, x):
		return self.__str__() + x
	def __radd__(self, x):
		return x + self.__str__()
		
	def __str__(self):
		return self.get()

def __makeFileObject(string):
	file = InputSource()
	file.setCharacterStream(StringReader(string))
	return file

def __parseString(xmlString): 
	return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(__makeFileObject(xmlString))

def __parseFile(xmlFile): 
	return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
	
class NodeNotFoundException(Exception):
	def __init__(self, value):
		self.parameter = value
	def __str__(self):
		return repr(self.parameter)