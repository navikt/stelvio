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
<#assign inboundProcessingPolicyUnsigned="OppkoblingstestInboundUnsignedPolicy"/>


<@dp.configuration domain="${cfgDomain}">
	<@dp.AAAPolicyClientSSL2LTPAAllAuthenticated
			name="${inboundAaaPolicyName}AllAuth"
			aaaFileName="local:///aaa/${inboundAaaFileName}"
			ppLtpaKeyFile="local:///aaa/${inboundBacksideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${inboundBacksideLTPAKeyPwd}"/>
	<@dp.MatchingRuleURL
			name="${matchingRuleAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleErrorCode
			name="${matchingRuleAllErrors}"
			errorCodeMatch="*"/>

	<#-- Inbound processing rules for Signed Policy -->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicy}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyName}AllAuth"}, 
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
				{"type":"xform", "name":"createFaultAction", "async":"off",
						"input":"INPUT",	"output":"faultGenerisk",
						"stylesheet":"local:///xslt/faultgenerisk.xsl",
						"params":[]},
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"faultGenerisk",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultGenerisk","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="${inboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicy}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicy}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicy}_error-rule"}
			]/>
			
	<#-- Inbound processing rules for Unsigned Policy -->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicyUnsigned}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyName}AllAuth"}, 
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
		name="${inboundProcessingPolicyUnsigned}"
		actions=[
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"INPUT",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="${inboundProcessingPolicyUnsigned}"
			actions=[
				{"type":"xform", "name":"createFaultAction", "async":"off",
						"input":"INPUT",	"output":"faultGenerisk",
						"stylesheet":"local:///xslt/faultgenerisk.xsl",
						"params":[]},
				{"type":"xform", "name":"logAction", "async":"on",
						"input":"faultGenerisk",	"output":"NULL",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultGenerisk","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="${inboundProcessingPolicyUnsigned}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyUnsigned}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyUnsigned}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicyUnsigned}_error-rule"}
			]/>
			
			
	<#-- Outbound processing rules-->
<#--	<@dp.ProcessingRequestRule
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
						"input":"INPUT",	"output":"faultGenerisk", "async":"off",
						"stylesheet":"local:///xslt/faultgenerisk.xsl",
						"params":[]},
				{"type":"xform", "name":"logAction",
						"input":"faultGenerisk",	"output":"NULL", "async":"on",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"resultAction",
					"input":"faultGenerisk","output":"OUTPUT"}
			]/>
			
	<@dp.WSStylePolicy
			name="${outboundProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicy}_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"${outboundProcessingPolicy}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${outboundProcessingPolicy}_error-rule"}
			]/>-->

	<#-- Generate Inbound proxies -->			
	
	<#list inboundProxies as proxy>
	<@dp.WSProxyStaticBackend
			name="${proxy.name}InboundSigned"
			version="${cfgVersion}"
			wsdlName="${proxy.wsdls[0].fileName}"
			wsdlLocation="local:///${proxy.wsdls[0].relativePath}"
			wsdlPortBinding=proxy.wsdls[0].portBinding
			policy="${inboundProcessingPolicy}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			frontsideUri="${proxy.wsdls[0].frontsideURI}_signed"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"
			backsideUri="${proxy.wsdls[0].endpointURI}"
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"/>
	<@dp.WSProxyStaticBackend
			name="${proxy.name}InboundUnsigned"
			version="${cfgVersion}"
			wsdlName="${proxy.wsdls[0].fileName}"
			wsdlLocation="local:///${proxy.wsdls[0].relativePath}"
			wsdlPortBinding=proxy.wsdls[0].portBinding
			policy="${inboundProcessingPolicyUnsigned}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			frontsideUri="${proxy.wsdls[0].frontsideURI}_unsigned"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"
			backsideUri="${proxy.wsdls[0].endpointURI}"
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"/>
<#--	<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}InboundSigned"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="${inboundProcessingPolicy}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"/>
	-->
	</#list>

	<#-- Generate Outbound proxies -->			
<#--	<#list outboundProxies as proxy>
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
	-->
</@dp.configuration>
