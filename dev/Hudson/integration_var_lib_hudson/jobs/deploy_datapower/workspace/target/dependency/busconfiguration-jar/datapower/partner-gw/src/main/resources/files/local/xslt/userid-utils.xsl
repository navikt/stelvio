<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:str="http://exslt.org/strings"
    xmlns:func="http://exslt.org/functions"
    xmlns:regexp="http://exslt.org/regular-expressions"
    xmlns:dputils="http://nav.no/datapower-utils"
    extension-element-prefixes="dputils"
    exclude-result-prefixes="dputils func str regexp">
    
    <xsl:output method="xml" version="1.0" encoding="utf-8" indent="yes" />

    <func:function name="dputils:cn-from-dn">
        <xsl:param name="dn"/>
		<xsl:variable name="cn" select="str:split($dn,',')"/>
        <func:result select="substring-after($cn, '=')"/>
    </func:function>
    
</xsl:stylesheet>
