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
	<xsl:param name="informationmodelFilePath" />
	<xsl:param name="servicemodelFilePath" />

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

				<div class="colmask" style="text-align: center;">
					<div>
						<h1>Tjenestespesifikasjoner</h1>
						<p>(Klikk på referansefiguren under for beskrivelse av tjenestespesifikasjoner)</p>
						<a href="{futil:getLinkToServicemodelFrontpage($outputDirPath, $currentFilePath)}" alt="Tjenestespesifikasjoner">
							<img src="images/esb_tjenestemodell_v0.91.jpeg" alt="Klikk her for tjenestespesifikasjoner på NAV ESB" border="0"/>
						</a>
					</div>
					<div>
						<h1>Informasjonsmodeller</h1>
						<p>(Klikk på referansefiguren under for beskrivelse av informasjonsmodellene)</p>
						<a href="{futil:getLinkToInformationmodelFrontpage($outputDirPath, $currentFilePath)}" alt="Informasjonsmodeller">
							<img src="images/esb_informasjonsmodell_v1.1.jpeg" alt="Klikk her for tjenestespesifikasjoner på NAV ESB" border="0"/>
						</a>
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
</xsl:stylesheet>
