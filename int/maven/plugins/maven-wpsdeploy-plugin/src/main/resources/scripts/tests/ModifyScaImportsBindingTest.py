from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import ModifySCAImportsBinding as msib
import mocks.AdminTaskMock as AdminTaskMock

msib.AdminTask = AdminTaskMock
	
def listSCAImportsTest():
	class ScaModule: moduleName = 'ignoredInput'
	scaImports = msib.listSCAImports(ScaModule())
	assertEqual(scaImports, ['NotatPortTypeWSIMP', 'PersonPortTypeWSIMP', 'UtbetalingPortTypeWSIMP', 'SakVedtakPortTypeWSIMP', 'ArenaWSIMP', 'OrganisasjonPortTypeWSIMP', 'OppgavePortTypeWSIMP'])
	
def modifySCAImportBindingTest():
	msib.modifySCAImportBinding('module_name', 'import_name', 'serverAddress')
	assert True
	
def swapEndpointServerAddressTest():
	myServerAddress = 'http://myNewServeradress:1337'
	newEndpoint = msib.swapEndpointServerAddress('ignoredInput', 'ignoredInput', myServerAddress)
	assertEqual(newEndpoint, myServerAddress + '/arena_ws/services/ArenaNotatService')
	
def getEndpointPathTest():
	endpoint = msib.getEndpointPath('ignoredInput', 'ignoredInput')
	assertEqual(endpoint, '/arena_ws/services/ArenaNotatService')