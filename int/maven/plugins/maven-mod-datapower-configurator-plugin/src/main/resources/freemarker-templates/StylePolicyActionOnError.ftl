 <#macro StylePolicyActionOnError name errorRule>
    <StylePolicyAction name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
        <mAdminState>enabled</mAdminState>
        <Type>on-error</Type>
        <NamedInOutLocationType>default</NamedInOutLocationType>
        <ErrorMode>abort</ErrorMode>
        <Rule>${errorRule}</Rule>
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