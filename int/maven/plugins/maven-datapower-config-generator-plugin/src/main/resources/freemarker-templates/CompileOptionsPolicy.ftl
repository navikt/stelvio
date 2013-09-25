<#macro CompileOptionsPolicy name minimumEscapingURLMap stackSize>
		<CompileOptionsPolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
			<mAdminState>enabled</mAdminState>
			<XSLTVersion>XSLT10</XSLTVersion>
			<Strict>off</Strict>
			<MinimumEscaping class="URLMap">minimumEscapingURLMap</MinimumEscaping>
			<StackSize>${stackSize}</StackSize>
			<WSIValidation>ignore</WSIValidation>
			<WSDLValidateBody>strict</WSDLValidateBody>
			<WSDLValidateHeaders>lax</WSDLValidateHeaders>
			<WSDLValidateFaults>strict</WSDLValidateFaults>
			<WSDLWrappedFaults>off</WSDLWrappedFaults>
			<WSDLStrictSOAPVersion>off</WSDLStrictSOAPVersion>
			<XACMLDebug>off</XACMLDebug>
		</CompileOptionsPolicy>
</#macro>