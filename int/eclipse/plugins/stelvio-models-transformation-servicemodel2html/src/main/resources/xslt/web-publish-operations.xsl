<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xmi="http://www.omg.org/XMI"
	xmlns:futil="no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:dutil="no.stelvio.esb.models.transformation.servicemodel2html.utils.DateUtils" version="1.0"
	xmlns:str="http://exslt.org/strings"
	exclude-result-prefixes="#default">

	<xsl:output method="html" indent="yes"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />

	<!-- Required input parameters for the transformation -->
	<xsl:param name="serviceInterfaceUUID" />
	<xsl:param name="serviceInterfaceName" />

	<xsl:param name="serviceOperationUUID" />
	<xsl:param name="serviceOperationName" />

	<xsl:param name="outputDirPath" />
	<xsl:param name="currentFilePath" />

	<!-- Document Root: calls serviceInterface and serviceOperation templates 
		(menu and content mode) -->
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="no" lang="no">
			<head>
				<title>
					NAV Tjenestebuss - tjenestespesifikasjon for
					<xsl:value-of select="$serviceOperationName" />
				</title>
				<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
				<meta name="description" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="keywords" content="NAV Tjenestebuss, tjenestespesifikasjon" />
				<meta name="robots" content="index, follow" />
				<link rel="stylesheet" type="text/css" media="screen" href="{futil:getRelativePathToCssDir($outputDirPath, $currentFilePath)}/screen.css" />
				<link rel="icon" type="image/x-icon" href="{futil:getRelativePathToImagesDir($outputDirPath, $currentFilePath)}/favicon.ico" />
				<script type="text/javascript" src="{futil:getRelativePathToJavascriptDir($outputDirPath, $currentFilePath)}/domcollapse.js">{}</script>
			</head>
			<body>

				<div id="header">
					<h1><a href="{futil:getLinkToFrontpage($outputDirPath, $currentFilePath)}">NAV Tjenestebuss</a></h1>
				</div>

				<div class="colmask threecol">
				<xsl:variable name="serviceOperation" select="//serviceOperations[@UUID=$serviceOperationUUID]"></xsl:variable>
					<div class="colmid">
						<div class="colleft">
							<!-- Middle column -->
							<div class="col1">
								<xsl:apply-templates
									select="$serviceOperation" mode="content">
									<xsl:sort select="@name" />
								</xsl:apply-templates>
							</div>

							<!-- Left column -->
							<div class="col2">
								<xsl:apply-templates
									select="//serviceInterface[@UUID=$serviceInterfaceUUID]" mode="menu">
									<xsl:sort select="@name" />
								</xsl:apply-templates>
							</div>

							<!-- Right column -->
							<div class="col3">
							
								<!-- EIS-1599: Diagrammer og vedlegg tas ut: -->
								<!--
								<h3>Diagrammer:</h3>
								<xsl:apply-templates select="$serviceOperation" mode="createDiagramList" />
								<h3>Vedlegg:</h3>
								<xsl:apply-templates select="$serviceOperation" mode="createAttachmentList" />
								-->
								<h3>Endringslogg:</h3>
								<xsl:apply-templates select="$serviceOperation/changelog"  >
								<xsl:sort select="@version" order="descending"/>
								<xsl:sort select="@date" order="descending"/>
								</xsl:apply-templates>
							</div>
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

	<!-- Menu: service interfaces -->
	<xsl:template match="serviceInterface" mode="menu">
		<div id="menu">
			<ul>
				<li>
					<a>
						<xsl:attribute name="href"><xsl:value-of
							select="@name" />.html</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
					<ul>
						<xsl:apply-templates select="./serviceOperations"
							mode="menu">
							<xsl:sort select="@name" />
						</xsl:apply-templates>
					</ul>
				</li>
			</ul>
		</div>
	</xsl:template>

	<!-- Menu: service interface operations -->
	<xsl:template match="serviceOperations" mode="menu">
		<li>
			<xsl:choose>
				<xsl:when test="@UUID = $serviceOperationUUID">
					<a class="selected-menu-item" title="{@name}">
						<xsl:attribute name="href"><xsl:value-of select="@name" />.html</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a title="{@name}">
						<xsl:attribute name="href"><xsl:value-of select="@name" />.html</xsl:attribute>
						<xsl:value-of select="@name" />
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>

	<!-- Content: service operation name, description and metadata. Calls inputMessage, 
		outputMessage og faults template -->
	<xsl:template match="serviceOperations" mode="content">
		<p id="heading-descr">Tjenesteoperasjon:</p>
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
				<th scope="row">Id</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@id" />
				</td>
			</tr>
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
			
			<!-- EIS-1599: Kun Id, versjon og navnerom beholdes av metadata, resten fjernes (dokumenteres i confluence) -->
			<!--
			<tr>
				<th scope="row">Produsent</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@producerComponent" />
				</td>
			</tr>
			<tr>
				<th scope="row">Produsent referanseid</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@producerServiceRef" />
				</td>
			</tr>
			<tr>
				<th scope="row">Tjenestekategori</th>
				<td>
					<xsl:value-of select="./serviceMetadata/serviceCategory/@name" />
					<xsl:if
						test="./serviceMetadata/serviceCategory/@name = 'Informasjonstjeneste'">
						(
						<xsl:value-of select="./serviceMetadata/serviceCategory/@function" />
						)
					</xsl:if>
				</td>
			</tr>
			<tr>
				<th scope="row">Scope</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@scope" />
				</td>
			</tr>
			<tr>
				<th scope="row">Endrer tilstand</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@stateManagement" />
				</td>
			</tr>
			<tr>
				<th scope="row">Transaksjonshåndtering</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@transactions" />
				</td>
			</tr>
			<tr>
				<th scope="row">Responstid</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@responseTime" />
				</td>
			</tr>
			<tr>
				<th scope="row">Volumkapasitet</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@volumeCapacity" />
				</td>
			</tr>
			<tr>
				<th scope="row">Oppetid</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@uptime" />
				</td>
			</tr>
			<tr>
				<th scope="row">Sikkerhet</th>
				<td>
					<xsl:value-of select="./serviceMetadata/@security" />
				</td>
			</tr>
			-->
		</table>
		
		<!-- EIS-1599: Behandlingsregler fjernes (dokumenteres i confluence) -->
		<!--
		<h2>Behandlingsregler</h2>
		<xsl:value-of select="@behaviourRules" disable-output-escaping="yes"/>
		-->
		
		<h2>Tjenestegrensesnitt</h2>
		<h3>Input</h3>
		<xsl:apply-templates select="./inputMessage" mode="content" />
		<h3>Output</h3>
		<xsl:apply-templates select="./outputMessage" mode="content" />
		<h3>Deklarerte feiltyper</h3>

		<xsl:choose>
			<xsl:when test="./faults">
				<p>
					<xsl:value-of select="@errorHandling" />
				</p>
				<table class="table-horizontal-header table-output">
					<caption>
						<xsl:value-of select="@name" />
						: feiltyper
					</caption>
					<thead>
						<tr>
							<th>ID</th>
							<th>Navn</th>
							<th>Feiltype</th>
							<th>Produsent referanseid</th>
							<th>Beskrivelse</th>
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates select="./faults" mode="content" />
					</tbody>
				</table>
			</xsl:when>
			<xsl:otherwise>
				Denne tjenesten har ingen deklarerte feiltyper.
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<!-- Content: parameter table for input message (complex type) -->
	<xsl:template match="inputMessage" mode="content">
		<p>
			<xsl:value-of select="@description"  disable-output-escaping="yes"/>
		</p>
		<table class="table-horizontal-header table-input">
			<xsl:variable name="complexTypeUUID" select="@typeRef" />
			<caption>
				<xsl:value-of select="../@name" />
				input:
				<xsl:value-of select="//complexTypes[@UUID=$complexTypeUUID]/@name" />
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
				<xsl:for-each select="//complexTypes[@UUID=$complexTypeUUID]/attributes">
					<xsl:sort select="@name" />
					<tr>
						<td>
							<xsl:value-of select="@name" />
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="@typeRef">
									<xsl:call-template name="createTypeLink">
										<xsl:with-param name="typeUUID">
											<xsl:value-of select="@typeRef" />
										</xsl:with-param>
									</xsl:call-template>
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
						<td>
							<xsl:value-of select="@description"  disable-output-escaping="yes"/>
						</td>
						
						<!-- EIS-1599: Mapping utgår -->
						<!--
						<td>
							<xsl:value-of select="@mappingToAttributeName" disable-output-escaping="yes"/>
						</td>
						-->
					</tr>
				</xsl:for-each>
			</tbody>

		</table>
	</xsl:template>

	<!-- Content: parameter table for output message (complex type) -->
	<xsl:template match="outputMessage" mode="content">
		<xsl:variable name="complexTypeUUID" select="@typeRef" />

		<xsl:choose>
			<xsl:when test="//complexTypes[@UUID=$complexTypeUUID]/attributes">
				<p>
					<xsl:value-of select="@description"  disable-output-escaping="yes"/>
				</p>
				<table class="table-horizontal-header table-output">
					<caption>
						<xsl:value-of select="../@name" />
						output:
						<xsl:value-of select="//complexTypes[@UUID=$complexTypeUUID]/@name" />
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
						<xsl:for-each select="//complexTypes[@UUID=$complexTypeUUID]/attributes">
							<xsl:sort select="@name" />
							<tr>
								<td>
									<xsl:value-of select="@name" />
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="@typeRef">
											<xsl:call-template name="createTypeLink">
												<xsl:with-param name="typeUUID">
													<xsl:value-of select="@typeRef" />
												</xsl:with-param>
											</xsl:call-template>
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
								<td>
									<xsl:value-of select="@description"  disable-output-escaping="yes"/>
								</td>
								
								<!-- EIS-1599: Mapping utgår -->
								<!--
								<td>
									<xsl:value-of select="@mappingToAttributeName" disable-output-escaping="yes"/>
								</td>
								-->
								
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
			</xsl:when>
			<xsl:otherwise>
				Denne tjenesten har ingen output
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Content: faults table -->
	<xsl:template match="faults" mode="content">
		<xsl:variable name="complexType" select="//complexTypes[@UUID=./@typeRef]" />
		<tr>
			<!-- Row: fault id -->
			<td>
				<xsl:value-of select="@id" />
			</td>
			<!-- Row: fault name (complex type name) -->
			<td>
				<xsl:choose>
					<xsl:when test="@typeRef">
						<xsl:call-template name="createTypeLink">
							<xsl:with-param name="typeUUID">
								<xsl:value-of select="@typeRef" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$complexType/@name" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- Row: fault type -->
			<td>
				<xsl:value-of select="@faultType" />
			</td>
			<!-- Row: producer fault referance -->
			<td>
				<xsl:value-of select="@producerFaultRef" />
			</td>
			<!-- Row: fault description -->
			<td>
				<xsl:value-of select="$complexType/@description"  disable-output-escaping="yes" />
			</td>
		</tr>
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
	
	<!-- Iterates list of referenced diagram UUIDs for service operation and calls 
		diagrams-template mode="createLink" -->
	<xsl:template match="serviceOperations" mode="createDiagramList">
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

	<!-- Iterates list of referenced diagram UUIDs for service operation and calls 
		diagrams-template mode="createLink" -->
	<xsl:template match="serviceOperations" mode="createAttachmentList">
		<xsl:if test="./attachments">
			<ul>
				<xsl:for-each select="./attachments">
					<li>
						<xsl:apply-templates select="." mode="createLink" />
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>		
	</xsl:template>
	
	<!-- Creates an link to the Diagram files for the current service operation -->
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
	
	<!-- Creates an link to the Attachment files for the current service operation -->
	<xsl:template match="attachments" mode="createLink">
		<xsl:variable name="attachmentLink"
			select="futil:getLinkToAttachment($outputDirPath, $currentFilePath, @FilePath)" />
		<a title="{@Name}">
			<xsl:attribute name="href">
				<xsl:value-of select="$attachmentLink" />
			</xsl:attribute>
			<xsl:value-of select="@Name" />
		</a>
	</xsl:template>
	
	<!-- Writes changelog -->
	<xsl:template match="changelog">
		<div class="trigger">
			<a href="#">v.<xsl:value-of select="@version" /> - <xsl:value-of select="@editor" /> (<xsl:value-of select="@date" />)</a>
		</div>
		<div class="hide">
			<xsl:value-of select="@description" disable-output-escaping="yes"/>
		</div>
	</xsl:template>

</xsl:stylesheet>
	
