<#if "${env?substring(1)}" == "">
    <#assign envPort="50"/>
    <#else>
        <#if "${envNr?length}" == "1">
            <#assign envPort="0${"${env?substring(1)}"}"/>
        <#else>
            <#assign envPort="${env?substring(1)}" />
        </#if>
</#if>
