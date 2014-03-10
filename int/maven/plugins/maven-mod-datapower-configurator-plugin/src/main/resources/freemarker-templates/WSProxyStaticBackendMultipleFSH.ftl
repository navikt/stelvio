<#include "WSEndpointRewritePolicyMultipleFSH.ftl"/>
<#include "WSGateway.ftl"/>
<#macro WSProxyStaticBackendMultipleFSH
		name
		version
		wsdlName
		wsdlLocation
		wsdlPortBinding
		policy
		frontsideHandlers
		backsideProtocol
		backsideHost
		backsidePort
		backsideUri
		backsideSSLProxy
		backsideTimeout>
	<@WSEndpointRewritePolicyMultipleFSH
		name="${name}"
		wsdlPortBinding=wsdlPortBinding
		frontsideHandlers=frontsideHandlers
		backsideProtocol="${backsideProtocol}"
		backsideHost="${backsideHost}"
		backsidePort="${backsidePort}"
		backsideUri="${backsideUri}"/>
	<@WSGateway
		name="${name}"
		version="${version}"
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