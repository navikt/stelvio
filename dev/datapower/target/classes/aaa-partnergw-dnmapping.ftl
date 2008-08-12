<aaa:AAAInfo xmlns:dpfunc="http://www.datapower.com/extensions/functions" xmlns:aaa="http://www.datapower.com/AAAInfo">
	<aaa:FormatVersion>1</aaa:FormatVersion>
	<aaa:Filename>${inboundAaaFileName}</aaa:Filename>
	<aaa:Summary/>
	<!-- Determine credential from output of the extract-identity phase. -->
	<aaa:Authenticate>
		<aaa:DN>/C=NO/ST=Oslo/L=Oslo/O=Statens Pensjonskasse/OU=ITO/CN=partner-gw-test.pensjonskassa.no</aaa:DN>
		<aaa:OutputCredential>srvElsam_SPK</aaa:OutputCredential>
	</aaa:Authenticate>
	<aaa:Authenticate>
		<aaa:DN>/O=test.klp.no/OU=Go to https://www.thawte.com/repository/index.html/OU=Thawte SSL123 certificate/OU=Domain Validated/CN=test.klp.no</aaa:DN>
		<aaa:OutputCredential>srvElsam_KLP</aaa:OutputCredential>
	</aaa:Authenticate>
	<aaa:Authenticate>
		<aaa:DN>/CN=soapui.developer.local/OU=Pensjonsprogrammet/OU=NAV Drift og utvikling/O=Arbeids og Velferdsetaten/ST=OSLO/C=NO</aaa:DN>
		<aaa:OutputCredential>srvElsam_test</aaa:OutputCredential>
	</aaa:Authenticate>
	<!-- Specify credential (if any) to use when there is no authenticated identity. -->
	<!-- Map credentials to different credentials. -->
	<!-- Determine resource from output of the extract-resource phase. -->
	<!-- Authorize access to resource for credentials. -->
	<aaa:Authorize>
		<aaa:InputCredential>srvElsam_SPK</aaa:InputCredential>
		<aaa:InputResource>3010</aaa:InputResource>
		<aaa:Access>allow</aaa:Access>
	</aaa:Authorize>
	<aaa:Authorize>
		<aaa:InputCredential>srvElsam_KLP</aaa:InputCredential>
		<aaa:InputResource>3200</aaa:InputResource>
		<aaa:Access>allow</aaa:Access>
	</aaa:Authorize>
	<aaa:Authorize>
		<aaa:InputCredential>srvElsam_test</aaa:InputCredential>
		<aaa:InputResource>3010</aaa:InputResource>
		<aaa:Access>allow</aaa:Access>
	</aaa:Authorize>
</aaa:AAAInfo>