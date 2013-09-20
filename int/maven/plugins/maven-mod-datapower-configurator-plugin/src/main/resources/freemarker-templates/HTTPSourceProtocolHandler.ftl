<#macro HTTPSourceProtocolHandler name localAddress localPort>
	<HTTPSourceProtocolHandler name="${name}"
		xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<LocalAddress>${localAddress}</LocalAddress>
		<LocalPort>${localPort}</LocalPort>
		<HTTPVersion>HTTP/1.1</HTTPVersion>
		<AllowedFeatures>
			<HTTP-1.0>on</HTTP-1.0>
			<HTTP-1.1>on</HTTP-1.1>
			<POST>on</POST>
			<GET>off</GET>
			<PUT>on</PUT>
			<HEAD>off</HEAD>
			<OPTIONS>off</OPTIONS>
			<TRACE>off</TRACE>
			<DELETE>off</DELETE>
			<CONNECT>off</CONNECT>
			<QueryString>on</QueryString>
			<FragmentIdentifiers>on</FragmentIdentifiers>
			<DotDot>off</DotDot>
			<CmdExe>off</CmdExe>
		</AllowedFeatures>
		<PersistentConnections>on</PersistentConnections>
		<AllowCompression>off</AllowCompression>
		<MaxURLLen>16384</MaxURLLen>
		<MaxTotalHdrLen>128000</MaxTotalHdrLen>
		<MaxHdrCount>0</MaxHdrCount>
		<MaxNameHdrLen>0</MaxNameHdrLen>
		<MaxValueHdrLen>0</MaxValueHdrLen>
		<MaxQueryStringLen>0</MaxQueryStringLen>
	</HTTPSourceProtocolHandler>
</#macro>