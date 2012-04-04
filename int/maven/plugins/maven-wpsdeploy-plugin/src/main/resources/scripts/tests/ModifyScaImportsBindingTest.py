from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises, assertContains
import ModifySCAImportsBinding as msib
import mocks.AdminTaskMock as AdminTaskMock

msib.AdminTask = AdminTaskMock

def parseScriptArgumentsTest():
	args = 'nav-tjeneste-medlemskap::sca/import/MedlemskapWSIMP::https://wasapp-q5.adeo.no;pensjon-tjeneste-simulering::sca/import/TjenestepensjonSimuleringV1WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;pensjon-tjeneste-simulering::sca/import/SimulereTjenestepensjonV0_2WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-sak-arena::sca/import/OrganisasjonPortTypeWSIMP::http://d26apfl004.test.local:7224;nav-prod-sak-arena::sca/import/ArenaWSIMP::http://d26apfl004.test.local:7123;nav-prod-sak-arena::sca/import/OppgavePortTypeWSIMP::http://d26apfl004.test.local:7224;nav-prod-sak-arena::sca/import/NotatPortTypeWSIMP::http://d26apfl004.test.local:7224;nav-prod-sak-arena::sca/import/PersonPortTypeWSIMP::http://d26apfl004.test.local:7224;nav-prod-sak-arena::sca/import/UtbetalingPortTypeWSIMP::http://d26apfl004.test.local:7224;nav-prod-sak-arena::sca/import/SakVedtakPortTypeWSIMP::http://d26apfl004.test.local:7224;nav-tjeneste-behandleMedlemskap::sca/import/BehandleMedlemskapWSIMP::https://wasapp-q5.adeo.no;nav-tjeneste-person::sca/import/PersonWSIMP::https://wasapp-u2.adeo.no/;nav-tjeneste-arbeidsgiver::sca/import/ArbeidsgiverWSIMP::https://155.55.1.82:9104/;nav-prod-elsam-oppkoblingstest::sca/import/OppkoblingstestV0_1WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-sto-norskpensjon::sca/import/PrivatPensjonWSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-tjeneste-henvendelsebehandling::sca/import/GSakHenvendelsebehandlingWSIMP::https://wasapp-u2.adeo.no/;nav-prod-dok-sms::sca/import/SMSGatewayWSIMP::https://partner-gw-q5.preprod.internsone.local:9443/;nav-tjeneste-henvendelse::sca/import/GSakHenvendelseWSIMP::https://wasapp-u2.adeo.no/;nav-tjeneste-ruting::sca/import/RutingWSIMP::https://wasapp-t5.adeo.no/;nav-tjeneste-arbeidOgAktivitetYtelse::sca/import/ArbeidOgAktivitetYtelseWSIMP::http://e25apfl003.utvikling.local:7294;nav-tjeneste-journalbehandling::sca/import/JournalbehandlingWSIMP::https://wasapp-q5.adeo.no;nav-prod-sak-infot::sca/import/infotrygdSakWSIMP::http://155.55.1.82:9220/;nav-prod-sak-infot::sca/import/infotrygdVedtakWSIMP::http://155.55.1.82:9220/;nav-tjeneste-behandlePerson::sca/import/BehandlePersonWSIMP::https://wasapp-u2.adeo.no/;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_3WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_4WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_5WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_6WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_7WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-prod-elsam-tpsamordningvarsling::sca/import/TPSamordningVarslingV0_8WSIMP::https://partner-gw-q5.preprod.internsone.local:9443;nav-tjeneste-behandleArbeidsgiver::sca/import/BehandleArbeidsgiverWSIMP::https://155.55.1.82:9104/;nav-tjeneste-arbeidOgAktivitet::sca/import/SakVedtakPortTypeWSIMP::http://e25apfl003.utvikling.local:7334;nav-tjeneste-oppgave::sca/import/GSakWSIMP::https://wasapp-u2.adeo.no/;nav-tjeneste-statistikk::sca/import/StatistikkWSIMP::http://d26apvl142.test.local:9083/;nav-tjeneste-journal::sca/import/JournalWSIMP::https://wasapp-q5.adeo.no;nav-tjeneste-oppgavebehandling::sca/import/GSakOppgavebehandlingWSIMP::https://wasapp-u2.adeo.no/'
	importBindings = msib.parseScriptArguments(args)
	assertEqual(importBindings['nav-prod-sak-arena']['OrganisasjonPortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 1')
	assertEqual(importBindings['nav-prod-sak-arena']['ArenaWSIMP'], 'http://d26apfl004.test.local:7123', 'Assert 2')
	assertEqual(importBindings['nav-prod-sak-arena']['OppgavePortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 3')
	assertEqual(importBindings['nav-prod-sak-arena']['NotatPortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 4')
	assertEqual(importBindings['nav-prod-sak-arena']['PersonPortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 5')
	assertEqual(importBindings['nav-prod-sak-arena']['UtbetalingPortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 6')
	assertEqual(importBindings['nav-prod-sak-arena']['SakVedtakPortTypeWSIMP'], 'http://d26apfl004.test.local:7224', 'Assert 7')
	assertEqual(importBindings['nav-tjeneste-medlemskap']['MedlemskapWSIMP'], 'https://wasapp-q5.adeo.no', 'Assert 7')
	
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