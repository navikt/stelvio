from lib.testUtil import assertEqual, assertTrue, assertFalse, assertRegex
import DeleteBLAGroups as dbg
import mocks.AdminTaskMock as AdminTaskMock

dbg.AdminTask = AdminTaskMock

False, True = 0,1 #Define False, True

def getBLAGrupsTest():
	blaGroups = dbg.getBLAGrups()
	assertEqual(blaGroups[0], 'WebSphere:blaname=PEN-group')