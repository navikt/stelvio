<#macro MatchingRuleXPath name xpathExpression>
	<Matching name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
        <MatchRules>
            <Type>xpath</Type>
            <HttpTag/>
            <HttpValue/>
            <Url/>
            <ErrorCode/>
            <XPATHExpression>${xpathExpression}</XPATHExpression>
        </MatchRules>
		<MatchWithPCRE>off</MatchWithPCRE>
		<CombineWithOr>off</CombineWithOr>
	</Matching>
</#macro>

<#macro MatchingRuleMultiXPath name xpathExpressions>
	<Matching name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
        <#list xpathExpressions as xpathExpression>
            <MatchRules>
                <Type>xpath</Type>
                <HttpTag/>
                <HttpValue/>
                <Url/>
                <ErrorCode/>
                <XPATHExpression>${xpathExpression.exp}</XPATHExpression>
            </MatchRules>
        </#list>
		<MatchWithPCRE>off</MatchWithPCRE>
		<CombineWithOr>off</CombineWithOr>
	</Matching>
</#macro>