<#macro MultiProtocolGatewayFrontsideParamStaticBackend name stylePolicy xmlManager frontProtocolClass frontsideHandler backendUrl sslProxyProfile>
	<MultiProtocolGateway name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<Type>static-backend</Type>
		<StylePolicy class="StylePolicy">${stylePolicy}</StylePolicy>
		<XMLManager class="XMLManager">${xmlManager}</XMLManager>
		<FrontProtocol class="${frontProtocolClass}">${frontsideHandler}</FrontProtocol>
		<BackendUrl>${backendUrl}</BackendUrl>
		<SSLProxy class="SSLProxyProfile">${sslProxyProfile}</SSLProxy>
	</MultiProtocolGateway>
</#macro>