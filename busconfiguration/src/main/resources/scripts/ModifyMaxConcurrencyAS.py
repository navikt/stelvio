def modifyResourceProperty (propSet, propName, propValue, propType):

        global AdminConfig

        attrs = []
        attrs.append(["name", propName])
        attrs.append(["value", propValue])
        attrs.append(["type", propType])

        try:
                _excp_ = 0
                result = AdminConfig.modify(propSet, attrs )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                result = `_value_`
                _excp_ = 1
        #endTry
        if (_excp_ ):
                print "Caught Exception modifying ResourceProperty"
                print result
                return
        #endIf
        return

#endDef

if len(sys.argv) != 2:
	print("ERROR (ModifyMaxConcurrencyAS): Syntax: wsadmin -lang jython -f ModifyMaxConcurrency.py <activation_record> <max concurrency>")
	sys.exit()

ACTIVATION_RECORD = sys.argv[0]
MAX_CONCURRENCY = sys.argv[1]

_excp_ = 0
found = 0
listen = AdminConfig.list('J2CActivationSpec').split(java.lang.System.getProperty('line.separator'))
for ent in listen:
        identifier = AdminConfig.showAttribute(ent, 'name')
        if (identifier == ACTIVATION_RECORD):
                res = AdminConfig.showAttribute(ent, 'resourceProperties').split(java.lang.System.getProperty(' '))
                for r in res:
                        if(r.find('maxConcurrency') >= 0 ):
				try:
                                	modifyResourceProperty(r, "maxConcurrency", MAX_CONCURRENCY, "java.lang.Integer")
					found=1
                                	break
		 		except:
                        		_type_, _value_, _tbck_ = sys.exc_info()
                         		result = `_value_`
                        		_excp_ = 1
                		#endTry
                		if ( _excp_ ): 
                        		print "ERROR (ModifyMaxConcurrencyAS): Error modifying maxConcurrency resource property for activation record: "
                        		print "ERROR (ModifyMaxConcurrencyAS): " + result
                        		sys.exit()
                		#endIf
		break

if( found ==0 ):
	print "WARNING (ModifyMaxConcurrencyAS): Did not find the activation specification, so no modifications were done"
else:
	AdminConfig.save()
