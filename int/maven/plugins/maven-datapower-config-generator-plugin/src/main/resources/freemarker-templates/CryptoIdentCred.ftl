<#macro CryptoIdentCred name key cert>
	<CryptoIdentCred name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Key class="CryptoKey">${key}</Key>
		<Certificate class="CryptoCertificate">${cert}</Certificate>
	</CryptoIdentCred>
</#macro>