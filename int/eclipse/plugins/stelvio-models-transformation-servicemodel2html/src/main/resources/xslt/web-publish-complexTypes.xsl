<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xmi="http://www.omg.org/XMI"
	xmlns:futil="no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils"
	xmlns:str="http://exslt.org/strings"
	xmlns:dutil="no.stelvio.esb.models.transformation.servicemodel2html.utils.DateUtils"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0"
	exclude-result-prefixes="#default">

	<xsl:output method="html" indent="yes"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />

	<!-- Required input parameters for the transformation -->
	<xsl:param name="complexTypeUUID" />
	<xsl:param name="complexTypeName" />
	<xsl:param name="servicePackageUUID" />
	<xsl:param name="servicePackageName" />

	<xsl:param name="outputDirPath" />
	<xsl:param name="currentFilePath" />

	<!-- Document Root: calling childPackages and complexTypes templates (menu 
		and content mode) -->
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="no" lang="no">
			<head>
				<title>
					NAV Tjenestebuss - informasjonsspesifikasjon for
					<xsl:value-of select="$complexTypeName" />
				</title>
				<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
				<meta name="description" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="keywords" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="robots" content="index, follow" />
				<link rel="stylesheet" type="text/css" media="screen"
					href="{futil:getRelativePathToCssDir($outputDirPath, $currentFilePath)}/screen.css" />
				<link rel="icon" type="image/x-icon"
					href="{futil:getRelativePathToImagesDir($outputDirPath, $currentFilePath)}/favicon.ico" />
			</head>
			<body>

				<div id="header">
					<h1>
						<a href="{futil:getLinkToFrontpage($outputDirPath, $currentFilePath)}">NAV Tjenestebuss</a>
					</h1>
				</div>

				<div class="colmask twocol">
					<div class="colmid">
						<div class="colleft">
							<xsl:variable name="complexType" select="//complexTypes[@UUID=$complexTypeUUID]"></xsl:variable>
							<!-- Middle column -->
							<div class="col1">
								<xsl:apply-templates select="$complexType" mode="content" />

							</div>

							<!-- Left column -->
							<div class="col2">
								<xsl:apply-templates
									select="//childPackages[@UUID=$servicePackageUUID]" mode="menu">
									<xsl:sort select="@name" />
								</xsl:apply-templates>
							</div>

							
							<!-- Right column -->	
							<!-- EIS-1599: Diagrammer og vedlegg dokumenteres i confluence -->
							<!--
							<div class="col3">
								<p></p>
								<h3>Diagrammer:</h3>
								<xsl:apply-templates select="$complexType" mode="createDiagramList" />
								<h3>Vedlegg:</h3>
								<xsl:apply-templates select="$complexType" mode="createAttachmentList" />
							</div>
							-->
							
						</div>
					</div>
				</div>

				<div id="footer">
					<p>
						Sist oppdatert:
						<xsl:value-of select="dutil:getCurrentDateTime()" />
					</p>
				</div>

			</body>
		</html>
	</xsl:template>

	<!-- Menu: service packages -->
	<xsl:template match="childPackages" mode="menu">
		<div id="menu">
			<ul>
				<li>
					<a>
						<xsl:attribute name="href">#</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
					<ul>
						<xsl:apply-templates select="./complexTypes"
							mode="menu">
							<xsl:sort select="@name" />
						</xsl:apply-templates>
					</ul>
				</li>
			</ul>
		</div>
	</xsl:template>

	<!-- Menu: complex types -->
	<xsl:template match="complexTypes" mode="menu">
		<li>
			<xsl:choose>
				<xsl:when test="@UUID = $complexTypeUUID">
					<a class="selected-menu-item" title="{@name}">
						<xsl:attribute name="href"><xsl:value-of
							select="@name" />.html</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a title="{@name}">
						<xsl:attribute name="href"><xsl:value-of
							select="@name" />.html</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>

	<!-- Content: complex type, calling createAssociatedByObjectsList, attributes -->
	<xsl:template match="complexTypes" mode="content">
		<xsl:choose>
			<xsl:when test="@isEnumeration = 'true'">
				<p id="heading-descr">Kodetabell:</p>
			</xsl:when>
			<xsl:when test="@isFault = 'true'">
				<p id="heading-descr">Feiltype:</p>
			</xsl:when>
			<xsl:otherwise>
				<p id="heading-descr">Informasjonsobjekt:</p>
			</xsl:otherwise>
		</xsl:choose>
		<h1>
			<xsl:value-of select="@name" />
		</h1>
		<p>
			<xsl:value-of select="@description" disable-output-escaping="yes"/>
		</p>
		<h2>Metadata</h2>
		<table class="table-vertical-header table-metadata">
			<caption>
				<xsl:value-of select="@name" />
				: metadata
			</caption>
			<tr>
				<th scope="row">Versjon</th>
				<td>
					<xsl:value-of select="@version" />
				</td>
			</tr>
			<tr>
				<th scope="row">Navnerom</th>
				<td>
					<xsl:value-of select="@namespace" />
				</td>
			</tr>
		</table>
		<h2>Arver fra objekter</h2>
		<table class="table-vertical-header table-metadata" id="service-interface">
			<caption>
				<xsl:value-of select="@name" />
				: arver fra objekter
			</caption>
			<tr>
				<th scope="row">Arver fra objekter</th>
				<td>
					<xsl:call-template name="createGeneralizationList" />
				</td>
			</tr>
		</table>
		<h2>Tilknytning til objekter</h2>		
		<table class="table-vertical-header table-metadata" id="service-interface">
			<caption>
				<xsl:value-of select="@name" />
				: tilknytning til objekter
			</caption>
			<tr>
				<th scope="row">Direkte tilknytning</th>
				<td>
					<xsl:call-template name="createAssociatedByObjectsList" />
				</td>
			</tr>
		</table>
		<h2>Tilknytning til operasjoner</h2>
		<table class="table-vertical-header table-metadata" id="service-interface">
			<caption>
				<xsl:value-of select="@name" />
				: tilknytning til operasjoner
			</caption>
			<tr>
				<th scope="row">Direkte tilknytning</th>
				<td>
					<xsl:call-template name="createUsedDirectByOperationList" />
				</td>
			</tr>
			<tr>
				<th scope="row">Indirekte tilknytning</th>
				<td>
					<!-- <xsl:apply-templates select="." mode="createUsedIndirectByOperationList" 
						/> -->
				</td>
			</tr>
		</table>

		<h2>Attributter</h2>
		<table class="table-horizontal-header table-input">
			<caption>
				<xsl:value-of select="@name" />
				: attributter
			</caption>
			<thead>
				<tr>
					<th>Navn</th>
					<th>Datatype</th>
					<th>Er liste?</th>
					<th>Er påkrevd?</th>
					<th>Beskrivelse</th>
					
					<!-- EIS-1599: Mapping utgår -->
					<!--
					<th>Mapping</th>
					-->
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="./attributes" mode="content">
					<xsl:sort select="@name" />
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>

	<!-- Content: complex type attributes -->
	<xsl:template match="attributes" mode="content">
		<tr>
			<td>
				<xsl:value-of select="@name" />
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="@typeRef">
						<xsl:apply-templates select="//complexTypes[@UUID=current()/@typeRef]"
							mode="createLink" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@typeName" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="center">
				<xsl:choose>
					<xsl:when test="@isList = 'false'">
						Nei
					</xsl:when>
					<xsl:otherwise>
						Ja
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="center">
				<xsl:choose>
					<xsl:when test="@isRequired = 'false'">
						Nei
					</xsl:when>
					<xsl:otherwise>
						Ja
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="center">
				<xsl:value-of select="@description" disable-output-escaping="yes"/>
			</td>
			
			<!-- EIS-1599: Mapping utgår -->
			<!--
			<td>
				<xsl:value-of select="@mappingToAttributeName" disable-output-escaping="yes"/>
			</td>
			-->
		</tr>
	</xsl:template>

	<!-- Creates an link to the HTML file for the current complex type -->
	<xsl:template match="complexTypes" mode="createLink">
		<xsl:variable name="complexTypeLink"
			select="futil:getLinkToComplextype($outputDirPath, $currentFilePath, @namespace, @name)" />
		<a title="{@name}">
			<xsl:attribute name="href">
			<xsl:value-of select="$complexTypeLink" />
		</xsl:attribute>
			<xsl:value-of select="@name" />
		</a>
	</xsl:template>

	<!-- Creates an link to the HTML file for the current service operation -->
	<xsl:template match="serviceOperations" mode="createLink">
		<xsl:variable name="operationLink"
			select="futil:getLinkToOperation($outputDirPath, $currentFilePath, ../@namespace, ../@name, @name)" />
		<a title="{@name}">
			<xsl:attribute name="href">
			<xsl:value-of select="$operationLink" />
		</xsl:attribute>
			<xsl:value-of select="@name" />
		</a>
	</xsl:template>
	
	<!-- Creates an link to the Diagram files for the current complex type -->
	<xsl:template match="diagrams" mode="createLink">
		<xsl:variable name="diagramLink"
			select="futil:getLinkToDiagram($outputDirPath, $currentFilePath, @UUID)" />
		<a title="{@name}">
			<xsl:attribute name="href">
				<xsl:value-of select="$diagramLink" />
			</xsl:attribute>
			<xsl:value-of select="@name" /> (<xsl:value-of select="@typeName" />)
		</a>
	</xsl:template>
	
		<!-- Creates an link to the Attachment files for the current complex type -->
	<xsl:template match="attachments" mode="createLink">
		<xsl:variable name="attachmentLink"
			select="futil:getLinkToAttachment($outputDirPath, $currentFilePath, @filePath)" />
		<a title="{@name}">
			<xsl:attribute name="href">
				<xsl:value-of select="$attachmentLink" />
			</xsl:attribute>
			<xsl:value-of select="@name" />
		</a>
	</xsl:template>

	<!-- Iterates list of referenced attributes UUIDs for complex type and calls 
		complexType-template mode="createLink" -->
	<xsl:template name="createAssociatedByObjectsList">
		<xsl:if test="@referencedAttributes">
			<ul>
				<xsl:variable name="docRoot" select="/" />
				<xsl:for-each select="str:tokenize(@referencedAttributes)">
					<li>
						<xsl:variable name="attrUUID" select="." />
						<!-- calls the complexType template for creating link -->
						<xsl:apply-templates select="$docRoot//attributes[@UUID=$attrUUID]/.."
							mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>
	
	<!-- Iterates list of generelizations UUIDs for complex type and calls 
		complexType-template mode="createLink" -->
	<xsl:template name="createGeneralizationList">
		<xsl:if test="@generalizations">
			<ul>
				<xsl:variable name="docRoot" select="/" />
				<xsl:for-each select="str:tokenize(@generalizations)">
					<li>
						<xsl:variable name="genUUID" select="." />
						<!-- calls the complexType template for creating link -->
						<xsl:apply-templates select="$docRoot//complexTypes[@UUID=$genUUID]" mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<!-- Iterates list of referenced message UUIDs for complex type and calls 
		serviceOperations-templte mode="createLink" -->
	<xsl:template name="createUsedDirectByOperationList">
		<xsl:if test="@referencedMessages">
			<ul>
				<xsl:variable name="docRoot" select="/" />
				<xsl:for-each select="str:tokenize(@referencedMessages)">
					<li>
						<xsl:variable name="messageUUID" select="." />

						<!-- Since message can be used for input and output of service operation, 
							we have to check both -->
						<xsl:variable name="serviceOperation"
							select="$docRoot//inputMessage[@UUID=$messageUUID]/.. | $docRoot//outputMessage[@UUID=$messageUUID]/.." />

						<xsl:apply-templates select="$serviceOperation"
							mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="complexTypes" mode="createUsedIndirectByOperationList">
		<!-- Create operation link for direct operation usage (request or response 
			object has reference to this object) -->
		<xsl:if test="@referencedMessages">
			<xsl:call-template name="createUsedDirectByOperationList" />
		</xsl:if>

		<!-- Create operation link for indirect operation usage -->
		<xsl:if test="@referencedAttributes">
			<xsl:variable name="docRoot" select="/" />
			<xsl:for-each select="str:tokenize(@referencedAttributes)">
				<xsl:variable name="attrUUID" select="." />

				<!-- calls the complexType template for creating link -->
				<xsl:apply-templates select="$docRoot//attributes[@UUID=$attrUUID]/.."
					mode="createUsedIndirectByOperationList" />
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<!-- Iterates list of referenced diagram UUIDs for complex type and calls 
		diagrams-template mode="createLink" -->
	<xsl:template match="complexTypes" mode="createDiagramList">
		<xsl:if test="@diagrams">
			<ul>
				<xsl:variable name="docRoot" select="/" />
				<xsl:for-each select="str:tokenize(@diagrams)">
					<li>
						<xsl:variable name="diagramUUID" select="." />
						<!-- calls the complexType template for creating link -->
						<xsl:apply-templates select="$docRoot//diagrams[@UUID=$diagramUUID]"
							mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>		
	</xsl:template>

	<!-- Iterates list of referenced diagram UUIDs for complex type and calls 
		diagrams-template mode="createLink" -->
	<xsl:template match="complexTypes" mode="createAttachmentList">
		<xsl:if test="@attachments">
			<ul>
				<xsl:for-each select="@attachments">
					<li>
						<xsl:apply-templates select="." mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>		
	</xsl:template>
</xsl:stylesheet>