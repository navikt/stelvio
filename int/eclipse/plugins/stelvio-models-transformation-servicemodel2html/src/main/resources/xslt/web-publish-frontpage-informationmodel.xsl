<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xmi="http://www.omg.org/XMI"
	xmlns:futil="no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:dutil="no.stelvio.esb.models.transformation.servicemodel2html.utils.DateUtils" version="1.0"
	xmlns:tjn="http://stelvio.no/int/models/service/metamodel"
	exclude-result-prefixes="#default">

	<xsl:output method="html" indent="yes"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />

	<!-- Required input parameters for the transformation -->
	<xsl:param name="outputDirPath" />
	<xsl:param name="currentFilePath" />

	<!-- Document Root: calls serviceInterface and serviceOperation templates 
		(menu and content mode) -->
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="no" lang="no">
			<head>
				<title>NAV Tjenestebuss</title>
				<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
				<meta name="description" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="keywords" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="robots" content="index, follow" />
				<link rel="stylesheet" type="text/css" media="screen" href="{futil:getRelativePathToCssDir($outputDirPath, $currentFilePath)}/screen.css" />
				<link rel="icon" type="image/x-icon" href="{futil:getRelativePathToImagesDir($outputDirPath, $currentFilePath)}/favicon.ico" />
			</head>
			<body>

				<div id="header">
					<h1><a href="{futil:getLinkToFrontpage($outputDirPath, $currentFilePath)}">NAV Tjenestebuss</a></h1>
				</div>

				<div class="colmask threecol">
					<div class="colmid">
						<div class="colleft">
							<!-- Middle column -->
							<div class="col1">
								<h1>Informasjonsmodeller</h1>
								<div id="informsjonsmodell-container">
									<ul>
										<li class="package">no.nav
											<xsl:apply-templates select="//childPackages[../@name='no.nav' and ../../@name = 'informasjonsmodell']" mode="informationmodel"/>
										</li>
										<li class="package">no
											<xsl:apply-templates select="//childPackages[../@name='no' and ../../@name = 'informasjonsmodell']" mode="informationmodel"/>
										</li>
									</ul>
								</div>
							</div>

							<!-- Left column -->
							<div class="col2">
								<p></p>
							</div>

							<!-- Right column -->
							<div class="col3">
								<h3>Beskrivelse av ikoner:</h3>
								<ul>
									<li class="package"> = pakke</li>
									<li class="complexType"> = informasjonsobjekt</li>
									<li class="serviceInterface"> = tjenestegrensesnitt</li>
									<li class="serviceOperation"> = tjenesteoperasjon</li>
								</ul>
							</div>
						</div>
					</div>
				</div>

				<div id="footer">
					<p>
						Last updated:
						<xsl:value-of select="dutil:getCurrentDateTime()" />
					</p>
				</div>

			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="childPackages" mode="servicemodel">
		<xsl:if test="./serviceInterface or ./childPackages">
			<ul>
				<li class="package"><xsl:value-of select="@name" /></li>
				<xsl:apply-templates select="./serviceInterface"/>
				<xsl:apply-templates select="./childPackages" mode="servicemodel"/>
			</ul>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="childPackages" mode="informationmodel">
		<ul>
			<li class="package"><xsl:value-of select="@name" /></li>
			<xsl:apply-templates select="./complexTypes"/>
			<xsl:apply-templates select="./childPackages" mode="informationmodel"/>
		</ul>
	</xsl:template>
	
	<xsl:template match="complexTypes">
		<xsl:variable name="generalizationUUID" select="@generalizations"></xsl:variable>
		<xsl:variable name="generalizationComplexType" select="//complexTypes[@UUID=$generalizationUUID]" />
		<xsl:choose >
			<xsl:when test="$generalizationComplexType/@name = 'Utvidelse'">
			</xsl:when>
			<xsl:otherwise>
				<ul>
					<li class="complexType">
						<xsl:call-template name="createTypeLink">
							<xsl:with-param name="typeUUID" select="@UUID" />
						</xsl:call-template>
					</li>
				</ul>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="createTypeLink">
		<xsl:param name="typeUUID" />
		<xsl:variable name="complexName"
			select="//complexTypes[@UUID=$typeUUID]/@name" />
		<xsl:variable name="complexNamespace"
			select="//complexTypes[@UUID=$typeUUID]/@namespace" />
		<xsl:variable name="complexTypeLink"
			select="futil:getLinkToComplextype($outputDirPath, $currentFilePath, $complexNamespace, $complexName)" />
		<a title="{$complexName}">
			<xsl:attribute name="href">
			<xsl:value-of select="$complexTypeLink" />
		</xsl:attribute>
			<xsl:value-of select="$complexName" />
		</a>
	</xsl:template>
	
</xsl:stylesheet>
