<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="xslt/input_map_req_2.map" md5sum="c36e83437364ee6eaa8238436c7d33f8" version="7.0.302" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.302
*
*   Mapping file:		input_map_req_2.map
*   Map declaration(s):	input_map_req_2
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fstelvio-example-contexthandler-service%252Fno%252Fnav%252Finf%252FContextHandlerVerifier%257DcallWSChainRequestMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fstelvio-example-contexthandler-provider-lib%252Fno%252Fnav%252Finf%252FContextHandlerVerifier%257DcallWSChainRequestMsg/xpath%3D%252Fbody/smo.xsd
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
    xmlns:in="wsdl.http://stelvio-example-contexthandler-service/no/nav/inf/ContextHandlerVerifier"
    xmlns:in2="http://stelvio-example-contexthandler-service/no/nav/dataobject"
    xmlns:in3="http://stelvio-example-contexthandler-service/no/nav/inf/ContextHandlerVerifier"
    xmlns:out="wsdl.http://stelvio-example-contexthandler-provider-lib/no/nav/inf/ContextHandlerVerifier"
    xmlns:out2="http://stelvio-example-contexthandler-provider-lib/no/nav/dataobject"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io4="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:out3="http://stelvio-example-contexthandler-provider-lib/no/nav/inf/ContextHandlerVerifier"
    xmlns:io5="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io6="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://stelvio-example-contexthandler-service/xslt/input_map_req_2"
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
              <xsl:value-of select="'out:callWSChainRequestMsg'"/>
            </xsl:attribute>
            <xsl:call-template name="map:input_map_req_22">
              <xsl:with-param name="body" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="body" mode="map:input_map_req_2"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:input_map_req_2">
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'out:callWSChainRequestMsg'"/>
      </xsl:attribute>
      <!-- a structural mapping: "in3:callWSChain"(<Anonymous>) to "out3:callWSChain"(<Anonymous>) -->
      <xsl:apply-templates select="in3:callWSChain" mode="localCallWSChainToCallWSChain_1904593985"/>
    </body>
  </xsl:template>

  <!-- This rule represents a type mapping: "body" to "body".  -->
  <xsl:template name="map:input_map_req_22">
    <xsl:param name="body"/>
    <!-- a structural mapping: "$body/in3:callWSChain"(<Anonymous>) to "out3:callWSChain"(<Anonymous>) -->
    <xsl:apply-templates select="$body/in3:callWSChain" mode="localCallWSChainToCallWSChain_1904593985"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "in3:callWSChain" to "out3:callWSChain".  -->
  <xsl:template match="in3:callWSChain"  mode="localCallWSChainToCallWSChain_1904593985">
    <out3:callWSChain>
      <!-- a structural mapping: "request"(requestObject) to "request"(requestObject) -->
      <xsl:apply-templates select="request" mode="localRequestToRequest_162636672"/>
    </out3:callWSChain>
  </xsl:template>

  <!-- This rule represents an element mapping: "request" to "request".  -->
  <xsl:template match="request"  mode="localRequestToRequest_162636672">
    <request>
      <!-- a simple data mapping: "request"(string) to "request"(string) -->
      <xsl:if test="request">
        <request>
          <xsl:value-of select="request"/>
        </request>
      </xsl:if>
    </request>
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
