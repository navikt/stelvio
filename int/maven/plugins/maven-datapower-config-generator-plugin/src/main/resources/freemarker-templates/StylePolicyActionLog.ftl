<#macro StylePolicyActionLog name destination input>
	<StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>log</Type>
		<Input>${input}</Input>
		<TxMode>default</TxMode>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<Destination>${destination}</Destination>
		<OutputType>default</OutputType>
		<LogLevel>info</LogLevel>
		<LogType class="LogLabel">xsltmsg</LogType>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>on</Asynchronous>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>0</RetryCount>
		<RetryInterval>1000</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
	</StylePolicyAction>
</#macro>
