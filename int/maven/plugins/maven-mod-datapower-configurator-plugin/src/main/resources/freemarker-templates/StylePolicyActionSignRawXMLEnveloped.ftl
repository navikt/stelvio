<#macro StylePolicyActionSignRawXMLEnveloped name input output xpath signCert signKey>

    <StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<Type>xform</Type>
		<Input>samltoken</Input>
		<Transform>store:///sign-enveloped.xsl</Transform>
		<Output>signedtoken</Output>
		<NamedInOutLocationType>default</NamedInOutLocationType>
		<StylesheetParameters>
			<ParameterName>{http://www.datapower.com/param/config}XPath</ParameterName>
			<ParameterValue>"${xpath}"</ParameterValue>
		</StylesheetParameters>
		<StylesheetParameters>
			<ParameterName>{http://www.datapower.com/param/config}c14nalg</ParameterName>
			<ParameterValue>exc-c14n</ParameterValue>
		</StylesheetParameters>
		<StylesheetParameters>
			<ParameterName>{http://www.datapower.com/param/config}keypair-cert</ParameterName>
			<ParameterValue>${signCert}</ParameterValue>
		</StylesheetParameters>
		<StylesheetParameters>
			<ParameterName>{http://www.datapower.com/param/config}keypair-key</ParameterName>
			<ParameterValue>${signKey}</ParameterValue>
		</StylesheetParameters>
		<OutputType>default</OutputType>
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
		<MethodRewriteType>GET</MethodRewriteType>
		<MethodType>POST</MethodType>
		<MethodType2>POST</MethodType2>
	</StylePolicyAction>

</#macro>