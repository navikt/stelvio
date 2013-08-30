<#macro MQQM name adminState hostName qmName ccsid channelName userName maximumMessageSize cacheTimeout unitsOfWork automaticBackout backoutThreshold backoutQueueName 
	totalConnectionLimit sharingConversations sslKey sslCipher sslProxyProfile retryAttempts>
	<MQQM name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>${adminState}</mAdminState>
		<HostName>${hostName}</HostName>
		<QMname>${qmName}</QMname>
		<CCSID>${ccsid}</CCSID>
		<ChannelName>${channelName}</ChannelName>
		<Heartbeat>300</Heartbeat>
		<UserName>${userName}</UserName>
		<MaximumMessageSize>${maximumMessageSize}</MaximumMessageSize>
		<CacheTimeout>${cacheTimeout}</CacheTimeout>
		<UnitsOfWork>${unitsOfWork}</UnitsOfWork>
		<AutomaticBackout>${automaticBackout}</AutomaticBackout>
		<BackoutThreshold>${backoutThreshold}</BackoutThreshold>
		<BackoutQueueName>${backoutQueueName}</BackoutQueueName>
		<TotalConnectionLimit>${totalConnectionLimit}</TotalConnectionLimit>
		<InitialConnections>1</InitialConnections>
		<SharingConversations>${sharingConversations}</SharingConversations>
		<SSLkey>${sslKey}</SSLkey>
		<PermitInsecureServers>off</PermitInsecureServers>
		<SSLcipher>${sslCipher}</SSLcipher>
		<SSLProxyProfile class="SSLProxyProfile">${sslProxyProfile}</SSLProxyProfile>
		<AutoRecovery>off</AutoRecovery>
		<ConvertInput>on</ConvertInput>
		<AutoRetry>on</AutoRetry>
		<RetryInterval>1</RetryInterval>
		<RetryAttempts>${retryAttempts}</RetryAttempts>
		<LongRetryInterval>1800</LongRetryInterval>
		<ReportingInterval>1</ReportingInterval>
		<AlternateUser>on</AlternateUser>
		<XMLManager class="XMLManager">default</XMLManager>
	</MQQM>
</#macro>