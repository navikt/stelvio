import sys, os, re
import lib.logUtil as log

l = log.getLogger(__name__)

endpointOrApplicationNameREGEX = re.compile('\}\S+/\S+\]|^\[resource')
simpleWsadminREGEX = re.compile('\[(\S+?)\s\[?(.*?)\]?\]')
def getPolicySetAttachements(applicationName):
	policySetAttachments = []
	lines = AdminTask.getPolicySetAttachments('[-applicationName %s -attachmentType client -expandResources *]' % applicationName).splitlines()
	for line in lines:
		if not endpointOrApplicationNameREGEX.search(line):
			policySetAttachment = dict(simpleWsadminREGEX.findall(line))
			
			if policySetAttachment['directAttachment'] == 'false':
				continue
			
			policySetAttachment['applicationName'] = applicationName
			policySetAttachments.append(policySetAttachment)
	return policySetAttachments
	
def createPolicySetAttachements(policySetAttachments):
	for policySetAttachment in policySetAttachments:
		cmd = '[-applicationName %(applicationName)s -attachmentType client -policySet "%(policySet)s" -resources %(resource)s ]]' % policySetAttachment
		l.debug("AdminTask.createPolicySetAttachment('"+ cmd +"')")
		attachmentId = AdminTask.createPolicySetAttachment(cmd)
		policySetAttachment['attachmentId'] = attachmentId
		if policySetAttachment.has_key('binding'):
			cmd = '[-bindingScope domain -bindingName "%(binding)s" -attachmentType client -bindingLocation [ [application %(applicationName)s] [attachmentId %(attachmentId)s] ]]' % policySetAttachment
			l.debug("AdminTask.setBinding('"+cmd+"')")
			AdminTask.setBinding(cmd)
	
def deletePolicySetAttachment(policySetAttachments):
	for policySetAttachment in policySetAttachments:
		cmd = '[-applicationName %(applicationName)s -attachmentType client -attachmentId %(attachmentId)s]' % policySetAttachment
		l.debug("AdminTask.deletePolicySetAttachment('"+ cmd +"')")
		AdminTask.deletePolicySetAttachment(cmd)
		l.info("Deleted policy set attachment for application %(applicationName)s with attachment id %s(attachmentId)s.")