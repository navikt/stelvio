<#macro MultiProtocolGatewayFrontsideParam name stylePolicy xmlManager frontProtocolClass frontsideHandler >
	<MultiProtocolGateway name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<Type>dynamic-backend</Type>
		<StylePolicy class="StylePolicy">${stylePolicy}</StylePolicy>
		<XMLManager class="XMLManager">${xmlManager}</XMLManager>
		<FrontProtocol class="${frontProtocolClass}">${frontsideHandler}</FrontProtocol>
	</MultiProtocolGateway>
</#macro>