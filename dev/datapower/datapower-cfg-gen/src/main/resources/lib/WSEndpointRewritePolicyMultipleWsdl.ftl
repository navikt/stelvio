<#macro WSEndpointRewritePolicyMultipleWsdl
	name
	wsdls
	frontsideProtocol
	frontsideHandler
	backsideProtocol
	backsideHost
	backsidePort>
	<#--backsideUri>-->
	<WSEndpointRewritePolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<#list wsdls as wsdl>
		<WSEndpointLocalRewriteRule>
			<ServicePortMatchRegexp>^${wsdl.portBinding}$</ServicePortMatchRegexp>
			<LocalEndpointProtocol>default</LocalEndpointProtocol>
			<LocalEndpointHostname>0.0.0.0</LocalEndpointHostname>
			<LocalEndpointPort>0</LocalEndpointPort>
			<LocalEndpointURI>${wsdl.frontsideURI}</LocalEndpointURI>
			<FrontProtocol class="${frontsideProtocol?upper_case}SourceProtocolHandler">${frontsideHandler}</FrontProtocol>
			<UseFrontProtocol>on</UseFrontProtocol>
			<WSDLBindingProtocol>soap-11</WSDLBindingProtocol>
			<FrontsidePortSuffix/>
		</WSEndpointLocalRewriteRule>
		</#list>
		<#list wsdls as wsdl>
		<WSEndpointRemoteRewriteRule>
			<ServicePortMatchRegexp>^${wsdl.portBinding}$</ServicePortMatchRegexp>
			<RemoteEndpointProtocol>${backsideProtocol}</RemoteEndpointProtocol>
			<RemoteEndpointHostname>${backsideHost}</RemoteEndpointHostname>
			<RemoteEndpointPort>${backsidePort}</RemoteEndpointPort>
			<#--<RemoteEndpointURI>${backsideUri}</RemoteEndpointURI>-->
			<RemoteEndpointURI>${wsdl.endpointURI}</RemoteEndpointURI>
		</WSEndpointRemoteRewriteRule>
		</#list>
	</WSEndpointRewritePolicy>
</#macro>