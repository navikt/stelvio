<#macro StylePolicyActionVerify name input valCred>
	<StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>filter</Type>
		<Input>${input}</Input>
		<Transform>store:///verify.xsl</Transform>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<StylesheetParameters>
			<ParameterName>{http://www.datapower.com/param/config}valcred</ParameterName>
			<ParameterValue>${valCred}</ParameterValue>
		</StylesheetParameters>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>off</Asynchronous>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>0</RetryCount>
		<RetryInterval>1000</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
	</StylePolicyAction>
</#macro>