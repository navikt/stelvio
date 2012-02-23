<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:dp="http://www.datapower.com/extensions"
    xmlns:dpconfig="http://www.datapower.com/param/config"
    xmlns:aaa="http://www.datapower.com/AAAInfo"
    extension-element-prefixes="dp "
    exclude-result-prefixes="dp dpconfig aaa">
    
    <xsl:param name="aaaInfoFile" select="document('customAAA.xml')"/>

	<xsl:template match="credentials">

		<xsl:variable name="userName">
			<xsl:choose>
				<xsl:when test="entry/username">
					<xsl:value-of select="entry/username"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="entry"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="mappedUser">
			<xsl:call-template name="mapUser">
				<xsl:with-param name="userName" select="$userName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:copy-of select="$mappedUser"/>
	</xsl:template>
	
	<xsl:template name="mapUser">
		<xsl:param name="userName"/>
		<!-- translate to upper case -->
		<xsl:variable name="inputUser" select="translate($userName,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
		<xsl:variable name="outputCredential">
			<xsl:for-each select="$aaaInfoFile//aaa:MapCredentials">
				<!-- translate to upper case -->
				<xsl:variable name="inputCredential" select="translate(aaa:InputCredential/text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
				<xsl:if test="$inputUser = $inputCredential">
					<xsl:copy-of select="aaa:OutputCredential/*"/>
				</xsl:if>
			</xsl:for-each>	
		</xsl:variable>
		<xsl:copy-of select="$outputCredential"/>
	</xsl:template>
		
</xsl:stylesheet>
