<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/extensions" xmlns:str="http://exslt.org/strings" xmlns:date="http://exslt.org/dates-and-times" xmlns:dputils="http://nav.no/datapower-utils" xmlns:dpconfig="http://www.datapower.com/param/config" extension-element-prefixes="dp date" exclude-result-prefixes="dp dpconfig dputils date env str xsd">

	<xsl:output method="xml" />
	<dp:summary xmlns="">
		<operation>xform</operation>
		<description>Set log destination and define log message</description>
	</dp:summary>

	<!-- Parameter specifying the statically mounted NFS share for storing logged messages -->
	<xsl:param name="dpconfig:log-store" select="''" />
	<dp:param name="dpconfig:log-store" type="dmReference" required="true"
		reftype="NFSStaticMount" xmlns="">
		<display>NFS Log Server</display>
		<description>The NFS Log Server the log message is posted to</description>
	</dp:param>

	<!-- Parameter to append the domain name to the log file name -->
	<xsl:param name="dpconfig:domain" select="on" />
	<dp:param name="dpconfig:domain" type="dmToggle" required="true" xmlns="">
		<display>Add domain name</display>
		<description>Appends the domain name to the start the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the proxy name to the log file name -->
	<xsl:param name="dpconfig:proxy" select="on" />
	<dp:param name="dpconfig:proxy" type="dmToggle" required="true" xmlns="">
		<display>Add proxy name</display>
		<description>Appends the proxy name to the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the operation name to the log file name -->
	<xsl:param name="dpconfig:operation" select="on" />
	<dp:param name="dpconfig:operation" type="dmToggle" required="true" xmlns="">
		<display>Add operation name</display>
		<description>Appends the operation name to the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the user name to the log file name -->
	<xsl:param name="dpconfig:user" select="on" />
	<dp:param name="dpconfig:user" type="dmToggle" required="true" xmlns="">
		<display>Add user name</display>
		<description>Appends the name of the authenticated user to the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the transaction id to the log file name -->
	<xsl:param name="dpconfig:transaction" select="on" />
	<dp:param name="dpconfig:transaction" type="dmToggle" required="true" xmlns="">
		<display>Add transaction-id</display>
		<description>Appends the transaction-id to the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the message direction (request/reponse) to the log file name -->
	<xsl:param name="dpconfig:rule" select="on" />
	<dp:param name="dpconfig:rule" type="dmToggle" required="true" xmlns="">
		<display>Add transaction-rule</display>
		<description>Appends the name of the transaction-rule to the log file name</description>
		<default>on</default>
	</dp:param>

	<!-- Parameter to append the message direction (request/reponse) to the log file name -->
	<xsl:param name="dpconfig:custom" select="on" />
	<dp:param name="dpconfig:custom" type="dmString" required="true" xmlns="">
		<display>Add custom string</display>
		<description>Appends a custom string to the log file name</description>
		<default></default>
	</dp:param>

	<!-- log category the message is logged under -->
	<xsl:param name="dpconfig:LogCategory" select="'xsltmsg'"/>
	<dp:param name="dpconfig:LogCategory" type="dmReference" reftype="LogLabel" xmlns="">
		<display>Log Category</display>
		<description>The Log Category associated with the message</description>
		<default>xsltmsg</default>
	</dp:param>

	<!-- log category the message is logged under -->
	<xsl:param name="dpconfig:LogPriority" select="'info'"/>
	<dp:param name="dpconfig:LogPriority" type="dmLogLevel" xmlns="">
		<display>Log Priority</display>
		<description>The Log Priority associated with the message</description>
		<default>info</default>
	</dp:param>

	<!-- Main -->
	<xsl:template match="/">

		<!-- Build log destination url -->
		<dp:set-variable name="'var://context/log/destination'" value="concat('dpnfs://',$dpconfig:log-store)"/>
		<xsl:if test="$dpconfig:domain = 'on'">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '/', dp:variable('var://service/domain-name'))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:proxy = 'on'">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '_', dp:variable('var://service/wsm/wsdl-source'))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:operation = 'on'">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '-', substring-after(dp:variable('var://service/wsm/operation'), '}'))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:user = 'on'">
			<xsl:variable name="authenticated-user" select="dp:variable('var://context/WSM/identity/authenticated-user')"/>
			<xsl:variable name="cn" select="str:split($authenticated-user,',')"/>
			<xsl:variable name="commonName" select="substring-after($cn, '=')"/>
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '-', substring-after(str:split(dp:variable('var://context/WSM/identity/authenticated-user'),','),'='))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:transaction = 'on'">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '-', dp:variable('var://service/transaction-id'))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:rule = 'on'">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '-', dp:variable('var://service/transaction-rule-type'))"/>
		</xsl:if>
		<xsl:if test="$dpconfig:custom">
			<dp:set-variable name="'var://context/log/destination'"
				value="concat(dp:variable('var://context/log/destination'), '-', $dpconfig:custom)"/>
		</xsl:if>
		<dp:set-variable name="'var://context/log/destination'"
			value="concat(dp:variable('var://context/log/destination'), '.xml')"/>

		<!-- build log message -->
		<env:Envelope>
			<env:Body>
				<log-entry>
					<date>
						<xsl:value-of select="date:date()"/>
					</date>
					<time>
						<xsl:value-of select="date:time()"/>
					</time>
					<transaction>
						<xsl:value-of select="dp:variable('var://service/transaction-id')"/>
					</transaction>
					<type>
						<xsl:value-of select="$dpconfig:LogCategory"/>
					</type>
					<level>
						<xsl:value-of select="$dpconfig:LogPriority"/>
					</level>
					<authenticated-user>
						<xsl:value-of select="dp:variable('var://context/WSM/identity/authenticated-user')"/>
					</authenticated-user>
					<client-service-address>
						<xsl:value-of select="dp:variable('var://service/client-service-address')"/>
					</client-service-address>
					<frontside-url>
						<xsl:value-of select="dp:variable('var://service/URL-in')"/>
					</frontside-url>
					<backside-url>
						<xsl:value-of select="dp:variable('var://service/URL-out')"/>
					</backside-url>
					<wsdl-port-operation>
						<xsl:value-of select="dp:variable('var://service/wsm/service-port-operation')"/>
					</wsdl-port-operation>
					<error-message>
						<xsl:value-of select="dp:variable('var://service/error-message')"/>
					</error-message>
					<message>
						<xsl:copy-of select="."/>
					</message>
				</log-entry>
			</env:Body>
		</env:Envelope>        

	</xsl:template>
</xsl:stylesheet>