def get():
	return """<?xml version="1.0"?>
<policySetBindings>
	<policySetBinding name="NAV ESB Arena WSImport Binding">
		<user>GOSYS</user>
		<password>${gosys-usernametoken-password}</password>
	</policySetBinding>
	<policySetBinding name="NAV ESB AAReg WSImport Binding">
		<user>${aa.usernametoken-username}</user>
		<password>${aa.usernametoken-password}</password>
	</policySetBinding>
	<policySetBinding name="NAV ESB UR WSImport Binding">
		<user>${oppdrag.srvur.username}</user>
		<password>${oppdrag.srvur.password}</password>
	</policySetBinding>
</policySetBindings>"""