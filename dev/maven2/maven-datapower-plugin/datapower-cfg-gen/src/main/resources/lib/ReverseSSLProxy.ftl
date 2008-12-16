<#macro	ReverseSSLProxy name cryptoProfile>
	<SSLProxyProfile name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Direction>reverse</Direction>
		<ReverseCryptoProfile class="CryptoProfile">${cryptoProfile}</ReverseCryptoProfile>
		<ServerCaching>on</ServerCaching>
		<SessionTimeout>300</SessionTimeout>
		<CacheSize>20</CacheSize>
		<ClientCache>on</ClientCache>
		<ClientAuthOptional>on</ClientAuthOptional>
	</SSLProxyProfile>
</#macro>