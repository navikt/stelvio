<#import "datapower-config.ftl" as dp>

<#assign defaultPolicyName="ServiceGateway"/>
<#assign httpSourceProtocolHandlerName="http_@localSourceProtocolAddress@_@localSourceProtocolPort@"/>

<#assign defaultMatchAll="ServiceGateway_match_all"/>
<#assign requestFetchAction="_request_fetch_0"/>
<#assign requestRouteAction="_request_route-action_0"/>
<#assign requestValidateAction="_request_validate_0"/>
<#assign requestResultsAction="_request_results_output_0"/>
<#assign responseResultsAction="_response_results_output_0"/>
<#assign errorXformAction="_error_xform_0"/>
<#assign errorResultsAction="_error_results_output_0"/>

<@dp.configuration domain="@cfgDomain@">
	<@dp.HTTPSourceProtocolHandler 
		name="${httpSourceProtocolHandlerName}"
		localAddress=@localSourceProtocolAddress@
		localPort=@localSourceProtocolPort@ />
	<@dp.MatchingRuleURL
		name="${defaultMatchAll}"
		urlMatch="*"/>
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"fetch", "name":"${requestFetchAction}", 
			"output":"service-registry", "destination":"@serviceRegistryDestinationFile@"} />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"route-action", "name":"${requestRouteAction}", 
			"input":"service-registry", "transform":"@endpointLookupTransformFile@"} />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"validate", "name":"${requestValidateAction}", 
			"input":"INPUT", "output":"NULL", "wsdlURL":"var://context/wsdl/url"} />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"results", "name":"${requestResultsAction}", 
			"input":"INPUT"}/>
	<@dp.MPGStylePolicyRuleRequest
		name="${defaultPolicyName}_request"
		actions=[
			{"${defaultPolicyName}${requestFetchAction}"}, 
			{"${defaultPolicyName}${requestRouteAction}"}, 
			{"${defaultPolicyName}${responseResultsAction}"}] />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"results", "name":"${responseResultsAction}", 
			"input":"INPUT"} />
	<@dp.MPGStylePolicyRuleResponse
		name="${defaultPolicyName}_response"
		actions=[{"${defaultPolicyName}${responseResultsAction}"}] />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"xform", "name":"${errorXformAction}", 
			"input":"INPUT", "transform":"@faultHandlerFile@", 
			"output":"PIPE"} />
	<@dp.MPGStylePolicyAction
		actionNamePrefix="${defaultPolicyName}"
		action={
			"type":"results", "name":"${errorResultsAction}", 
			"input":"PIPE"} />
	<@dp.MPGStylePolicyRuleError
		name="${defaultPolicyName}_error"
		actions=[
			{"${defaultPolicyName}${errorXformAction}"}, 
			{"${defaultPolicyName}${errorResultsAction}"}] />
	<@dp.MPGStylePolicy 
		name="${defaultPolicyName}"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"${defaultPolicyName}_request}"}, 
			{"matchingRule":"${defaultMatchAll}","processingRule":"${defaultPolicyName}_response}"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${defaultPolicyName}_error}"}]/>
	<@dp.MultiProtocolGateway
		name="${defaultPolicyName}"
		httpSourceProtocolHandler="${httpSourceProtocolHandlerName}"
		stylePolicy="${defaultPolicyName}"/>
</@dp.configuration>