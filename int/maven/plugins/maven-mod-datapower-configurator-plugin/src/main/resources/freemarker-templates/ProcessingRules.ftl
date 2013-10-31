<#include "ProcessingRule.ftl"/>

<#macro ProcessingRequestRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="request-rule"
		actions=actions/>
</#macro>

<#macro ProcessingFaultRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="response-rule"
		actions=actions
		subtype="fault-"/>
</#macro>

<#macro ProcessingResponseRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="response-rule"
		actions=actions/>
</#macro>

<#macro ProcessingErrorRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="error-rule"
		actions=actions/>
</#macro>

<#macro MPGProcessingRequestRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="mpgw"
		direction="request-rule"
		actions=actions/>
</#macro>

<#macro MPGProcessingFaultRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="mpgw"
		direction="response-rule"
		actions=actions
		subtype="fault-"/>
</#macro>

<#macro MPGProcessingResponseRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="mpgw"
		direction="response-rule"
		actions=actions/>
</#macro>

<#macro MPGProcessingErrorRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="mpgw"
		direction="error-rule"
		actions=actions/>
</#macro>

<#macro WSProcessingRequestRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="request-rule"
		actions=actions/>
</#macro>

<#macro WSProcessingFaultRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="response-rule"
		actions=actions
		subtype="fault-"/>
</#macro>

<#macro WSProcessingResponseRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="response-rule"
		actions=actions/>
</#macro>

<#macro WSProcessingErrorRule name actions>
	<@ProcessingRule
		name="${name}"
        gwtype="wsgw"
		direction="error-rule"
		actions=actions/>
</#macro>
