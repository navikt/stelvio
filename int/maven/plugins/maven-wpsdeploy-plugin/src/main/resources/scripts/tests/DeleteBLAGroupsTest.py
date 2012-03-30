from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import DeleteBLAGroups as dbg
import mocks.AdminTaskMock as AdminTaskMock

dbg.AdminTask = AdminTaskMock

False, True = 0,1 #Define False, True

blaGroups = dbg.getBLAGrups()
def getBLAGrupsTest():
	assertEqual(blaGroups, ['WebSphere:blaname=PEN-group'])

def getBLAGroupsLengthTest():
	assertEqual(len(blaGroups), 1)

compGroup = dbg.getCompUnitsInGroup('ignored input')
def getCompUnitsTest():
	assertEqual(compGroup[0], 'WebSphere:cuname=PEN-group_ekstern-pensjon-tjeneste-beregning_v1', 'The first item in the list did not mach the expected result')
	assertEqual(len(compGroup), 20, 'The list whas not the expected lenght')
	
def deleteCompUnitTest():
	assertTrue(dbg.deleteCompUnit('ignored input','ignored input'))
	
def deleteEmptyBlaGroupTest():
	assertTrue(dbg.deleteEmptyBlaGroup('ignored input'))