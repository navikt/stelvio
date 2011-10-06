<#macro MPGStylePolicyRuleResponse name actions>
	<StylePolicyRule name="ServiceGateway_response" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>response-rule</Direction>
		<InputFormat>none</InputFormat>
		<OutputFormat>none</OutputFormat>
		<NonXMLProcessing>off</NonXMLProcessing>
		<Unprocessed>off</Unprocessed>
		<#list actions as action>
		<Actions class="StylePolicyAction">${action}</Actions>
		</#list>
	</StylePolicyRule>
</#macro>