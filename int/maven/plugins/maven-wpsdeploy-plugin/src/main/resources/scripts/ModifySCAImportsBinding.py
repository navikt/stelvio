import sys, re
import lib.logUtil as log
from lib.stringUtil import strip
from lib.saveUtil import save
from lib.policySetAttachmentUtil import getPolicySetAttachements, createPolicySetAttachements
import lib.scaModuleUtil as sca

l = log.getLogger(__name__)

def main():
	EAR_FILES_TO_DEPLOY_FOLDER = sys.argv[1]
	ARG_DSV = sys.argv[2]
	
	l.info('Getting a list of all installed modules...')
	scaModulesToDeploy = sca.getModulesToBeInstalled(EAR_FILES_TO_DEPLOY_FOLDER)
	l.debug('scaModulesToDeploy:', [str(x) for x in scaModulesToDeploy])

	modulesImports = parseScriptArguments(ARG_DSV)
	l.debug('Parsed arguments to the script and got this:', modulesImports)
		
	for scaModuleToDeploy in scaModulesToDeploy:
		moduleName = scaModuleToDeploy.moduleName
		shortName = scaModuleToDeploy.shortName
		applicationName = scaModuleToDeploy.applicationName
		
		if modulesImports.has_key(shortName):
			serverAddresses = modulesImports[shortName]
			scaImportNames = listSCAImports(scaModuleToDeploy)
			
			policySetAttachements = []
			for scaImportName in scaImportNames:
				if serverAddresses.has_key(scaImportName):
					serverAddress = serverAddresses[scaImportName]
					newEndpoint = swapEndpointServerAddress(moduleName, scaImportName, serverAddress)
					
					modifySCAImportBinding(moduleName, scaImportName, newEndpoint)
					l.info("Modified import [" + scaImportName + "] on module [" + moduleName + "] to [" + newEndpoint + "].")
					
					policySetAttachements += list(getPolicySetAttachements(applicationName)) #This must be done after each endpoint modification, because if a endpoint in a import has been changed; ONLY that import has policySet and binding.
					
			l.debug('policySetAttachements:', policySetAttachements)
			createPolicySetAttachements(policySetAttachements)
	save()
		
def parseScriptArguments(argumentsDsv):
	'''sys.argv[2]:
	nav-prod-sak-arena::sca/import/SakVedtakPortTypeWSIMP::http://e25apfl003.utvikling.local:7334;nav-prod-sak-arena::sca/import/OrganisasjonPortTypeWSIMP::http://d26apfl004.test.local:7224'''
	argParserREGEX = re.compile('^(.*?)::sca/import/(.*?)::(.*?)$')
	modulesImports = {}
	for arg in argumentsDsv.split(";"):
		moduleName, importName, serverAddress = argParserREGEX.search(arg).groups()
		modulesImports.setdefault(moduleName, {})[importName] = serverAddress
	return modulesImports
	
def listSCAImports(scaModule):
	return AdminTask.listSCAImports('-moduleName ' + scaModule.moduleName).splitlines()

def modifySCAImportBinding(moduleName, importName, endpoint):	
	cmd = '[-moduleName ' + moduleName + ' -import ' + importName + ' -endpoint ' + endpoint + ']'
	l.debug("AdminTask.modifySCAImportWSBinding('"+ cmd +"')")
	AdminTask.modifySCAImportWSBinding(cmd)
	
def swapEndpointServerAddress(moduleName, importName, serverAddress):
	originalEndpointPath = getEndpointPath(moduleName, importName)
	newEndpoint = strip(serverAddress, '/') + originalEndpointPath
	return newEndpoint

'''AdminTask.showSCAImportWSBinding:
{service=ns1:ArenaNotatService, port=ns1:ArenaNotatServicePort, deployedEndpoint=http://d26apfl004.test.local:7224/arena_ws/services/ArenaNotatService, currentEndpoint=http://d26apfl004.test.local:7224/arena_ws/services/ArenaNotatService, importBindingType=JaxWsImportBinding, policyAttachments=[{resource=WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/notatservice}ArenaNotatService}, {resource=WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/notatservice}ArenaNotatService/ArenaNotatServicePort/hentNotatListe}]}'''
findEndpointREGEX = re.compile('currentEndpoint=(.*?),\s')
splitUrlREGEX = re.compile('(.+?)://(.+?)(?::(.+?))?(/.*)')
def getEndpointPath(moduleName, importName):
	binding = AdminTask.showSCAImportWSBinding('[-moduleName ' + moduleName + ' -import ' + importName + ']')
	l.debug('binding:'+binding)
	endpointUrl = findEndpointREGEX.search(binding).group(1)
	l.debug('endpointUrl:',endpointUrl)
	sheme, domain, port, path = splitUrlREGEX.search(endpointUrl).groups()
	return path
	
if __name__ == '__main__': main()