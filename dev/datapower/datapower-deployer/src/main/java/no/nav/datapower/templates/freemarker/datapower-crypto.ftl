<#include "CryptoCertificate.ftl">
<#include "CryptoKey.ftl">
<#include "SSLCertificate.ftl">
<#include "TrustedCertificate.ftl">
<#include "CryptoIdentCred.ftl">
<#include "CryptoValCred.ftl">
<#include "ForwardCryptoProfile.ftl">
<#include "ForwardSSLProxy.ftl">
<#include "ReverseCryptoProfile.ftl">
<#include "ReverseSSLProxy.ftl">

<#macro FrontsideSSL name keystoreName keystoreFile keystorePwd>
	<#local sslIdCred="${name}_CryptoIdCred"/>
	<#local sslCryptoProfile="${name}_SSLCryptoProfile"/>
	<#local sslProxyProfile="${name}_SSLProxyProfile"/>
	<@SSLCertificate name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoKey name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoIdentCred name="${sslIdCred}" key="${keystoreName}" cert="${keystoreName}"/>
	<@ReverseCryptoProfile name="${sslCryptoProfile}" identCred="${sslIdCred}"/>
	<@ReverseSSLProxy name="${sslProxyProfile}" cryptoProfile="${sslCryptoProfile}"/>
</#macro>

<#macro BacksideSSL name trustedCerts>
	<#local sslValCred="${name}_CryptoValCred">
	<#local sslCryptoProfile="${name}_SSLCryptoProfile">
	<#local sslProxyProfile="${name}_SSLProxyProfile">
	<#list trustedCerts as cert>
	<@TrustedCertificate name="${cert.name}" file="${cert.file}"/>
	</#list>
	<@CryptoValCred name="${sslValCred}" trustedCerts=trustedCerts/>
	<@ForwardCryptoProfile name="${sslCryptoProfile}" valCred="${sslValCred}"/>
	<@ForwardSSLProxy name="${sslProxyProfile}" cryptoProfile="${sslCryptoProfile}"/>
</#macro>

