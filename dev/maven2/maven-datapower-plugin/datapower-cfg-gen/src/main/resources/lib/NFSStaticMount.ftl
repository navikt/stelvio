<#macro NFSStaticMount name uri>
	<NFSStaticMount name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<remote>${uri}</remote>
		<LocalFilesystemAccess>on</LocalFilesystemAccess>
		<Version>3</Version>
		<Transport>tcp</Transport>
		<Authentication>auth_sys</Authentication>
		<ReadOnly>off</ReadOnly>
		<ReadSize>4096</ReadSize>
		<WriteSize>4096</WriteSize>
		<Timeout>7</Timeout>
		<Retransmissions>3</Retransmissions>
	</NFSStaticMount>
</#macro>