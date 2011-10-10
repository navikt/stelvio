<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsa="http://www.w3.org/2005/08/addressing" xmlns:dp="http://www.datapower.com/extensions" extension-element-prefixes="dp" exclude-result-prefixes="dp">

  <xsl:template match="/">

    <!-- Extract SOAPAction-->
    <xsl:variable name="SOAPAction" select="dp:http-request-header('SOAPAction')"/>

    <!-- Extract WS-Addressing Action -->
    <xsl:variable name="WSAddressingAction" select="/soapenv:envelope/soapenv:header/wsa:Action/text()"/>

    <!-- Extract environment from URL -->
    <xsl:variable name="Environment" select="substring(dp:http-url(), 2)"/>

    <!-- Lookup endpoint -->
    <xsl:choose>
      <xsl:when test="$WSAddressingAction">
        <xsl:variable name="endpoint" select="/*[namespace-uri()='http://www.stelvio.no/ServiceRegistry' and local-name()='serviceRegistry']/service/serviceVersion[operation/@soapAction=$SOAPAction]/serviceInstance[environment=$Environment]/@endpoint" />
        <xsl:if test="not($endpoint)">
          <dp:reject>No service instance found for WS-addressing action '<xsl:value-of select="$WSAddressingAction"/>'</dp:reject>
        </xsl:if>
        <dp:set-variable name="'var://service/routing-url'" value="$endpoint"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="endpoint" select="/*[namespace-uri()='http://www.stelvio.no/ServiceRegistry' and local-name()='serviceRegistry']/service/serviceVersion[operation/@soapAction=$SOAPAction]/serviceInstance[environment=$Environment]/@endpoint" />
          <xsl:if test="not($endpoint)">
            <dp:reject>No service instance found for action '<xsl:value-of select="$SOAPAction"/>' in environment '<xsl:value-of select="$Environment"/>'</dp:reject>
          </xsl:if>
          <dp:set-variable name="'var://service/routing-url'" value="$endpoint"/>
          <!-- Extract WSDL URL -->
          <xsl:variable name="WsdlUrl" select="/*[namespace-uri()='http://www.stelvio.no/ServiceRegistry' and local-name()='serviceRegistry']/service/serviceVersion[operation/@soapAction=$SOAPAction]/serviceInstance[environment=$Environment]/@wsdlAddress"/>
          <dp:set-variable name="'var://context/wsdl/url'" value="$WsdlUrl"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>