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
	<#local cryptoProfile="${name}_SSLCryptoProfile"/>
	<@SSLCertificate name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoKey name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoIdentCred name="${sslIdCred}" key="${keystoreName}" cert="${keystoreName}"/>
	<@ReverseCryptoProfile name="${cryptoProfile}" identCred="${sslIdCred}"/>
	<@ReverseSSLProxy name="${name}_SSLProxyProfile" cryptoProfile="${cryptoProfile}"/>
</#macro>

<#macro BacksideSSL name trustedCerts>
	<#local sslValCred="${name}_CryptoValCred">
	<#local cryptoProfile="${name}_SSLCryptoProfile">
	<#list trustedCerts as cert>
	<@TrustedCertificate name="${cert.name}" file="${cert.file}"/>
	</#list>
	<@CryptoValCred name="${sslValCred}" trustedCerts=trustedCerts/>
	<@ForwardCryptoProfile name="${cryptoProfile}" valCred="${sslValCred}"/>
	<@ForwardSSLProxy name="${name}_SSLProxyProfile" cryptoProfile="${cryptoProfile}"/>
</#macro>

