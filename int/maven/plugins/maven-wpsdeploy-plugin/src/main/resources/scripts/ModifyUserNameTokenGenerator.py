import sys
from lib.saveUtil import save
from lib.javaPropertiesUtil import PropertiesReader
import lib.logUtil as log
l = log.getLogger(__name__)

enviromentPropertyPath = sys.argv[1]

propReader = PropertiesReader()
propReader.load(enviromentPropertyPath)

policySetBindingsList = propReader.get('policySetBindings').split(',')
policySetBindingsUsersList = propReader.get('policySetBindingsUsers').split(',')
policySetBindingsPasswordsList = propReader.get('policySetBindingsPasswords').split(',')

if not (len(policySetBindingsList) == len(policySetBindingsUsersList) == len(policySetBindingsPasswordsList)):
	l.error('Wrong number of arguments for either "policySetBindings", "policySetBindingsUsers" or "policySetBindingsPasswords"!\nThe variables should be CSV strings with an equal number or values!')

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