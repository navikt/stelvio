<#macro WSStylePolicy
	name
	policyMapsList>
	<WSStylePolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<DefStylesheetForSoap>store:///filter-reject-all.xsl</DefStylesheetForSoap>
		<DefStylesheetForXsl>store:///identity.xsl</DefStylesheetForXsl>
		<#list policyMapsList as policyMaps>
		<PolicyMaps>
			<WSDLComponentType>all</WSDLComponentType>
			<WSDLComponentValue/>
			<Match class="Matching">${policyMaps.matchingRule}</Match>
			<Rule class="WSStylePolicyRule">${policyMaps.processingRule}</Rule>
			<Subscription/>
		</PolicyMaps>
		</#list>
	</WSStylePolicy>
</#macro>