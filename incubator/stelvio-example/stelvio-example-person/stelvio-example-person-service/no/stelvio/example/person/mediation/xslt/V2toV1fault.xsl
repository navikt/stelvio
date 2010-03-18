<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="no/stelvio/example/person/mediation/xslt/V2toV1fault.map" md5sum="d79ccfd29203d4d6c6bf31b50268b26c" version="7.0.101" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.101
*
*   Mapping file:		V2toV1fault.map
*   Map declaration(s):	V2toV1fault
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252FV1%252FPersonService%257DgetPerson_getPersonPersonNotFoundFaultMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252FV2%252FPersonService%257DgetPersonResponseMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:in="http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:in2="wsdl.http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io4="http://www.stelvio.no/example/person/V1"
    xmlns:io5="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:out="http://www.stelvio.no/example/person/V2/PersonService"
    xmlns:io6="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:out2="wsdl.http://www.stelvio.no/example/person/V2/PersonService"
    xmlns:io7="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://www.stelvio.no/example/person/mediation/xslt/V2toV1fault"
    exclude-result-prefixes="in in2 map xalan"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root template  -->
  <xsl:template match="/">
    <xsl:apply-templates select="body" mode="map:V2toV1fault"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:V2toV1fault">
    <body>
      <out:getPersonResponse>
        <getPersonResponse xsi:nil="true"/>
      </out:getPersonResponse>
    </body>
  </xsl:template>

  <!-- *****************    Utility Templates    ******************  -->
  <!-- copy the namespace declarations from the source to the target -->
  <xsl:template name="copyNamespaceDeclarations">
    <xsl:param name="root"/>
    <xsl:for-each select="$root/namespace::*">
      <xsl:copy/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
