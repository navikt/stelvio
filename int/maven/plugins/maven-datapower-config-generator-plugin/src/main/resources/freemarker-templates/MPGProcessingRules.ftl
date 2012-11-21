<#include "MPGProcessingRule.ftl"/>

<#macro MPGProcessingRequestRule name actions>
	<@MPGProcessingRule
		name="${name}"
		direction="request-rule"
		actions=actions/>
</#macro>

<#macro MPGProcessingFaultRule name actions>
	<@MPGProcessingRule
		name="${name}"
		direction="response-rule"
		actions=actions
		subtype="fault-"/>
</#macro>

<#macro MPGProcessingResponseRule name actions>
	<@MPGProcessingRule
		name="${name}"
		direction="response-rule"
		actions=actions/>
</#macro>

<#macro MPGProcessingErrorRule name actions>
	<@MPGProcessingRule
		name="${name}"
		direction="error-rule"
		actions=actions/>
</#macro>