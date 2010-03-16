<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="no/stelvio/example/person/xslt/PersonNotFoundImpl.map" md5sum="b5277774a4431b9df1c8babfcbf37416" version="7.0.101" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.101
*
*   Mapping file:		PersonNotFoundImpl.map
*   Map declaration(s):	PersonNotFoundImpl
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252FV1%252FPersonService%257DgetPersonRequestMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252Fwww.stelvio.no%252Fexample%252Fperson%252FV1%252FPersonService%257DgetPerson_getPersonPersonNotFoundFaultMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:io="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io3="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io2="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io4="http://www.stelvio.no/example/person/V1"
    xmlns:io5="http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:io6="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:io7="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:io8="wsdl.http://www.stelvio.no/example/person/V1/PersonService"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io9="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://www.stelvio.no/example/person/xslt/PersonNotFoundImpl"
    exclude-result-prefixes="map xalan"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root template  -->
  <xsl:template match="/">
    <xsl:apply-templates select="body" mode="map:PersonNotFoundImpl"/>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:PersonNotFoundImpl">
    <body>
      <io5:getPersonFault1_getPersonPersonNotFoundFault>
        <!-- a simple data mapping: "io5:getPerson/getPersonRequest/id"(string) to "id"(string) -->
        <xsl:if test="io5:getPerson/getPersonRequest/id">
          <id>
            <xsl:value-of select="io5:getPerson/getPersonRequest/id"/>
          </id>
        </xsl:if>
      </io5:getPersonFault1_getPersonPersonNotFoundFault>
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
