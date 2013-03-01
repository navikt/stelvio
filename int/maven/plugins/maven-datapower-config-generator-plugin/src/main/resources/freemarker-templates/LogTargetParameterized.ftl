<#macro LogTarget 
		name
		size 
		nfsStaticMount
		logFileName
		rotations
		logEvents>	
	<LogTarget name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>nfs</Type>
		<SoapVersion>soap11</SoapVersion>
		<Format>text</Format>
		<TimestampFormat>syslog</TimestampFormat>
		<Size>${size}</Size>
		<NFSMount class="NFSStaticMount">${nfsStaticMount}</NFSMount>
		<NFSFile>${logFileName}</NFSFile>
		<ArchiveMode>rotate</ArchiveMode>
		<UploadMethod>ftp</UploadMethod>
		<Rotate>${rotations}</Rotate>
		<UseANSIColor>off</UseANSIColor>
		<SyslogFacility>user</SyslogFacility>
		<SigningMode>off</SigningMode>
		<EncryptMode>off</EncryptMode>
		<RateLimit>100</RateLimit>
		<FeedbackDetection>off</FeedbackDetection>
		<IdenticalEventSuppression>off</IdenticalEventSuppression>
		<IdenticalEventPeriod>10</IdenticalEventPeriod>
		<LogEventFilter>0x80e002d9</LogEventFilter>
		<LogEventFilter>0x80e002da</LogEventFilter>
		<#list logEvents as event>
		<LogEvents>
			<Class class="LogLabel">${event.class}</Class>
			<Priority>${event.priority}</Priority>
		</LogEvents>
		</#list>
	</LogTarget>
</#macro>