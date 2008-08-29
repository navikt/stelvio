<#import "datapower-config.ftl" as dp>

<#assign defaultRequestSLMAction="PSELVDefault_request-rule_slmAction"/>
<#assign defaultRequestAAAAction="PSELVDefault_request-rule_aaaAction"/>
<#assign defaultRequestResultAction="PSELVDefault_request-rule_resultAction"/>
<#assign defaultResponseResultAction="PSELVDefault_response-rule_resultAction"/>


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
			name="PSELVDefault_match_all"
			urlMatch="*"/>
	<@dp.StylePolicyActionSLM
			name="${defaultRequestSLMAction}"
			input="INPUT"/>
	<@dp.StylePolicyActionAAA
			name="${defaultRequestAAAAction}"
			aaaPolicy="${aaaPolicyName}"
			input="INPUT"
			output="PIPE"/>
	<@dp.StylePolicyActionResult
			name="${defaultRequestResultAction}"
			input="PIPE"
			output="OUTPUT"/>
	<@dp.StylePolicyActionResult
			name="${defaultResponseResultAction}"
			input="INPUT"
			output="OUTPUT"/>
	<@dp.WSStylePolicyRuleRequest
			name="PSELVDefault_request-rule"
			actions=["${defaultRequestSLMAction}","${defaultRequestAAAAction}","${defaultRequestResultAction}"]/>
	<@dp.WSStylePolicyRuleResponse
			name="PSELVDefault_response-rule"
			actions=["${defaultResponseResultAction}"]/>
	<@dp.WSStylePolicy
			name="${cfgProcessingPolicy}"
			policyMapsList=[
				{"matchingRule":"PSELVDefault_match_all","processingRule":"PSELVDefault_request-rule"},
				{"matchingRule":"PSELVDefault_match_all","processingRule":"PSELVDefault_response-rule"}
			]/>
	<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
	<#list wsdls as wsdl>
	<@dp.WSProxyStaticBackend
			name="${wsdl.proxyName}"
			version="${cfgVersion}"
			wsdlName="${wsdl.fileName}"
			wsdlLocation="local:///wsdl/${wsdl.fileName}"
			wsdlPortBinding="${wsdl.portBinding}"
			policy="${cfgProcessingPolicy}"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="${wsdl.endpointURI}"
			backsideProtocol="${backsideProtocol}"
			backsideHost="${backsideHost}"
			backsidePort="${backsidePort}"
			backsideUri="${wsdl.endpointURI}"/>
	</#list>
</@dp.configuration>
