<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:dp="http://www.datapower.com/extensions" xmlns:dpconfig="http://www.datapower.com/param/config" extensions-Element-prefixes="dp">

  <dp:summary xmlns="">
    <operation>xform</operation> 
    <description>Fault handler that creates a SOAP Fault message with the internal DataPower error as the faultstring value</description> 
  </dp:summary>
  <!--  Parameter specifying the statically mounted NFS share for storing logged messages --> 
  <xsl:param name="dpconfig:fault-prefix" select="''" /> 
  <dp:param name="dpconfig:fault-prefix" type="dmString" required="true" xmlns="">
    <display>Faultstring Prefix</display> 
    <description>Appends a prefix to the value of the  fault string</description>
    <default>DataPower</default>
    <tab-override>basic</tab-override> 
    <no-save-checkbox>true</no-save-checkbox>
  </dp:param>
  
  <xsl:variable name="error" select="dp:variable('var://service/error-message')"/>

  <xsl:template match="/">

    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
      <soapenv:Body>
        <soapenv:Fault>
          <faultcode>soapenv:Client</faultcode>
          <faultstring><xsl:value-of select="concat($dpconfig:fault-prefix, ': ')"/><xsl:value-of select="$error"/></faultstring>
        </soapenv:Fault>
      </soapenv:Body>
    </soapenv:Envelope>

  </xsl:template>
</xsl:stylesheet>