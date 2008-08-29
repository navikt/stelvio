<#include "WSEndpointRewritePolicy.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyWSADynamicBackend
		name
		version
		wsdlName
		wsdlLocation
		wsdlPortBinding
		policy
		frontsideHandler
		frontsideProtocol
		backsideSSLProxy
		endpointUri>
	<@WSEndpointRewritePolicy
		name="${name}"
		wsdlPortBinding="${wsdlPortBinding}"
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		frontsideUri="${endpointUri}"
		backsideProtocol="https"
		backsideHost="localhost"
		backsidePort="1234"
		backsideUri="/ERROR-should-be-used-with-dynamic-backends-ERROR"/>
	<@WSGateway
		name="${name}"
		version="${version}"
		sslProxy="${backsideSSLProxy}"
<#--	sslProxy="${backside}_SSLProxyProfile"-->
		wsdlName="${wsdlName}"
		wsdlLocation="${wsdlName}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"
		type="dynamic-backend"
		wsaMode="wsa2sync"/>
</#macro>