<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dp="http://www.datapower.com/extensions"
	xmlns:dpconfig="http://www.datapower.com/param/config"
	extension-element-prefixes="dp">

	<xsl:param name="dpconfig:log-store" select="''" />
	<dp:param name="dpconfig:log-store" type="dmURL" required="true" xmlns="">
		<display>Log Server URL</display>
		<description>
			The Log Server URL the log message is posted to
		</description>
	</dp:param>

	<xsl:template match="/">
		<!-- <dp:set-variabl name="'var://context/log/destination'"
			value="concat($dpconfig:log-store, dp:variable('var://service/domain-name'), '/', dp:variable('var://service/transaction-policy-name'), '/', substring-after(dp:variable('var://service/wsm/operation'), '}'),'-', dp:variable('var://context/WSM/identity/authenticated-user'), '-', dp:variable('var://service/transaction-id'), '-', dp:variable('var://service/transaction-rule-type'), '.xml')" />-->
		<xsl:variable name="domain" 		select="'var://service/domain-name'"/>
		<xsl:variable name="policy" 		select="'var://service/transaction-policy-name'"/>
		<xsl:variable name="operation" 		select="'var://service/wsm/operation'"/>
		<xsl:variable name="user" 			select="'var://context/WSM/identity/authenticated-user'"/>
		<xsl:variable name="transaction" 	select="'var://service/transaction-id'"/>
		<xsl:variable name="rule" 			select="'var://service/transaction-rule-type'"/>
		<dp:set-variable name="'var://context/log/destination'"
			value="concat($dpconfig:log-store, dp:variable('var://service/domain-name'), '/', dp:variable($policy), '/', substring-after(dp:variable($operation), '}'),'-', dp:variable($user), '-', dp:variable($transaction), '-', dp:variable($rule), '.xml')" />
	</xsl:template>
</xsl:stylesheet>
