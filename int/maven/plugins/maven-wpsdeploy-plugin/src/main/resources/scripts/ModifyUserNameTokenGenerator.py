import sys
from lib.saveUtil import save
from lib.parseConfiguration import readPolicySetBindingConfig

import lib.XMLUtil as XML
import lib.logUtil as log
l = log.getLogger(__name__)

def main():
	configPath = sys.argv[1]
	
	configSets = readPolicySetBindingConfig(configPath)
		
	for config in configSets:
		setBindingAuth(*config)
		
def setBindingAuth(bindingname, userid, password):
	l.info("Modifying binding "+bindingname+".")
	l.debug("AdminTask.setBinding('[-policyType WSSecurity -attachmentType client -bindingScope domain -attributes ",
		"[ [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.classname com.ibm.websphere.wssecurity.callbackhandler.UNTGenerateCallbackHandler] ",
		"[application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.userid '"+userid+"'] [description '" + bindingname + "'] ",
		"[domain global] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.password '*********'] ] -bindingName '"+bindingname+"' -bindingLocation ]')")
	try:
		result = AdminTask.setBinding('[-policyType WSSecurity -attachmentType client -bindingScope domain -attributes [ [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.classname com.ibm.websphere.wssecurity.callbackhandler.UNTGenerateCallbackHandler] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.userid '+userid+'] [description "' + bindingname + '"] [domain global] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.password '+password+'] ] -bindingName "'+bindingname+'" -bindingLocation ]')
	except:
		l.exception("Exception modyfing binding "+bindingname+".")

	save()
	
	
if __name__ == '__main__': main()