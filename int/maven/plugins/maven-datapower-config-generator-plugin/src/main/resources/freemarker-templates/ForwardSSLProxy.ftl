<#macro	ForwardSSLProxy name cryptoProfile>
	<SSLProxyProfile name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>forward</Direction>
		<ForwardCryptoProfile class="CryptoProfile">${cryptoProfile}</ForwardCryptoProfile>
		<ServerCaching>on</ServerCaching>
		<SessionTimeout>300</SessionTimeout>
		<CacheSize>20</CacheSize>
		<ClientCache>on</ClientCache>
		<ClientAuthOptional>off</ClientAuthOptional>
	</SSLProxyProfile>
</#macro>