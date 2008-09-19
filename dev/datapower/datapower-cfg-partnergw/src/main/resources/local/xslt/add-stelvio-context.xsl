<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

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
  		<sc:StelvioContext xmlns:sc="http://www.nav.no/StelvioContextPropagation">
   			<sc:userId>srvPensjon</sc:userId>
   			<sc:applicationId>TPLEV</sc:applicationId>
   		</sc:StelvioContext>   	
    	</soapenv:Header>
    </xsl:template>
</xsl:stylesheet>
