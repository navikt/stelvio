<#include "WSEndpointRewritePolicyMultipleWsdl.ftl"/>
<#include "WSGatewayMultipleWsdl.ftl"/>
<#macro WSProxyWSADynamicBackendMultipleWsdl
		name
		version
		wsdls
		policy
		frontsideHandler
		frontsideProtocol
		backsideSSLProxy
		wsaRequireAaa
		backsideTimeout>
	<@WSEndpointRewritePolicyMultipleWsdl
		name="${name}"
		wsdls=wsdls
		frontsideProtocol="${frontsideProtocol}"
		frontsideHandler="${frontsideHandler}"
		backsideProtocol="https"
		backsideHost="localhost"
		backsidePort="1234"/>
		<#--backsideUri="/ERROR-should-be-used-with-dynamic-backends-ERROR"/>-->
	<@WSGatewayMultipleWsdl
		name="${name}"
		version="${version}"
		wsdls=wsdls
		sslProxy="${backsideSSLProxy}"
		rewritePolicy="${name}"
		stylePolicy="${policy}"
		type="dynamic-backend"
		toggleNoWsa="off"
		wsaMode="wsa2sync"
		wsaRequireAaa="${wsaRequireAaa}"
		backTimeout="${backsideTimeout}"
		<#-- includeResponseTypeEncoding introduced for interoperability with SPK, consider for all proxies later -->
		includeResponseTypeEncoding="on"/>
</#macro>