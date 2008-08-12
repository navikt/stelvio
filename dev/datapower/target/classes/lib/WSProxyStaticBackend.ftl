<#include "WSEndpointRewritePolicy.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyStaticBackend
		name
		version
		wsdlName
		wsdlLocation
		wsdlPortBinding
		policy
		frontsideHandler
		frontsideProtocol
		frontsideUri
		backsideProtocol
		backsideHost
		backsidePort
		backsideUri>
	<@WSEndpointRewritePolicy
		name="${name}"
		wsdlPortBinding="${wsdlPortBinding}"
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		frontsideUri="${frontsideUri}"
		backsideProtocol="${backsideProtocol}"
		backsideHost="${backsideHost}"
		backsidePort="${backsidePort}"
		backsideUri="${backsideUri}"/>
	<@WSGateway
		name="${name}"
		version="${version}"
		sslProxy="${backsideHost}_SSLProxyProfile"
		wsdlName="${wsdlName}"
		wsdlLocation="${wsdlLocation}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"
		type="static-from-wsdl"
		wsaMode="sync2sync"/>
</#macro>