import sys, re
import lib.logUtil as log
from lib.saveUtil import save
from lib.policySetAttachmentUtil import getPolicySetAttachements, createPolicySetAttachements
import lib.scaModuleUtil as sca

l = log.getLogger(__name__)
EAR_FILES_TO_DEPLOY_FOLDER = sys.argv[1]
ARG_DSV = sys.argv[2]

def main():
	l.info('Getting a list of all installed modules...')
	scaModulesToDeploy = sca.getModulesToBeInstalled(EAR_FILES_TO_DEPLOY_FOLDER)
	l.debug('scaModulesToDeploy:', [str(x) for x in scaModulesToDeploy])

	modulesImportBindings = parseScriptArguments(ARG_DSV)
	l.debug('Parsed arguments to the script and got this:', [k+': '+str(modulesImportBindings[k]) for k in modulesImportBindings.keys()])
		
	for scaModuleToDeploy in scaModulesToDeploy:
		moduleName = scaModuleToDeploy.moduleName
		shortName = scaModuleToDeploy.shortName
		applicationName = scaModuleToDeploy.applicationName
		
		if modulesImportBindings.has_key(shortName):
			importBindings = modulesImportBindings[shortName]
			scaImportNames = listSCAImports(scaModuleToDeploy)
			
			policySetAttachements = []
			for scaImportName in scaImportNames:
				if importBindings.has_key(scaImportName):
					importBinding = importBindings[scaImportName]
					
					modifySCAImportBinding(moduleName, scaImportName, importBinding.serverAddress)
					policySetAttachements += list(getPolicySetAttachements(applicationName))
					l.debug('policySetAttachements:', policySetAttachements)
			
			l.info('Writing policy set attachments back...')
			createPolicySetAttachements(policySetAttachements)
	save()
		
def parseScriptArguments(argumentsDsv):
	'''sys.argv[2]
	nav-prod-sak-arena::sca/import/SakVedtakPortTypeWSIMP::http://e25apfl003.utvikling.local:7334;nav-prod-sak-arena::sca/import/OrganisasjonPortTypeWSIMP::http://d26apfl004.test.local:7224'''
	argParserREGEX = re.compile('^(.*?)::sca/import/(.*?)::(.*?)$')
	importBindings = {}
	for arg in argumentsDsv.split(";"):
		moduleName, importName, serverAddress = argParserREGEX.search(arg).groups()
		importBindings.setdefault(moduleName, {})[importName] = SCAImportBinding(importName, serverAddress)
	return importBindings
	
def listSCAImports(scaModule):
	return AdminTask.listSCAImports('-moduleName ' + scaModule.moduleName).splitlines()

def modifySCAImportBinding(module_name, import_name, serverAddress):
	originalEndpointPath = getEndpointPath(module_name, import_name)
	
	newEndpoint = addTrailingSlash(serverAddress) + originalEndpointPath
	cmd = '[-moduleName ' + module_name + ' -import ' + import_name + ' -endpoint ' + newEndpoint + ']'
	l.debug("AdminTask.modifySCAImportWSBinding('"+ cmd +"')")
	AdminTask.modifySCAImportWSBinding(cmd)
	l.info("Modified import [" + import_name + "] on module [" + module_name + "] to [" + newEndpoint + "].")
	
def addTrailingSlash(string):
	if string[-1] != '/':
		return string +'/'
	return string

'''AdminTask.showSCAImportWSBinding:
{service=ns1:ArenaNotatService, port=ns1:ArenaNotatServicePort, deployedEndpoint=http://d26apfl004.test.local:7224/arena_ws/services/ArenaNotatService, currentEndpoint=http://d26apfl004.test.local:7224/arena_ws/services/ArenaNotatService, importBindingType=JaxWsImportBinding, policyAttachments=[{resource=WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/notatservice}ArenaNotatService}, {resource=WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/notatservice}ArenaNotatService/ArenaNotatServicePort/hentNotatListe}]}'''
findEndpointREGEX = re.compile('currentEndpoint=(.*?),\s')
splitUrlREGEX = re.compile('(.+?)://(.+?)(?::(.+?))?/(.*)')
def getEndpointPath(module_name, import_name):
	binding = AdminTask.showSCAImportWSBinding('[-moduleName ' + module_name + ' -import ' + import_name + ']')
	l.debug('binding:'+binding)
	endpointUrl = findEndpointREGEX.search(binding).group(1)
	l.debug('endpointUrl:',endpointUrl)
	sheme, domain, port, path = splitUrlREGEX.search(endpointUrl).groups()
	return path

class SCAImportBinding:
	def __init__(self, importName, serverAddress):
		self.importName = importName
		self.serverAddress = serverAddress
	def __str__(self):
		return '[%s %s %s]' % (self.moduleName, self.importName, self.serverAddress)
	
if __name__ == '__main__': main()