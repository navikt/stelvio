<#include "StylePolicyActionAAA.ftl"/>
<#include "StylePolicyActionAntiVirusScan.ftl"/>
<#include "StylePolicyActionCall.ftl"/>
<#include "StylePolicyActionConditional.ftl"/>
<#include "StylePolicyActionFetch.ftl"/>
<#include "StylePolicyActionLog.ftl"/>
<#include "StylePolicyActionOnError.ftl"/>
<#include "StylePolicyActionResult.ftl"/>
<#include "StylePolicyActionSign.ftl"/>
<#include "StylePolicyActionSignXpath.ftl"/>
<#include "StylePolicyActionSLM.ftl"/>
<#include "StylePolicyActionTransform.ftl"/>
<#include "StylePolicyActionTransformParameterized.ftl"/>
<#include "StylePolicyActionVerify.ftl"/>
<#include "StylePolicyActionRoute.ftl"/>
<#include "StylePolicyActionSetVar.ftl"/>
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
		AntiVirus Action
	-->
	<#elseif action.type == "antivirus">
	<@StylePolicyActionAntiVirusScan
		name="${actionNamePrefix}${action.name}"
		webwasher="${action.webwasher}"
        icap_server="${action.icap_server}"
        icap_port="${action.icap_port}"
        icap_uri="${action.icap_uri}"/>
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
		Call Action
	-->
	<#elseif action.type == "call">
	<@StylePolicyActionCall
		name="${actionNamePrefix}${action.name}"
        ruleRef="${action.ruleRef}"
		input="${action.input}"
		output="${action.output}"/>
	<#--
		LOG Message Action
	-->
	<#elseif action.type == "log">
	<@StylePolicyActionLog
		name="${actionNamePrefix}${action.name}"
		destination="${action.destination}"
		input="${action.input}"/>
    <#--
		OnError Action
	-->
	<#elseif action.type == "on-error">
	<@StylePolicyActionOnError
		name="${actionNamePrefix}${action.name}"
		errorRule="${action.errorRule}"/>
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
		Sign xpath Action
	-->
	<#elseif action.type == "signXpath">
	<@StylePolicyActionSignXpath
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
		output="${action.output}"
		xpath="${action.xpath}"
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
		Set Var
	-->
	<#elseif action.type == "setVar">
	<@StylePolicyActionSetVar
		name="${actionNamePrefix}${action.name}"
		input="${action.input}"
        variable="${action.variable}"
        value="${action.value}"/>
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
	</#if> 
</#macro>