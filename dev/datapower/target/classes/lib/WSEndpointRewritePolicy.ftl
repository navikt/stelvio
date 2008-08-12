<#macro WSEndpointRewritePolicy name
	wsdlPortBinding
	frontsideProtocol
	frontsideUri
	frontsideHandler
	backsideProtocol
	backsideHost
	backsidePort
	backsideUri>
	<WSEndpointRewritePolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<WSEndpointLocalRewriteRule>
			<ServicePortMatchRegexp>^${wsdlPortBinding}$</ServicePortMatchRegexp>
			<LocalEndpointProtocol>default</LocalEndpointProtocol>
			<LocalEndpointHostname>0.0.0.0</LocalEndpointHostname>
			<LocalEndpointPort>0</LocalEndpointPort>
			<LocalEndpointURI>${frontsideUri}</LocalEndpointURI>
			<FrontProtocol class="${frontsideProtocol?upper_case}SourceProtocolHandler">${frontsideHandler}</FrontProtocol>
			<UseFrontProtocol>on</UseFrontProtocol>
			<WSDLBindingProtocol>soap-11</WSDLBindingProtocol>
			<FrontsidePortSuffix/>
		</WSEndpointLocalRewriteRule>
		<WSEndpointRemoteRewriteRule>
			<ServicePortMatchRegexp>^${wsdlPortBinding}$</ServicePortMatchRegexp>
			<RemoteEndpointProtocol>${backsideProtocol}</RemoteEndpointProtocol>
			<RemoteEndpointHostname>${backsideHost}</RemoteEndpointHostname>
			<RemoteEndpointPort>${backsidePort}</RemoteEndpointPort>
			<RemoteEndpointURI>${backsideUri}</RemoteEndpointURI>
		</WSEndpointRemoteRewriteRule>
	</WSEndpointRewritePolicy>
</#macro>