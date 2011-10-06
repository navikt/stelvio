<#include "MPGStylePolicyActionFetch.ftl"/>
<#include "MPGStylePolicyActionResults.ftl"/>
<#include "MPGStylePolicyActionRouteAction.ftl"/>
<#include "MPGStylePolicyActionValidate.ftl"/>
<#include "MPGStylePolicyActionTransform.ftl"/>
<#macro MPGStylePolicyAction actionNamePrefix action>
	<#--
		Fetch Action
	-->
	<#elseif action.type == "fetch">
	<@MPGStylePolicyActionFetch
		name="${actionNamePrefix}${action.name}"
		destination="${action.destination}"
		output="${action.output}"/>
	<#--
		Results Action
	-->
	<#elseif action.type == "results">
	<@MPGStylePolicyActionResults
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"/>
	<#--
		Route-Action Action
	-->
	<#elseif action.type == "route-action">
	<@MPGStylePolicyActionRouteAction
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		transform="${action.transform}"/>
	<#--
		XSLT Transform Action
	-->
	<#elseif action.type == "xform">
	<@MPGStylePolicyActionTransform
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		transform="${action.transform}"
		output="${action.output}"
		async="${action.async!'off'}"/>
	<#--
		Validate Action
	-->
	<#elseif action.type == "validate">
	<@MPGStylePolicyActionValidate
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		wsdlURL="${action.wsdlURL}"/>
	<#else>
	</#if> 
</#macro>