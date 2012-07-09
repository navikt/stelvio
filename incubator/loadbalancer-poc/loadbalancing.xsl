<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:math="http://exslt.org/math" xmlns:dp="http://www.datapower.com/extensions" xmlns:dpconfig="http://www.datapower.com/param/config" xmlns:xmi="http://www.omg.org/XMI" extension-element-prefixes="exsl dp" exclude-result-prefixes="dp dpconfig xmi">

	<xsl:output method="xml" encoding="utf-8" indent="yes"/>

	<!-- Main -->
	<xsl:template match="/">
	
		<xsl:attribute name="rand"><xsl:value-of select="floor(math:random() * 7)" /></xsl:attribute>
		
		<xsl:choose>
			<xsl:when test="rand=0">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:when test="rand=1">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:when test="rand=2">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:when test="rand=3">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:when test="rand=4">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:when test="rand=5">
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:when>
			<xsl:otherwise>
				<dp:set-variable name="'var://service/routing-url'" value="'mq://host:port?QueueManager=queueManager;UserName=userName;Channel=channelName;ChannelTimeout=channelTimeout;channelLimit=channelLimit;Size=maxMsgSize;MQCSPUserId=MQCSPUserID;MQCSPPassword=MQCSPPassword;queryParameters'"/>
			</xsl:otherwise>
		</xsl:choose>     

	</xsl:template>
</xsl:stylesheet>