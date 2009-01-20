<#import "datapower-config.ftl" as dp>

<#--
<#assign partnerSPK={"tpNr":"3010","sslCertDN":"${inboundFrontsideUserDN_SPK}","srvElsamDN":"${inboundBacksideUserDN_SPK}"}/>
<#assign partnerKLP={"tpNr":"3200","sslCertDN":"${inboundFrontsideUserDN_KLP}","srvElsamDN":"${inboundBacksideUserDN_KLP}"}/>
-->

<#--<#assign partnerTrustedCerts=[
{"name":"${partnerTrustCert_VeriSignPrimary}","file":"pubcert:///${partnerTrustCert_VeriSignPrimary}.pem"},
{"name":"${partnerTrustCert_VeriSignIntermediate}","file":"pubcert:///${partnerTrustCert_VeriSignIntermediate}.pem"},
{"name":"${partnerTrustCert_Thawte}","file":"pubcert:///${partnerTrustCert_Thawte}.pem"},
{"name":"soapui.developer.local","file":"cert:///soapui.developer.local.pem"}
]/>-->


<#assign inboundBacksideTrustedCerts=[
{"name":"${inboundBacksideTrustCertName}","file":"pubcert:///${inboundBacksideTrustCertName}.pem"}
]/>


<#assign logActionParams=[
	{"name":"log-store",	"value":"${nfsLogTargetName}"},	
	{"name":"domain",		"value":"on"},	
	{"name":"proxy",		"value":"on"},	
	{"name":"operation",	"value":"on"},	
	{"name":"user",			"value":"on"},	
	{"name":"transaction",	"value":"on"},
	{"name":"rule",			"value":"on"}	
]/>

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

	<#-- Inbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicy}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyName}"}, 
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"INPUT",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"xform", "name":"addStelvioCtxAction", "async":"off",
						"input":"aaaOutput",	"output":"stelvioContextIncluded",
						"stylesheet":"local:///xslt/add-stelvio-context.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"stelvioContextIncluded","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="${inboundProcessingPolicy}"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"${navSigningKeystoreName}",
						"signKey":"${navSigningKeystoreName}"}
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"signedResponse",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"signedResponse","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="${inboundProcessingPolicy}"
			actions=[
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT",	"output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"logAction",
						"input":"faultHandler",	"output":"NULL", "async":"on",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="${inboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicy}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicy}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicy}_error-rule"}
			]/>
			
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicy}"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedRequest",
						"signCert":"${navSigningKeystoreName}",
						"signKey":"${navSigningKeystoreName}"}
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"signedRequest",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"signedRequest","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="${outboundProcessingPolicy}"
		actions=[
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"logAction",
						"input":"INPUT",	"output":"NULL", "async":"on",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"xform", "name":"stripWssAction", "async":"off",
						"input":"INPUT",	"output":"wssHeaderRemoved",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"wssHeaderRemoved","output":"OUTPUT"}
			]/>
	<@dp.ProcessingErrorRule
		name="${outboundProcessingPolicy}"
		actions=[
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT",	"output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"logAction",
						"input":"faultHandler",	"output":"NULL", "async":"on",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}
			]/>
			
	<@dp.WSStylePolicy
			name="${outboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicy}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicy}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${outboundProcessingPolicy}_error-rule"}
			]/>
		
	<#-- SMS Gateway -->	
	<#-- TODO her -->
	
	<#assign nameSMS="SMSGateway"/>
	<#assign outboundBacksideHostSMS="smsgw.carrot.no"/>
	<#assign outboundBacksideProtocolSMS="https"/>
	<#assign outboundBacksidePortSMS="443"/>
	
	<#assign outboundProcessingPolicySMS="${nameSMS}_Policy"/>

	<#assign wsdlNameSMS="carrot_sms_gateway.wsdl"/>
	<#assign aaaPolicyNameSMS="aaa0"/>
	
	<@dp.BacksideSSL
			name="${outboundBacksideHostSMS}"
			trustedCerts=[{"name":"Thawte-Server-CA","file":"pubcert:///Thawte-Server-CA.pem"}]/>
	<@dp.AAAPolicyAuthenticateAllLTPA
			name="aaaPolicyAuthenticateAll"
			auLtpaKeyFile="local:///aaa/${outboundFrontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${outboundFrontsideLTPAKeyPwd}"/>
	
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"slm",	"name":"slmAction",	
						"input":"INPUT",	"output":"NULL"},
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"aaaPolicyAuthenticateAll"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},				 
				{"type":"result", "name":"resultAction",
					"input":"PIPE","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>
	<@dp.ProcessingErrorRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT",	"output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"logAction",
						"input":"faultHandler",	"output":"NULL", "async":"on",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}					
			]/>
			
	<@dp.WSStylePolicy
			name="${outboundProcessingPolicySMS}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicySMS}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicySMS}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${outboundProcessingPolicySMS}_error-rule"}
			]/>
	

	<@dp.WSProxyStaticBackend		
		name="${nameSMS}"
		version="${cfgVersion}"
		wsdlName="${wsdlNameSMS}"
		wsdlLocation="local:///wsdl/${wsdlNameSMS}"
		wsdlPortBinding="{http://ws.v4.sms.carrot.no}${nameSMS}"
		policy="${outboundProcessingPolicySMS}"
		frontsideHandler="${outboundFrontsideHandler}"
		frontsideProtocol="${outboundFrontsideProtocol}"
		frontsideUri="/smsgw/services/SMSGateway"
		backsideProtocol="${outboundBacksideProtocolSMS}"
		backsideHost="${outboundBacksideHostSMS}"
		backsidePort="${outboundBacksidePortSMS}"
		backsideUri="/smsgw/services/SMSGateway"
	/>

	<#-- Generate Inbound proxies -->			
	<#list inboundProxies as proxy>
	<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="${inboundProcessingPolicy}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"/>
	</#list>	

	<#-- Generate Outbound proxies -->			
	<#list outboundProxies as proxy>
	<@dp.WSProxyWSADynamicBackendMultipleWsdl
			name="${proxy.name}Outbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="${outboundProcessingPolicy}"
			frontsideHandler="${outboundFrontsideHandler}"
			frontsideProtocol="${outboundFrontsideProtocol}"
			backsideSSLProxy="${inboundFrontsideHost}_SSLProxyProfile"
			wsaRequireAaa="off"/>
	</#list>
</@dp.configuration>
