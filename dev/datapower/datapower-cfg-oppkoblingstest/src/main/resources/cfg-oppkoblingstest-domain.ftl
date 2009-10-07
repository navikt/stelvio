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
<#assign logFaultActionParams=[
	{"name":"log-store",	"value":"${nfsLogTargetName}"},
	{"name":"domain",		"value":"on"},
	{"name":"proxy",		"value":"on"},
	{"name":"operation",	"value":"on"},
	{"name":"user",			"value":"on"},
	{"name":"transaction",	"value":"on"},
	{"name":"rule",			"value":"off"},
	{"name":"custom",		"value":"fault"}
]/>

<#assign signatureValCred="messageSignature_CryptoValCred"/>
<#assign matchingRuleAll="ELSAMDefault_match_all"/>
<#assign matchingRuleAllErrors="ELSAMDefault_match_allErrors"/>
<#assign matchingRuleAllSoapFaults="ELSAMDefault_match_allSoapFaults"/>
<#assign logDestination="var://context/log/destination"/>
<#assign inboundProcessingPolicyUnsigned="OppkoblingstestInboundUnsignedPolicy"/>
<#assign inboundProcessingPolicySigned="OppkoblingstestInboundSignedPolicy">


<@dp.configuration domain="${cfgDomain}">
	<@dp.AAAPolicyClientSSL2LTPAAllAuthenticated
			name="${inboundAaaPolicyName}AllAuth"
			aaaFileName="local:///aaa/${inboundAaaFileName}"
			ppLtpaKeyFile="local:///aaa/${inboundBacksideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${inboundBacksideLTPAKeyPwd}"/>
	<@dp.MatchingRuleURL
			name="${matchingRuleAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleXPath
			name="${matchingRuleAllSoapFaults}"
			xpathExpression="/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Envelope']/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Body']/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Fault']"/>
	<@dp.MatchingRuleErrorCode
			name="${matchingRuleAllErrors}"
			errorCodeMatch="*"/>

	<#-- Inbound processing rules for Signed Policy -->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicySigned}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyName}AllAuth"}, 
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"addStelvioCtxAction", "async":"off",
						"input":"aaaOutput",	"output":"stelvioContextIncluded",
						"stylesheet":"local:///xslt/add-stelvio-context.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"stelvioContextIncluded","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingFaultRule
		name="${inboundProcessingPolicySigned}"
		actions=[
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="${inboundProcessingPolicySigned}"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"${navSigningKeystoreName}",
						"signKey":"${navSigningKeystoreName}"}
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedResponse",	"output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"signedResponse","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="${inboundProcessingPolicySigned}"
			actions=[
				{"type":"xform", "name":"prepareErrorLogAction",
						"input":"INPUT", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendErrorLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"createFaultAction", "async":"off",
						"input":"INPUT",	"output":"faultGenerisk",
						"stylesheet":"local:///xslt/faultgenerisk.xsl",
						"params":[]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultGenerisk", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"faultGenerisk","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="${inboundProcessingPolicySigned}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicySigned}_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"${inboundProcessingPolicySigned}_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicySigned}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicySigned}_error-rule"}
			]/>
			
	<#-- Inbound processing rules for Unsigned Policy -->
	<@dp.ProcessingRequestRule
		name="${inboundProcessingPolicyUnsigned}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"${inboundAaaPolicyName}AllAuth"}, 
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"addStelvioCtxAction", "async":"off",
						"input":"aaaOutput",	"output":"stelvioContextIncluded",
						"stylesheet":"local:///xslt/add-stelvio-context.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"stelvioContextIncluded","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingFaultRule
		name="${inboundProcessingPolicyUnsigned}"
		actions=[
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="${inboundProcessingPolicyUnsigned}"
		actions=[
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="${inboundProcessingPolicyUnsigned}"
			actions=[
				{"type":"xform", "name":"prepareErrorLogAction",
						"input":"INPUT", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendErrorLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"createFaultAction", "async":"off",
						"input":"INPUT",	"output":"faultGenerisk",
						"stylesheet":"local:///xslt/faultgenerisk.xsl",
						"params":[]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultGenerisk", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"faultGenerisk","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="${inboundProcessingPolicyUnsigned}"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyUnsigned}_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"${inboundProcessingPolicyUnsigned}_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"${inboundProcessingPolicyUnsigned}_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"${inboundProcessingPolicyUnsigned}_error-rule"}
			]/>
			
			
	<#-- Outbound processing rules - this is generated by partner-gw config -->
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
			policy="${inboundProcessingPolicySigned}"
			frontsideHandler="${inboundFrontsideHandler}"
			frontsideProtocol="${inboundFrontsideProtocol}"
			frontsideUri="${proxy.wsdls[0].frontsideURI}_signed"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="${inboundBacksideHost}"
			backsidePort="${inboundBacksidePort}"
			backsideUri="${proxy.wsdls[0].endpointURI}"
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"/>
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
			backsideSSLProxy="${inboundBacksideHost}_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"/>
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
			wsaRequireAaa="off"
			backsideTimeout="${outboundBacksideConnectionTimeout}"/>
	</#list>
</@dp.configuration>
