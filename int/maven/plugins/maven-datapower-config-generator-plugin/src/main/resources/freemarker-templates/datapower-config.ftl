<#macro configuration domain>
<datapower-configuration version="3">
	<configuration domain="${domain}">
		<#nested>
	</configuration>
</datapower-configuration>
</#macro>

<#include "AAAPolicyAuthenticateAllLTPA.ftl">
<#include "AAAPolicyBasicAuth.ftl">
<#include "AAAPolicyClientSSL2LTPA.ftl">
<#include "AAAPolicyClientSSL2LTPAAllAuthenticated.ftl">
<#include "AAAPolicyClientSSL2LTPAURLauth.ftl">
<#include "AAAPolicyCustomId2URLAuth.ftl">
<#include "AAAPolicyLTPA2LTPA.ftl">
<#include "AAAPolicyLTPA2None.ftl">
<#include "AAAPolicyUsernameToken2LTPA.ftl">
<#include "AAAPolicyUsernameToken2None.ftl">
<#include "AAAPolicyUsernameToken2NoneAnyAuth.ftl">
<#include "AAAPolicyUsernameToken2UsernameToken.ftl">
<#include "AAAPolicyUsernameToken2UsernameTokenAuthorize.ftl">
<#include "AAAPolicyUsernameToken2UsernameTokenAuthorizeLDAPS.ftl">
<#include "AAAPolicyUsernameToken2UsernameTokenLDAPS.ftl">
<#include "datapower-crypto.ftl">
<#include "FrontsideHandler.ftl">
<#include "HTTPSourceProtocolHandler.ftl">
<#include "HTTPSSourceProtocolHandler.ftl">
<#include "LDAPSearchParameters.ftl">
<#include "LoadBalancerGroup.ftl">
<#include "LogTarget.ftl">
<#include "MatchingRuleErrorCode.ftl">
<#include "MatchingRuleURL.ftl">
<#include "MatchingRuleXPath.ftl">
<#include "MPGProcessingRules.ftl">
<#include "MPGStylePolicy.ftl">
<#include "MultiProtocolGateway.ftl">
<#include "NFSStaticMount.ftl">
<#include "ProcessingRules.ftl">
<#include "RequestFaultResponseErrorRuleProcessingPolicy.ftl">
<#include "RequestResponseErrorRuleProcessingPolicy.ftl">
<#include "RequestResponseRuleProcessingPolicy.ftl">
<#include "SLMPolicy.ftl">
<#include "StylePolicyActionAAA.ftl">
<#include "StylePolicyActionConditional.ftl">
<#include "StylePolicyActionFetch.ftl">
<#include "StylePolicyActionLog.ftl">
<#include "StylePolicyActionResult.ftl">
<#include "StylePolicyActionRoute.ftl">
<#include "StylePolicyActionSign.ftl">
<#include "StylePolicyActionSLM.ftl">
<#include "StylePolicyActionTransform.ftl">
<#include "StylePolicyActionTransformParameterized.ftl">
<#include "StylePolicyActionVerify.ftl">
<#include "WSGatewayLoadBalancing.ftl">
<#include "WSProxyStaticBackend.ftl">
<#include "WSProxyStaticBackendLoadBalancing.ftl">
<#include "WSProxyStaticBackendMultipleWsdl.ftl">
<#include "WSProxyWSADynamicBackend.ftl">
<#include "WSProxyWSADynamicBackendMultipleWsdl.ftl">
<#include "WSStylePolicy.ftl">
<#include "WSStylePolicyRuleError.ftl">
<#include "WSStylePolicyRuleRequest.ftl">
<#include "WSStylePolicyRuleResponse.ftl">
<#include "XMLFirewallServiceLoopback.ftl">
<#include "XMLManager.ftl">