<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="no/stelvio/example/person/mediation/xslt/input_map.map" md5sum="e8cff54e032117905c6bddc9392ede33" version="7.0.101" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.101
*
*   Mapping file:		input_map.map
*   Map declaration(s):	input_map
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252FV1%252FPersonService%257DgetPersonRequestMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252Fprovider%252FV1%252FPersonService%257DgetPersonRequestMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:in="http://www.stelvio.no/example/person/V1"
    xmlns:in2="http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:in3="wsdl.http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:out="http://www.stelvio.no/example/person/provider/V1/PersonService"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io4="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:io5="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:out2="wsdl.http://www.stelvio.no/example/person/provider/V1/PersonService"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io6="http://www.w3.org/2005/08/addressing"
    xmlns:out3="http://www.stelvio.no/example/person/provider/V1"
    xmlns:map="http://www.stelvio.no/example/person/mediation/xslt/input_map"
    exclude-result-prefixes="in in2 in3 map xalan"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root template  -->
  <xsl:template match="/">
    <xsl:apply-templates select="body" mode="map:input_map"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:input_map">
    <body>
      <!-- a structural mapping: "in2:getPerson"(<Anonymous>) to "out:getPerson"(<Anonymous>) -->
      <xsl:apply-templates select="in2:getPerson" mode="localGetPersonToGetPerson_602190334"/>
    </body>
  </xsl:template>

  <!-- This rule represents an element mapping: "in2:getPerson" to "out:getPerson".  -->
  <xsl:template match="in2:getPerson"  mode="localGetPersonToGetPerson_602190334">
    <out:getPerson>
      <!-- a structural mapping: "getPersonRequest"(GetPersonRequest) to "getPersonRequest"(GetPersonRequest) -->
      <xsl:apply-templates select="getPersonRequest" mode="localGetPersonRequestToGetPersonRequest_778350914"/>
    </out:getPerson>
  </xsl:template>

  <!-- This rule represents an element mapping: "getPersonRequest" to "getPersonRequest".  -->
  <xsl:template match="getPersonRequest"  mode="localGetPersonRequestToGetPersonRequest_778350914">
    <getPersonRequest>
      <!-- a simple data mapping: "id"(string) to "id"(string) -->
      <id>
        <xsl:value-of select="id"/>
      </id>
    </getPersonRequest>
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
