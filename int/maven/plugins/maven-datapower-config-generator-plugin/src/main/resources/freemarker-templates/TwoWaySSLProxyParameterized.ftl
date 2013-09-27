<#macro	TwoWaySSLProxyParameterized name forwardCryptoProfile reverseCryptoProfile permitInsecureServers>
	<SSLProxyProfile name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>two-way</Direction>
		<ForwardCryptoProfile class="CryptoProfile">${forwardCryptoProfile}</ForwardCryptoProfile>
		<ReverseCryptoProfile class="CryptoProfile">${reverseCryptoProfile}</ReverseCryptoProfile>
		<ServerCaching>on</ServerCaching>
		<SessionTimeout>300</SessionTimeout>
		<CacheSize>20</CacheSize>
		<ClientCache>on</ClientCache>
		<ClientAuthOptional>off</ClientAuthOptional>
		<PermitInsecureServers>${permitInsecureServers}</PermitInsecureServers>
	</SSLProxyProfile>
</#macro>