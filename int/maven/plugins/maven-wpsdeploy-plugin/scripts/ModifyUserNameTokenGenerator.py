WSADMIN_SCRIPTS_HOME = sys.argv[0]
WSADMIN_SCRIPTS_HOME = WSADMIN_SCRIPTS_HOME.replace('\t','\\t')
INPUT = sys.argv[1]

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/Log.py" )

def setBindingAuth(userid, password, bindingname):
	log("INFO", "Modifying binding "+bindingname+".")
	log("INFO", "Binding info: userId: "+userid+" pw: " +password+" bindingname: " + bindingname)
	try:
		_excp_ = 0
		result = AdminTask.setBinding('[-policyType WSSecurity -attachmentType client -bindingScope domain -attributes [ [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.classname com.ibm.websphere.wssecurity.callbackhandler.UNTGenerateCallbackHandler] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.userid '+userid+'] [description "' + bindingname + '"] [domain global] [application.securityoutboundbindingconfig.tokengenerator_0.callbackhandler.basicauth.password '+password+'] ] -bindingName "'+bindingname+'" -bindingLocation ]')
	except:
		_type_, _value_, _tbck_ = sys.exc_info()
		result = `_value_`
		_excp_ = 1
	#endTry
	if (_excp_ != 0):
		msg = "Exception modyfing binding "+bindingname+"."
		log("ERROR", msg)
		log("ERROR", result)
	else:
		log("INFO", "Executed with success. Saving...")
		AdminConfig.save()
	#endIf
#endDef

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


