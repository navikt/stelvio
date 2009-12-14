<#macro configuration domain>
<datapower-configuration version="3">
	<configuration domain="${domain}">
		<#nested>
	</configuration>
</datapower-configuration>
</#macro>

<#include "datapower-crypto.ftl">
<#include "AAAPolicyLTPA2LTPA.ftl">
<#include "AAAPolicyClientSSL2LTPA.ftl">
<#include "AAAPolicyClientSSL2LTPAAllAuthenticated.ftl">
<#include "AAAPolicyAuthenticateAllLTPA.ftl">
<#include "AAAPolicyBasicAuth.ftl">
<#include "AAAPolicyLTPA2None.ftl">
<#include "FrontsideHandler.ftl">
<#include "MatchingRuleErrorCode.ftl">
<#include "MatchingRuleURL.ftl">
<#include "MatchingRuleXPath.ftl">
<#include "NFSStaticMount.ftl">
<#include "ProcessingRules.ftl">
<#include "RequestResponseRuleProcessingPolicy.ftl">
<#include "RequestResponseErrorRuleProcessingPolicy.ftl">
<#include "RequestFaultResponseErrorRuleProcessingPolicy.ftl">
<#include "SLMPolicy.ftl">
<#include "StylePolicyActionAAA.ftl">
<#include "StylePolicyActionLog.ftl">
<#include "StylePolicyActionResult.ftl">
<#include "StylePolicyActionSLM.ftl">
<#include "StylePolicyActionTransform.ftl">
<#include "StylePolicyActionTransformParameterized.ftl">
<#include "StylePolicyActionVerify.ftl">
<#include "StylePolicyActionSign.ftl">
<#include "WSStylePolicy.ftl">
<#include "WSStylePolicyRuleError.ftl">
<#include "WSStylePolicyRuleRequest.ftl">
<#include "WSStylePolicyRuleResponse.ftl">
<#include "WSProxyStaticBackend.ftl">
<#include "WSProxyStaticBackendMultipleWsdl.ftl">
<#include "WSProxyWSADynamicBackend.ftl">
<#include "WSProxyWSADynamicBackendMultipleWsdl.ftl">
<#include "LogTarget.ftl">
