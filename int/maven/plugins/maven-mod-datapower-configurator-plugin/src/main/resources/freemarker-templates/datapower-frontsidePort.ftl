<#assign envNr="${env?substring(1)}"/>

<#if "${env?substring(1)}" == "">
    <#assign envPort="00"/>
<#else>
    <#if "${envNr?length}" == "1">
        <#assign envPort="0${envNr}"/>
    <#else>
        <#assign envPort="${env?substring(1)}" />
    </#if>
</#if>