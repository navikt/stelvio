from lib.assertions import assertEqual, assertTrue, assertFalse, assertRegex
import lib.policySetAttachmentUtil as psau
import mocks.AdminTaskMock as AdminTaskMock

False, True = 0,1 #Define False, True

psau.AdminTask = AdminTaskMock

def getPolicySetAttachementsTest():
	psAttacments = psau.getPolicySetAttachements('nav-prod-sak-arenaApp')
	assertEqual(psAttacments[0], {'attachmentId': '1389',
		'resource': 'WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/oppgaveservice}ArenaOppgaveService',
		'binding': 'NAV ESB Arena WSImport Binding',
		'providerPolicySet': 'false',
		'bindingScope': 'domain',
		'applicationName': 'nav-prod-sak-arenaApp',
		'directAttachment': 'true',
		'policyApplied': 'client',
		'policySet': 'NAV ESB Arena WSImport'}
	)
	
def createPolicySetAttachementsTest():
	psau.createPolicySetAttachements(psau.getPolicySetAttachements('nav-prod-sak-arenaApp'))
	assert True

def deletePolicySetAttachmentTest():
	psau.deletePolicySetAttachment(psau.getPolicySetAttachements('nav-prod-sak-arenaApp'))
	assert True
	