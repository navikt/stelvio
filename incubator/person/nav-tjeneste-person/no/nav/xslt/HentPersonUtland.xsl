<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="no/nav/xslt/HentPersonUtland.map" md5sum="a22959161fd8358e1ef039e16a023ab2" version="7.0.302" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.302
*
*   Mapping file:		HentPersonUtland.map
*   Map declaration(s):	HentPersonUtland
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fnav.no%252Fvirksomhet%252Ftjenester%252Fperson%252Fv1%257DhentPersonRequest/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fnav-lib-frg-tps%252Fno%252Fnav%252Flib%252Ffrg%252Finf%257DhentTPSDataRequestMsg/xpath%3D%252Fbody/smo.xsd
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
    xmlns:in="http://nav.no/virksomhet/part/person/v1"
    xmlns:in2="http://nav.no/virksomhet/tjenester/person/v1"
    xmlns:in3="http://nav.no/virksomhet/tjenester/person/feil/v1"
    xmlns:in4="http://nav.no/virksomhet/tjenester/felles/v1"
    xmlns:in5="wsdl.http://nav.no/virksomhet/tjenester/person/v1"
    xmlns:in6="http://nav.no/virksomhet/tjenester/person/meldinger/v1"
    xmlns:out="http://nav-lib-frg-tps/no/nav/lib/frg/tps/fault"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io4="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:out2="http://nav-lib-frg-tps/no/nav/lib/frg/inf"
    xmlns:io5="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:out3="wsdl.http://nav-lib-frg-tps/no/nav/lib/frg/inf"
    xmlns:io6="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://nav-tjeneste-person/no/nav/xslt/HentPersonUtland"
    xmlns:msl="http://www.ibm.com/xmlmap"
    exclude-result-prefixes="set in msl math exsl in2 in3 date in4 xalan in5 in6 str map"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root wrapper template  -->
  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="msl:datamap">
        <msl:datamap>
          <dataObject>
            <xsl:attribute name="xsi:type">
              <xsl:value-of select="'out3:hentTPSDataRequestMsg'"/>
            </xsl:attribute>
            <xsl:call-template name="map:HentPersonUtland2">
              <xsl:with-param name="body" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="body" mode="map:HentPersonUtland"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:HentPersonUtland">
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'out3:hentTPSDataRequestMsg'"/>
      </xsl:attribute>
      <!-- a structural mapping: "in2:hentPerson"(<Anonymous>) to "out2:hentTPSData"(<Anonymous>) -->
      <xsl:apply-templates select="in2:hentPerson" mode="localHentPersonToHentTPSData_597842575"/>
    </body>
  </xsl:template>

  <!-- This rule represents a type mapping: "body" to "body".  -->
  <xsl:template name="map:HentPersonUtland2">
    <xsl:param name="body"/>
    <!-- a structural mapping: "$body/in2:hentPerson"(<Anonymous>) to "out2:hentTPSData"(<Anonymous>) -->
    <xsl:apply-templates select="$body/in2:hentPerson" mode="localHentPersonToHentTPSData_597842575"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "in2:hentPerson" to "out2:hentTPSData".  -->
  <xsl:template match="in2:hentPerson"  mode="localHentPersonToHentTPSData_597842575">
    <out2:hentTPSData>
      <!-- a structural mapping: "request"(HentPersonRequest) to "tpsPersonDataReq"(tpsPersonDataType) -->
      <xsl:apply-templates select="request" mode="localRequestToTpsPersonDataReq_1156630971"/>
    </out2:hentTPSData>
  </xsl:template>

  <!-- This rule represents an element mapping: "request" to "tpsPersonDataReq".  -->
  <xsl:template match="request"  mode="localRequestToTpsPersonDataReq_1156630971">
    <tpsPersonDataReq>
      <tpsServiceRutine>
        <!-- a simple mapping with no associated source:  to "serviceRutinenavn"(<SRnavn>) -->
        <serviceRutinenavn>
          <xsl:text>FS03-FDNUMMER-PERSUTLA-O </xsl:text>
        </serviceRutinenavn>
        <!-- a simple data mapping: "fnr"(string) to "fnr"(Tfnr) -->
        <xsl:if test="fnr">
          <fnr>
            <xsl:value-of select="fnr"/>
          </fnr>
        </xsl:if>
        <!-- a simple data mapping: "dato"(date) to "aksjonsDato"(DBdate) -->
        <xsl:if test="dato">
          <aksjonsDato>
            <xsl:value-of select="dato"/>
          </aksjonsDato>
        </xsl:if>
        <!-- a simple mapping with no associated source:  to "aksjonsKode"(<AK>) -->
        <aksjonsKode>
          <xsl:text>A</xsl:text>
        </aksjonsKode>
        <!-- a simple mapping with no associated source:  to "aksjonsKode2"(<AK2>) -->
        <aksjonsKode2>
          <xsl:text>0</xsl:text>
        </aksjonsKode2>
      </tpsServiceRutine>
    </tpsPersonDataReq>
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
