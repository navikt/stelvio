<#macro WSEndpointRewritePolicyMultipleFSH name
	wsdlPortBinding
	frontsideHandlers
	backsideProtocol
	backsideHost
	backsidePort
	backsideUri>	
	<WSEndpointRewritePolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<#assign suffix=-1 >
		<#list wsdlPortBinding as wsdlPortBindingItem>
			<#list frontsideHandlers as fsh>
			<#assign suffix=suffix+1 >
		<WSEndpointLocalRewriteRule>
			<ServicePortMatchRegexp>^${wsdlPortBindingItem}$</ServicePortMatchRegexp>
			<LocalEndpointProtocol>default</LocalEndpointProtocol>
			<LocalEndpointHostname>0.0.0.0</LocalEndpointHostname>
			<LocalEndpointPort>0</LocalEndpointPort>
			<LocalEndpointURI>${fsh.uri}</LocalEndpointURI>
			<FrontProtocol class="${fsh.protocol?upper_case}SourceProtocolHandler">${fsh.name}</FrontProtocol>
			<UseFrontProtocol>on</UseFrontProtocol>
			<#if wsdlPortBindingItem?ends_with("Soap12")>
			<WSDLBindingProtocol>soap-12</WSDLBindingProtocol>
			<#else>
			<WSDLBindingProtocol>soap-11</WSDLBindingProtocol>
			</#if>			
			<FrontsidePortSuffix>${suffix}</FrontsidePortSuffix>
		</WSEndpointLocalRewriteRule>
			</#list>
		</#list>
		<#list wsdlPortBinding as wsdlPortBindingItem>		
		<WSEndpointRemoteRewriteRule>
			<ServicePortMatchRegexp>^${wsdlPortBindingItem}$</ServicePortMatchRegexp>
			<RemoteEndpointProtocol>${backsideProtocol}</RemoteEndpointProtocol>
			<RemoteEndpointHostname>${backsideHost}</RemoteEndpointHostname>
			<RemoteEndpointPort>${backsidePort}</RemoteEndpointPort>
			<RemoteEndpointURI>${backsideUri}</RemoteEndpointURI>
		</WSEndpointRemoteRewriteRule>
		</#list>
	</WSEndpointRewritePolicy>	
</#macro>