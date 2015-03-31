<#macro Statistics name adminState loadInterval>
	<Statistics name="${name}" intrinsic="true" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>${adminState}</mAdminState>
		<LoadInterval read-only="true">${loadInterval}</LoadInterval>
	</Statistics>
</#macro>