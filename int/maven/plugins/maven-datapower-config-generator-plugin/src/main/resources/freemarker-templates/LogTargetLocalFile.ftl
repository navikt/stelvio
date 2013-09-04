<#macro LogTargetLocalFile 
		name
		size 
		nfsStaticMount
		logFileName
		rotations
		logEvents>	
	<LogTarget name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>nfs</Type>
		<Priority>normal</Priority>
		<SoapVersion>soap11</SoapVersion>
		<Format>text</Format>
		<TimestampFormat>syslog</TimestampFormat>
		<Size>${size}</Size>
		<NFSMount class="NFSStaticMount">${nfsStaticMount}</NFSMount>
		<LocalFile>logtemp:///${name}.xml</LocalFile>
		<NFSFile>${logFileName}</NFSFile>
		<ArchiveMode>rotate</ArchiveMode>
		<UploadMethod>ftp</UploadMethod>
		<Rotate>${rotations}</Rotate>
		<UseANSIColor>off</UseANSIColor>
		<SyslogFacility>user</SyslogFacility>
		<SigningMode>off</SigningMode>
		<EncryptMode>off</EncryptMode>
		<RateLimit>100</RateLimit>
		<ConnectTimeout>60</ConnectTimeout>
		<IdleTimeout>15</IdleTimeout>
		<ActiveTimeout>0</ActiveTimeout>
		<FeedbackDetection>off</FeedbackDetection>
		<IdenticalEventSuppression>off</IdenticalEventSuppression>
		<IdenticalEventPeriod>10</IdenticalEventPeriod>
		<#list logEvents as event>
		<LogEvents>
			<Class class="LogLabel">${event.class}</Class>
			<Priority>${event.priority}</Priority>
		</LogEvents>
		</#list>
	</LogTarget>
</#macro>