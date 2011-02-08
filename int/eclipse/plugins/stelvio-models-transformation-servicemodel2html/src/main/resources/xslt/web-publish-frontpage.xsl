<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xmi="http://www.omg.org/XMI"
	xmlns:futil="no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:dutil="no.stelvio.esb.models.transformation.servicemodel2html.utils.DateUtils" version="1.0"
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
								<h1>Service interfaces</h1>
								<xsl:for-each select="//serviceInterface">
									<xsl:sort order="ascending" select="@name"/>
									<h2> <xsl:value-of select="@name"/> </h2>
									<table class="table-vertical-header table-metadata">
									<tr>
										<th scope="row">Namespace</th>
										<td>
											<xsl:apply-templates select="./serviceOperations[1]" mode="createLink" />
										</td>
									</tr>
									</table>
								</xsl:for-each>
							</div>

							<!-- Left column -->
							<div class="col2">
								<p></p>
							</div>

							<!-- Right column -->
							<div class="col3">
								<p></p>
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
	
	<!-- Creates an link to the HTML file for the current service operation -->
	<xsl:template match="serviceOperations" mode="createLink">
		<xsl:variable name="operationLink"
			select="futil:getLinkToOperation($outputDirPath, $currentFilePath, ../@namespace, ../@name, @name)" />
		<a title="{@name}">
			<xsl:attribute name="href">
			<xsl:value-of select="$operationLink" />
		</xsl:attribute>
			<xsl:value-of select="@namespace" />
		</a>
	</xsl:template>
	
</xsl:stylesheet>
