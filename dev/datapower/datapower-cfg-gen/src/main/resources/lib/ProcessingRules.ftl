<#include "ProcessingRule.ftl"/>

<#macro ProcessingRequestRule name actions>
	<@ProcessingRule
		name="${name}"
		direction="request-rule"
		actions=actions/>
</#macro>

<#macro ProcessingResponseRule name actions>
	<@ProcessingRule
		name="${name}"
		direction="response-rule"
		actions=actions/>
</#macro>

<#macro ProcessingErrorRule name actions>
	<@ProcessingRule
		name="${name}"
		direction="error-rule"
		actions=actions/>
</#macro>