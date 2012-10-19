from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import os
import ModifyUserNameTokenGenerator as muntg
import mocks.policySetBindingsMock as pMock


def readPolicySetBindingConfigTest():
	tempFile = 'tmp.xml'
	f = open(tempFile, 'w+')
	f.write(pMock.get())
	f.close()
	d = muntg.readPolicySetBindingConfig(tempFile)
	assertContains(d, ['NAV ESB Arena WSImport Binding', 'GOSYS', '${gosys-usernametoken-password}'])
	os.remove(tempFile)