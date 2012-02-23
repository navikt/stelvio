<#import "datapower-config.ftl" as dp>

<#assign inboundBacksideTrustedCerts=[
{"name":"@inboundBacksideTrustCertName@","file":"pubcert:///@inboundBacksideTrustCertName@.cer"}
]/>


<#assign logActionParams=[
	{"name":"log-store",	"value":"@nfsLogTargetName@"},
	{"name":"domain",		"value":"on"},
	{"name":"proxy",		"value":"on"},
	{"name":"operation",	"value":"on"},
	{"name":"user",			"value":"on"},
	{"name":"transaction",	"value":"on"},
	{"name":"receiver",		"value":"on"},
	{"name":"rule",			"value":"on"}
]/>
<#assign logFaultActionParams=[
	{"name":"log-store",	"value":"@nfsLogTargetName@"},
	{"name":"domain",		"value":"on"},
	{"name":"proxy",		"value":"on"},
	{"name":"operation",	"value":"on"},
	{"name":"user",			"value":"on"},
	{"name":"transaction",	"value":"on"},
	{"name":"rule",			"value":"off"},
	{"name":"receiver",		"value":"on"},
	{"name":"custom",		"value":"fault"}
]/>

<#assign signatureValCred="messageSignature_CryptoValCred"/>
<#assign matchingRuleAll="ELSAMDefault_match_all"/>
<#assign matchingRuleAllErrors="ELSAMDefault_match_allErrors"/>
<#assign matchingRuleAllSoapFaults="ELSAMDefault_match_allSoapFaults"/>
<#assign logDestination="var://context/log/destination"/>
<#assign serviceClassificationDestination="local:///aaa/service-classification.xml"/>

<#assign nameNP="NorskPensjon"/>
<#assign outboundProcessingPolicyNP="${nameNP}OutboundPolicy"/>

<@dp.configuration domain="@cfgDomain@">
	<#-- Frontside SSL configuration for Inbound calls -->
	<@dp.TwoWaySSLClientAuth
			name="@inboundFrontsideHost@"
			keystoreName="@navSSLKeystoreName@"
			keystoreFile="@navSSLKeystoreFile@"
			keystorePwd="@navSSLKeystorePwd@"
			trustedCerts=partnerTrustedCerts/>
			
	<#-- Frontside SSL configuration for Outbound calls -->
	<@dp.FrontsideSSL
			name="@outboundFrontsideHost@"
			keystoreName="@outboundFrontsideSSLKeystoreName@"
			keystoreFile="@outboundFrontsideSSLKeystoreFile@"
			keystorePwd="@outboundFrontsideSSLKeystorePwd@"/>
			
	<#-- Backside SSL configuration for Inbound calls -->
	<@dp.BacksideSSL
			name="@inboundBacksideHost@"
			trustedCerts=[{"name":"@inboundBacksideTrustCertName@","file":"pubcert:///@inboundBacksideTrustCertName@.cer"}]/>
	<@dp.CryptoValCredPKIX
			name="${signatureValCred}"
			trustedCerts=partnerTrustedCerts/>
	<@dp.SigningCertificate
			name="@navSigningKeystoreName@"
			file="@navSigningKeystoreFile@"
			password="@navSigningKeystorePwd@"/>
			
	<#-- HTTPSFrontsideProtocolHandler for Inbound calls -->
	<@dp.FrontsideHandler
			name="@inboundFrontsideHandler@"
			protocol="${inboundFrontsideProtocol}"
			address="@inboundFrontsideHost@"
			port="@inboundFrontsidePort@"
			sslProxy="@inboundFrontsideHost@_SSLProxyProfile"/>
			
	<#-- HTTPSFrontsideProtocolHandler for Outbound calls -->
	<@dp.FrontsideHandler
			name="@outboundFrontsideHandler@"
			protocol="${inboundFrontsideProtocol}"
			address="@outboundFrontsideHost@"
			port="@outboundFrontsidePort@"
			sslProxy="@outboundFrontsideHost@_SSLProxyProfile"/>
			
	<#-- AAAPolicy for Inbound calls ELSAM with TPnr authorization-->
	<@dp.AAAPolicyClientSSL2LTPA
			name="@inboundAaaPolicyNameTP@"
			aaaFileName="local:///aaa/@inboundAaaFileNameTP@"
			ppLtpaKeyFile="local:///aaa/@inboundBacksideLTPAKeyFile@"
			ppLtpaKeyFilePwd="@inboundBacksideLTPAKeyPwd@"/>
			
	<#-- AAAPolicy for Inbound calls NorskPensjon -->
	<@dp.AAAPolicyClientSSL2LTPAAllAuthenticated
			name="@inboundAaaPolicyNameNP@"
			aaaFileName="local:///aaa/@inboundAaaPolicyFileNP@"
			ppLtpaKeyFile="local:///aaa/@inboundBacksideLTPAKeyFile@" 
			ppLtpaKeyFilePwd="@inboundBacksideLTPAKeyPwd@"/>
			
	<#-- AAAPolicy for Outbound calls -->
	<@dp.AAAPolicyAuthenticateAllLTPA
			name="@outboundAaaPolicyName@"
			auLtpaKeyFile="local:///aaa/@outboundFrontsideLTPAKeyFile@"
			auLtpaKeyFilePwd="@outboundFrontsideLTPAKeyPwd@"/>
			
	<#-- AAAPolicy for Inbound calls ELSAM with URL authorization -->
	<@dp.AAAPolicyClientSSL2LTPAURLauth
			name="@inboundAaaPolicyNameURL@"
			aaaFileName="local:///aaa/@inboundAaaFileNameURL@"
			ppLtpaKeyFile="local:///aaa/@inboundBacksideLTPAKeyFile@"
			ppLtpaKeyFilePwd="@inboundBacksideLTPAKeyPwd@"/>	
	
	<#-- Various parameters -->
	<@dp.NFSStaticMount
			name="@nfsLogTargetName@"
			uri="@nfsLogTargetUri@"/>
	<@dp.MatchingRuleURL
			name="${matchingRuleAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleXPath
			name="${matchingRuleAllSoapFaults}"
			xpathExpression="/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Envelope']/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Body']/*[namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/' and local-name()='Fault']"/>
	<@dp.MatchingRuleErrorCode
			name="${matchingRuleAllErrors}"
			errorCodeMatch="*"/>

	<#-- Inbound processing rules ELSAM with TPnr-->
	<@dp.ProcessingRequestRule
		name="@inboundProcessingPolicyTP@"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"@inboundAaaPolicyNameTP@"}, 
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"xform", "name":"addStelvioCtxAction",
						"input":"aaaOutput",	"output":"stelvioContextIncluded", "async":"off",
						"stylesheet":"local:///xslt/add-stelvio-context.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"stelvioContextIncluded","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingFaultRule
		name="@inboundProcessingPolicyTP@"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"@navSigningKeystoreName@",
						"signKey":"@navSigningKeystoreName@"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedResponse",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"signedResponse","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingResponseRule
		name="@inboundProcessingPolicyTP@"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"@navSigningKeystoreName@",
						"signKey":"@navSigningKeystoreName@"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedResponse",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"signedResponse","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="@inboundProcessingPolicyTP@"
			actions=[
				{"type":"xform", "name":"prepareErrorLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendErrorLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT",	"output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultHandler",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="@inboundProcessingPolicyTP@"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyTP@_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"@inboundProcessingPolicyTP@_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyTP@_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"@inboundProcessingPolicyTP@_error-rule"}
			]/>
			
			
	<#-- Inbound processing rules ELSAM with URL resource authorization-->
	<@dp.ProcessingRequestRule
		name="@inboundProcessingPolicyURL@"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"@inboundAaaPolicyNameURL@"}, 
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"xform", "name":"addStelvioCtxAction",
						"input":"aaaOutput",	"output":"stelvioContextIncluded", "async":"off",
						"stylesheet":"local:///xslt/add-stelvio-context.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"stelvioContextIncluded","output":"OUTPUT"}
			]/>	


	<@dp.WSStylePolicy
			name="@inboundProcessingPolicyURL@"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyURL@_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"@inboundProcessingPolicyTP@_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyTP@_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"@inboundProcessingPolicyTP@_error-rule"}
			]/>
			
	<#-- Inbound processing rules for Norsk Pensjon-->
	<@dp.ProcessingRequestRule
		name="@inboundProcessingPolicyNP@"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"@inboundAaaPolicyNameNP@"}, 
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
	<@dp.ProcessingResponseRule
		name="@inboundProcessingPolicyNP@"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"@navSigningKeystoreName@",
						"signKey":"@navSigningKeystoreName@"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedResponse",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"signedResponse","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingErrorRule
			name="@inboundProcessingPolicyNP@"
			actions=[
				{"type":"xform", "name":"prepareErrorLogAction",
						"input":"INPUT",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendErrorLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT", "output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultHandler",	"output":"log", "async":"off",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}
			]/>

	<@dp.WSStylePolicy
			name="@inboundProcessingPolicyNP@"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyNP@_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"@inboundProcessingPolicyNP@_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"@inboundProcessingPolicyNP@_error-rule"}
			]/>			
			
	<#-- Outbound processing rules ELSAM-->
	<@dp.ProcessingRequestRule
		name="@outboundProcessingPolicy@"
		actions=[
				{"type":"aaa", "name":"aaaAction",	
						"input":"INPUT", "output":"PIPE",
						"policy":"@outboundAaaPolicyName@"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
						"signCert":"@navSigningKeystoreName@",
						"signKey":"@navSigningKeystoreName@"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedRequest", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"signedRequest","output":"OUTPUT"}
			]/>	
	<@dp.ProcessingFaultRule
		name="@outboundProcessingPolicy@"
		actions=[
				{"type":"xform", "name":"stripWssAction", "async":"off",
						"input":"INPUT", "output":"wssHeaderRemoved",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"wssHeaderRemoved",	"output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logFaultActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"wssHeaderRemoved","output":"OUTPUT"}
			]/>
	<@dp.ProcessingResponseRule
		name="@outboundProcessingPolicy@"
		actions=[
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT",	"output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"xform", "name":"stripWssAction", "async":"off",
						"input":"INPUT", "output":"wssHeaderRemoved",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"result", "name":"resultAction",
					"input":"wssHeaderRemoved","output":"OUTPUT"}
			]/>
	<@dp.ProcessingErrorRule
		name="@outboundProcessingPolicy@"
		actions=[
				{"type":"xform", "name":"prepareErrorLogAction",
						"input":"INPUT", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendErrorLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT", "output":"faultHandler", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]},
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultHandler", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logFaultActionParams},
				{"type":"fetch", "name":"fetchClassificationAction",
						"destination":"${serviceClassificationDestination}", 
						"output":"serviceClassification"},
				{"type":"xform", "name":"enforceLogPolicyAction",
						"input":"serviceClassification", "output":"loggingDecision",
						"stylesheet":"local:///xslt/enforce-log-policy.xsl",
						"params":[]},
				{"type":"conditional", "name":"conditionalLogAction",
						"input":"loggingDecision",
						"conditions":[
							{
								"expression":"/logging[normalize-space(.)='true']",
								"conditionAction":{"type":"result", "name":"sendLogAction", "input":"log", "output":"NULL", "async":"on", "destination":"${logDestination}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"faultHandler","output":"OUTPUT"}
			]/>
			
	<@dp.WSStylePolicy
			name="@outboundProcessingPolicy@"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"@outboundProcessingPolicy@_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"@outboundProcessingPolicy@_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"@outboundProcessingPolicy@_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"@outboundProcessingPolicy@_error-rule"}
			]/>
	
	<#-- Outbound processing rule Norsk Pensjon-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicyNP}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"@outboundAaaPolicyName@"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
						"signCert":"@navSigningKeystoreName@",
						"signKey":"@navSigningKeystoreName@"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"signedRequest", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"result", "name":"resultAction",
					"input":"signedRequest","output":"OUTPUT"}
			]/>	
			
	<@dp.ProcessingResponseRule
		name="${outboundProcessingPolicyNP}"
		actions=[
				{"type":"verify", "name":"verifiyAction",
						"input":"INPUT",	"output":"NULL",
						"valCred":"${signatureValCred}"},
				{"type":"xform", "name":"prepareLogAction",
						"input":"INPUT", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
						"params":logActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
				{"type":"xform", "name":"stripWssAction",
						"input":"INPUT",	"output":"wssHeaderRemoved", "async":"off",
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
						"stylesheet":"local:///xslt/nfs-message-logger-outbound.xsl",
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

	<#-- Generate Inbound proxies for ELSAM with TPnr policy-->			
	<#list ELSAMInboundTPProxies as proxy>		
		<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="@inboundProcessingPolicyTP@"
			frontsideHandler="@inboundFrontsideHandler@"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideSSLProxy="@inboundBacksideHost@_SSLProxyProfile"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="@inboundBacksideHost@"
			backsidePort="@inboundBacksidePort@"
			backsideTimeout="@inboundBacksideConnectionTimeout@"/>
	</#list>

	<#-- Generate Inbound proxies for ELSAM with URL policy-->			
	<#list ELSAMInboundURLProxies as proxy>		
		<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="@inboundProcessingPolicyURL@"
			frontsideHandler="@inboundFrontsideHandler@"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideSSLProxy="@inboundBacksideHost@_SSLProxyProfile"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="@inboundBacksideHost@"
			backsidePort="@inboundBacksidePort@"
			backsideTimeout="@inboundBacksideConnectionTimeout@"/>
	</#list>	
	
	<#-- Generate Inbound proxies for Norsk Pensjon -->
	<#list NorskPensjonInboundProxies as proxy>
		<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="@inboundProcessingPolicyNP@"
			frontsideHandler="@inboundFrontsideHandler@"
			frontsideProtocol="${inboundFrontsideProtocol}"
			backsideSSLProxy="@inboundBacksideHost@_SSLProxyProfile"
			backsideProtocol="${inboundBacksideProtocol}"
			backsideHost="@inboundBacksideHost@"
			backsidePort="@inboundBacksidePort@"
			backsideTimeout="@inboundBacksideConnectionTimeout@"/>	
	</#list>

	<#-- Generate Outbound proxies for ELSAM -->			
	<#list ELSAMOutboundProxies as proxy>
	<@dp.WSProxyWSADynamicBackendMultipleWsdl
			name="${proxy.name}Outbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="@outboundProcessingPolicy@"
			frontsideHandler="@outboundFrontsideHandler@"
			frontsideProtocol="${outboundFrontsideProtocol}"
			backsideSSLProxy="@inboundFrontsideHost@_SSLProxyProfile"
			wsaRequireAaa="off"
			backsideTimeout="@outboundBacksideConnectionTimeout@"/>
	</#list>
	
	<#-- Generate Outbound proxies for Norsk Pensjon -->
	<#list NorskPensjonOutboundProxies as proxy>
	<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Outbound"
			version="${cfgVersion}"
			wsdls=proxy.wsdls
			policy="${outboundProcessingPolicyNP}"
			frontsideHandler="@outboundFrontsideHandler@"
			frontsideProtocol="${outboundFrontsideProtocol}"
			backsideTimeout="@outboundBacksideConnectionTimeout@"
			backsideProtocol="${outboundBacksideProtocolNP}"
			backsideHost="@outboundBacksideHostNP@"
			backsidePort="@outboundBacksidePortNP@"
			backsideSSLProxy="@inboundFrontsideHost@_SSLProxyProfile"
			/>
	</#list>
	
	<#-- SMS Gateway -->	
	
	<#assign nameSMS="SMSGateway"/>
	<#assign outboundProcessingPolicySMS="${nameSMS}_Policy"/>
	
	<@dp.BacksideSSL
			name="@outboundBacksideHostSMS@"
			trustedCerts=[{"name":"Thawte-Server-CA","file":"pubcert:///Thawte-Server-CA.pem"}]/>
	
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"@outboundAaaPolicyName@"},
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
				{"type":"xform", "name":"prepareLogAction",
						"input":"faultHandler", "output":"log",
						"stylesheet":"local:///xslt/nfs-message-logger.xsl",
						"params":logFaultActionParams},
				{"type":"result", "name":"sendLogAction",
						"input":"log", "output":"NULL", "async":"on",
						"destination":"${logDestination}"},
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
	
	<#list SMSOutboundProxies as proxy>
	<@dp.WSProxyStaticBackend		
		name="${proxy.name}Outbound"
		version="${cfgVersion}"
		wsdlName="@wsdlNameSMS@"
		wsdlLocation="local:///wsdl/@wsdlNameSMS@"
		wsdlPortBinding=["{http://ws.v4.sms.carrot.no}${nameSMS}"]
		policy="${outboundProcessingPolicySMS}"
		frontsideHandler="@outboundFrontsideHandler@"
		frontsideProtocol="${outboundFrontsideProtocol}"
		frontsideUri="@endpointURISMS@"
		backsideProtocol="${outboundBacksideProtocolSMS}"
		backsideHost="@outboundBacksideHostSMS@"
		backsidePort="@outboundBacksidePortSMS@"
		backsideUri="@endpointURISMS@"
		backsideSSLProxy="@outboundBacksideHostSMS@_SSLProxyProfile"
		backsideTimeout=120
	/>	
	</#list>
		
	
	<#-- LogTargets for remote logging-->
	<#-- Requires the nfsStaticMount object created above-->
	<@dp.LogTarget
		name="debug-log"
		size="@maxSizeDebug@"
		nfsStaticMount="@loggingRemoteHost@"
		logFileName="@test@example.com"
		rotations="@numRotDebug@"
		logPriority="debug"
	/>
	<@dp.LogTarget
		name="error-log"
		size="@maxSizeError@"
		nfsStaticMount="@loggingRemoteHost@"
		logFileName="@test@example.com"
		rotations="@numRotError@"
		logPriority="error"
	/>
	<@dp.LogTarget
		name="system-log"
		size="@maxSizeSystem@"
		nfsStaticMount="@loggingRemoteHost@"
		logFileName="@test@example.com"
		rotations="@numRotSystem@"
		logPriority="info"
	/>
	
</@dp.configuration>
