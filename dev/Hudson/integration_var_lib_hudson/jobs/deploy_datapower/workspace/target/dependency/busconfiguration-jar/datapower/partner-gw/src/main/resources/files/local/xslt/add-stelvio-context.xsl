<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:str="http://exslt.org/strings"
    xmlns:func="http://exslt.org/functions"
    xmlns:dputils="http://nav.no/datapower-utils"
	xmlns:dp="http://www.datapower.com/extensions"
    xmlns:regexp="http://exslt.org/regular-expressions"
	extension-element-prefixes="dp"
    exclude-result-prefixes="func str regexp">

  	<!--<xsl:include href="E:/Develop/wdp/xslt-utils/userid-utils.xsl"/>-->
  	<xsl:include href="local:///xslt/userid-utils.xsl"/>
    
    <xsl:output method="xml" version="1.0" encoding="utf-8" indent="yes" />


    <xsl:template match="@* | node()">
    	<xsl:copy>
    		<xsl:apply-templates select="@* | node()"/>
    	</xsl:copy>
    </xsl:template>
    
	<xsl:template match="//soapenv:Header">
    <!--<xsl:template match="*[local-name()='Header']/">-->
    	<soapenv:Header>
    	<xsl:copy-of select="*"/>
		<!--<xsl:variable name="dn" select="string('CN=srvPensjon,OU=ServiceAccounts,DC=test,DC=local')"/>-->
		<sc:StelvioContext xmlns:sc="http://www.nav.no/StelvioContextPropagation">
   			<userId><xsl:value-of select="dputils:cn-from-dn(dp:variable('var://context/WSM/identity/authenticated-user'))"/></userId>
   			<applicationId>TPLEV</applicationId>
   		</sc:StelvioContext>   	
    	</soapenv:Header>
    </xsl:template>

    <func:function name="func:cn-from-dn">
        <xsl:param name="dn"/>
		<xsl:variable name="cn" select="str:split($dn,',')"/>
        <func:result select="substring-after($cn, '=')"/>
    </func:function>
    
</xsl:stylesheet>

