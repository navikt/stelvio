<#macro StylePolicyActionResult name input output async destination retryCount retryInterval>
	<StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>results</Type>
		<Input>${input}</Input>
		<Output>${output}</Output>
		<NamedInOutLocationType>default</NamedInOutLocationType>
	<#if destination != ''>
		<Destination>${destination}</Destination>
	</#if>
		<OutputType>default</OutputType>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>${async}</Asynchronous>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>${retryCount}</RetryCount>
		<RetryInterval>${retryInterval}</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
	</StylePolicyAction>
</#macro>