<#macro XMLManagerExternalReferences name bytesScanned>
	<XMLManager xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management" name="${name}">
	<mAdminState>enabled</mAdminState>
	<ParserLimitsExternalReferences>allow</ParserLimitsExternalReferences>
	<ParserLimitsBytesScanned>${bytesScanned}</ParserLimitsBytesScanned>
	<UserAgent class="HTTPUserAgent">default</UserAgent>
</XMLManager>
</#macro>