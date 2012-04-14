from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import lib.scaModuleUtil as smu
import mocks.AdminTaskMock as AdminTaskMock
import mocks.AdminAppMock as AdminApp
import mocks.osMock as os
import mocks.openMock as open
		
smu.os = os
smu.AdminTask = AdminTaskMock
smu.AdminApp = AdminApp
smu.open = open.open

VERSIONED_SHORT_NAME = 'ekstern-pensjon-tjeneste-beregning'
VERSIONED_MODULE_NAME = 'ekstern-pensjon-tjeneste-beregning_v1'
VERSIONED_APLICATION_NAME = 'ekstern-pensjon-tjeneste-beregning_v1App'
VERSIONED_VERSION = '1.1.3'
VERSIONED_SCA_VERSION = 1

UNVERSIONED_SHORT_NAME = 'nav-bsrv-frg-finntjenestepensjonsforhold'
UNVERSIONED_MODULE_NAME = 'nav-bsrv-frg-finntjenestepensjonsforhold'
UNVERSIONED_APLICATION_NAME = 'nav-bsrv-frg-finntjenestepensjonsforholdApp'
UNVERSIONED_VERSION = '7.4.0'
UNVERSIONED_SCA_VERSION = None

def getInstalledModulesTest():
	scaModules = smu.getInstalledModules()
	s = scaModules[0]
	assertEqual(s.moduleName, VERSIONED_MODULE_NAME, 'moduleName %s whas not equal to %s' % (s.moduleName, VERSIONED_MODULE_NAME))
	assertEqual(s.applicationName, VERSIONED_APLICATION_NAME, 'applicationName %s whas not equal to %s' % (s.applicationName, VERSIONED_APLICATION_NAME))
	assertEqual(s, VERSIONED_APLICATION_NAME, '__str__() %s whas not equal to %s' % (s, VERSIONED_APLICATION_NAME))
	assertEqual(s.shortName, VERSIONED_SHORT_NAME, 'shortName %s whas not equal to %s' % (s.shortName, VERSIONED_SHORT_NAME))
	assertEqual(s.version, '0.0.0', 'version %s whas not equal to %s' % (s.version, '0.0.0'))
	assertEqual(s.scaVersion, VERSIONED_SCA_VERSION, 'scaVersion %s whas not equal to %s' % (s.scaVersion, VERSIONED_SCA_VERSION))
	
def getModulesToBeInstalledVersionedTest():
	scaModules = smu.getModulesToBeInstalled('ignored input')
	s = scaModules[256]
	assertEqual(s.moduleName, VERSIONED_MODULE_NAME, 'moduleName %s whas not equal to %s' % (s.moduleName, VERSIONED_MODULE_NAME))
	assertEqual(s.applicationName, VERSIONED_APLICATION_NAME, 'applicationName %s whas not equal to %s' % (s.applicationName, VERSIONED_APLICATION_NAME))
	assertEqual(s, VERSIONED_APLICATION_NAME, '__str__() %s whas not equal to %s' % (s, VERSIONED_APLICATION_NAME))
	assertEqual(s.shortName, VERSIONED_SHORT_NAME, 'shortName %s whas not equal to %s' % (s.shortName, VERSIONED_SHORT_NAME))
	assertEqual(s.version, VERSIONED_VERSION, 'version %s whas not equal to %s' % (s.version, VERSIONED_VERSION))
	assertEqual(s.scaVersion, VERSIONED_SCA_VERSION, 'scaVersion %s whas not equal to %s' % (s.scaVersion, VERSIONED_SCA_VERSION))
	
def getModulesToBeInstalledUnversionedTest():
	scaModules = smu.getModulesToBeInstalled('ignored input')
	s = scaModules[136]
	assertEqual(s.moduleName, UNVERSIONED_MODULE_NAME, 'moduleName %s whas not equal to %s' % (s.moduleName, UNVERSIONED_MODULE_NAME))
	assertEqual(s.applicationName, UNVERSIONED_APLICATION_NAME, 'applicationName %s whas not equal to %s' % (s.applicationName, UNVERSIONED_APLICATION_NAME))
	assertEqual(s, UNVERSIONED_APLICATION_NAME, '__str__() %s whas not equal to %s' % (s, UNVERSIONED_APLICATION_NAME))
	assertEqual(s.shortName, UNVERSIONED_SHORT_NAME, 'shortName %s whas not equal to %s' % (s.shortName, UNVERSIONED_SHORT_NAME))
	assertEqual(s.version, UNVERSIONED_VERSION, 'version %s whas not equal to %s' % (s.version, UNVERSIONED_VERSION))
	assertEqual(s.scaVersion, UNVERSIONED_SCA_VERSION, 'scaVersion %s whas not equal to %s' % (s.scaVersion, UNVERSIONED_SCA_VERSION))

def equalVersionedTest():
	scaModule = smu.ScaModule(VERSIONED_SHORT_NAME, '0.0.0', 1)
	assertTrue(scaModule == VERSIONED_APLICATION_NAME)
	
def equalUnversionedTest():
	scaModule = smu.ScaModule(UNVERSIONED_SHORT_NAME, '0.0.0', None)
	assertTrue(scaModule == UNVERSIONED_APLICATION_NAME)