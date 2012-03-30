from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
from lib.scaModuleUtil import ScaModule
import CreateBLAGroups as cbg

False, True = 0,1 #Define False, True

import mocks.AdminTaskMock as AdminTaskMock
cbg.AdminTask = AdminTaskMock

DEFAULT_MODULE_NAME = 'ekstern-pensjon-tjeneste-beregning'

def __sca():
	return [ScaModule(DEFAULT_MODULE_NAME, 1), ScaModule(DEFAULT_MODULE_NAME, 2)]
cbg.getInstalledModules = __sca

scaModule1 = ScaModule(DEFAULT_MODULE_NAME, 1)
scaModule2 = ScaModule(DEFAULT_MODULE_NAME, 2)

def filterOutNonGroupXmlFilesTest():
	list = cbg.filterOutNonGroupXmlFiles(['SAM-group.xml', 'SkattOgTrekk-group.xml', 'README.txt', 'PEN-group.xml', 'POPP-group.xml'])
	assertContains(list, 'SAM-group.xml', 'Could not find file that should be in the list')
	assertEqual(len(list), 4, 'Found more files than expected')
	
def getIntersectionTest():
	blaModules =  ['ekstern-pensjon-tjeneste-beregning','ekstern-pensjon-tjeneste-person','nav-ent-pen-beregning','nav-ent-pen-grunnlag','nav-ent-pen-krav','nav-ent-pen-pensjonsberegning','nav-ent-pen-pensjonsgrunnlag','nav-ent-pen-pensjonskrav','nav-ent-pen-pensjonsskjema','nav-ent-pen-pensjonsvedtak','nav-ent-pen-person','nav-ent-test-get','nav-prod-pen-pen','pensjon-tjeneste-beregning','pensjon-tjeneste-grunnlag','pensjon-tjeneste-iverksetting','pensjon-tjeneste-krav','pensjon-tjeneste-sak','pensjon-tjeneste-tilbakekrevingHendelse','pensjon-tjeneste-vedtak']
	scaModules = [ScaModule('pensjon-tjeneste-beregning', 1), ScaModule('pensjon-tjeneste-beregning', 2), ScaModule('nav-tjeneste-oppgave', 2), ScaModule('nav-ent-pen-grunnlag', None), ScaModule('nav-ent-pen-krav', None)]
	intersection= cbg.getIntersection(blaModules, scaModules)
	assertContains(intersection, ScaModule('nav-ent-pen-grunnlag', None), 'Did not find the expected module in the list!')
	assertEqual(len(intersection), 4, 'The list whas of unexpected length')

def addUnitToGroupTest():
	assertTrue(cbg.addUnitToGroup(scaModule1, 'ignored input'))
	
def createNewBLAGroupTest():
	assertTrue(cbg.createNewBLAGroup('ignored input'))
	
def listToCSVTest():
	assertEqual(cbg.listToCSV(['a','b','c','d','e']), 'a, b, c, d, e', 'Could not convert string array to CSV')
	assertEqual(cbg.listToCSV([1,2,3,4,5]), '1, 2, 3, 4, 5', 'Could not convert integer array to CSV')

