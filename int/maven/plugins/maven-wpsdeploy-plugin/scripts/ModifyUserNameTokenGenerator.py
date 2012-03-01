INPUT = sys.argv[1]

from lib.saveUtil import save
import lib.logUtil as log
l = log.getLogger(__name__)

def setBindingAuth(userid, password, bindingname):
	l.info("Modifying binding "+bindingname+"."))
	l.info("Binding info: userId: "+userid+" pw: " +password+" bindingname: " + bindingname))
	try:
		result = AdminTask.setBinding('[-policyType WSSecurity -attachmentType client -bindingScope domain -attributes [ [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.classname com.ibm.websphere.wssecurity.callbackhandler.UNTGenerateCallbackHandler] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.userid '+userid+'] [description "' + bindingname + '"] [domain global] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.password '+password+'] ] -bindingName "'+bindingname+'" -bindingLocation ]')
	except:
		l.exception("Exception modyfing binding "+bindingname+".")

	l.info("Executed with success. Saving..."))
	save()

if (INPUT.count("#") > 0):
	arr = INPUT.split("#")
	for module in arr:
		vars=module.split(";")
		module=vars[0]
		username=vars[1].split("=")[1]
		password=vars[2].split("=")[1]
		binding=vars[3].split("=")[1]
		setBindingAuth(username, password, binding)
else:
	module = INPUT
	vars=module.split(";")
	module=vars[0]
	username=vars[1].split("=")[1]
	password=vars[2].split("=")[1]
	binding=vars[3].split("=")[1]
	setBindingAuth(username, password, binding)


