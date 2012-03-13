import sys
from lib.saveUtil import save
import lib.Util as Util
import lib.logUtil as log
l = log.getLogger(__name__)

enviromentPropertyFile = sys.argv[1]

Util.readProperties(enviromentPropertyFile)

policySetBindingsList = Util.getProperty('policySetBindings').split(',')
policySetBindingsUsersList = Util.getProperty('policySetBindingsUsers').split(',')
policySetBindingsPasswordsList = Util.getProperty('policySetBindingsPasswords').split(',')

if not (len(policySetBindingsList) == len(policySetBindingsUsersList) == len(policySetBindingsPasswordsList)):
	l.exit('Wrong number of arguments for either "policySetBindings", "policySetBindingsUsers" or "policySetBindingsPasswords"!\nThe variables should be CSV strings with equal number or values!')

def main():
	for i in range(policySetBindingsList):
		setBindingAuth(policySetBindingsList[i],
			policySetBindingsUsersList[i],
			policySetBindingsPasswordsList[i]
		)

def setBindingAuth(userid, password, bindingname):
	l.info("Modifying binding "+bindingname+".")
	try:
		result = AdminTask.setBinding('[-policyType WSSecurity -attachmentType client -bindingScope domain -attributes [ [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.classname com.ibm.websphere.wssecurity.callbackhandler.UNTGenerateCallbackHandler] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.userid '+userid+'] [description "' + bindingname + '"] [domain global] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.password '+password+'] ] -bindingName "'+bindingname+'" -bindingLocation ]')
	except:
		l.exception("Exception modyfing binding "+bindingname+".")

	l.info("Executed with success.")
	save()
	
	
if __name__ == '__main__': main()