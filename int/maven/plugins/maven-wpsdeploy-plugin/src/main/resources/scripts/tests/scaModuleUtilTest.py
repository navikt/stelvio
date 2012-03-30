from lib.assertions import assertEqual, assertTrue, assertFalse, assertRegex
import lib.scaModuleUtil as smu
import mocks.AdminTaskMock as AdminTaskMock
import mocks.osMock as os
		
False, True = 0,1 #Define False, True

smu.os = os
smu.AdminTask = AdminTaskMock

VERSIONED_MODULE_NAME = 'ekstern-pensjon-tjeneste-beregning_v1'
VERSIONED_APLICATION_NAME = 'ekstern-pensjon-tjeneste-beregning_v1App'
VERSIONED_SHORT_NAME = 'ekstern-pensjon-tjeneste-beregning'
VERSIONED_SCA_VERSION = 1

UNVERSIONED_MODULE_NAME = 'nav-bsrv-frg-finntjenestepensjonsforhold'
UNVERSIONED_APLICATION_NAME = 'nav-bsrv-frg-finntjenestepensjonsforholdApp'
UNVERSIONED_SHORT_NAME = 'nav-bsrv-frg-finntjenestepensjonsforhold'
UNVERSIONED_SCA_VERSION = None


def getInstalledModulesTest():
	scaModules = smu.getInstalledModules()
	__versionedAttrChecker(scaModules[0])
	
def getModulesToBeInstalledVersionedTest():
	scaModules = smu.getModulesToBeInstalled('ignored input')
	__versionedAttrChecker(scaModules[0])
	
def getModulesToBeInstalledUnversionedTest():
	scaModules = smu.getModulesToBeInstalled('ignored input')
	__unversionedAttrChecker(scaModules[6])

def equalVersionedTest():
	scaModule = smu.ScaModule(VERSIONED_SHORT_NAME, 1)
	assertTrue(scaModule == VERSIONED_APLICATION_NAME)
	
def equalVersionedTest():
	scaModule = smu.ScaModule(UNVERSIONED_SHORT_NAME, None)
	assertTrue(scaModule == UNVERSIONED_APLICATION_NAME)
	
def __versionedAttrChecker(scaModule):
	assertEqual(scaModule.moduleName, VERSIONED_MODULE_NAME)
	assertEqual(scaModule.applicationName, VERSIONED_APLICATION_NAME)
	assertEqual(scaModule, VERSIONED_APLICATION_NAME)
	assertEqual(scaModule.shortName, VERSIONED_SHORT_NAME)
	assertEqual(scaModule.scaVersion, VERSIONED_SCA_VERSION)
	
def __unversionedAttrChecker(scaModule):
	assertEqual(scaModule.moduleName, UNVERSIONED_MODULE_NAME)
	assertEqual(scaModule.applicationName, UNVERSIONED_APLICATION_NAME)
	assertEqual(scaModule, UNVERSIONED_APLICATION_NAME)
	assertEqual(scaModule.shortName, UNVERSIONED_SHORT_NAME)
	assertEqual(scaModule.scaVersion, UNVERSIONED_SCA_VERSION)