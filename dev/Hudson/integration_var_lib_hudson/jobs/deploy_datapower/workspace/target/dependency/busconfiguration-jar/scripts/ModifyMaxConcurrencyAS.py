import os.environ
import re
import time
from time import strftime

WSADMIN_SCRIPTS_HOME    = sys.argv[0]
WSADMIN_SCRIPTS_HOME    = WSADMIN_SCRIPTS_HOME.replace('\t','\\t')
ARG_LIST                = sys.argv[1].split(";")

execfile( WSADMIN_SCRIPTS_HOME+"/scripts/Log.py" )

def main():
        start = time.clock()
        listen = AdminConfig.list('J2CActivationSpec').split(java.lang.System.getProperty('line.separator'))


        listenMap = {}

        for ent in listen:
                key = ent.split("(")[0]
                listenMap[key] = ent;

        for arg in ARG_LIST:
                temp = arg.split("::");
                ACTIVATION_RECORD = temp[0]
                MAX_CONCURRENCY = temp[1]
                ent = listenMap[ACTIVATION_RECORD]
                identifier = AdminConfig.showAttribute(ent, 'name')
                res = AdminConfig.showAttribute(ent, 'resourceProperties').split(java.lang.System.getProperty(' '))
                for r in res:
                        _excp_ = 0
                        if(r.find('maxConcurrency') >= 0 ):
                                try:
                                        log("INFO", "Modified max concurrency resource property for " + ACTIVATION_RECORD)
                                        modifyResourceProperty(r, "maxConcurrency", MAX_CONCURRENCY, "java.lang.Integer")
                                        break
                                except:
                                        _type_, _value_, _tbck_ = sys.exc_info()
                                        result = `_value_`
                                        _excp_ = 1
                                #endTry
                                if ( _excp_ ):
                                        log("ERROR", "Error modifying maxConcurrency resource property for activation record: ")
                                        log("ERROR", result)
                                        sys.exit()
                                #endIf


        AdminConfig.save()
        stop = time.clock()
        log("INFO", "Time elapsed: " + str(round(stop - start, 2)) + " seconds.")


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

main()
        