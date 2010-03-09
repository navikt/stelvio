<#macro TrustedCertificate name file>
	<CryptoCertificate name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Filename>${file}</Filename>
		<PasswordAlias>off</PasswordAlias>
		<IgnoreExpiration>off</IgnoreExpiration>
	</CryptoCertificate>
</#macro>