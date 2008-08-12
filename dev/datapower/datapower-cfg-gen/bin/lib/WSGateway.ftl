<#macro WSGateway
		name
		version
		sslProxy
		wsdlName
		wsdlLocation
		rewritePolicy
		stylePolicy
		type
		wsaMode>
	<WSGateway name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<UserSummary>${version}</UserSummary>
		<Priority>normal</Priority>
		<XMLManager class="XMLManager">default</XMLManager>
		<SSLProxy class="SSLProxyProfile">${sslProxy}</SSLProxy>
		<DefaultParamNamespace>http://www.datapower.com/param/config</DefaultParamNamespace>
		<QueryParamNamespace>http://www.datapower.com/param/query</QueryParamNamespace>
		<PropagateURI>off</PropagateURI>
		<MonitorProcessingPolicy>terminate-at-first-throttle</MonitorProcessingPolicy>
		<RequestAttachments>strip</RequestAttachments>
		<ResponseAttachments>strip</ResponseAttachments>
		<RootPartNotFirstAction>process-in-order</RootPartNotFirstAction>
		<FrontAttachmentFormat>dynamic</FrontAttachmentFormat>
		<BackAttachmentFormat>dynamic</BackAttachmentFormat>
		<MIMEFrontHeaders>on</MIMEFrontHeaders>
		<MIMEBackHeaders>on</MIMEBackHeaders>
		<StreamOutputToBack>buffer-until-verification</StreamOutputToBack>
		<StreamOutputToFront>buffer-until-verification</StreamOutputToFront>
		<MaxMessageSize>0</MaxMessageSize>
		<GatewayParserLimits>off</GatewayParserLimits>
		<ParserLimitsElementDepth>512</ParserLimitsElementDepth>
		<ParserLimitsAttributeCount>128</ParserLimitsAttributeCount>
		<ParserLimitsMaxNodeSize>33554432</ParserLimitsMaxNodeSize>
		<ParserLimitsForbidExternalReferences>on</ParserLimitsForbidExternalReferences>
		<ParserLimitsExternalReferences>forbid</ParserLimitsExternalReferences>
		<ParserLimitsAttachmentByteCount>2000000000</ParserLimitsAttachmentByteCount>
		<DebugMode persisted="false">off</DebugMode>
		<DebuggerType>internal</DebuggerType>
		<DebugHistory>25</DebugHistory>
		<RequestType>soap</RequestType>
		<ResponseType>soap</ResponseType>
		<SOAPSchemaURL>store:///schemas/soap-envelope.xsd</SOAPSchemaURL>
		<FrontTimeout>120</FrontTimeout>
		<BackTimeout>120</BackTimeout>
		<FrontPersistentTimeout>180</FrontPersistentTimeout>
		<BackPersistentTimeout>180</BackPersistentTimeout>
		<IncludeResponseTypeEncoding>off</IncludeResponseTypeEncoding>
		<BackHTTPVersion>HTTP/1.1</BackHTTPVersion>
		<AllowCompression>off</AllowCompression>
		<PersistentConnections>on</PersistentConnections>
		<LoopDetection>off</LoopDetection>
		<FollowRedirects>on</FollowRedirects>
		<DoHostRewriting>on</DoHostRewriting>
		<DoChunkedUpload>off</DoChunkedUpload>
		<ProcessHTTPErrors>on</ProcessHTTPErrors>
		<HTTPClientIPLabel>X-Client-IP</HTTPClientIPLabel>
		<WSAMode>${wsaMode}</WSAMode>
		<WSARequireAAA>on</WSARequireAAA>
		<WSAStrip>on</WSAStrip>
		<WSADefaultReplyTo>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</WSADefaultReplyTo>
		<WSADefaultFaultTo>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</WSADefaultFaultTo>
		<WSAForce>off</WSAForce>
		<WSAGenStyle>sync</WSAGenStyle>
		<WSAHTTPAsyncResponseCode>204</WSAHTTPAsyncResponseCode>
		<WSATimeout>120</WSATimeout>
		<WSRMEnabled>off</WSRMEnabled>
		<WSRMSequenceExpiration>3600</WSRMSequenceExpiration>
		<WSRMDestinationAcceptCreateSequence>on</WSRMDestinationAcceptCreateSequence>
		<WSRMDestinationMaximumSequences>400</WSRMDestinationMaximumSequences>
		<WSRMDestinationInOrder>off</WSRMDestinationInOrder>
		<WSRMDestinationMaximumInOrderQueueLength>10</WSRMDestinationMaximumInOrderQueueLength>
		<WSRMDestinationAcceptOffers>off</WSRMDestinationAcceptOffers>
		<WSRMFrontForce>off</WSRMFrontForce>
		<WSRMBackForce>off</WSRMBackForce>
		<WSRMBackCreateSequence>off</WSRMBackCreateSequence>
		<WSRMFrontCreateSequence>off</WSRMFrontCreateSequence>
		<WSRMSourceMakeOffer>off</WSRMSourceMakeOffer>
		<WSRMUsesSequenceSSL>off</WSRMUsesSequenceSSL>
		<WSRMSourceMaximumSequences>400</WSRMSourceMaximumSequences>
		<WSRMSourceRetransmissionInterval>10</WSRMSourceRetransmissionInterval>
		<WSRMSourceExponentialBackoff>on</WSRMSourceExponentialBackoff>
		<WSRMSourceMaximumRetransmissions>4</WSRMSourceMaximumRetransmissions>
		<WSRMSourceMaximumQueueLength>30</WSRMSourceMaximumQueueLength>
		<WSRMSourceRequestAckCount>1</WSRMSourceRequestAckCount>
		<WSRMSourceInactivityClose>360</WSRMSourceInactivityClose>
		<Type>${type}</Type>
		<AutoCreateSources>off</AutoCreateSources>
		<EndpointRewritePolicy class="WSEndpointRewritePolicy">${rewritePolicy}</EndpointRewritePolicy>
		<StylePolicy class="WSStylePolicy">${stylePolicy}</StylePolicy>
		<RemoteFetchRetry>
			<AutomaticRetry>off</AutomaticRetry>
			<RetryInterval>1</RetryInterval>
			<ReportingInterval>1</ReportingInterval>
			<TotalRetries>1</TotalRetries>
		</RemoteFetchRetry>
		<BaseWSDL>
			<WSDLSourceLocation>${wsdlLocation}</WSDLSourceLocation>
			<WSDLName>${wsdlName}</WSDLName>
		</BaseWSDL>
		<UserToggles>
			<WSDLName>${wsdlName}</WSDLName>
			<ServiceName>*</ServiceName>
			<ServicePortName>*</ServicePortName>
			<PortTypeName>*</PortTypeName>
			<BindingName>*</BindingName>
			<OperationName>*</OperationName>
			<Toggles>
				<Enable>on</Enable>
				<Publish>on</Publish>
				<VerifyFaults>off</VerifyFaults>
				<VerifyHeaders>off</VerifyHeaders>
				<NoRequestValidation>off</NoRequestValidation>
				<NoResponseValidation>off</NoResponseValidation>
				<SuppressFaultsElementsForRPCWrappers>off</SuppressFaultsElementsForRPCWrappers>
				<NoWSA>on</NoWSA>
				<NoWSRM>on</NoWSRM>
			</Toggles>
			<Subscription/>
		</UserToggles>
		<SOAPActionPolicy>lax</SOAPActionPolicy>
	</WSGateway>
</#macro>