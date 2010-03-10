<#include "StylePolicyActionAAA.ftl"/>
<#include "StylePolicyActionLog.ftl"/>
<#include "StylePolicyActionResult.ftl"/>
<#include "StylePolicyActionSign.ftl"/>
<#include "StylePolicyActionSLM.ftl"/>
<#include "StylePolicyActionTransform.ftl"/>
<#include "StylePolicyActionTransformParameterized.ftl"/>
<#include "StylePolicyActionVerify.ftl"/>
<#macro ProcessingRule name direction actions subtype="">
	<#list actions as action>
	<#assign actionName="${name}_${subtype}${direction}-${action.name}"/>
	<#--
		Execute AAA Policy Action
	-->
	<#if action.type == "aaa">
	<@StylePolicyActionAAA
		name="${actionName}"
		aaaPolicy="${action.policy}"
		input="${action.input}"
		output="${action.output}"/>

	<#--
		LOG Message Action
	-->
	<#elseif action.type == "log">
	<@StylePolicyActionLog
		name="${actionName}"
		destination="${action.destination}"
		input="${action.input}"/>
	<#--
		Result Action
	-->
	<#elseif action.type == "result">
	<@StylePolicyActionResult
		name="${actionName}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"
		destination="${action.destination!''}"/>
	<#--
		Add WS-Security Signature Action
	-->
	<#elseif action.type == "sign">
	<@StylePolicyActionSign
		name="${actionName}"
		input="${action.input}"
		output="${action.output}"
		signCert="${action.signCert}"
		signKey="${action.signKey}"/>
	<#--
		SLM Action
	-->
	<#elseif action.type == "slm">
	<@StylePolicyActionSLM
		name="${actionName}"
		input="${action.input}"/>
	<#--
		XSLT Transform Action
	-->
	<#elseif action.type == "xform">
	<#if action.params?size == 0> 
	<@StylePolicyActionTransform
		name="${actionName}"
		input="${action.input}"
		output="${action.output}"
		async="${action.async!'off'}"
		stylesheet="${action.stylesheet}"/>
	<#else>
	<@StylePolicyActionTransformParameterized
		name="${actionName}"
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
		name="${actionName}"
		input="${action.input}"
		valCred="${action.valCred}"/>
	<#else>
	</#if> 
	</#list>
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