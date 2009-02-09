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
			/>					
	</#list>
	
	<#-- Workmate -->
	<#--
	<#assign workmatewsdl="workmate.wsdl"/> 
	<#assign wsdlPortBindingList = ["{http://schemas.adbsys.com/WDA.HttpHandlers.WMBDRequest.WebService01.xsd}WebService01Soap",
	"{http://schemas.adbsys.com/WDA.HttpHandlers.WMBDRequest.WebService01.xsd}WebService01Soap12"]>
	<#assign workmateBacksideProtocol="https"/>
	<#assign workmateBacksideHost="wasapp-t.adeo.no"/>
	<#assign workmateBacksidePort="443"/>
	<#assign workmateURI="/WorkMateOM/WebServices/WDA.HttpHandlers.WMBDRequest.WebService01.03.asmx"/>
	-->
	
	<#if deployWorkmate=="true">
	<@dp.BacksideSSL
			name="${workmateBacksideHost}"
			trustedCerts=[{"name":"${backsideTrustCertName}","file":"pubcert:///${backsideTrustCertName}.pem"}]/>
	
	<@dp.ProcessingRequestRule
		name="workmateProcessingRequestRule"
		actions=[
			{"type":"slm",	"name":"slmAction",	
					"input":"INPUT",	"output":"NULL"}, 
			{"type":"result", "name":"resultAction",
				"input":"INPUT","output":"OUTPUT"}
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
			/>
			
	</#if>
</@dp.configuration>
