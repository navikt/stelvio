<#import "datapower-config.ftl" as dp>

<#assign partnerSPK={"tpNr":"3010","sslCertDN":"${inboundFrontsideUserDN_SPK}","srvElsamDN":"${inboundBacksideUserDN_SPK}"}/>
<#assign partnerKLP={"tpNr":"3200","sslCertDN":"${inboundFrontsideUserDN_KLP}","srvElsamDN":"${inboundBacksideUserDN_KLP}"}/>

<#assign partnerTrustedCerts=[
{"name":"${partnerTrustCert_VeriSignPrimary}","file":"pubcert:///${partnerTrustCert_VeriSignPrimary}.pem"},
{"name":"${partnerTrustCert_VeriSignIntermediate}","file":"pubcert:///${partnerTrustCert_VeriSignIntermediate}.pem"},
{"name":"${partnerTrustCert_Thawte}","file":"pubcert:///${partnerTrustCert_Thawte}.pem"}
{"name":"soapui.developer.local","file":"cert:///soapui.developer.local.pem"}
]/>
<#assign inboundBacksideTrustedCerts=[
{"name":"${inboundBacksideTrustCertName}","file":"pubcert:///${inboundBacksideTrustCertName}.pem"}
]/>


<#-- Default processing rules -->
<#assign defaultSetLogDestAction="ELSAMDefault_setlogdestAction"/>
<#assign defaultSignMessageBodyAction="ELSAMDefault_signAction"/>
<#assign defaultVerifySignatureAction="ELSAMDefault_verifyAction"/>
<#assign defaultLogMessageAction="ELSAMDefault_logmsgAction"/>
<#assign defaultStripWSSHeaderAction="ELSAMDefault_stripwssAction"/>
<#assign defaultResultAction="ELSAMDefault_resultAction"/>
<#assign defaultErrorAction="ELSAMDefault_errorAction"/>


<#-- Inbound request variables -->
<#assign inboundRequestRule="ELSAMInbound_request-rule"/>
<#assign inboundAAAAction="ELSAMInbound_aaaAction"/>

<#-- Inbound response variables -->
<#assign inboundResponseRule="ELSAMInbound_response-rule"/>

<#-- Inbound error variables -->
<#assign inboundErrorRule="ELSAMInbound_error-rule"/>

<#-- Outbound request variables -->
<#assign outboundRequestRule="ELSAMOutbound_request-rule"/>
<#assign outboundAAAAction="ELSAMOutbound_aaaAction"/>

<#-- Outbound response variables -->
<#assign outboundResponseRule="ELSAMOutbound_response-rule"/>

<#assign signatureValCred="messageSignature_CryptoValCred"/>
<#assign matchingRuleAll="ELSAMDefault_match_all"/>
<#assign matchingRuleAllErrors="ELSAMDefault_match_allErrors"/>


<@dp.configuration domain="${cfgDomain}">
	<#-- Frontside SSL configuration for Inbound calls -->
	<@dp.TwoWaySSLClientAuth
			name="${inboundFrontsideHost}"
			keystoreName="${navSSLKeystoreName}"
			keystoreFile="${navSSLKeystoreFile}"
			keystorePwd="${navSSLKeystorePwd}"
			trustedCerts=partnerTrustedCerts/>
	<#-- Frontside SSL configuration for Outbound calls -->
	<@dp.FrontsideSSL
			name="${outboundFrontsideHost}"
			keystoreName="${outboundFrontsideSSLKeystoreName}"
			keystoreFile="${outboundFrontsideSSLKeystoreFile}"
			keystorePwd="${outboundFrontsideSSLKeystorePwd}"/>
	<#-- Backside SSL configuration for Inbound calls -->
	<@dp.BacksideSSL
			name="${inboundBacksideHost}"
			trustedCerts=[{"name":"${inboundBacksideTrustCertName}","file":"pubcert:///${inboundBacksideTrustCertName}.pem"}]/>
	<@dp.CryptoValCredPKIX
			name="${signatureValCred}"
			trustedCerts=partnerTrustedCerts/>
	<@dp.SigningCertificate
			name="${navSigningKeystoreName}"
			file="${navSigningKeystoreFile}"
			password="${navSigningKeystorePwd}"/>
	<#-- HTTPSFrontsideProtocolHandler for Inbound calls -->
	<@dp.FrontsideHandler
			name="${inboundFrontsideHandler}"
			protocol="${inboundFrontsideProtocol}"
			address="${inboundFrontsideHost}"
			port="${inboundFrontsidePort}"
			sslProxy="${inboundFrontsideHost}_SSLProxyProfile"/>
	<#-- HTTPSFrontsideProtocolHandler for Outbound calls -->
	<@dp.FrontsideHandler
			name="${outboundFrontsideHandler}"
			protocol="${inboundFrontsideProtocol}"
			address="${outboundFrontsideHost}"
			port="${outboundFrontsidePort}"
			sslProxy="${outboundFrontsideHost}_SSLProxyProfile"/>
	<#-- AAAPolicy for Inbound calls -->
	<@dp.AAAPolicyClientSSL2LTPA
			name="${inboundAaaPolicyName}"
			aaaFileName="local:///aaa/${inboundAaaFileName}"
			ppLtpaKeyFile="local:///aaa/${inboundBacksideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${inboundBacksideLTPAKeyPwd}"/>
	<@dp.NFSStaticMount
			name="${nfsLogTargetName}"
			uri="${nfsLogTargetUri}"/>
	<@dp.MatchingRuleURL
			name="${matchingRuleAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleErrorCode
			name="${matchingRuleAllErrors}"
			errorCodeMatch="*"/>
	<#-- Inbound request rule processiong actions -->
	<@dp.StylePolicyActionAAA
			name="${inboundAAAAction}"
			aaaPolicy="${inboundAaaPolicyName}"
			input="INPUT"
			output="NULL"/>
	<#--stylesheetName="local:///xslt/set-log-destination.xsl"-->
	<@dp.StylePolicyActionTransformParameterized
			name="${defaultSetLogDestAction}"
			stylesheetName="local:///xslt/nfs-message-logger.xsl"
			input="INPUT"
			output="NULL"
			params=[{"name":"log-store","value":"${nfsLogTargetName}"}]/>
	<@dp.StylePolicyActionVerify
			name="${defaultVerifySignatureAction}"
			input="INPUT"
			valCred="${signatureValCred}"/>
	<@dp.StylePolicyActionSign
			name="${defaultSignMessageBodyAction}"
			input="INPUT"
			signCert="${navSigningKeystoreName}"
			signKey="${navSigningKeystoreName}"/>
	<@dp.StylePolicyActionLog
			name="${defaultLogMessageAction}"
			destination="var://context/log/destination"
			input="INPUT"/>
	<@dp.StylePolicyActionTransform
			name="${defaultStripWSSHeaderAction}"
			stylesheetName="store:///strip-security-header.xsl"
			input="INPUT"
			output="result"/>
	<@dp.StylePolicyActionTransform
			name="${defaultErrorAction}"
			stylesheetName="local:///xslt/faultgenerisk.xsl"
			input="INPUT"
			output="result"/>
	<@dp.StylePolicyActionResult
			name="${defaultResultAction}"
			input="result"
			output="OUTPUT"/>

	<#-- Inbound processing rules-->
	<@dp.WSStylePolicyRuleRequest
			name="${inboundRequestRule}"
			actions=[
				"${inboundAAAAction}",
				"${defaultSetLogDestAction}",
				"${defaultVerifySignatureAction}",
				"${defaultLogMessageAction}",
				"${defaultStripWSSHeaderAction}",
				"${defaultResultAction}"
			]/>
<#--			"${inboundRequestAAAAction}",
				"${inboundRequestSetLogDestAction}",
				"${inboundRequestVerifySignatureAction}",
				"${inboundRequestLogMessageAction}",
				"${inboundRequestStripWSSHeaderAction}",
				"${inboundRequestResultAction}"]/>-->
	<@dp.WSStylePolicyRuleResponse
			name="${inboundResponseRule}"
			actions=[
				"${defaultSetLogDestAction}"
				"${defaultSignMessageBodyAction}"
				"${defaultLogMessageAction}"
				"${defaultResultAction}"
			]/>
	<@dp.WSStylePolicyRuleError
			name="${inboundErrorRule}"
			actions=[
				"${defaultErrorAction}",
				"${defaultResultAction}"
			]/>
	<@dp.WSStylePolicy
			name="${inboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundRequestRule}"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundResponseRule}"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundErrorRule}"}
			]/>
			
	<#-- Outbound processing rules-->
	<@dp.WSStylePolicyRuleRequest
			name="${outboundRequestRule}"
			actions=[
				<#--"${outboundAAAAction}",-->
				"${defaultSignMessageBodyAction}"
				"${defaultResultAction}"
			]/>
	<@dp.WSStylePolicyRuleResponse
			name="${outboundResponseRule}"
			actions=[
				"${defaultVerifySignatureAction}",
				"${defaultStripWSSHeaderAction}",
				"${defaultResultAction}"
			]/>
	<@dp.WSStylePolicy
			name="${outboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundRequestRule}"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundResponseRule}"}
			]/>
	
<#--	<@dp.WSProxyStaticBackend
			name="TPSamordningRegistreringInbound"
			version="${cfgVersion}"
			wsdlName="Port.wsdl"
			wsdlLocation="local:///wsdl/no/nav/tpsamordningregistrering/V0_2/ws/Port.wsdl"
			wsdlPortBinding="{http://nav.no/elsam/tpsamordningregistrering/V0_2/Binding}TPSamordningRegistreringWSEXP_TPSamordningRegistreringHttpPort"
			policy="${inboundProcessingPolicy}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"
			endpointUri="/nav-cons-elsam-tptilb-tpsamordningregistreringV0_2Web/sca/TPSamordningRegistreringWSEXP"/>-->
	<#list inboundWsdls as inboundWsdl>
	<@dp.WSProxyStaticBackend
			name="${inboundWsdl.proxyName}Inbound"
			version="${cfgVersion}"
			wsdlName="${inboundWsdl.fileName}"
			wsdlLocation="local:///${inboundWsdl.relativePath}"
			wsdlPortBinding="${inboundWsdl.portBinding}"
			policy="${inboundProcessingPolicy}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			frontsideUri="${inboundWsdl.frontsideURI}"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"
			backsideUri="${inboundWsdl.endpointURI}"/>
	</#list>
	<#list outboundWsdls as outboundWsdl>
	<@dp.WSProxyWSADynamicBackend
			name="${outboundWsdl.proxyName}Outbound"
			version="${cfgVersion}"
			wsdlName="${outboundWsdl.fileName}"
			wsdlLocation="local:///${outboundWsdl.relativePath}"
			wsdlPortBinding="${outboundWsdl.portBinding}"
			policy="${outboundProcessingPolicy}"
			frontsideHandler="${outboundFrontsideHandler}"
			frontsideProtocol="${outboundFrontsideProtocol}"
			backsideSSLProxy="${inboundFrontsideHost}_SSLProxyProfile"
			endpointUri="${outboundWsdl.endpointURI}"/>
	</#list>
</@dp.configuration>
