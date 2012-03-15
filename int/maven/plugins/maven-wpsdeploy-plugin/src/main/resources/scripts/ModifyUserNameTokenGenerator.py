import sys
from lib.saveUtil import save
import lib.Utils as Utils
import lib.logUtil as log
l = log.getLogger(__name__)

enviromentPropertyFile = sys.argv[1]

Utils.readProperties(enviromentPropertyFile)

policySetBindingsList = Utils.getProperty('policySetBindings').split(',')
policySetBindingsUsersList = Utils.getProperty('policySetBindingsUsers').split(',')
policySetBindingsPasswordsList = Utils.getProperty('policySetBindingsPasswords').split(',')

if not (len(policySetBindingsList) == len(policySetBindingsUsersList) == len(policySetBindingsPasswordsList)):
	l.error('Wrong number of arguments for either "policySetBindings", "policySetBindingsUsers" or "policySetBindingsPasswords"!\nThe variables should be CSV strings with equal number or values!')

def main():
	for i in range(len(policySetBindingsList)):
		setBindingAuth(policySetBindingsList[i],
			policySetBindingsUsersList[i],
			policySetBindingsPasswordsList[i]
		)

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