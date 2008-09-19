<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:dp="http://www.datapower.com/extensions" extensions-Element-prefixes="dp">

  <xsl:variable name="error" select="dp:variable('var://service/error-message')"/>

  <xsl:template match="/">

    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
      <soapenv:Body>
        <soapenv:Fault>
          <faultcode>soapenv:Client</faultcode>
          <faultstring><xsl:value-of select="$error"/></faultstring>
        </soapenv:Fault>
      </soapenv:Body>
    </soapenv:Envelope>

  </xsl:template>
</xsl:stylesheet>