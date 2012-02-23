<#import "datapower-config.ftl" as dp>

<#assign inboundBacksideTrustedCerts=[
{"name":"Commfides_Premium_SSL_Wildcard_SGC","file":"pubcert:///Commfides_Premium_SSL_Wildcard_SGC.cer"}
]/>


<#assign logActionParams=[
	{"name":"log-store",	"value":"hporap04.internsone.local"},
	{"name":"domain",		"value":"on"},
	{"name":"proxy",		"value":"on"},
	{"name":"operation",	"value":"on"},
	{"name":"user",			"value":"on"},
	{"name":"transaction",	"value":"on"},
	{"name":"receiver",		"value":"on"},
	{"name":"rule",			"value":"on"}
]/>
<#assign logFaultActionParams=[
	{"name":"log-store",	"value":"hporap04.internsone.local"},
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
			name="partner-gw-t2.nav.no"
			keystoreName="partner-gw-t2.nav.no"
			keystoreFile="cert:///partner-gw-t2.nav.no.p12"
			keystorePwd="Test1234"
			trustedCerts=partnerTrustedCerts/>
			
	<#-- Frontside SSL configuration for Outbound calls -->
	<@dp.FrontsideSSL
			name="partner-gw-t2.test.internsone.local"
			keystoreName="partner-gw-t2.test.internsone.local"
			keystoreFile="cert:///partner-gw-t2.test.internsone.local.p12"
			keystorePwd="Passord1234"/>
			
	<#-- Backside SSL configuration for Inbound calls -->
	<@dp.BacksideSSL
			name="tjenestebuss-t2.adeo.no"
			trustedCerts=[{"name":"Commfides_Premium_SSL_Wildcard_SGC","file":"pubcert:///Commfides_Premium_SSL_Wildcard_SGC.cer"}]/>
	<@dp.CryptoValCredPKIX
			name="${signatureValCred}"
			trustedCerts=partnerTrustedCerts/>
	<@dp.SigningCertificate
			name="NAV-PartnerGateway-TEST-SigningCert"
			file="cert:///NAV-PartnerGateway-TEST-SigningCert.p12"
			password="testtest"/>
			
	<#-- HTTPSFrontsideProtocolHandler for Inbound calls -->
	<@dp.FrontsideHandler
			name="https_partner-gw-t2.nav.no_443"
			protocol="https"
			address="partner-gw-t2.nav.no"
			port="443"
			sslProxy="partner-gw-t2.nav.no_SSLProxyProfile"/>
			
	<#-- HTTPSFrontsideProtocolHandler for Outbound calls -->
	<@dp.FrontsideHandler
			name="https_partner-gw-t2.test.internsone.local_9443"
			protocol="https"
			address="partner-gw-t2.test.internsone.local"
			port="9443"
			sslProxy="partner-gw-t2.test.internsone.local_SSLProxyProfile"/>
			
	<#-- AAAPolicy for Inbound calls ELSAM with TPnr authorization-->
	<@dp.AAAPolicyClientSSL2LTPA
			name="SSLDNToLTPATP"
			aaaFileName="local:///aaa/ekstern_samhandler_TP_AAAInfo.xml"
			ppLtpaKeyFile="local:///aaa/test.local.ltpa"
			ppLtpaKeyFilePwd="Test1234"/>
			
	<#-- AAAPolicy for Inbound calls NorskPensjon -->
	<@dp.AAAPolicyClientSSL2LTPAAllAuthenticated
			name="InboundAAAPolicyNP"
			aaaFileName="local:///aaa/norskpensjon_AAAInfo.xml"
			ppLtpaKeyFile="local:///aaa/test.local.ltpa" 
			ppLtpaKeyFilePwd="Test1234"/>
			
	<#-- AAAPolicy for Outbound calls -->
	<@dp.AAAPolicyAuthenticateAllLTPA
			name="LTPAAllAuthenticated"
			auLtpaKeyFile="local:///aaa/test.local.ltpa"
			auLtpaKeyFilePwd="Test1234"/>
			
	<#-- AAAPolicy for Inbound calls ELSAM with URL authorization -->
	<@dp.AAAPolicyClientSSL2LTPAURLauth
			name="SSLDNToLTPAURL"
			aaaFileName="local:///aaa/ekstern_samhandler_URL_AAAInfo.xml"
			ppLtpaKeyFile="local:///aaa/test.local.ltpa"
			ppLtpaKeyFilePwd="Test1234"/>	
	
	<#-- Various parameters -->
	<@dp.NFSStaticMount
			name="hporap04.internsone.local"
			uri="hporap04.internsone.local:/var/opt/datapower"/>
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
		name="ELSAMInboundProcessingPolicyTP"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"SSLDNToLTPATP"}, 
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
		name="ELSAMInboundProcessingPolicyTP"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"NAV-PartnerGateway-TEST-SigningCert",
						"signKey":"NAV-PartnerGateway-TEST-SigningCert"},
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
		name="ELSAMInboundProcessingPolicyTP"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"NAV-PartnerGateway-TEST-SigningCert",
						"signKey":"NAV-PartnerGateway-TEST-SigningCert"},
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
			name="ELSAMInboundProcessingPolicyTP"
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
			name="ELSAMInboundProcessingPolicyTP"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMInboundProcessingPolicyTP_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"ELSAMInboundProcessingPolicyTP_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMInboundProcessingPolicyTP_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"ELSAMInboundProcessingPolicyTP_error-rule"}
			]/>
			
			
	<#-- Inbound processing rules ELSAM with URL resource authorization-->
	<@dp.ProcessingRequestRule
		name="ELSAMInboundProcessingPolicyURL"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"SSLDNToLTPAURL"}, 
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
			name="ELSAMInboundProcessingPolicyURL"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMInboundProcessingPolicyURL_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"ELSAMInboundProcessingPolicyTP_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMInboundProcessingPolicyTP_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"ELSAMInboundProcessingPolicyTP_error-rule"}
			]/>
			
	<#-- Inbound processing rules for Norsk Pensjon-->
	<@dp.ProcessingRequestRule
		name="InboundProcessingPolicyNP"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"aaaOutput",
						"policy":"InboundAAAPolicyNP"}, 
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
		name="InboundProcessingPolicyNP"
		actions=[
				{"type":"sign", "name":"signAction",
						"input":"INPUT",	"output":"signedResponse",
						"signCert":"NAV-PartnerGateway-TEST-SigningCert",
						"signKey":"NAV-PartnerGateway-TEST-SigningCert"},
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
			name="InboundProcessingPolicyNP"
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
			name="InboundProcessingPolicyNP"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"InboundProcessingPolicyNP_request-rule"}, 
				{"matchingRule":"${matchingRuleAll}","processingRule":"InboundProcessingPolicyNP_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"InboundProcessingPolicyNP_error-rule"}
			]/>			
			
	<#-- Outbound processing rules ELSAM-->
	<@dp.ProcessingRequestRule
		name="ELSAMDefaultOutboundProcessingPolicy"
		actions=[
				{"type":"aaa", "name":"aaaAction",	
						"input":"INPUT", "output":"PIPE",
						"policy":"LTPAAllAuthenticated"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
						"signCert":"NAV-PartnerGateway-TEST-SigningCert",
						"signKey":"NAV-PartnerGateway-TEST-SigningCert"},
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
		name="ELSAMDefaultOutboundProcessingPolicy"
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
		name="ELSAMDefaultOutboundProcessingPolicy"
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
		name="ELSAMDefaultOutboundProcessingPolicy"
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
			name="ELSAMDefaultOutboundProcessingPolicy"
			policyMapsList=[
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMDefaultOutboundProcessingPolicy_request-rule"}, 
				{"matchingRule":"${matchingRuleAllSoapFaults}","processingRule":"ELSAMDefaultOutboundProcessingPolicy_fault-response-rule"},
				{"matchingRule":"${matchingRuleAll}","processingRule":"ELSAMDefaultOutboundProcessingPolicy_response-rule"},
				{"matchingRule":"${matchingRuleAllErrors}","processingRule":"ELSAMDefaultOutboundProcessingPolicy_error-rule"}
			]/>
	
	<#-- Outbound processing rule Norsk Pensjon-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicyNP}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"LTPAAllAuthenticated"},
				{"type":"xform", "name":"stripWSSecAction",
						"input":"PIPE",	"output":"PIPE", "async":"off",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
				{"type":"sign", "name":"signAction",
						"input":"PIPE",	"output":"signedRequest",
						"signCert":"NAV-PartnerGateway-TEST-SigningCert",
						"signKey":"NAV-PartnerGateway-TEST-SigningCert"},
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
			version="blir_overskrevet"
			wsdls=proxy.wsdls
			policy="ELSAMInboundProcessingPolicyTP"
			frontsideHandler="https_partner-gw-t2.nav.no_443"
			frontsideProtocol="https"
			backsideSSLProxy="tjenestebuss-t2.adeo.no_SSLProxyProfile"
			backsideProtocol="https"
			backsideHost="tjenestebuss-t2.adeo.no"
			backsidePort="443"
			backsideTimeout="120"/>
	</#list>

	<#-- Generate Inbound proxies for ELSAM with URL policy-->			
	<#list ELSAMInboundURLProxies as proxy>		
		<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="blir_overskrevet"
			wsdls=proxy.wsdls
			policy="ELSAMInboundProcessingPolicyURL"
			frontsideHandler="https_partner-gw-t2.nav.no_443"
			frontsideProtocol="https"
			backsideSSLProxy="tjenestebuss-t2.adeo.no_SSLProxyProfile"
			backsideProtocol="https"
			backsideHost="tjenestebuss-t2.adeo.no"
			backsidePort="443"
			backsideTimeout="120"/>
	</#list>	
	
	<#-- Generate Inbound proxies for Norsk Pensjon -->
	<#list NorskPensjonInboundProxies as proxy>
		<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Inbound"
			version="blir_overskrevet"
			wsdls=proxy.wsdls
			policy="InboundProcessingPolicyNP"
			frontsideHandler="https_partner-gw-t2.nav.no_443"
			frontsideProtocol="https"
			backsideSSLProxy="tjenestebuss-t2.adeo.no_SSLProxyProfile"
			backsideProtocol="https"
			backsideHost="tjenestebuss-t2.adeo.no"
			backsidePort="443"
			backsideTimeout="120"/>	
	</#list>

	<#-- Generate Outbound proxies for ELSAM -->			
	<#list ELSAMOutboundProxies as proxy>
	<@dp.WSProxyWSADynamicBackendMultipleWsdl
			name="${proxy.name}Outbound"
			version="blir_overskrevet"
			wsdls=proxy.wsdls
			policy="ELSAMDefaultOutboundProcessingPolicy"
			frontsideHandler="https_partner-gw-t2.test.internsone.local_9443"
			frontsideProtocol="https"
			backsideSSLProxy="partner-gw-t2.nav.no_SSLProxyProfile"
			wsaRequireAaa="off"
			backsideTimeout="60"/>
	</#list>
	
	<#-- Generate Outbound proxies for Norsk Pensjon -->
	<#list NorskPensjonOutboundProxies as proxy>
	<@dp.WSProxyStaticBackendMultipleWsdl
			name="${proxy.name}Outbound"
			version="blir_overskrevet"
			wsdls=proxy.wsdls
			policy="${outboundProcessingPolicyNP}"
			frontsideHandler="https_partner-gw-t2.test.internsone.local_9443"
			frontsideProtocol="https"
			backsideTimeout="60"
			backsideProtocol="https"
			backsideHost="webservice.preprod.norskpensjon.no"
			backsidePort="443"
			backsideSSLProxy="partner-gw-t2.nav.no_SSLProxyProfile"
			/>
	</#list>
	
	<#-- SMS Gateway -->	
	
	<#assign nameSMS="SMSGateway"/>
	<#assign outboundProcessingPolicySMS="${nameSMS}_Policy"/>
	
	<@dp.BacksideSSL
			name="smsgw.carrot.no"
			trustedCerts=[{"name":"Thawte-Server-CA","file":"pubcert:///Thawte-Server-CA.pem"}]/>
	
	<#-- Outbound processing rules-->
	<@dp.ProcessingRequestRule
		name="${outboundProcessingPolicySMS}"
		actions=[
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"LTPAAllAuthenticated"},
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
		version="blir_overskrevet"
		wsdlName="carrot_sms_gateway.wsdl"
		wsdlLocation="local:///wsdl/carrot_sms_gateway.wsdl"
		wsdlPortBinding=["{http://ws.v4.sms.carrot.no}${nameSMS}"]
		policy="${outboundProcessingPolicySMS}"
		frontsideHandler="https_partner-gw-t2.test.internsone.local_9443"
		frontsideProtocol="https"
		frontsideUri="/smsgw/services/SMSGateway"
		backsideProtocol="https"
		backsideHost="smsgw.carrot.no"
		backsidePort="443"
		backsideUri="/smsgw/services/SMSGateway"
		backsideSSLProxy="smsgw.carrot.no_SSLProxyProfile"
		backsideTimeout=120
	/>	
	</#list>
		
	
	<#-- LogTargets for remote logging-->
	<#-- Requires the nfsStaticMount object created above-->
	<@dp.LogTarget
		name="debug-log"
		size="5000"
		nfsStaticMount="hporap04.internsone.local"
		logFileName="partner-gw-t2_debug.log"
		rotations="10"
		logPriority="debug"
	/>
	<@dp.LogTarget
		name="error-log"
		size="1000"
		nfsStaticMount="hporap04.internsone.local"
		logFileName="partner-gw-t2_error.log"
		rotations="3"
		logPriority="error"
	/>
	<@dp.LogTarget
		name="system-log"
		size="1000"
		nfsStaticMount="hporap04.internsone.local"
		logFileName="partner-gw-t2_system.log"
		rotations="5"
		logPriority="info"
	/>
	
</@dp.configuration>
