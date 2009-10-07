<#macro RequestFaultResponseErrorRuleProcessingPolicy
	name
	matchingRule
	faultMatchingRule
	errorMatchingRule
	requestRule
	faultRule
	responseRule
	errorRule>
	<WSStylePolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<DefStylesheetForSoap>store:///filter-reject-all.xsl</DefStylesheetForSoap>
		<DefStylesheetForXsl>store:///identity.xsl</DefStylesheetForXsl>
		<PolicyMaps>
			<WSDLComponentType>all</WSDLComponentType>
			<WSDLComponentValue/>
			<Match class="Matching">${matchingRule}</Match>
			<Rule class="WSStylePolicyRule">${requestRule}</Rule>
			<Subscription/>
		</PolicyMaps>
		<PolicyMaps>
			<WSDLComponentType>all</WSDLComponentType>
			<WSDLComponentValue/>
			<Match class="Matching">${faultMatchingRule}</Match>
			<Rule class="WSStylePolicyRule">${faultRule}</Rule>
			<Subscription/>
		</PolicyMaps>
		<PolicyMaps>
			<WSDLComponentType>all</WSDLComponentType>
			<WSDLComponentValue/>
			<Match class="Matching">${matchingRule}</Match>
			<Rule class="WSStylePolicyRule">${responseRule}</Rule>
			<Subscription/>
		</PolicyMaps>
		<PolicyMaps>
			<WSDLComponentType>all</WSDLComponentType>
			<WSDLComponentValue/>
			<Match class="Matching">${errorMatchingRule}</Match>
			<Rule class="WSStylePolicyRule">${errorRule}</Rule>
			<Subscription/>
		</PolicyMaps>
	</WSStylePolicy>
</#macro>