<#include "WSEndpointRewritePolicy.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyStaticBackendLoadBalancing
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
		loadBalancerGroup
		xmlMgrName
		backsidePort
		backsideUri
		backsideSSLProxy
		backsideTimeout>
	<@WSEndpointRewritePolicy
		name="${name}"
		wsdlPortBinding=wsdlPortBinding
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		frontsideUri="${frontsideUri}"
		backsideProtocol="${backsideProtocol}"
		backsideHost="${loadBalancerGroup}"
		backsidePort="${backsidePort}"
		backsideUri="${backsideUri}"/>
	<@WSGatewayLoadBalancing
		name="${name}"
		version="${version}"
		xmlMgrName="${xmlMgrName}"
		sslProxy="${backsideSSLProxy}"
		wsdlName="${wsdlName}"
		wsdlLocation="${wsdlLocation}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"
		type="static-from-wsdl"
		toggleNoWsa="on"
		wsaMode="sync2sync"
		wsaRequireAaa="off"
		backTimeout="${backsideTimeout}"/>
</#macro>