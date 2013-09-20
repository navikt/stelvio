<#include "StylePolicyAction.ftl"/>

<#macro ProcessingRule name gwtype direction actions subtype="">
     <#list actions as action>
		<#--
			Create conditional actions in addition without adding to Processing rule
		-->
		<#if action.type == "conditional">
			<#list action.conditions as condition>
				<@StylePolicyAction actionNamePrefix="${name}_${subtype}${direction}-" action=condition.conditionAction/>
			</#list>
		</#if>
		<@StylePolicyAction actionNamePrefix="${name}_${subtype}${direction}-" action=action/>
	</#list>
    <#if gwtype == "mpgw">
        <@CreateMPGStylePolicyRule name="${name}" direction="${direction}" actions=actions subtype="${subtype}" />
    </#if>
    <#if gwtype == "wsgw">
        <@CreateWSStylePolicyRule name="${name}" direction="${direction}" actions=actions subtype="${subtype}" />
    </#if>
</#macro>

<#macro CreateWSStylePolicyRule name direction actions subtype>
    <#--
		Add the specified actions to a Processing rule
	-->
	<WSStylePolicyRule name="${name}_${subtype}${direction}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>${direction}</Direction>
		<InputFormat>none</InputFormat>
		<OutputFormat>none</OutputFormat>
		<NonXMLProcessing>off</NonXMLProcessing>
		<Unprocessed>off</Unprocessed>
		<#list actions as action>
		<Actions class="StylePolicyAction">${name}_${subtype}${direction}-${action.name}</Actions>
		</#list>
	</WSStylePolicyRule>
</#macro>

<#macro CreateMPGStylePolicyRule name direction actions subtype>
    <#--
		Add the specified actions to a Processing rule
	-->
	<StylePolicyRule name="${name}_${subtype}${direction}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>${direction}</Direction>
		<InputFormat>none</InputFormat>
		<OutputFormat>none</OutputFormat>
		<NonXMLProcessing>off</NonXMLProcessing>
		<Unprocessed>off</Unprocessed>
		<#list actions as action>
		<Actions class="StylePolicyAction">${name}_${subtype}${direction}-${action.name}</Actions>
		</#list>
	</StylePolicyRule>
</#macro>