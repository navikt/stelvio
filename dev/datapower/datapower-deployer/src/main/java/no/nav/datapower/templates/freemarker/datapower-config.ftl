<#macro configuration domain>
<datapower-configuration version="3">
	<configuration domain="${domain}">
		<#nested>
	</configuration>
</datapower-configuration>
</#macro>

<#include "datapower-crypto.ftl">
<#include "AAAPolicy.ftl">
<#include "AAAAction.ftl">
<#include "FrontsideHandler.ftl">
<#include "ProcessingPolicy.ftl">
<#include "RequestRule.ftl">
<#include "ResponseRule.ftl">
<#include "ResultAction.ftl">
<#include "SLMPolicy.ftl">
<#include "SLMAction.ftl">
<#include "URLMatchingRule.ftl">
<#include "WSProxyStaticBackend.ftl">
