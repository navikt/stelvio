<#macro MQFrontsideHandler name queueManager getQueue retrieveBackoutSettings>
	<MQSourceProtocolHandler name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<QueueManager class="MQQM">${queueManager}</QueueManager>
		<GetQueue>${getQueue}</GetQueue>
		<CodePage>0</CodePage>
		<GetMessageOptions>4097</GetMessageOptions>
		<ParseProperties>off</ParseProperties>
		<AsyncPut>off</AsyncPut>
		<ExcludeHeaders>
			<MQCIH>on</MQCIH>
			<MQDLH>on</MQDLH>
			<MQIIH>on</MQIIH>
			<MQRFH>on</MQRFH>
			<MQRFH2>on</MQRFH2>
			<MQWIH>on</MQWIH>
		</ExcludeHeaders>
		<ConcurrentConnections>1</ConcurrentConnections>
		<PollingInterval>30</PollingInterval>
		<BatchSize>0</BatchSize>
		<ContentTypeHeader>None</ContentTypeHeader>
		<RetrieveBackoutSettings>${retrieveBackoutSettings}</RetrieveBackoutSettings>
		<UseQMNameInURL>off</UseQMNameInURL>
	</MQSourceProtocolHandler>
</#macro>