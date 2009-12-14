<#macro MatchingRuleErrorCode name errorCodeMatch>
	<Matching name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<MatchRules>
			<Type>errorcode</Type>
			<HttpTag/>
			<HttpValue/>
			<Url/>
			<ErrorCode>${errorCodeMatch}</ErrorCode>
			<XPATHExpression/>
		</MatchRules>
		<MatchWithPCRE>off</MatchWithPCRE>
		<CombineWithOr>off</CombineWithOr>
	</Matching>
</#macro>