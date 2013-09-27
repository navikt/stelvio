<#macro TwoWayCryptoProfileParameterized name identCred valCred ciphers disableSSLv2 disableSSLv3 disableTLSv1>
	<CryptoProfile name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<IdentCredential class="CryptoIdentCred">${identCred}</IdentCredential>
		<ValCredential class="CryptoValCred">${valCred}</ValCredential>
		<Ciphers>${ciphers}</Ciphers>
		<SSLOptions>
			<OpenSSL-default>on</OpenSSL-default>
			<Disable-SSLv2>${disableSSLv2}</Disable-SSLv2>
			<Disable-SSLv3>${disableSSLv3}</Disable-SSLv3>
			<Disable-TLSv1>${disableTLSv1}</Disable-TLSv1>
		</SSLOptions>
		<ClientCAList>off</ClientCAList>
	</CryptoProfile>
</#macro>