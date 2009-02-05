<#import "datapower-config.ftl" as dp>


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
	<#-- AAAPolicy for Inbound calls ELSAM -->
	<@dp.AAAPolicyClientSSL2LTPA
			name="${inboundAaaPolicyName}"
			aaaFileName="local:///aaa/${inboundAaaFileName}"
			ppLtpaKeyFile="local:///aaa/${inboundBacksideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${inboundBacksideLTPAKeyPwd}"/>
	<#-- AAAPolicy for Inbound calls NorskPensjon -->
	<@dp.AAAPolicyClientSSL2LTPAAllAuthenticated
			name="${inboundAaaPolicyNameNP}"
			aaaFileName="local:///aaa/${inboundAaaPolicyFileNP}"
			ppLtpaKeyFile="local:///aaa/${inboundBacksideLTPAKeyFile}" 
			ppLtpaKeyFilePwd="${inboundBacksideLTPAKeyPwd}"/>
	<#-- AAAPolicy for Outbound calls -->
	<@dp.AAAPolicyAuthenticateAllLTPA
			name="${outboundAaaPolicyName}"
			auLtpaKeyFile="local:///aaa/${outboundFrontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${outboundFrontsideLTPAKeyPwd}"/>

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
			
	<#-- Inbound processing rules for Norsk Pensjon-->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicyNP}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyNameNP}"}, 
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
		name="${inboundProcessingPolicyNP}"
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
			name="${inboundProcessingPolicyNP}"
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
			name="${inboundProcessingPolicyNP}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyNP}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyNP}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicyNP}_error-rule"}
			]/>			
			
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicy}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"${outboundAaaPolicyName}"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
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
		

	<#-- Generate Inbound proxies -->			
	<#list inboundProxies as proxy>
	<#assign tempPolicy="${inboundProcessingPolicy}"/>
		<#switch proxy.name>
			<#case "NPSimulering">
				<#assign tempPolicy="${inboundProcessingPolicyNP}">
				<#break>
			<#case "NPTjenestepensjon">
				<#assign tempPolicy="${inboundProcessingPolicyNP}">
				<#break>				
			<#default>
				<#assign tempPolicy="${inboundProcessingPolicy}">
		</#switch>				
	<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy=tempPolicy
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
	
	<#-- SMS Gateway -->	
	<#-- TODO her -->
	
	<#assign nameSMS="SMSGateway"/>
	<#--<#assign outboundBacksideHostSMS="smsgw.carrot.no"/>
	<#assign outboundBacksideProtocolSMS="https"/>
	<#assign outboundBacksidePortSMS="443"/>
	-->
	<#assign outboundProcessingPolicySMS="${nameSMS}_Policy"/>

	<#assign wsdlNameSMS="carrot_sms_gateway.wsdl"/>
	<#assign aaaPolicyNameSMS="aaa0"/>
	
	<@dp.BacksideSSL
			name="${outboundBacksideHostSMS}"
			trustedCerts=[{"name":"Thawte-Server-CA","file":"pubcert:///Thawte-Server-CA.pem"}]/>
	
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"slm",	"name":"slmAction",	
						"input":"INPUT",	"output":"NULL"},
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"${outboundAaaPolicyName}"},
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
		wsdlPortBinding=["{http://ws.v4.sms.carrot.no}${nameSMS}"]
		policy="${outboundProcessingPolicySMS}"
		frontsideHandler="${outboundFrontsideHandler}"
		frontsideProtocol="${outboundFrontsideProtocol}"
		frontsideUri="${endpointURISMS}"
		backsideProtocol="${outboundBacksideProtocolSMS}"
		backsideHost="${outboundBacksideHostSMS}"
		backsidePort="${outboundBacksidePortSMS}"
		backsideUri="${endpointURISMS}"
		backsideSSLProxy="${outboundBacksideHostSMS}_SSLProxyProfile"
	/>
	
	<#-- Norsk Pensjon outbound-->

	<#assign nameNP="NorskPensjon"/>
	<#assign outboundProcessingPolicyNP="${nameNP}OutboundPolicy"/>
	<#assign wsdlNameNP="privatpensjon.wsdl"/>
	<#assign aaaPolicyNameNP="aaa0"/>	
	
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicyNP}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"${outboundAaaPolicyName}"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},				 
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
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
		name="${outboundProcessingPolicyNP}"
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
		name="${outboundProcessingPolicyNP}"
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
			name="${outboundProcessingPolicyNP}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicyNP}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicyNP}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${outboundProcessingPolicyNP}_error-rule"}
			]/>
	
	<@dp.WSProxyStaticBackend		
		name="${nameNP}Outbound"
		version="${cfgVersion}"
		wsdlName="${wsdlNameNP}"
		wsdlLocation="local:///wsdl/${wsdlNameNP}"
		wsdlPortBinding=["{http://norskpensjon.no/api/pensjon}privatpensjonPort"]
		policy="${outboundProcessingPolicyNP}"
		frontsideHandler="${outboundFrontsideHandler}"
		frontsideProtocol="${outboundFrontsideProtocol}"
		frontsideUri="${endpointURINP}"
		backsideProtocol="${outboundBacksideProtocolNP}"
		backsideHost="${outboundBacksideHostNP}"
		backsidePort="${outboundBacksidePortNP}"
		backsideUri="${endpointURINP}"
		backsideSSLProxy="${inboundFrontsideHost}_SSLProxyProfile"
	/>	
	
</@dp.configuration>
