<#include "WSEndpointRewritePolicy.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyStaticBackend
		name
		version
		wsdlName
		wsdlPortBinding
		policy
		frontside
		backside
		endpointUri>
	<@WSEndpointRewritePolicy
		name="${name}"
		wsdlPortBinding="${wsdlPortBinding}"
		frontsideProtocol="${frontside.Protocol}"
		frontsideHandler="${frontside.Handler}"
		frontsideUri="${endpointUri}"
		backsideProtocol="${backside.Protocol}"
		backsideHost="${backside.Host}"
		backsidePort="${backside.Port}"
		backsideUri="${endpointUri}"/>
	<@WSGateway
		name="${name}"
		version="${version}"
		sslProxy="${backside.Host}_SSLProxy"
		wsdlName="${wsdlName}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"/>
</#macro>