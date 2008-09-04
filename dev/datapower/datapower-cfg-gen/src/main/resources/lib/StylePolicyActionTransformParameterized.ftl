<#macro StylePolicyActionTransformParameterized name stylesheetName input output params>
	<StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>xform</Type>
		<Input>${input}</Input>
		<Transform>${stylesheetName}</Transform>
		<#--<TxMode>default</TxMode>-->
		<Output>${output}</Output>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<StylesheetParameters>
		<#list params as param>
			<ParameterName>{http://www.datapower.com/param/config}${param.name}</ParameterName>
			<ParameterValue>${param.value}</ParameterValue>
		</#list>
		</StylesheetParameters>
		<OutputType>default</OutputType>
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