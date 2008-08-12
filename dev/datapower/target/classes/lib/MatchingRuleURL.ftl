<#macro MatchingRuleURL name urlMatch>
	<Matching name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<MatchRules>
			<Type>url</Type>
			<HttpTag/>
			<HttpValue/>
			<Url>${urlMatch}</Url>
			<ErrorCode/>
			<XPATHExpression/>
		</MatchRules>
		<MatchWithPCRE>off</MatchWithPCRE>
		<CombineWithOr>off</CombineWithOr>
	</Matching>
</#macro>
