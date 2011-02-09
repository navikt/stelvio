class SCAModule:
	def __init__(self, module_string):
		attributes = module_string.split(":")
		self.name = attributes[0]
		self.app_name = attributes[1]

	def __repr__(self):
		return self.name

class SCAModuleList:
	def __init__(self, module_list_string):
		modules = module_list_string.split("\n")
		self.module_list = []
		for module in modules:
			self.module_list.append(SCAModule(module))

	def __repr__(self):
		return str(self.module_list)
		
class SCAExportBinding:
	def __init__(self, sca_export_binding_string):
		if not(sca_export_binding_string.startswith('exportBinding:')):
			raise ParseError("Expected showSCAExportBinding string to start with 'exportBinding'")
		values = sca_export_binding_string[14:].split(',')
		self.type = values[0].split('=')[1]

modules = SCAModuleList(AdminTask.listSCAModules())
for module in modules.module_list:
	if module.name == 'pensjon-opptjening-tjeneste':
		scaExports = AdminTask.listSCAExports('-moduleName ' + module.name + ' -applicationName ' + module.app_name).split()
		for scaExport in scaExports:
			scaExportBinding = SCAExportBinding(AdminTask.showSCAExportBinding('-moduleName ' + module.name + ' -export ' + scaExport + ' -applicationName ' + module.app_name))
			if scaExportBinding.type == 'JaxWsExportBinding':
				print 'module=' + module.name + ',app=' + module.app_name + ',export=' + scaExport