<#macro CryptoValCredPKIX name trustedCerts>
	<CryptoValCred name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<#list trustedCerts as cert>
		<Certificate class="CryptoCertificate">${cert.name}</Certificate>
		</#list>
		<CertValidationMode>pkix</CertValidationMode>
		<UseCRL>off</UseCRL>
		<RequireCRL>off</RequireCRL>
		<CRLDPHandling>ignore</CRLDPHandling>
		<InitialPolicySet>2.5.29.32.0</InitialPolicySet>
		<ExplicitPolicy>off</ExplicitPolicy>
	</CryptoValCred>
</#macro>