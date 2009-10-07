<#import "datapower-config.ftl" as dp>

<#assign defaultMatchAll="PSELVDefault_match_all"/>
<#assign defaultMatchAllErrors="PSELVDefault_match_allErrors"/>

<@dp.configuration domain="${cfgDomain}">
	<@dp.FrontsideSSL
			name="${frontsideHost}"
			keystoreName="${frontsideSSLKeystoreName}"
			keystoreFile="${frontsideSSLKeystoreFile}"
			keystorePwd="${frontsideSSLKeystorePwd}"/>
	<@dp.BacksideSSL
			name="${backsideHost}"
			trustedCerts=[{"name":"${backsideTrustCertName}","file":"pubcert:///${backsideTrustCertName}.pem"}]/>
	<@dp.FrontsideHandler
			name="${frontsideHandler}"
			protocol="${frontsideProtocol}"
			address="${frontsideHost}"
			port="${frontsidePort}"
			sslProxy="${frontsideHost}_SSLProxyProfile"/>
	<@dp.AAAPolicyLTPA2LTPA
			name="${aaaPolicyName}"
			aaaFileName="local:///aaa/${aaaFileName}"
			auLtpaKeyFile="local:///aaa/${frontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${frontsideLTPAKeyPwd}"
			ppLtpaKeyFile="local:///aaa/${backsideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${backsideLTPAKeyPwd}"/>
	<@dp.SLMPolicy
			name="PSELVDefaultSLMPolicy"/>
	<@dp.MatchingRuleURL
			name="${defaultMatchAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleErrorCode
			name="${defaultMatchAllErrors}"
			errorCodeMatch="*"/>

	<@dp.ProcessingRequestRule
			name="${cfgProcessingPolicy}"
			actions=[
				{"type":"slm",	"name":"slmAction",	
						"input":"INPUT",	"output":"NULL"},
				{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"${aaaPolicyName}"}, 
				{"type":"result", "name":"resultAction",
					"input":"PIPE","output":"OUTPUT"}
			]/>
	<@dp.ProcessingResponseRule
			name="${cfgProcessingPolicy}"
			actions=[
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>
	<@dp.ProcessingErrorRule
			name="${cfgProcessingPolicy}"
			actions=[
				{"type":"xform", "name":"createFaultAction",
						"input":"INPUT",	"output":"PIPE", "async":"off",
						"stylesheet":"local:///xslt/fault-handler.xsl",
						"params":[
							{"name":"fault-prefix","value":"DataPower Security Gateway"}
						]
				},
				{"type":"result", "name":"resultAction",
					"input":"PIPE","output":"OUTPUT"}
			]/>
			
	<@dp.WSStylePolicy
			name="${cfgProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_request-rule"},
				{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_response-rule"}
				{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${cfgProcessingPolicy}_error-rule"}
			]/>
			
	<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
	<#list wsdls as wsdl>
	<@dp.WSProxyStaticBackend
			name="${wsdl.proxyName}"
			version="${cfgVersion}"
			wsdlName="${wsdl.fileName}"
			wsdlLocation="local:///wsdl/${wsdl.fileName}"
			wsdlPortBinding=wsdl.portBinding
			policy="${cfgProcessingPolicy}"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="${wsdl.endpointURI}"
			backsideProtocol="${backsideProtocol}"
			backsideHost="${backsideHost}"
			backsidePort="${backsidePort}"
			backsideUri="${wsdl.endpointURI}"
			backsideSSLProxy="${backsideHost}_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"
			/>					
	</#list>
	
	<#-- Workmate -->
	<#-- FIXME workmate. Comment in the below to be able to deploy workmate -->
	
	<#-- 
	
	<#if deployWorkmate=="true">
	<@dp.BacksideSSL
			name="${workmateBacksideHost}"
			trustedCerts=[{"name":"${backsideTrustCertName}","file":"pubcert:///${backsideTrustCertName}.pem"}]/>
	
	
	<@dp.AAAPolicyBasicAuth
			name="${workmateAaaPolicyName}"
			aaaFileName="local:///aaa/${workmateAaaFilename}"/>
	
	<@dp.ProcessingRequestRule
		name="workmateProcessingRequestRule"
		actions=[
			{"type":"slm",	"name":"slmAction",	
					"input":"INPUT",	"output":"NULL"}, 
			{"type":"aaa",	"name":"aaaAction",	
						"input":"INPUT",	"output":"PIPE",
						"policy":"${workmateAaaPolicyName}"}, 
			{"type":"result", "name":"resultAction",
				"input":"PIPE","output":"OUTPUT"}
		]/>
	<@dp.WSStylePolicy
		name="workmateProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"workmateProcessingRequestRule_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${cfgProcessingPolicy}_error-rule"}
		]/>
	<@dp.WSProxyStaticBackend
			name="Workmate_WebService01"
			version="${cfgVersion}"
			wsdlName="${workmatewsdl}"
			wsdlLocation="local:///wsdl/${workmatewsdl}"
			wsdlPortBinding=wsdlPortBindingList
			policy="workmateProcessingPolicy"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="${workmateURI}"
			backsideProtocol="${workmateBacksideProtocol}"
			backsideHost="${workmateBacksideHost}"
			backsidePort="${workmateBacksidePort}"
			backsideUri="${workmateURI}"
			backsideSSLProxy="${workmateBacksideHost}_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"
			/>		
	</#if>
	
	-->
	
	<#-- Proxies for DineUtbetalinger.  -->
	<#-- One proxy for the services on WPS and one proxy for the direct call to UTI-->
		
		<#-- AAAPolicy for UTI-service. Using HTTP Basic Authentication, so no post-processing-->
		<@dp.AAAPolicyLTPA2None
			name="SBLUTBUTIAaaPolicy"
			aaaFileName="local:///aaa/${SBLUTBUTIaaaFileName}"
			auLtpaKeyFile="local:///aaa/${frontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${frontsideLTPAKeyPwd}"
		/>
		
		
		<#-- Request-rule to add the AAAPolicy for services on UTI-->
		<@dp.ProcessingRequestRule
			name="SBLUTBUTIProcessingRequestRule"
			actions=[
			{"type":"aaa",	"name":"aaaAction",
						"input":"INPUT",	"output":"PIPE",
						"policy":"SBLUTBUTIAaaPolicy"}, 
			{"type":"xform", "name":"StripSecHeadersAction", "async":"off",
						"input":"PIPE",	"output":"PIPE",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},			
			{"type":"result", "name":"resultAction", 
				"input":"PIPE","output":"OUTPUT"}
		]/>

		<@dp.WSStylePolicy
		name="SBLUTBUTIProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"SBLUTBUTIProcessingRequestRule_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${cfgProcessingPolicy}_error-rule"}
		]/>
		
		<@dp.BacksideSSL
			name="SBLUTBUTI"
			trustedCerts=[{"name":"${SBLUTBUTITrustCertName}","file":"pubcert:///${SBLUTBUTITrustCertName}"}
		]/>
			
		<@dp.WSProxyStaticBackend
			name="SBLUTBUTI"
			version="${cfgVersion}"
			wsdlName="${SBLUTBUTIWsdl}"
			wsdlLocation="local:///wsdl/${SBLUTBUTIWsdl}"
			wsdlPortBinding=["{urn:nav:ikt:personkort:integration:uti}UTIServicePort"]
			policy="SBLUTBUTIProcessingPolicy"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="${SBLUTBUTIURI}"
			backsideProtocol="${SBLUTBUTIbacksideProtocol}"
			backsideHost="${SBLUTBUTIbacksideHost}"
			backsidePort="${SBLUTBUTIbacksidePort}"
			backsideUri="${SBLUTBUTIURI}"
			backsideSSLProxy="SBLUTBUTI_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"
		/>	
		
	<#-- Configuration objects for SBLUTB-services on WPS-->

		<@dp.AAAPolicyLTPA2LTPA
			name="SBLUTBWPSAaaPolicy"
			aaaFileName="local:///aaa/${SBLUTBWPSaaaFileName}"
			auLtpaKeyFile="local:///aaa/${frontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${frontsideLTPAKeyPwd}"
			ppLtpaKeyFile="local:///aaa/${backsideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${backsideLTPAKeyPwd}"/>
		
		<#-- Request-rule to add the AAAPolicy for services on WPS-->
		<@dp.ProcessingRequestRule
			name="SBLUTBWPSProcessingRequestRule"
			actions=[
			{"type":"aaa",	"name":"aaaAction",
						"input":"INPUT",	"output":"PIPE",
						"policy":"SBLUTBWPSAaaPolicy"}, 
			{"type":"result", "name":"resultAction", 
				"input":"PIPE","output":"OUTPUT"}
		]/>

		<@dp.WSStylePolicy
		name="SBLUTBWPSProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"SBLUTBWPSProcessingRequestRule_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${cfgProcessingPolicy}_error-rule"}
		]/>
	
	<#-- Proxy for bestillFrikortBrev on Frikortkjernen -->
		
		<@dp.AAAPolicyLTPA2None
			name="FrikortESAaaPolicy"
			aaaFileName="local:///aaa/${FrikortESAaaFileName}"
			auLtpaKeyFile="local:///aaa/${frontsideLTPAKeyFile}" 
			auLtpaKeyFilePwd="${frontsideLTPAKeyPwd}"
		/>
		
		<#-- Request-rule to add the AAAPolicy for Frikort-->
		<@dp.ProcessingRequestRule
			name="Frikort"
			actions=[
			{"type":"aaa",	"name":"aaaAction",
						"input":"INPUT",	"output":"PIPE",
						"policy":"FrikortESAaaPolicy"}, 
			{"type":"xform", "name":"StripSecHeadersAction", "async":"off",
						"input":"PIPE",	"output":"PIPE",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},
			{"type":"result", "name":"resultAction", 
				"input":"PIPE","output":"OUTPUT"}
		]/>

		<@dp.WSStylePolicy
		name="FrikortESProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"Frikort_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${cfgProcessingPolicy}_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${cfgProcessingPolicy}_error-rule"}
		]/>
	
		<@dp.BacksideSSL
			name="FrikortES"
			trustedCerts=[{"name":"${FrikortESTrustCertName}","file":"pubcert:///${FrikortESTrustCertName}"}
		]/>
		
		<@dp.WSProxyStaticBackend
			name="FrikortEksternService"
			version="${cfgVersion}"
			wsdlName="${FrikortESWsdl}"
			wsdlLocation="local:///wsdl/${FrikortESWsdl}"
			wsdlPortBinding=["{http://ekstern.ws.frikort.nav.no}EksternService"]
			policy="FrikortESProcessingPolicy"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="${FrikortESURI}"
			backsideProtocol="${FrikortESBacksideProtocol}"
			backsideHost="${FrikortESBacksideHost}"
			backsidePort="${FrikortESBacksidePort}"
			backsideUri="${FrikortESURI}"
			backsideSSLProxy="FrikortES_SSLProxyProfile"
			backsideTimeout="${inboundBacksideConnectionTimeout}"
		/>

		<HTTPUserAgent name="default" intrinsic="true" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
			<mAdminState>enabled</mAdminState>
			<UserSummary>Default User Agent</UserSummary>
			<MaxRedirects>8</MaxRedirects>
			<Timeout>300</Timeout>
			<BasicAuthPolicies>
				<RegExp>"${FrikortESBasicURLMatch}"</RegExp>
				<UserName>"${FrikortESBasicUserName}"</UserName>
				<Password>"${FrikortESBasicPassword}"</Password>
			</BasicAuthPolicies>
			<BasicAuthPolicies>
				<RegExp>"${SBLUTBUTIBasicURLMatch}"</RegExp>
				<UserName>"${SBLUTBUTIBasicUserName}"</UserName>
				<Password>"${SBLUTBUTIBasicPassword}"</Password>
			</BasicAuthPolicies>
		</HTTPUserAgent>	
		
</@dp.configuration>
