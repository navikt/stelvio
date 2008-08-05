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
	<@dp.AAAPolicy
			name="${aaaPolicyName}"
			aaaFileName="local:///aaa/${aaaFileName}"
			auLtpaKeyFile="local:///aaa/${frontsideLTPAKeyFile}"
			auLtpaKeyFilePwd="${frontsideLTPAKeyPwd}"
			ppLtpaKeyFile="local:///aaa/${backsideLTPAKeyFile}"
			ppLtpaKeyFilePwd="${backsideLTPAKeyPwd}"/>
	<@dp.SLMPolicy
			name="PSELVDefaultSLMPolicy"/>
	<@dp.URLMatchingRule
			name="PSELVDefault_match_all"
			urlMatch="*"/>
	<@dp.AAAAction
			name="${defaultRequestAAAAction}"
			aaaPolicy="${aaaPolicyName}"
			input="INPUT"
			output="PIPE"/>
	<@dp.ResultAction
			name="${defaultRequestResultAction}"
			input="PIPE"
			output="OUTPUT"/>
	<@dp.SLMAction
			name="${defaultRequestSLMAction}"
			input="INPUT"/>
	<@dp.ResultAction
			name="${defaultResponseResultAction}"
			input="INPUT"
			output="OUTPUT"/>
	<@dp.RequestRule
			name="PSELVDefault_request-rule"
			actions=["${defaultRequestSLMAction}","${defaultRequestAAAAction}","${defaultRequestResultAction}"]/>
	<@dp.ResponseRule
			name="PSELVDefault_response-rule"
			actions=["${defaultResponseResultAction}"]/>
	<@dp.ProcessingPolicy
			name="${cfgProcessingPolicy}"
			matchingRule="PSELVDefault_match_all"
			requestRule="PSELVDefault_request-rule"
			responseRule="PSELVDefault_response-rule"/>
	<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
	<#list wsdls as wsdl>
	<@dp.WSProxyStaticBackend
			name="${wsdl.proxyName}"
			version="${cfgVersion}"
			wsdlName="${wsdl.fileName}"
			wsdlPortBinding="${wsdl.portBinding}"
			policy="${cfgProcessingPolicy}"
			frontsideHandler="${frontsideHandler}"
			frontsideProtocol="${frontsideProtocol}"
			backsideProtocol="${backsideProtocol}"
			backsideHost="${backsideHost}"
			backsidePort="${backsidePort}"
			endpointUri="${wsdl.endpointURI}"/>
	</#list>
</@dp.configuration>
