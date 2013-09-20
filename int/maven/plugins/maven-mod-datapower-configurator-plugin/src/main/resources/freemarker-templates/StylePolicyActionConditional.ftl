<#macro StylePolicyActionConditional name input conditionActionNamePrefix conditions>
	<StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>conditional</Type>
		<Input>${input}</Input>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<Transactional>off</Transactional>
		<SOAPValidation>body</SOAPValidation>
		<SQLSourceType>static</SQLSourceType>
		<Asynchronous>off</Asynchronous>
	<#list conditions as condition>
		<Condition>
			<Expression>${condition.expression}</Expression>
			<ConditionAction>${conditionActionNamePrefix}${condition.conditionAction.name}</ConditionAction>
		</Condition>
	</#list>
		<ResultsMode>first-available</ResultsMode>
		<RetryCount>0</RetryCount>
		<RetryInterval>1000</RetryInterval>
		<MultipleOutputs>off</MultipleOutputs>
		<IteratorType>XPATH</IteratorType>
		<Timeout>0</Timeout>
	</StylePolicyAction>
</#macro>