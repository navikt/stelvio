<#macro XMLFirewallServiceLoopback name SSLProxyProfile StylePolicy>
	<XMLFirewallService name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<LocalAddress>0.0.0.0</LocalAddress>
		<UserSummary>Loopback XML Firewall Service</UserSummary>
		<Priority>normal</Priority>
		<LocalPort>2048</LocalPort>
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
		<SSLProxy class="SSLProxyProfile">${SSLProxyProfile}</SSLProxy>
		<StylePolicy class="StylePolicy">${StylePolicy}</StylePolicy>
		<MaxMessageSize>0</MaxMessageSize>
		<RequestType>soap</RequestType>
		<ResponseType>unprocessed</ResponseType>
		<RequestAttachments>strip</RequestAttachments>
		<ResponseAttachments>strip</ResponseAttachments>
		<RootPartNotFirstAction>process-in-order</RootPartNotFirstAction>
		<FrontAttachmentFormat>dynamic</FrontAttachmentFormat>
		<BackAttachmentFormat>dynamic</BackAttachmentFormat>
		<MIMEHeaders>on</MIMEHeaders>
		<RewriteErrors>on</RewriteErrors>
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
	</XMLFirewallService>
</#macro>