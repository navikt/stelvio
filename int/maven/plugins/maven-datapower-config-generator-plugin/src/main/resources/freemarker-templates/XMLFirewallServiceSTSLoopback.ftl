<#macro XMLFirewallServiceSTSLoopback name port cfgHost>
	<Matching name="${name}-XMLFW" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<MatchRules>
			<Type>url</Type>
			<HttpTag/>
			<HttpValue/>
			<Url>*</Url>
			<ErrorCode/>
			<XPATHExpression/>
			<Method>default</Method>
		</MatchRules>
		<MatchWithPCRE>off</MatchWithPCRE>
		<CombineWithOr>off</CombineWithOr>
	</Matching>
	<StylePolicyAction name="${name}-XMLFW_request_get_plaintext" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>xform</Type>
		<Input>INPUT</Input>
		<Transform>store:///identity.xsl</Transform>
		<Output>tempvar1</Output>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>off</Asynchronous>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>0</RetryCount>
		<RetryInterval>1000</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
		<MethodRewriteType>GET</MethodRewriteType>
		<MethodType>POST</MethodType>
		<MethodType2>POST</MethodType2>
	</StylePolicyAction>
	<StylePolicyAction name="${name}-XMLFW_request_results" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>results</Type>
		<Input>tempvar1</Input>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>off</Asynchronous>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>0</RetryCount>
		<RetryInterval>1000</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
		<MethodRewriteType>GET</MethodRewriteType>
		<MethodType>POST</MethodType>
		<MethodType2>POST</MethodType2>
	</StylePolicyAction>
	<StylePolicyRule name="${name}-XMLFW_request" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>request-rule</Direction>
		<InputFormat>none</InputFormat>
		<OutputFormat>none</OutputFormat>
		<NonXMLProcessing>off</NonXMLProcessing>
		<Unprocessed>off</Unprocessed>
		<Actions class="StylePolicyAction">${name}-XMLFW_request_get_plaintext</Actions>
		<Actions class="StylePolicyAction">${name}-XMLFW_request_results</Actions>
	</StylePolicyRule>
	<StylePolicy name="${name}-XMLFW" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<DefStylesheetForSoap>store:///filter-reject-all.xsl</DefStylesheetForSoap>
		<DefStylesheetForXsl>store:///identity.xsl</DefStylesheetForXsl>
		<PolicyMaps>
			<Match class="Matching">${name}-XMLFW</Match>
			<Rule class="StylePolicyRule">${name}-XMLFW_request</Rule>
		</PolicyMaps>
	</StylePolicy>
	<XMLFirewallService name="${name}-XMLFW" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<LocalAddress>${cfgHost}</LocalAddress>
		<Priority>normal</Priority>
		<LocalPort>${port}</LocalPort>
		<HTTPTimeout>120</HTTPTimeout>
		<HTTPPersistTimeout>180</HTTPPersistTimeout>
		<DoHostRewrite>on</DoHostRewrite>
		<SuppressHTTPWarnings>off</SuppressHTTPWarnings>
		<HTTPCompression>off</HTTPCompression>
		<HTTPIncludeResponseTypeEncoding>off</HTTPIncludeResponseTypeEncoding>
		<AlwaysShowErrors>off</AlwaysShowErrors>
		<DisallowGet>on</DisallowGet>
		<DisallowEmptyResponse>off</DisallowEmptyResponse>
		<HTTPPersistentConnections>on</HTTPPersistentConnections>
		<HTTPClientIPLabel>X-Client-IP</HTTPClientIPLabel>
		<HTTPProxyPort>800</HTTPProxyPort>
		<HTTPVersion>
			<Front>HTTP/1.1</Front>
			<Back>HTTP/1.1</Back>
		</HTTPVersion>
		<DoChunkedUpload>off</DoChunkedUpload>
		<DefaultParamNamespace>http://www.datapower.com/param/config</DefaultParamNamespace>
		<QueryParamNamespace>http://www.datapower.com/param/query</QueryParamNamespace>
		<ForcePolicyExec>off</ForcePolicyExec>
		<MonitorProcessingPolicy>terminate-at-first-throttle</MonitorProcessingPolicy>
		<DebugMode persisted="false">off</DebugMode>
		<DebuggerType>internal</DebuggerType>
		<DebugHistory>25</DebugHistory>
		<WebGUIMode>off</WebGUIMode>
		<Type>loopback-proxy</Type>
		<XMLManager class="XMLManager">default</XMLManager>
		<StylePolicy class="StylePolicy">${name}-XMLFW</StylePolicy>
		<MaxMessageSize>0</MaxMessageSize>
		<RequestType>soap</RequestType>
		<ResponseType>soap</ResponseType>
		<RequestAttachments>strip</RequestAttachments>
		<ResponseAttachments>strip</ResponseAttachments>
		<RootPartNotFirstAction>process-in-order</RootPartNotFirstAction>
		<FrontAttachmentFormat>dynamic</FrontAttachmentFormat>
		<BackAttachmentFormat>dynamic</BackAttachmentFormat>
		<MIMEHeaders>on</MIMEHeaders>
		<RewriteErrors>on</RewriteErrors>
		<DelayErrors>on</DelayErrors>
		<DelayErrorsDuration>1000</DelayErrorsDuration>
		<SOAPSchemaURL>store:///schemas/soap-envelope.xsd</SOAPSchemaURL>
		<WSDLResponsePolicy>off</WSDLResponsePolicy>
		<FirewallParserLimits>off</FirewallParserLimits>
		<ParserLimitsBytesScanned>4194304</ParserLimitsBytesScanned>
		<ParserLimitsElementDepth>512</ParserLimitsElementDepth>
		<ParserLimitsAttributeCount>128</ParserLimitsAttributeCount>
		<ParserLimitsMaxNodeSize>33554432</ParserLimitsMaxNodeSize>
		<ParserLimitsForbidExternalReferences>on</ParserLimitsForbidExternalReferences>
		<ParserLimitsMaxPrefixes>0</ParserLimitsMaxPrefixes>
		<ParserLimitsMaxNamespaces>0</ParserLimitsMaxNamespaces>
		<ParserLimitsMaxLocalNames>0</ParserLimitsMaxLocalNames>
		<ParserLimitsAttachmentByteCount>2000000000</ParserLimitsAttachmentByteCount>
		<ParserLimitsAttachmentPackageByteCount>0</ParserLimitsAttachmentPackageByteCount>
		<ParserLimitsExternalReferences>forbid</ParserLimitsExternalReferences>
		<CredentialCharset>protocol</CredentialCharset>
	</XMLFirewallService>
</#macro>