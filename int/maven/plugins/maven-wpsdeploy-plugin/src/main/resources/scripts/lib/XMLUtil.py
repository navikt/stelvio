import types
import os

import java.io.File as File
import java.io.StringReader as StringReader
import org.xml.sax.InputSource as InputSource
import javax.xml.parsers.DocumentBuilderFactory as DocumentBuilderFactory
import org.w3c.dom.Node as Node
# ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE, ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, DOCUMENT_FRAGMENT_NODE, NOTATION_NODE

False, True = 0,1 # Define False, True

def getChildNodeValue(element, nodeName):
	elements = element.getChildNodes()
	for i in range(elements.getLength()):
		e = elements.item(i)
		if e.getNodeName() == nodeName: 
			return e.getChildNodes().item(0).getNodeValue()

def getElementsByTagName(xml, tagName):
	tagsObj = xml.getElementsByTagName(tagName)
	tagList = []
	for i in range(tagsObj.getLength()): 
		tagList.append(tagsObj.item(i))
	return tagList

class XMLParser:
	def __init__(self, xml, document=None):
		if isinstance(xml, Node):
			self.dom = xml
			self.document = document
		elif isinstance(xml, types.StringType):
			if os.path.exists(xml):
				self.document = self.dom = __parseFile(xml)
			else:
				self.document = self.dom = __parseString(xml)

	def getChild(self, nodeName):
			for node in self.getChildren():
					if node.getName() == nodeName:
							return node

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
			traveler = self
			if path.startswith('/'):
				xml = self.__makeInstance(self.document)
			else:
				xml = self
				
			nodePaths = path.split('/')
			if not nodePaths:
				return None
				
			for nodePath in nodePaths[:-1]:
				if not nodePath:
					traveler = xml.fc()
				else:
					traveler = xml.findFirst(nodePath)
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
			out.append(function(node))

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