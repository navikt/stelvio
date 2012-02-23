import os, re, sys
sys.path.append(re.search("-f\s+(/?\S+/)", os.environ.get("IBM_JAVA_COMMAND_LINE")).group(1)) #adding skript directory til path to be able to normaly libs
#****************************************************************************
#
# File name: ModifySCAImportsBinding.py
#
# Description: This script sets SCA import bindings a given
#			  module, both provided as arguments.
#
#			  Used by the plugin:
#			  no.nav.maven.plugins:maven-wpsdeploy-plugin
#
# Author: test@example.com
#
#****************************************************************************

import time
import lib.logUtil as l
from Utils import wsadminToDict


WSADMIN_SCRIPTS_HOME	= sys.argv[0]
WSADMIN_SCRIPTS_HOME	= WSADMIN_SCRIPTS_HOME.replace('\t','\\t')
ARG_LIST				= sys.argv[1].split(";")

def main():
		start = time.clock()

		ears = os.listdir( WSADMIN_SCRIPTS_HOME + "/../EARFilesToDeploy/" )

		temp_ears = []

		#remove .ear from application name
		for ear in ears:
				ear = re.split("-[0-9]+\.", ear)[0]
				temp_ears.append(ear)
		# end for   
			 
		ears = temp_ears

		# poplating SCAModules and imports
		bindings = {}
		for arg in ARG_LIST:
			scaImport = SCAImport(arg)
			if not bindings.has_key(scaImport.moduleName):
				bindings[scaImport.moduleName] = SCAModule()
			bindings[scaImport.moduleName].addSCAImport(scaImport)

		sca_modules = listSCAModules().splitlines()
		# flag set whenever a sca import binding is modified
		
		
		for module in sca_modules:
			modified = 0
			moduleName = re.sub('_v\d+','',module.split(":")[0])
			
			if bindings.has_key(moduleName) and moduleName in ears:
				bindings[moduleName].addSCAModuleInformation(module)
				scaModule = bindings[moduleName]
				sca_imports = AdminTask.listSCAImports(' -moduleName ' +  scaModule.moduleNameLong + ' -applicationName ' +  scaModule.applicationName).splitlines()
				for sca_import in sca_imports:
					if scaModule.scaImports.has_key(sca_import):
						modifySCAImportBinding(scaModule.moduleNameLong, sca_import, scaModule.scaImports[sca_import].endpoint)
						modified = 1
						
			if modified:
				deletePolicySetAttachment(scaModule.applicationName)
				createPolicySetAttachment(scaModule.applicationName)
				
		AdminConfig.save()

		stop = time.clock()

		l.info("Time elapsed: " + str(round(stop - start, 2)) + " seconds.")

def deletePolicySetAttachment(applicationName): 
		delete_psa = AdminTask.getPolicySetAttachments('[-applicationName '+applicationName+' -attachmentType client]').splitlines()
		for psa in delete_psa:
				if (len(psa) > 0):
						id = wsadminToDict(psa)['id']
						if (id > 0):
								AdminTask.deletePolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -attachmentId '+id+']')
								l.info("Delete policy set attachment for application "+applicationName+" with attachment id "+id+".")
						#endIf
				#endIf
		#endFor
#endDef

def createPolicySetAttachment(applicationName):
		psa = AdminTask.getPolicySetAttachments('[-applicationName '+applicationName+' -attachmentType client -expandResources *]').splitlines()

		for a in psa:
				# Checks if psa contains { which denotes a webservice
				if (a.count("}") > 0):
						# Splits a into list.
						b = a.split("}")
						# Checks if b[1] contains /, if not that denotes a Service
						if (b[1].count("/") == 0):
								# Splits on "[resource", retrieves index 1:
								# i.e. WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/oppgaveservice}ArenaOppgaveService] [attachmentId 1368] [directAttachment true] [binding [NAV ESB Arena WSImport Binding]] [bindingScope domain] ]
								# Splits on "]" and retrieves index 0
								# i.e. WebService:/nav-prod-sak-arenaWeb.war:{http://arena.nav.no/services/oppgaveservice}ArenaOppgaveService
								resource = a.split("[resource ")[1].split("]")[0]
								
								if (applicationName == "nav-prod-sak-arenaApp"): 
										attachmentId = AdminTask.createPolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -policySet "NAV ESB Arena WSImport" -resources '+resource+' ]]')
										l.info("Attached policy set NAV ESB Arena WSImport to "+resource+".")
										AdminTask.setBinding('[-bindingScope domain -bindingName "NAV ESB Arena WSImport Binding" -attachmentType client -bindingLocation [ [application '+applicationName+'] [attachmentId '+attachmentId+'] ]]')
										l.info("Set binding NAV ESB Arena WSImport Binding for "+resource+".")
								elif (re.search('nav-tjeneste-arbeidOgAktivitet_v\d+App', applicationName)):
										attachmentId = AdminTask.createPolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -policySet "NAV ESB Arena WSImport" -resources '+resource+' ]]')
										l.info("Attached policy set NAV ESB Arena WSImport to "+resource+".")
										AdminTask.setBinding('[-bindingScope domain -bindingName "NAV ESB Arena WSImport Binding" -attachmentType client -bindingLocation [ [application '+applicationName+'] [attachmentId '+attachmentId+'] ]]')
										l.info("Set binding NAV ESB Arena WSImport Binding for "+resource+".")
								elif (re.search('nav-tjeneste-arbeidOgAktivitetYtelse_v\d+App', applicationName)):
										attachmentId = AdminTask.createPolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -policySet "NAV ESB Arena WSImport" -resources '+resource+' ]]')
										l.info("Attached policy set NAV ESB Arena WSImport to "+resource+".")
										AdminTask.setBinding('[-bindingScope domain -bindingName "NAV ESB Arena WSImport Binding" -attachmentType client -bindingLocation [ [application '+applicationName+'] [attachmentId '+attachmentId+'] ]]')
										l.info("Set binding NAV ESB Arena WSImport Binding for "+resource+".")
								elif (applicationName == "nav-prod-sak-infotApp"):
										attachmentId = AdminTask.createPolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -policySet "NAV ESB INFOT WSImport" -resources '+resource+' ]]')
										l.info("Attached policy set NAV ESB INFOT WSImport to "+resource+".")
										l.info("Using default service client binding for "+resource+".")
								else:
										attachmentId = AdminTask.createPolicySetAttachment('[-applicationName '+applicationName+' -attachmentType client -policySet "NAV ESB WSImport" -resources '+resource+' ]]')
										l.info("Attached policy set NAV ESB WSImport to "+resource+".")
										l.info("Using default service client binding for "+resource+".")
								#endIf
						#endIf
				#endIf
		#endFor
#endDef

def listSCAModules():
		return AdminTask.listSCAModules()
# end def listSCAModules


def modifySCAImportBinding(module_name, import_name, endpoint):
	endpointDetails = getEndpointDetails(module_name, import_name)
	if len(endpointDetails) < 1:
		l.warning("Unable to find currentEndpoint for " + module_name + " with import_name " + import_name)
	
	if endpoint.endswith("/"):
		endpoint = endpoint[:len(endpoint)-1]
			
	endpoint = endpoint + endpointDetails
		
	AdminTask.modifySCAImportWSBinding('[-moduleName ' + module_name + ' -import ' + import_name + ' -endpoint ' + endpoint + ']')
	l.info("Modified import [" + import_name + "] on module [" + module_name + "] to [" + endpoint + "].")
# end def modifySCAImportBinding

def getEndpointDetails(module_name, import_name):
	binding = AdminTask.showSCAImportWSBinding('[-moduleName ' + module_name + ' -import ' + import_name + ']')
	binding = binding.split(", ")
	currEndpoint = ""
	
	for x in binding:
		if x.find("currentEndpoint") != -1:
			currEndpoint = x
	
	if len(currEndpoint) < 1:
		return ""
	
	completeEndpoint = currEndpoint.split("=")[1]
	retString = "/"
	splitEndpoint = completeEndpoint.split("/")[3:]
	
	for x in splitEndpoint:
		retString += x + "/"
	
	#remove the last inevitable slash	
	retString = retString[:len(retString)-1]
	
	return retString
#end def getEndpointDetails


#nav-prod-sak-arena::sca/import/SakVedtakPortTypeWSIMP::http://e25apfl003.utvikling.local:7334;
class SCAImport:
	"""Denne modulen kan bli kalt med:
		- SCAImportString
	"""
	def __init__(self, SCAImportString):
		self.SCAImportString = SCAImportString
		scaItems = SCAImportString.split("::")
		self.moduleName = scaItems[0]
		self.importName = scaItems[1].split("/")[2]
		self.endpoint = scaItems[2]

	def __hash__(self):
		return hash(self.moduleName + self.importName + self.endpoint)
		
	def __str__(self):
		return "moduleName: " + self.moduleName + " importName: "  + self.importName + " endpoint: " + self.endpoint

	def __repr__(self):
		return self.__str__()
		
class SCAModule:
	"""Denne modulen kan bli kalt med:
		- ikke navngitte arugment  ('SCAModuleString')
	"""
	def __init__(self, *args):
		# pensjon-prosess-behandleAFPPrivat_v0_1_4:pensjon-prosess-behandleAFPPrivat_v0_1_4App:0.1.4:pensjon-prosess-behandleAFPPrivat: :
		self.scaImports = {}
		if args:
			addSCAModuleInformation(args[0])
		else:
			self.SCAModuleString = ""
			self.moduleName = ""
			self.moduleNameLong = ""
			self.applicationName = ""
			
	def addSCAModuleInformation(self, SCAModuleString):
		self.SCAModuleString = SCAModuleString
		self.moduleName = re.sub('_v\d+','',SCAModuleString.split(":")[0]) # Removes _v? from the module name
		self.moduleNameLong = SCAModuleString.split(":")[0]
		self.applicationName = SCAModuleString.split(":")[1]
		
	def addSCAImportFromString(self, SCAImportString):
		scaImport = SCAImport(SCAImportString)
		if self.moduleName == "":
			self.moduleName = scaImport.moduleName
		self.scaImports[scaImport.importName] = scaImport
	
	def addSCAImport(self, scaImport):
		self.scaImports[scaImport.importName] = scaImport
		
	def __hash__(self):
		return hash(self.moduleName + self.applicationName + self.moduleNameLong)
		
	def __str__(self):
		return "moduleName: " + self.moduleName + " applicationName: "  + self.applicationName + " moduleNameLong: "  + self.moduleNameLong
		
	def __repr__(self):
		return self.__str__()

		
main()
						