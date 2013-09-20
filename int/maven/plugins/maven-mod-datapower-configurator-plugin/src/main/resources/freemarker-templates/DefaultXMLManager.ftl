<#macro DefaultXMLManager bytesScanned>
	<XMLManager name="default" intrinsic="true" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<ParserLimitsBytesScanned>${bytesScanned}</ParserLimitsBytesScanned>
	</XMLManager>
</#macro>