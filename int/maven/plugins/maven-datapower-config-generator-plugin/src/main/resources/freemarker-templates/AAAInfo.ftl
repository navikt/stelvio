<#macro AAAInfo fileName frontsideUserDN backsideUserDN>
<aaa:AAAInfo xmlns:aaa="http://www.datapower.com/AAAInfo">
    <aaa:FormatVersion>1</aaa:FormatVersion>
    <aaa:Filename>${fileName}</aaa:Filename>
    <aaa:Summary/>
    <!-- Determine credential from output of the extract-identity phase. -->
    <aaa:Authenticate>
        <aaa:DN>${frontsideUserDN}</aaa:DN>
        <aaa:OutputCredential>authenticatedUser</aaa:OutputCredential>
    </aaa:Authenticate>
    <!-- Specify credential (if any) to use when there is no authenticated identity. -->
    <!-- Map credentials to different credentials. -->
    <aaa:MapCredentials>
        <aaa:InputCredential>${frontsideUserDN}</aaa:InputCredential>
        <aaa:OutputCredential>${backsideUserDN}</aaa:OutputCredential>
    </aaa:MapCredentials>
    <!-- Determine resource from output of the extract-resource phase. -->
    <!-- Authorize access to resource for credentials. -->
    <aaa:Authorize>
        <aaa:InputCredential>${backsideUserDN}</aaa:InputCredential>
        <aaa:InputResource>/*</aaa:InputResource>
        <aaa:Access>allow</aaa:Access>
    </aaa:Authorize>
</aaa:AAAInfo>
</#macro>