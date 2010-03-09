<#include "WSEndpointRewritePolicyMultipleWsdl.ftl"/>
<#include "WSGatewayMultipleWsdl.ftl"/>
<#macro WSProxyStaticBackendMultipleWsdl
		name
		version
		wsdls
		policy
		frontsideHandler
		frontsideProtocol
		backsideSSLProxy
		backsideProtocol
		backsideHost
		backsidePort
		backsideTimeout>
	<@WSEndpointRewritePolicyMultipleWsdl
		name="${name}"
		wsdls=wsdls
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		backsideProtocol="${backsideProtocol}"
		backsideHost="${backsideHost}"
		backsidePort="${backsidePort}"/>
	<@WSGatewayMultipleWsdl
		name="${name}"
		version="${version}"
		wsdls=wsdls
		sslProxy="${backsideSSLProxy}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"
		type="static-from-wsdl"
		toggleNoWsa="on"
		wsaMode="sync2sync"
		wsaRequireAaa="off"
		backTimeout="${backsideTimeout}"
		<#-- consider introducing includeRepsonseTypeEncoding for all proxies -->
		includeResponseTypeEncoding="off"/>
</#macro>
