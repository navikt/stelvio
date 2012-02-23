<#import "datapower-config.ftl" as dp>

<#assign navDefaultPolicyName="NAV"/>
<#assign sblUTBUTIPolicyName="SBLUTBUTI"/>

<#assign navDefaultAAAPolicyName="NAVDefaultAAA_Intern2Sensitiv"/>
<#assign navUsernameTokenPolicyName="NAVUsernameToken_Intern2Sensitiv"/>
<#assign sblUTBUTIAAAPolicyName="SBLUTBUTIAAAInfo"/>
<#assign TMSPolicyName="TMSAAAInfo" />
<#assign sblUTBUTIUsernameTokenPolicyName="SBLUTBUTIUsernameToken"/>
<#assign TMSUsernameTokenPolicyName="TMSUsernameToken" />
<#assign ldapSearchParametersName="LDAPSearch_sAMAccountName"/>

<#assign defaultMatchAll="${navDefaultPolicyName}Default_match_all"/>
<#assign defaultMatchAllErrors="${navDefaultPolicyName}Default_match_allErrors"/>

<@dp.configuration domain="@cfgDomain@">
	<HTTPUserAgent name="default" intrinsic="true" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<UserSummary>Default User Agent</UserSummary>
		<MaxRedirects>8</MaxRedirects>
		<Timeout>300</Timeout>
		<BasicAuthPolicies>
			<RegExp>@FrikortESBasicURLMatch@</RegExp>
			<UserName>@FrikortESBasicUserName@</UserName>
			<Password>@FrikortESBasicPassword@</Password>
		</BasicAuthPolicies>
		<BasicAuthPolicies>
			<RegExp>@SBLUTBUTIBasicURLMatch@</RegExp>
			<UserName>@SBLUTBUTIBasicUserName@</UserName>
			<Password>@SBLUTBUTIBasicPassword@</Password>
		</BasicAuthPolicies>
	</HTTPUserAgent>
	<@dp.FrontsideSSL
			name="@frontsideHost@"
			keystoreName="@frontsideSSLKeystoreName@"
			keystoreFile="@frontsideSSLKeystoreFile@"
			keystorePwd="@frontsideSSLKeystorePwd@"/>
	<@dp.BacksideSSL
			name="@backsideHost@"
			trustedCerts=[{"name":"@backsideTrustCertName@","file":"pubcert:///@backsideTrustCertName@.cer"}]/>
	<@dp.FrontsideHandler
			name="@frontsideHandler@"
			protocol="${frontsideProtocol}"
			address="@frontsideHost@"
			port="@frontsidePort@"
			sslProxy="@frontsideHost@_SSLProxyProfile"/>
	<@dp.LDAPSearchParameters
			name="${ldapSearchParametersName}"
			ldapBaseDN="@frontsideLDAPBaseDN@"
			ldapReturnedAttribute="dn"
			ldapFilterPrefix="sAMAccountName="
			ldapScope="subtree"/>
	<@dp.AAAPolicyLTPA2LTPA
			name="${navDefaultAAAPolicyName}"
			aaaFileName="local:///aaa/${navDefaultAAAPolicyName}.xml"
			auLtpaKeyFile="local:///aaa/@frontsideLTPAKeyFile@"
			auLtpaKeyFilePwd="@frontsideLTPAKeyPwd@"
			ppLtpaKeyFile="local:///aaa/@backsideLTPAKeyFile@"
			ppLtpaKeyFilePwd="@backsideLTPAKeyPwd@"/>
	<@dp.AAAPolicyUsernameToken2LTPA
			name="${navUsernameTokenPolicyName}"
			<#-- this policy share AAA xml file with the LTPA2LTPA policy above -->
			aaaFileName="local:///aaa/${navDefaultAAAPolicyName}.xml"
			auLdapHost="@frontsideLDAPHost@"
			auLdapPort="@frontsideLDAPPort@"
			auLdapBindDN="@frontsideLDAPBindDN@"
			auLdapBindPwd="@frontsideLDAPBindPwd@"
			auLdapSearchParameters="${ldapSearchParametersName}"
			ppLtpaKeyFile="local:///aaa/@backsideLTPAKeyFile@"
			ppLtpaKeyFilePwd="@backsideLTPAKeyPwd@"/>
	<@dp.SLMPolicy
			name="PSELVDefaultSLMPolicy"/>
	<@dp.MatchingRuleURL
			name="${defaultMatchAll}"
			urlMatch="*"/>
	<@dp.MatchingRuleErrorCode
			name="${defaultMatchAllErrors}"
			errorCodeMatch="*"/>

	<@dp.ProcessingRequestRule
			name="${navDefaultPolicyName}DefaultProcessingPolicy"
			actions=[
				{"type":"slm",	"name":"slmAction",	
						"input":"INPUT",	"output":"NULL"},
				{"type":"conditional", "name":"conditionalAuthenticationAction",
						"input":"INPUT",
						"conditions":[
							{
								"expression":"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Security']/*[local-name()='BinarySecurityToken']",
								"conditionAction":{"type":"aaa", "name":"aaaLTPAConditionalAction", "input":"INPUT", "output":"aaaOutput", "policy":"${navDefaultAAAPolicyName}"}
							},
							{
								<#-- this condition matches everything other than LTPA, but the policy only allows UsernameToken -->
								"expression":"*",
								"conditionAction":{"type":"aaa", "name":"aaaUsernameTokenConditionalAction", "input":"INPUT", "output":"aaaOutput", "policy":"${navUsernameTokenPolicyName}"}
							}
						]},
				{"type":"result", "name":"resultAction",
					"input":"aaaOutput","output":"OUTPUT"}
			]/>
	<@dp.ProcessingResponseRule
			name="${navDefaultPolicyName}DefaultProcessingPolicy"
			actions=[
				{"type":"result", "name":"resultAction",
					"input":"INPUT","output":"OUTPUT"}
			]/>
	<@dp.ProcessingErrorRule
			name="${navDefaultPolicyName}DefaultProcessingPolicy"
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
			name="${navDefaultPolicyName}DefaultProcessingPolicy"
			policyMapsList=[
				{"matchingRule":"${defaultMatchAll}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_request-rule"},
				{"matchingRule":"${defaultMatchAll}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_response-rule"}
				{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_error-rule"}
			]/>
			
	<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
	<#list NAVProxies as proxy>
		<#list proxy.wsdls as wsdl>
			<@dp.WSProxyStaticBackend
					name="${wsdl.proxyName}"
					version="${cfgVersion}"
					wsdlName="${wsdl.fileName}"
					wsdlLocation="local:///wsdl/${wsdl.fileName}"
					wsdlPortBinding=wsdl.portBinding
					policy="${navDefaultPolicyName}DefaultProcessingPolicy"
					frontsideHandler="@frontsideHandler@"
					frontsideProtocol="${frontsideProtocol}"
					frontsideUri="${wsdl.endpointURI}"
					backsideProtocol="${backsideProtocol}"
					backsideHost="@backsideHost@"
					backsidePort="@backsidePort@"
					backsideUri="${wsdl.endpointURI}"
					backsideSSLProxy="@backsideHost@_SSLProxyProfile"
					backsideTimeout="@inboundBacksideConnectionTimeout@"
					/>					
		</#list>
	</#list>
	
	
	<#-- Proxy for DineUtbetalinger direct calls to UTI.  -->
		
		<#-- AAAPolicies for UTI-service. Using HTTP Basic Authentication, so no post-processing -->
		<@dp.AAAPolicyLTPA2None
			name="${sblUTBUTIAAAPolicyName}"
			aaaFileName="local:///aaa/${sblUTBUTIAAAPolicyName}.xml"
			auLtpaKeyFile="local:///aaa/@frontsideLTPAKeyFile@"
			auLtpaKeyFilePwd="@frontsideLTPAKeyPwd@"/>
		<@dp.AAAPolicyUsernameToken2None
			name="${sblUTBUTIUsernameTokenPolicyName}"
			<#-- this policy share AAA xml file with the LTPA2None policy above -->
			aaaFileName="local:///aaa/${sblUTBUTIAAAPolicyName}.xml"
			auLdapHost="@frontsideLDAPHost@"
			auLdapPort="@frontsideLDAPPort@"
			auLdapBindDN="@frontsideLDAPBindDN@"
			auLdapBindPwd="@frontsideLDAPBindPwd@"
			auLdapSearchParameters="${ldapSearchParametersName}"/>
		
		
		<#-- Request-rule to add the AAAPolicy for services on UTI-->
		<@dp.ProcessingRequestRule
			name="${sblUTBUTIPolicyName}ProcessingPolicy"
			actions=[
			{"type":"conditional", "name":"conditionalAuthenticationAction",
						"input":"INPUT",
						"conditions":[
							{
								"expression":"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Security']/*[local-name()='BinarySecurityToken']",
								"conditionAction":{"type":"aaa", "name":"aaaLTPAConditionalAction", "input":"INPUT", "output":"aaaOutput", "policy":"${sblUTBUTIAAAPolicyName}"}
							},
							{
								<#-- this condition matches everything other than LTPA, but the policy only allows UsernameToken -->
								"expression":"*",
								"conditionAction":{"type":"aaa", "name":"aaaUsernameTokenConditionalAction", "input":"INPUT", "output":"aaaOutput", "policy":"${sblUTBUTIUsernameTokenPolicyName}"}
							}
						]}, 
			{"type":"xform", "name":"StripSecHeadersAction", "async":"off",
						"input":"aaaOutput",	"output":"PIPE",
						"stylesheet":"store:///strip-security-header.xsl",
						"params":[]},			
			{"type":"result", "name":"resultAction", 
				"input":"PIPE","output":"OUTPUT"}
		]/>

		<@dp.WSStylePolicy
		name="${sblUTBUTIPolicyName}ProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"${sblUTBUTIPolicyName}ProcessingPolicy_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_error-rule"}
		]/>
		
		<@dp.BacksideSSL
			name="SBLUTBUTI"
			trustedCerts=[{"name":"@SBLUTBUTITrustCertName@","file":"@SBLUTBUTITrustCertName@.cer"}
		]/>
			
		<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
			<#list SBLUTBUTIProxies as proxy>
				<#list proxy.wsdls as wsdl>	
					<@dp.WSProxyStaticBackend
							<!-- name is set to SBLUTBUTI instead of using the proxyName from the wsdl -->
							<!-- due to a less obvious name being used in the current version of the UTI wsdl -->
							name="SBLUTBUTI"
							version="${cfgVersion}"
							wsdlName="${wsdl.fileName}"
							wsdlLocation="local:///wsdl/${wsdl.fileName}"
							wsdlPortBinding=wsdl.portBinding
							policy="${sblUTBUTIPolicyName}ProcessingPolicy"
							frontsideHandler="@frontsideHandler@"
							frontsideProtocol="${frontsideProtocol}"
							frontsideUri="${wsdl.endpointURI}"
							backsideProtocol="${SBLUTBUTIbacksideProtocol}"
							backsideHost="@SBLUTBUTIbacksideHost@"
							backsidePort="@SBLUTBUTIbacksidePort@"
							backsideUri="${wsdl.endpointURI}"
							backsideSSLProxy="SBLUTBUTI_SSLProxyProfile"
							backsideTimeout="@inboundBacksideConnectionTimeout@"
							/>					
			</#list>
		</#list>
	
	<#-- Proxy for calls to TMS (tilbakemelding for sykemeldere) from Internsone.  -->	
	
		<@dp.BacksideSSL
			name="TMS"
			trustedCerts=[{"name":"@TMSTrustCertName@","file":"pubcert:///@TMSTrustCertName@.cer"}
		]/>	
	
		<#-- AAAPolicy for TMS-proxies. Using UsernameToken backend-->
		<@dp.AAAPolicyUsernameToken2UsernameToken
			name="${TMSUsernameTokenPolicyName}"
			auLdapHost="@frontsideLDAPHost@"
			auLdapPort="@frontsideLDAPPort@"
			auLdapBindDN="@frontsideLDAPBindDN@"
			auLdapBindPwd="@frontsideLDAPBindPwd@"
			auLdapSearchParameters="${ldapSearchParametersName}"/>
	
		<@dp.ProcessingRequestRule
			name="${TMSPolicyName}ProcessingPolicy"
			actions=[
			{"type":"aaa", "name":"aaaUsernameTokenAction", "input":"INPUT", "output":"aaaOutput", "policy":"${TMSUsernameTokenPolicyName}"				
			}, 			
			{"type":"result", "name":"resultAction", 
				"input":"aaaOutput","output":"OUTPUT"}
		]/>	
	
		<@dp.WSStylePolicy
		name="${TMSPolicyName}ProcessingPolicy"
		policyMapsList=[
			{"matchingRule":"${defaultMatchAll}","processingRule":"${TMSPolicyName}ProcessingPolicy_request-rule"},
			{"matchingRule":"${defaultMatchAll}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_error-rule"}
		]/>
	
		<#-- Loop through the specified list of WSDLs and create one WSproxy for each WSDL -->
			<#list TMSProxies as proxy>
				<#list proxy.wsdls as wsdl>	
					<@dp.WSProxyStaticBackend
							name="TMS${wsdl.proxyName}"
							version="${cfgVersion}"
							wsdlName="${wsdl.fileName}"
							wsdlLocation="local:///wsdl/${wsdl.fileName}"
							wsdlPortBinding=wsdl.portBinding
							policy="${TMSPolicyName}ProcessingPolicy"
							frontsideHandler="@frontsideHandler@"
							frontsideProtocol="${frontsideProtocol}"
							frontsideUri="@TMSbacksideURI@${wsdl.endpointURI}"
							backsideProtocol="@TMSbacksideProtocol@"
							backsideHost="@TMSbacksideHost@"
							backsidePort="@TMSbacksidePort@"
							backsideUri="@TMSbacksideURI@${wsdl.endpointURI}"
							backsideSSLProxy="TMS_SSLProxyProfile"
							backsideTimeout="@inboundBacksideConnectionTimeout@"
							/>					
			</#list>
		</#list>	
	
	
	<#-- Proxy for bestillFrikortBrev on Frikortkjernen -->
	
	<#-- The xml file defining the AAA policy as well as the wsdl file for Frikort is provided during deployment-->
	<#-- rather than during build (as is the case with the other proxies). In addition, Frikort proxy and policy -->
	<#-- still depends on an excessive amount of variables defined in the environment properties files that should -->
	<#-- have been defined as variables inside this code instead. Finally, there should be added a policy for Frikort --> 
	<#-- within the pom.xml for secgw in similar fashion as for the others -->
		
		<@dp.AAAPolicyLTPA2None
			name="FrikortESAaaPolicy"
			aaaFileName="local:///aaa/@FrikortESAaaFileName@"
			auLtpaKeyFile="local:///aaa/@frontsideLTPAKeyFile@" 
			auLtpaKeyFilePwd="@frontsideLTPAKeyPwd@"
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
			{"matchingRule":"${defaultMatchAll}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_response-rule"}
			{"matchingRule":"${defaultMatchAllErrors}","processingRule":"${navDefaultPolicyName}DefaultProcessingPolicy_error-rule"}
		]/>
	
		<@dp.BacksideSSL
			name="FrikortES"
			trustedCerts=[{"name":"@FrikortESTrustCertName@","file":"pubcert:///@FrikortESTrustCertName@.cer"}
		]/>
		
		<@dp.WSProxyStaticBackend
			name="FrikortEksternService"
			version="${cfgVersion}"
			wsdlName="@FrikortESWsdl@"
			wsdlLocation="local:///wsdl/@FrikortESWsdl@"
			wsdlPortBinding=["{http://ekstern.ws.frikort.nav.no}EksternService"]
			policy="FrikortESProcessingPolicy"
			frontsideHandler="@frontsideHandler@"
			frontsideProtocol="${frontsideProtocol}"
			frontsideUri="@FrikortESURI@"
			backsideProtocol="${FrikortESBacksideProtocol}"
			backsideHost="@FrikortESBacksideHost@"
			backsidePort="@FrikortESBacksidePort@"
			backsideUri="@FrikortESURI@"
			backsideSSLProxy="FrikortES_SSLProxyProfile"
			backsideTimeout="@inboundBacksideConnectionTimeout@"
		/>	
		
</@dp.configuration>
