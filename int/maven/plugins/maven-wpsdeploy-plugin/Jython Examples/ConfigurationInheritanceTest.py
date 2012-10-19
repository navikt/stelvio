from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import os
import ConfigurationInheritance as ci
import mocks.propertiesMock as pMock
	
def createHelpfile(fileName, content):
	f = open(fileName, 'w+')
	f.write(content)
	f.close()
	

def extractPropertiesTest():
	testdict = {}
	ci.extractProperties(pMock.get().splitlines(), testdict)
	assertEqual(testdict['ELSAM-TPTILB_TPSamordningVarsling_varsleVedtakNAV_Endpoints'], 'V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService')
	assertEqual(testdict['FGSAKRunAsValue'], 'REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234')
	
def writePropertiesTest():
	testdict = {'ELSAM-TPTILB_TPSamordningVarsling_varsleVedtakNAV_Endpoints':  'V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService', 'FGSAKRunAsValue': 'REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234', 'jndiNamePenPersonService': 'ejb/no/nav/provider/pensjon/penperson/ejb/PenPersonServiceHome'}
	tmpfile = 'tmp1.class'
	ci.writeProperties(testdict, tmpfile)
	f = open(tmpfile)
	fileContent = f.read()
	f.close()
	os.remove(tmpfile)
	
	assertContains(fileContent.splitlines(), 'FGSAKRunAsValue=REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234')
	
def readPropertiesTest():
	tmpfile1 = 'tmp1.properties'
	tmpfile2 = 'tmp2.properties'
	createHelpfile(tmpfile1, 'FGSAKRunAsValue=REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234\njndiNamePenVersion=ejb/no/nav/provider/pensjon/application/ejb/VersionServiceHome')
	createHelpfile(tmpfile2, 'jndiNamePenPersonService=ejb/no/nav/provider/pensjon/penperson/ejb/PenPersonServiceHome\nproviderUrlPenPersonService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810')
	
	lines = ci.readProperties('.')
	os.remove(tmpfile1)
	os.remove(tmpfile2)
	assertContains(lines, 'FGSAKRunAsValue=REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234', 'Could not read properties from the first tmp file')
	assertContains(lines, 'providerUrlPenPersonService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810', 'Could not read properties from the second tmp file')
	
def readPropertiesDirNotExistsTest():
	lines = ci.readProperties('nonExistantDir')
	assert(True)