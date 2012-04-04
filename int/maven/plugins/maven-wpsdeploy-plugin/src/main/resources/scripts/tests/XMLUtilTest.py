from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
from lib.XMLUtil import parseXML, NodeNotFoundException


xml = parseXML('''<configuration xmlns="http://www.nav.no/pensjonsprogrammet/wpsconfiguration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.nav.no/pensjonsprogrammet/wpsconfiguration moduleconfig.xsd">
	<module>ekstern-pensjon-tjeneste-beregning</module>
	<module>ekstern-pensjon-tjeneste-person</module>
	<module>nav-ent-pen-beregning</module>
	<module>nav-ent-pen-grunnlag</module>
	<module>nav-ent-pen-krav</module>
	<module>nav-ent-pen-pensjonsberegning</module>
	<module>nav-ent-pen-pensjonsgrunnlag</module>
	<module>nav-ent-pen-pensjonskrav</module>
	<module>nav-ent-pen-pensjonsskjema</module>
	<module>nav-ent-pen-pensjonsvedtak</module>
	<module>nav-ent-pen-person</module>
	<module>nav-ent-test-get</module>
	<module>nav-prod-pen-pen</module>
	<module>pensjon-tjeneste-beregning</module>
	<module>pensjon-tjeneste-grunnlag</module>
	<module>pensjon-tjeneste-iverksetting</module>
	<module>pensjon-tjeneste-krav</module>
	<module>pensjon-tjeneste-sak</module>
	<module>pensjon-tjeneste-tilbakekrevingHendelse</module>
	<module>pensjon-tjeneste-vedtak</module>
	<test>
		<testLevel1>
			<testLevel2>
				<textLevel>My text</textLevel>
			</testLevel2>
		</testLevel1>
	</test>
</configuration>''')
modules = ['ekstern-pensjon-tjeneste-beregning',
'ekstern-pensjon-tjeneste-person',
'nav-ent-pen-beregning',
'nav-ent-pen-grunnlag',
'nav-ent-pen-krav',
'nav-ent-pen-pensjonsberegning',
'nav-ent-pen-pensjonsgrunnlag',
'nav-ent-pen-pensjonskrav',
'nav-ent-pen-pensjonsskjema',
'nav-ent-pen-pensjonsvedtak',
'nav-ent-pen-person',
'nav-ent-test-get',
'nav-prod-pen-pen',
'pensjon-tjeneste-beregning',
'pensjon-tjeneste-grunnlag',
'pensjon-tjeneste-iverksetting',
'pensjon-tjeneste-krav',
'pensjon-tjeneste-sak',
'pensjon-tjeneste-tilbakekrevingHendelse',
'pensjon-tjeneste-vedtak']

def findAllTest():
	all = [str(x) for x in xml.findAll('module')]
	assertEqual(modules, all)	

def findFirstTest():
	assertEqual(modules[0], xml.findFirst('module'))


def firstChildTest():
	assertEqual(modules[0], xml.fc().fc())

def eachTest():
	customNodeList = [x+"myNode" for x in modules]
	def p(node):
		if node.getName() == 'module':
			return node+"myNode"
	assertEqual(customNodeList, xml.fc().each(p))

def getChildExceptionTest():
	assertRaises(NodeNotFoundException, xml.getChild, 'nonExistingChild')

def getChildrenFromFullPathTest():
	module = xml.fc().fc()
	fullPathChildren = [str(n) for n in module.getChildren('/configuration/module')]
	assertEqual(modules, fullPathChildren)

def getChildrenFromFullPathFromChildLevelTest():
	fullPathChildren = [str(n) for n in xml.fc().fc().getChildren('/configuration/module')]
	assertEqual(modules, fullPathChildren)

def getChildrenFromRelativePathTest():
	root = xml
	fullPathChildren = [str(n) for n in root.getChildren('configuration/module')]
	assertEqual(modules, fullPathChildren)

def getChildrenFromFullWhildcardPathTest():
	whildcardChildren = [str(n) for n in xml.fc().fc().getChildren('//test/testLevel1/testLevel2/textLevel')]
	assertEqual(['My text'], whildcardChildren)

def getChildrenFromRelativeWhildcardPathTest():
	whildcardChildren = [str(n) for n in xml.fc().getChildren('test/testLevel1/testLevel2/textLevel')]
	assertEqual(['My text'], whildcardChildren)

def getChildrenTest():
	children = [str(n) for n in xml.fc().getChildren() if n.getName() == 'module']
	assertEqual(modules, children)

def getChildTest():
	child = xml.fc().getChild('module')
	assertEqual(modules[0], child)
