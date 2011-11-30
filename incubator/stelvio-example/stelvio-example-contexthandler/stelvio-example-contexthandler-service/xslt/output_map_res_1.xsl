<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="xslt/output_map_res_1.map" md5sum="feb2b04b0e71b301b0aa57d0360e4efc" version="7.0.302" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.302
*
*   Mapping file:		output_map_res_1.map
*   Map declaration(s):	output_map_res_1
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fstelvio-example-contexthandler-provider-lib%252Fno%252Fnav%252Finf%252FContextHandlerVerifier%257DcallNoWSChainResponseMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fstelvio-example-contexthandler-service%252Fno%252Fnav%252Finf%252FContextHandlerVerifier%257DcallNoWSChainResponseMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:str="http://exslt.org/strings"
    xmlns:set="http://exslt.org/sets"
    xmlns:math="http://exslt.org/math"
    xmlns:exsl="http://exslt.org/common"
    xmlns:date="http://exslt.org/dates-and-times"
    xmlns:in2="http://stelvio-example-contexthandler-provider-lib/no/nav/dataobject"
    xmlns:in="wsdl.http://stelvio-example-contexthandler-provider-lib/no/nav/inf/ContextHandlerVerifier"
    xmlns:in3="http://stelvio-example-contexthandler-provider-lib/no/nav/inf/ContextHandlerVerifier"
    xmlns:out="wsdl.http://stelvio-example-contexthandler-service/no/nav/inf/ContextHandlerVerifier"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:out2="http://stelvio-example-contexthandler-service/no/nav/dataobject"
    xmlns:io4="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:io5="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:out3="http://stelvio-example-contexthandler-service/no/nav/inf/ContextHandlerVerifier"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io6="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://stelvio-example-contexthandler-service/xslt/output_map_res_1"
    xmlns:msl="http://www.ibm.com/xmlmap"
    exclude-result-prefixes="xalan str set in msl math map exsl in2 date in3"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root wrapper template  -->
  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="msl:datamap">
        <msl:datamap>
          <dataObject>
            <xsl:attribute name="xsi:type">
              <xsl:value-of select="'out:callNoWSChainResponseMsg'"/>
            </xsl:attribute>
            <xsl:call-template name="map:output_map_res_12">
              <xsl:with-param name="body" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="body" mode="map:output_map_res_1"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:output_map_res_1">
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'out:callNoWSChainResponseMsg'"/>
      </xsl:attribute>
      <!-- a structural mapping: "in3:callNoWSChainResponse"(<Anonymous>) to "out3:callNoWSChainResponse"(<Anonymous>) -->
      <xsl:apply-templates select="in3:callNoWSChainResponse" mode="localCallNoWSChainResponseToCallNoWSChainResponse_1424655100"/>
    </body>
  </xsl:template>

  <!-- This rule represents a type mapping: "body" to "body".  -->
  <xsl:template name="map:output_map_res_12">
    <xsl:param name="body"/>
    <!-- a structural mapping: "$body/in3:callNoWSChainResponse"(<Anonymous>) to "out3:callNoWSChainResponse"(<Anonymous>) -->
    <xsl:apply-templates select="$body/in3:callNoWSChainResponse" mode="localCallNoWSChainResponseToCallNoWSChainResponse_1424655100"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "in3:callNoWSChainResponse" to "out3:callNoWSChainResponse".  -->
  <xsl:template match="in3:callNoWSChainResponse"  mode="localCallNoWSChainResponseToCallNoWSChainResponse_1424655100">
    <out3:callNoWSChainResponse>
      <!-- a structural mapping: "response"(responseObject) to "response"(responseObject) -->
      <xsl:apply-templates select="response" mode="localResponseToResponse_1690329476"/>
    </out3:callNoWSChainResponse>
  </xsl:template>

  <!-- This rule represents an element mapping: "response" to "response".  -->
  <xsl:template match="response"  mode="localResponseToResponse_1690329476">
    <response>
      <!-- a simple data mapping: "response"(string) to "response"(string) -->
      <xsl:if test="response">
        <response>
          <xsl:value-of select="response"/>
        </response>
      </xsl:if>
    </response>
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