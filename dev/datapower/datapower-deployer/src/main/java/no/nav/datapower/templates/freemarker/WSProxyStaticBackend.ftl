<#include "WSEndpointRewritePolicy.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyStaticBackend
		name
		version
		wsdlName
		wsdlPortBinding
		policy
		frontsideHandler
		frontsideProtocol
		backsideProtocol
		backsideHost
		backsidePort
		endpointUri>
	<@WSEndpointRewritePolicy
		name="${name}"
		wsdlPortBinding="${wsdlPortBinding}"
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		frontsideUri="${endpointUri}"
		backsideProtocol="${backsideProtocol}"
		backsideHost="${backsideHost}"
		backsidePort="${backsidePort}"
		backsideUri="${endpointUri}"/>
	<@WSGateway
		name="${name}"
		version="${version}"
		sslProxy="${backsideHost}_SSLProxyProfile"
		wsdlName="${wsdlName}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"/>
</#macro>