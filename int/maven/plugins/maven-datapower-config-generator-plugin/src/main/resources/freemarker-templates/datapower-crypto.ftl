<#include "CryptoCertificate.ftl">
<#include "CryptoKey.ftl">
<#include "SSLCertificate.ftl">
<#include "SigningCertificate.ftl">
<#include "TrustedCertificate.ftl">
<#include "CryptoIdentCred.ftl">
<#include "CryptoValCred.ftl">
<#include "CryptoValCredParameterized.ftl">
<#include "CryptoValCredPKIX.ftl">
<#include "ForwardCryptoProfile.ftl">
<#include "ForwardSSLProxy.ftl">
<#include "ReverseCryptoProfile.ftl">
<#include "ReverseSSLProxy.ftl">
<#include "TwoWaySSLProxy.ftl">
<#include "TwoWaySSLProxyParameterized.ftl">
<#include "TwoWayCryptoProfile.ftl">
<#include "TwoWayCryptoProfileParameterized.ftl">

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

<#macro TwoWaySSLClientAuth
		name
		keystoreName
		keystoreFile
		keystorePwd
		trustedCerts>
	<#local sslIdCred="${name}_CryptoIdCred"/>
	<#local sslValCred="${name}_CryptoValCred">
	<#local sslCryptoProfile="${name}_SSLCryptoProfile"/>
	<#local sslProxyProfile="${name}_SSLProxyProfile"/>
	<@SSLCertificate name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoKey name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoIdentCred name="${sslIdCred}" key="${keystoreName}" cert="${keystoreName}"/>
	<#list trustedCerts as cert>
	<@TrustedCertificate name="${cert.name}" file="${cert.file}"/>
	</#list>
	<@CryptoValCred name="${sslValCred}" trustedCerts=trustedCerts/>
	<@TwoWayCryptoProfile name="${sslCryptoProfile}" identCred="${sslIdCred}" valCred="${sslValCred}"/>
	<@TwoWaySSLProxy name="${sslProxyProfile}" forwardCryptoProfile="${sslCryptoProfile}" reverseCryptoProfile="${sslCryptoProfile}"/>
</#macro>

<#macro TwoWaySSLClientAuthParameterized
		name
		keystoreName
		keystoreFile
		keystorePwd
		trustedCerts
		useCRL
		ciphers
		disableSSLv2
		disableSSLv3
		disableTLSv1
		permitInsecureServers>
	<#local sslIdCred="${name}_CryptoIdCred"/>
	<#local sslValCred="${name}_CryptoValCred">
	<#local sslCryptoProfile="${name}_SSLCryptoProfile"/>
	<#local sslProxyProfile="${name}_SSLProxyProfile"/>
	<@SSLCertificate name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoKey name="${keystoreName}" file="${keystoreFile}" password="${keystorePwd}"/>
	<@CryptoIdentCred name="${sslIdCred}" key="${keystoreName}" cert="${keystoreName}"/>
	<#list trustedCerts as cert>
	<@TrustedCertificate name="${cert.name}" file="${cert.file}"/>
	</#list>
	<@CryptoValCredParameterized name="${sslValCred}" trustedCerts=trustedCerts useCRL=useCRL/>
	<@TwoWayCryptoProfileParameterized name="${sslCryptoProfile}" identCred="${sslIdCred}" valCred="${sslValCred}" ciphers=ciphers 
		disableSSLv2=disableSSLv2 disableSSLv3=disableSSLv3 disableTLSv1=disableTLSv1/>
	<@TwoWaySSLProxyParameterized name="${sslProxyProfile}" forwardCryptoProfile="${sslCryptoProfile}" reverseCryptoProfile="${sslCryptoProfile}" permitInsecureServers=permitInsecureServers/>
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

