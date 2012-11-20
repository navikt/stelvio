<#include "StylePolicyActionAAA.ftl"/>
<#include "StylePolicyActionConditional.ftl"/>
<#include "StylePolicyActionFetch.ftl"/>
<#include "StylePolicyActionLog.ftl"/>
<#include "StylePolicyActionResult.ftl"/>
<#include "StylePolicyActionSign.ftl"/>
<#include "StylePolicyActionSLM.ftl"/>
<#include "StylePolicyActionTransform.ftl"/>
<#include "StylePolicyActionTransformParameterized.ftl"/>
<#include "StylePolicyActionVerify.ftl"/>
<#include "StylePolicyActionRoute.ftl"/>
<#macro StylePolicyAction actionNamePrefix action>
	<#--
		Execute AAA Policy Action
	-->
	<#if action.type == "aaa">
	<@StylePolicyActionAAA
		name="${actionNamePrefix}${action.name}"
		aaaPolicy="${action.policy}"
		input="${action.input}"
		output="${action.output}"/>
	<#--
		Fetch Action
	-->
	<#elseif action.type == "fetch">
	<@StylePolicyActionFetch
		name="${actionNamePrefix}${action.name}"
		destination="${action.destination}"
		output="${action.output}"/>
	<#--
		Route Action
	-->
	<#elseif action.type == "route">
	<@StylePolicyActionRoute
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		transform="${action.transform}"/>
	<#--
		Conditional Action
	-->
	<#elseif action.type == "conditional">
	<@StylePolicyActionConditional
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		conditionActionNamePrefix=actionNamePrefix
		conditions=action.conditions/>
	<#--
		LOG Message Action
	-->
	<#elseif action.type == "log">
	<@StylePolicyActionLog
		name="${actionNamePrefix}${action.name}"
		destination="${action.destination}"
		input="${action.input}"/>
	<#--
		Result Action
	-->
	<#elseif action.type == "result">
	<@StylePolicyActionResult
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"
		destination="${action.destination!''}"/>
	<#--
		Add WS-Security Signature Action
	-->
	<#elseif action.type == "sign">
	<@StylePolicyActionSign
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		signCert="${action.signCert}"
		signKey="${action.signKey}"/>
	<#--
		SLM Action
	-->
	<#elseif action.type == "slm">
	<@StylePolicyActionSLM
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"/>
	<#--
		XSLT Transform Action
	-->
	<#elseif action.type == "xform">
	<#if action.params?size == 0> 
	<@StylePolicyActionTransform
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"
		stylesheet="${action.stylesheet}"/>
	<#else>
	<@StylePolicyActionTransformParameterized
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"
		stylesheet="${action.stylesheet}"
		params=action.params/>
	</#if>
	<#--
		Verify WS-Security Signature Action
	-->
	<#elseif action.type == "verify">
	<@StylePolicyActionVerify
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		valCred="${action.valCred}"/>
	<#else>
	</#if> 
</#macro>