<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dp="http://www.datapower.com/extensions" xmlns:dpconfig="http://www.datapower.com/param/config" xmlns:xmi="http://www.omg.org/XMI" extension-element-prefixes="dp" exclude-result-prefixes="dp dpconfig xmi">

	<xsl:output method="xml" encoding="utf-8" indent="yes"/>

	<!-- Main -->
	<xsl:template match="/">
		
  		<xsl:variable name="binding" select="dp:variable('var://service/wsm/binding')" /> 
  		
  		<!-- getting the integrity for the service matching the current binding -->
  		<xsl:variable name="integrity" select="/services/service[./binding/text()=$binding]/classification/integrity/text()" />
		
		<!-- setting the logging element to true if integrity is MIDDELS or HOY -->
		<logging>
			<xsl:choose>
				<xsl:when test="$integrity = 'MIDDELS' or $integrity = 'HOY'">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</logging>        

	</xsl:template>

</xsl:stylesheet>