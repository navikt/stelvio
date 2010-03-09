<#macro AAAInfoBasicAuth fileName frontsideUsername frontsidePassword>
<aaa:AAAInfo xmlns:aaa="http://www.datapower.com/AAAInfo">
    <aaa:FormatVersion>1</aaa:FormatVersion>
    <aaa:Filename>${fileName}</aaa:Filename>
    <aaa:Summary/>
    <!-- Determine credential from output of the extract-identity phase. -->
    <aaa:Authenticate>
  		<aaa:Username>${frontsideUsername}</aaa:Username> 
		<aaa:Password>${frontsidePassword}</aaa:Password> 
  		<aaa:OutputCredential>authenticatedUser</aaa:OutputCredential> 
    </aaa:Authenticate>
    <!-- Determine resource from output of the extract-resource phase. -->
    <!-- Authorize access to resource for credentials. -->
    <aaa:Authorize>
        <aaa:InputCredential>authenticatedUser</aaa:InputCredential>
        <aaa:InputResource>/*</aaa:InputResource>
        <aaa:Access>allow</aaa:Access>
    </aaa:Authorize>
</aaa:AAAInfo>
</#macro>