<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="no/nav/xslt/EndreAdresseRequestBrukerMap.map" md5sum="f5f53f36b05915f610b43627cd7e7f95" version="7.0.302" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.302
*
*   Mapping file:		EndreAdresseRequestBrukerMap.map
*   Map declaration(s):	EndreAdresseRequestBrukerMap
*   Input file(s):		smo://smo/name%3Dwsdl-primary/transientContext%3D%257Bhttp%253A%252F%252Fnav-tjeneste-person%252Fno%252Fnav%252Fbo%257DlocalVariables/message%3D%257Bhttp%253A%252F%252Fnav.no%252Fvirksomhet%252Ftjenester%252Ffamilieforhold%252Fv1%257DhentFamilierelasjonerResponse/xpath%3D%252F/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/transientContext%3D%257Bhttp%253A%252F%252Fnav-tjeneste-person%252Fno%252Fnav%252Fbo%257DlocalVariables/message%3D%257Bhttp%253A%252F%252Fnav-lib-frg-sfe%252Fno%252Fnav%252Flib%252Ffrg%252Finf%257DendreAdresseRequestMsg/xpath%3D%252F/smo.xsd
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
    xmlns:in="http://nav.no/virksomhet/grunnlag/felles/v1"
    xmlns:in2="http://nav.no/virksomhet/tjenester/familieforhold/v1"
    xmlns:in3="wsdl.http://nav.no/virksomhet/tjenester/familieforhold/v1"
    xmlns:in4="http://nav.no/virksomhet/tjenester/felles/v1"
    xmlns:in5="http://nav.no/virksomhet/tjenester/familieforhold/feil/v1"
    xmlns:in6="http://nav.no/virksomhet/grunnlag/familieforhold/v1"
    xmlns:in7="http://nav.no/virksomhet/tjenester/familieforhold/meldinger/v1"
    xmlns:out="http://nav-lib-frg-sfe/no/nav/lib/frg/sfe/fault"
    xmlns:io="http://nav-tjeneste-person/no/nav/bo"
    xmlns:io2="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:out2="http://www.rtv.no/SFEData"
    xmlns:out3="wsdl.http://nav-lib-frg-sfe/no/nav/lib/frg/inf"
    xmlns:io4="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io5="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:io6="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:out4="http://nav-lib-frg-sfe/no/nav/lib/frg/inf"
    xmlns:io7="http://www.w3.org/2005/08/addressing"
    xmlns:SetStelvioContext="xalan://no.nav.java.SetStelvioContext"
    xmlns:map="http://nav-tjeneste-person/no/nav/xslt/EndreAdresseRequestBrukerMap"
    xmlns:msl="http://www.ibm.com/xmlmap"
    exclude-result-prefixes="set in msl math exsl in2 in3 date in4 xalan in5 in6 str in7 map SetStelvioContext"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root wrapper template  -->
  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="msl:datamap">
        <msl:datamap>
          <dataObject>
            <xsl:attribute name="xsi:type">
              <xsl:value-of select="'io3:ServiceMessageObject'"/>
            </xsl:attribute>
            <xsl:call-template name="map:EndreAdresseRequestBrukerMap2">
              <xsl:with-param name="smo" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="io3:smo" mode="map:EndreAdresseRequestBrukerMap"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "io3:smo" to "io3:smo".  -->
  <xsl:template match="io3:smo"  mode="map:EndreAdresseRequestBrukerMap">
    <io3:smo>
      <context>
        <transient>
          <xsl:attribute name="xsi:type">
            <xsl:value-of select="'io:localVariables'"/>
          </xsl:attribute>
          <!-- a structural mapping: "context/transient/brukerAdresse"(adresse) to "brukerAdresse"(adresse) -->
          <xsl:if test="context/transient/brukerAdresse">
            <xsl:copy-of select="context/transient/brukerAdresse"/>
          </xsl:if>
          <!-- a structural mapping: "context/transient/epsAdresse"(adresse) to "epsAdresse"(adresse) -->
          <xsl:if test="context/transient/epsAdresse">
            <xsl:copy-of select="context/transient/epsAdresse"/>
          </xsl:if>
          <!-- a structural mapping: "context/transient/epsFnrListe[1]"(fnrListe) to "epsFnrListe"(fnrListe) -->
          <xsl:if test="context/transient/epsFnrListe[1]">
            <xsl:copy-of select="context/transient/epsFnrListe[1]"/>
          </xsl:if>
          <!-- a simple data mapping: "context/transient/brukerFnr"(string) to "brukerFnr"(string) -->
          <xsl:if test="context/transient/brukerFnr">
            <brukerFnr>
              <xsl:value-of select="context/transient/brukerFnr"/>
            </brukerFnr>
          </xsl:if>
        </transient>
      </context>
      <body>
        <xsl:attribute name="xsi:type">
          <xsl:value-of select="'out3:endreAdresseRequestMsg'"/>
        </xsl:attribute>
        <out4:endreAdresse>
          <sfeEndreAdresseReq>
            <out2:sfeAjourforing>
              <out2:systemInfo>
                <!-- a simple mapping with no associated source:  to "out2:kilde"(<string>) -->
                <out2:kilde>
                  <xsl:value-of select="SetStelvioContext:setKilde()"/>
                </out2:kilde>
                <!-- a simple mapping with no associated source:  to "out2:brukerID"(Tbrukerid) -->
                <out2:brukerID>
                  <xsl:value-of select="SetStelvioContext:setBrukerID()"/>
                </out2:brukerID>
              </out2:systemInfo>
              <out2:endreAdresseLinjer>
                <!-- a simple data mapping: "context/transient/brukerFnr"(string) to "out2:offentligIdent"(Tfnr) -->
                <out2:offentligIdent>
                  <xsl:value-of select="context/transient/brukerFnr"/>
                </out2:offentligIdent>
                <!-- a simple mapping with no associated source:  to "out2:typeAdresse"(string) -->
                <out2:typeAdresse>
                  <xsl:text>TIAD</xsl:text>
                </out2:typeAdresse>
                <!-- a simple mapping with no associated source:  to "out2:datoAdresse"(DBdate) -->
                <out2:datoAdresse>
                  <xsl:value-of select="date:date-time()"/>
                </out2:datoAdresse>
                <!-- a simple mapping with no associated source:  to "out2:adresse1"(string) -->
                <out2:adresse1>
                  <xsl:text>v/Dødsbo</xsl:text>
                </out2:adresse1>
                <!-- a simple data mapping: "context/transient/brukerAdresse/adresseLinje1"(string) to "out2:adresse2"(string) -->
                <out2:adresse2>
                  <xsl:value-of select="context/transient/brukerAdresse/adresseLinje1"/>
                </out2:adresse2>
                <!-- a simple data mapping: "context/transient/brukerAdresse/adresseLinje2"(string) to "out2:adresse3"(string) -->
                <out2:adresse3>
                  <xsl:value-of select="context/transient/brukerAdresse/adresseLinje2"/>
                </out2:adresse3>
                <!-- a simple data mapping: "context/transient/brukerAdresse/postnr"(string) to "out2:postnr"(string) -->
                <out2:postnr>
                  <xsl:value-of select="context/transient/brukerAdresse/postnr"/>
                </out2:postnr>
                <!-- a simple data mapping: "context/transient/brukerAdresse/landKode"(string) to "out2:kodeLand"(string) -->
                <!-- variables for custom code -->
                <xsl:variable name="landKode" select="context/transient/brukerAdresse/landKode"/>
                <xsl:if test="$landKode">
                  <out2:kodeLand>
                    <xsl:value-of select="context/transient/brukerAdresse/landKode"/>
                  </out2:kodeLand>
                </xsl:if>
              </out2:endreAdresseLinjer>
            </out2:sfeAjourforing>
          </sfeEndreAdresseReq>
        </out4:endreAdresse>
      </body>
    </io3:smo>
  </xsl:template>

  <!-- This rule represents a type mapping: "io3:smo" to "io3:smo".  -->
  <xsl:template name="map:EndreAdresseRequestBrukerMap2">
    <xsl:param name="smo"/>
    <context>
      <transient>
        <xsl:attribute name="xsi:type">
          <xsl:value-of select="'io:localVariables'"/>
        </xsl:attribute>
        <!-- a structural mapping: "$smo/context/transient/brukerAdresse"(adresse) to "brukerAdresse"(adresse) -->
        <xsl:if test="$smo/context/transient/brukerAdresse">
          <xsl:copy-of select="$smo/context/transient/brukerAdresse"/>
        </xsl:if>
        <!-- a structural mapping: "$smo/context/transient/epsAdresse"(adresse) to "epsAdresse"(adresse) -->
        <xsl:if test="$smo/context/transient/epsAdresse">
          <xsl:copy-of select="$smo/context/transient/epsAdresse"/>
        </xsl:if>
        <!-- a structural mapping: "$smo/context/transient/epsFnrListe[1]"(fnrListe) to "epsFnrListe"(fnrListe) -->
        <xsl:if test="$smo/context/transient/epsFnrListe[1]">
          <xsl:copy-of select="$smo/context/transient/epsFnrListe[1]"/>
        </xsl:if>
        <!-- a simple data mapping: "$smo/context/transient/brukerFnr"(string) to "brukerFnr"(string) -->
        <xsl:if test="$smo/context/transient/brukerFnr">
          <brukerFnr>
            <xsl:value-of select="$smo/context/transient/brukerFnr"/>
          </brukerFnr>
        </xsl:if>
      </transient>
    </context>
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'out3:endreAdresseRequestMsg'"/>
      </xsl:attribute>
      <out4:endreAdresse>
        <sfeEndreAdresseReq>
          <out2:sfeAjourforing>
            <out2:systemInfo>
              <!-- a simple mapping with no associated source:  to "out2:kilde"(<string>) -->
              <out2:kilde>
                <xsl:value-of select="SetStelvioContext:setKilde()"/>
              </out2:kilde>
              <!-- a simple mapping with no associated source:  to "out2:brukerID"(Tbrukerid) -->
              <out2:brukerID>
                <xsl:value-of select="SetStelvioContext:setBrukerID()"/>
              </out2:brukerID>
            </out2:systemInfo>
            <out2:endreAdresseLinjer>
              <!-- a simple data mapping: "$smo/context/transient/brukerFnr"(string) to "out2:offentligIdent"(Tfnr) -->
              <out2:offentligIdent>
                <xsl:value-of select="$smo/context/transient/brukerFnr"/>
              </out2:offentligIdent>
              <!-- a simple mapping with no associated source:  to "out2:typeAdresse"(string) -->
              <out2:typeAdresse>
                <xsl:text>TIAD</xsl:text>
              </out2:typeAdresse>
              <!-- a simple mapping with no associated source:  to "out2:datoAdresse"(DBdate) -->
              <out2:datoAdresse>
                <xsl:value-of select="date:date-time()"/>
              </out2:datoAdresse>
              <!-- a simple mapping with no associated source:  to "out2:adresse1"(string) -->
              <out2:adresse1>
                <xsl:text>v/Dødsbo</xsl:text>
              </out2:adresse1>
              <!-- a simple data mapping: "$smo/context/transient/brukerAdresse/adresseLinje1"(string) to "out2:adresse2"(string) -->
              <out2:adresse2>
                <xsl:value-of select="$smo/context/transient/brukerAdresse/adresseLinje1"/>
              </out2:adresse2>
              <!-- a simple data mapping: "$smo/context/transient/brukerAdresse/adresseLinje2"(string) to "out2:adresse3"(string) -->
              <out2:adresse3>
                <xsl:value-of select="$smo/context/transient/brukerAdresse/adresseLinje2"/>
              </out2:adresse3>
              <!-- a simple data mapping: "$smo/context/transient/brukerAdresse/postnr"(string) to "out2:postnr"(string) -->
              <out2:postnr>
                <xsl:value-of select="$smo/context/transient/brukerAdresse/postnr"/>
              </out2:postnr>
              <!-- a simple data mapping: "$smo/context/transient/brukerAdresse/landKode"(string) to "out2:kodeLand"(string) -->
              <!-- variables for custom code -->
              <xsl:variable name="landKode" select="$smo/context/transient/brukerAdresse/landKode"/>
              <xsl:if test="$landKode">
                <out2:kodeLand>
                  <xsl:value-of select="$smo/context/transient/brukerAdresse/landKode"/>
                </out2:kodeLand>
              </xsl:if>
            </out2:endreAdresseLinjer>
          </out2:sfeAjourforing>
        </sfeEndreAdresseReq>
      </out4:endreAdresse>
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
