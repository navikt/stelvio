<#macro ReverseCryptoProfile name identCred>
	<CryptoProfile name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<IdentCredential class="CryptoIdentCred">${identCred}</IdentCredential>
		<Ciphers>DEFAULT</Ciphers>
		<SSLOptions>
			<OpenSSL-default>on</OpenSSL-default>
			<Disable-SSLv2>on</Disable-SSLv2>
			<Disable-SSLv3>off</Disable-SSLv3>
			<Disable-TLSv1>off</Disable-TLSv1>
		</SSLOptions>
		<ClientCAList>off</ClientCAList>
	</CryptoProfile>
</#macro>