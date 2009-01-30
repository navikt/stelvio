if len(sys.argv) != 4:
	print("ERROR (InsertDB2DataSourceCustomProperty): Syntax: wsadmin -lang jython -f insertDataSourceCustomProperty.py <name> <value> <description> <datasources>")
	sys.exit()

PROPERTYNAME=sys.argv[0]
PROPERTYVALUE=sys.argv[1]
PROPERTYDESCRIPTION=sys.argv[2]
PROPERTYDATASOURCES=sys.argv[3]

updated = 0
_excp_ = 0
listen = AdminConfig.list('DataSource').split(java.lang.System.getProperty('line.separator'))

if(PROPERTYDATASOURCES != 'all'):
	dslist = PROPERTYDATASOURCES.split(',')


attr = [["name", PROPERTYNAME], ["type", "java.lang.Integer"], ["value", PROPERTYVALUE], ["description", PROPERTYDESCRIPTION]]
for ent in listen:
	dsname = AdminConfig.showAttribute(ent,'name')
	prov = AdminConfig.showAttribute(ent, 'provider')
	pr = AdminConfig.showAttribute(prov, 'providerType')
	if(pr == 'DB2 Universal JDBC Driver Provider' or pr == 'DB2 Universal JDBC Driver Provider (XA)'):
	 	try:
       			ps = AdminConfig.showAttribute(ent, 'propertySet')
			if(PROPERTYDATASOURCES == 'all'):
        			AdminConfig.create("J2EEResourceProperty", ps, attr)
				print "INFO (InsertDB2DataSourceCustomProperty) Adding custom property " + PROPERTYNAME + " to datasource " + dsname
				updated=1
			else:
				for ds in dslist:
					if(ds == dsname):
                                		AdminConfig.create("J2EEResourceProperty", ps, attr)
                                		print "INFO (InsertDB2DataSourceCustomProperty) Adding custom property " + PROPERTYNAME + " to datasource " + dsname
                                		updated=1
		except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                      	result = `_value_`
                      	_excp_ = 1
                #endTry
                if ( _excp_ ):
                        print "ERROR (InsertDB2DataSourceCustomProperty): Error inserting new custom property for datasource: "
                        print "ERROR (InsertDB2DataSourceCustomProperty): " + result
                        sys.exit()
                #endIf


if( updated ==0 ):
        print "WARNING (InsertDB2DataSourceCustomProperty): No custom properties were inserted."
else:
        AdminConfig.save()
