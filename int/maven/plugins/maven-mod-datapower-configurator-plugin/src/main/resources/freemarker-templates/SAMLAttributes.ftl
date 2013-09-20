<#macro SAMLAttributes name attributes>
    <SAMLAttributes name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
    <#list attributes as attribute>
        <SAMLAttribute>
            <SourceType>${attribute.type}</SourceType>
            <Name>${attribute.name}</Name>
            <Format>${attribute.format}</Format>
            <#if attribute.type == "xpath">
                <XPath>${attribute.xpath}</XPath>
                <ValueData/>
            <#else>
                <XPath/>
                <ValueData>${attribute.valueData}</ValueData>
            </#if>
            <SubValueData/>
            <FriendlyName/>
        </SAMLAttribute>
    </#list>
    </SAMLAttributes>
</#macro>