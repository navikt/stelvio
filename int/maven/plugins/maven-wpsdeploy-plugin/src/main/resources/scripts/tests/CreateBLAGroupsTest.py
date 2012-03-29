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

def addUnitToGroupTest():
	assertTrue(cbg.addUnitToGroup(scaModule1, 'ignored input'))
	
def createNewBLAGroupTest():
	assertTrue(cbg.createNewBLAGroup('ignored input'))
	
def listToCSVTest():
	assertEqual(cbg.listToCSV(['a','b','c','d','e']), 'a, b, c, d, e', 'Could not convert string array to CSV')
	assertEqual(cbg.listToCSV([1,2,3,4,5]), '1, 2, 3, 4, 5', 'Could not convert integer array to CSV')