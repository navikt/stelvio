<#macro XMLManager name userSummary compileOptionsPolicy parserLimitsBytesScanned parserLimitsMaxNodeSize>
	<XMLManager name="${name}" intrinsic="true" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<UserSummary>${userSummary}</UserSummary>
		<#if compileOptionsPolicy??>
		<CompileOptionsPolicy class="CompileOptionsPolicy">${compileOptionsPolicy}</CompileOptionsPolicy>
		</#if>
		<CacheSize>256</CacheSize>
		<SHA1Caching>on</SHA1Caching>
		<StaticDocumentCalls>on</StaticDocumentCalls>
		<SearchResults>on</SearchResults>
		<SupportTxWarn>off</SupportTxWarn>
		<Memoization>on</Memoization>
		<ParserLimitsBytesScanned>${parserLimitsBytesScanned}</ParserLimitsBytesScanned>
		<ParserLimitsElementDepth>512</ParserLimitsElementDepth>
		<ParserLimitsAttributeCount>128</ParserLimitsAttributeCount>
		<ParserLimitsMaxNodeSize>${parserLimitsMaxNodeSize}</ParserLimitsMaxNodeSize>
		<ParserLimitsForbidExternalReferences>on</ParserLimitsForbidExternalReferences>
		<ParserLimitsExternalReferences>forbid</ParserLimitsExternalReferences>
		<ParserLimitsMaxPrefixes>0</ParserLimitsMaxPrefixes>
		<ParserLimitsMaxNamespaces>0</ParserLimitsMaxNamespaces>
		<ParserLimitsMaxLocalNames>0</ParserLimitsMaxLocalNames>
		<DocCacheMaxDocs>5000</DocCacheMaxDocs>
		<DocCacheSize>0</DocCacheSize>
		<UserAgent class="HTTPUserAgent">default</UserAgent>
	</XMLManager>
</#macro>