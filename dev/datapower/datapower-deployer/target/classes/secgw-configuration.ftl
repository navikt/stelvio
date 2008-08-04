<#import "datapower-config.ftl" as dp>

<#assign requestActions=[
	"PSELVDefault_request-rule_defaultaction_slm",
	"PSELVDefault_request-rule_aaa_0",
	"PSELVDefault_request-rule_defaultaction_result"]/>
<#assign responseActions=[
	"PSELVDefault_response-rule_defaultaction_result"]/>

<#assign defaultRequestSLMAction="PSELVDefault_request-rule_slmAction"/>
<#assign defaultRequestAAAAction="PSELVDefault_request-rule_aaaAction"/>
<#assign defaultRequestResultAction="PSELVDefault_request-rule_resultAction"/>
<#assign defaultResponseResultAction="PSELVDefault_response-rule_resultAction"/>


<@dp.configuration domain="${cfg.domain}">
	<@dp.FrontsideSSL
			name="${frontside.Host}"
			keystoreName="${frontside.SSLKeystoreName}"
			keystoreFile="${frontside.SSLKeystoreFile}"
			keystorePwd="${frontside.SSLKeystorePwd}"/>
	<@dp.BacksideSSL
			name="${backside.Host}"
			trustedCerts=[{"name":"${backside.TrustCertName}","file":"cert:///${backside.TrustCertName}.pem"}]/>
	<@dp.FrontsideHandler
			protocol="${frontside.Protocol}"
			address="${frontside.Host}"
			port="${frontside.Port}"
			sslProxy="${frontside.Host}_SSLProxy"/>
	<@dp.AAAPolicy
			name="${aaa.PolicyName}"
			aaaFileName="${aaa.FileName}"
			auLtpaKeyFile="${frontside.LTPAKeyFile}"
			auLtpaKeyFilePwd="${frontside.LTPAKeyPwd}"
			ppLtpaKeyFile="${backside.LTPAKeyFile}"
			ppLtpaKeyFilePwd="${backside.LTPAKeyPwd}"/>
	<@dp.SLMPolicy
			name="PSELVDefaultSLMPolicy"/>
	<@dp.URLMatchingRule
			name="PSELVDefault_match_all"
			urlMatch="*"/>
	<@dp.AAAAction
			name="PSELVDefault_request-rule_aaaAction"
			aaaPolicy="${aaa.PolicyName}"
			input="INPUT"
			output="PIPE"/>
	<@dp.ResultAction
			name="PSELVDefault_request-rule_resultAction"
			input="PIPE"
			output="OUTPUT"/>
	<@dp.SLMAction
			name="PSELVDefault_request-rule_slmAction"
			input="INPUT"/>
	<@dp.ResultAction
			name="PSELVDefault_response-rule_resultAction"
			input="INPUT"
			output="OUTPUT"/>
	<@dp.RequestRule
			name="PSELVDefault_request-rule"
			actions=["${defaultRequestSLMAction}","${defaultRequestAAAAction}","${defaultRequestResultAction}"]/>
	<@dp.ResponseRule
			name="PSELVDefault_response-rule"
			actions=["${defaultRequestResultAction}"]/>
	<@dp.ProcessingPolicy
			name="${cfg.ProcessingPolicy}"
			matchingRule="PSELVDefault_match_all"
			requestRule="PSELVDefault_request-rule"
			responseRule="PSELVDefault_response-rule"/>
	<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
	<#list wsdls as wsdl>
	<@dp.WSProxyStaticBackend
			name="${wsdl.proxyName}"
			version="${cfg.version}"
			wsdlName="${wsdl.fileName}"
			wsdlPortBinding="${wsdl.portBinding}"
			policy="${cfg.ProcessingPolicy}"
			frontside=frontside
			backside=backside
			endpointUri="${wsdl.endpointURI}"/>
	</#list>
</@dp.configuration>
