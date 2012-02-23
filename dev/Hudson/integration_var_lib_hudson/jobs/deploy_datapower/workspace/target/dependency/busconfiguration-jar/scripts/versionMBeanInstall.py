#****************************************************************************
# File name: versionMBeanInstall.py
#
# Description: This script downloads and configure the VersionMBean adapter used
#         to populate "Versjonssoversikten".
#
# Author: test@example.com
#
#****************************************************************************

import os
import re

execfile( "./Log.py" )

def wsadminToDict( text ) :
    cmdName = 'wsadminToDict'
    result = {}
    if ( text.count( '[' ) == text.count( ']' ) ) and ( text[ 0 ] == '[' ) and ( text[ -1 ] == ']' ) :
        innerText = text[ 1:-1 ].strip()
        delimiters = '.|!@#'          # Possible delimiter values
        for delim in delimiters :
            if innerText.count( delim ) == 0 :
                for pair in innerText.replace( '] [', ']%s[' % delim ).split( delim ) :
                    if ( pair.count('[') == pair.count(']') ) and ( pair[0] == '[' ) and ( pair[-1] == ']' ) :
                        contents = pair[ 1:-1 ].strip()
                        try :
                            ( name, value ) = contents.split( ' ', 1 )
                        except :
                            ( name, value ) = ( contents, '' )
                        result[ name ] = value
                    else :
                        log("ERROR", "Unexpected text: ["+pair+"] (ignored).")
                break
            else :
                log("WARNING", "Discarding "+delim+" as delimiter. Moving on...")
                #return {}
        if (len(result) == 0):
            log("ERROR", "Unable to process ["+text+"]. No suitable delimiter was found in ["+delimiters+"]")

    else :
        log("ERROR", "Unexpected data format: ["+text+"], empty dictionary returned.")

    return result
#endDef

def wsadminToList(inStr):
    outList=[]
    if (len(inStr)>0 and inStr[0]=='[' and inStr[-1]==']'):
        tmpList = inStr[1:-1].split(" ")
    else:
        tmpList = inStr.split("\n")  #splits for Windows or Linux
    for item in tmpList:
        item = item.rstrip();   #removes any Windows "\r"
        if (len(item)>0):
            outList.append(item)
    return outList
#endDef

def downloadToDest(url, scp_dest):
    string = 'http://confluence.adeo.no/download/attachments/145795/VersionMBean.jar'
    file = string.split('/')[-1]
    user = "wasadm"
    log("INFO", "Executing: ["+'cd /tmp && wget '+url+' && scp /tmp/'+file+' '+scp_dest+"]")
    os.system('cd /tmp && wget '+url+' && scp /tmp/'+file+' '+scp_dest)
#endDef downloadToDest()

def getDmgrHostName():
    cell = AdminConfig.list("Cell")
    cellName = AdminConfig.showAttribute(cell, "name")
    nodes = AdminConfig.list('Node', cell).splitlines()
    nodeName = ''
    for node in nodes:
        if (node.count('Manager') > 0 or node.count('Dmgr') > 0):
        	return AdminConfig.showAttribute(node, 'hostName')
    return None
        	
def getWasInstallRoot():
    cell = AdminConfig.list("Cell")
    cellName = AdminConfig.showAttribute(cell, "name")
    nodes = AdminConfig.list('Node', cell).splitlines()
    nodeName = ''
    for node in nodes:
        if (node.count('Manager') > 0 or node.count('Dmgr') > 0):
            nodeName = AdminConfig.showAttribute(node, "name")
            log("INFO", "Node is ["+nodeName+"]")
            break

    cellManagerWebSphereVariableMap = AdminConfig.getid("/Cell:"+cellName+"/Node:"+nodeName+"/VariableMap:/")
    cellManagerEntries = AdminConfig.showAttribute(cellManagerWebSphereVariableMap, "entries")
    entryList = wsadminToList(cellManagerEntries)

    varVal = ''

    if (len(cellManagerEntries) != '[]'):
       for cellEntry in entryList:
          varName = AdminConfig.showAttribute(cellEntry, "symbolicName")
          log("DEBUG", varName)
          if (varName == 'WAS_INSTALL_ROOT'):
             varVal = AdminConfig.showAttribute(cellEntry, "value")
             log("INFO", "WAS_INSTALL_ROOT is ["+varVal+"]")
             break
       #endFor
    #endIf
    return varVal

#endDef

def versionMBeanInstall():
    adapter_url = 'http://confluence.adeo.no/download/attachments/145795/VersionMBean.jar'
    user = 'wasadm'
    hostName = getDmgrHostName()
    was_install_root = getWasInstallRoot()
    if (len(was_install_root) == 0):
        log("ERROR", "Unable to determin WAS_INSTALL_ROOT")
        sys.exit()
    dest = was_install_root+'/lib/ext'
    log("INFO", "Retrieving VersionMBean.jar from ["+adapter_url+"]")
    downloadToDest(adapter_url, user+'@'+hostName+':'+dest)
    cell = AdminConfig.list('Cell')
    cell_name = AdminConfig.showAttribute(cell, "name")
    log("INFO", "Cell name is ["+cell_name+"]")
    nodes = AdminConfig.list('Node', cell).splitlines()
    node_name = ''
    server_name = ''
    existingJVMArgs = ''
    jmxArgs = ' -Djavax.management.builder.initial= -Dcom.sun.management.jmxremote'

    for node in nodes:
        if (node.count('Manager') > 0 or node.count('Dmgr') > 0):
            node_name = AdminConfig.showAttribute(node, "name")
            log("INFO", node_name+" found.")
            servers = AdminConfig.list("Server", node).split(lineSeparator)
            for server in servers:
                server_name = AdminConfig.showAttribute(server, "name")

                if (server_name == 'dmgr'):
                    log("INFO", server_name+" found.")
                    JVMProps = AdminTask.showJVMProperties('[ -serverName '+server_name+' ]').splitlines()[0]
                    propsDict = wsadminToDict(JVMProps)
                    if (len(propsDict) == 0):
                        sys.exit()

                    for key, value in propsDict.items():
                        if (key == 'genericJvmArguments'):
                            existingJVMArgs = value
                            break

                    AdminTask.setJVMProperties('[-serverName dmgr -genericJvmArguments "'+existingJVMArgs[1:-1]+jmxArgs+'"]')
                    log("INFO", "Deployment manager generic JVM arguments modified: ["+existingJVMArgs[1:-1]+jmxArgs+"]")
                    dmgr_id = AdminConfig.getid('/Cell:'+cell_name+'/Node:'+node_name+'/Server:'+server_name+'/')
                    jvm_id = AdminConfig.list('JavaVirtualMachine', dmgr_id)
                    log("INFO", "Setting property [com.sun.management.jmxremote.authenticate] with value [false] [req] on "+jvm_id+".")
                    res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "com.sun.management.jmxremote.authenticate"] [description ""] [value "false"] [required "false"]]')
                    log("INFO", "Result: ["+res+"]")
                    log("INFO", "Setting property [com.sun.management.jmxremote.port] with value [9999] on "+jvm_id+".")
                    res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "com.sun.management.jmxremote.port"] [description ""] [value "9999"] [required "false"]]')
                    log("INFO", "Result: ["+res+"]")
                    log("INFO", "Setting property [com.sun.management.jmxremote.ssl] with value [false] on "+jvm_id+".")
                    res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "com.sun.management.jmxremote.ssl"] [description ""] [value "false"] [required "false"]]')
                    log("INFO", "Result: ["+res+"]")
                    log("INFO", "Setting property [manifest.dir] with value [${USER_INSTALL_ROOT}/config/cells/${WAS_CELL_NAME}/applications] on "+jvm_id+".")
                    res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "manifest.dir "] [description ""] [value "${USER_INSTALL_ROOT}/config/cells/${WAS_CELL_NAME}/applications"] [required "false"]]')
                    log("INFO", "Result: ["+res+"]")
                    if (was_install_root.count('ProcServer')>0):
                        log("INFO", "Process server detected.")
                        log("INFO", "Setting property [bus.configuration.version] with value [${BUS_CONFIGURATION_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "bus.configuration.version"] [description ""] [value "${BUS_CONFIGURATION_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                        log("INFO", "Setting property [esb.release.version] with value [${ESB_RELEASE_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "esb.release.version"] [description ""] [value "${ESB_RELEASE_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                        log("INFO", "Setting property [nav.modules.version] with value [${NAV_MODULES_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "nav.modules.version"] [description ""] [value "${NAV_MODULES_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                        log("INFO", "Setting property [esb.modules.version] with value [${ESB_MODULES_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "esb.modules.version"] [description ""] [value "	${ESB_MODULES_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                        log("INFO", "Setting property [pensjon.modules.version] with value [${PENSJON_MODULES_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "pensjon.modules.version"] [description ""] [value "${PENSJON_MODULES_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                        log("INFO", "Setting property [pensjon.process.modules.version] with value [${PENSJON_PROCESS_MODULES_VERSION}] on "+jvm_id+".")
                        res = AdminConfig.create('Property', jvm_id, '[[validationExpression ""] [name "pensjon.process.modules.version"] [description ""] [value "${PENSJON_PROCESS_MODULES_VERSION}"] [required "false"]]')
                        log("INFO", "Result: ["+res+"]")
                    log("INFO", "Creating custom service on deployment manager ["+jvm_id+"].")
                    res = AdminConfig.create('CustomService', dmgr_id, '[[enable "true"] [classpath "${WAS_INSTALL_ROOT}/lib/ext/VersionMBean.jar"] [classname "extmbean.version.CustomVersion"] [externalConfigURL ""] [displayName "VersionMBean"] [description "Custom MBean for sjekk av versjoner - Testsenteret"]]')
                    log("INFO", "Result: ["+res+"]")
                    break
                #endIf
            #endFor
        #endIf
    #endFor
#endDef main()