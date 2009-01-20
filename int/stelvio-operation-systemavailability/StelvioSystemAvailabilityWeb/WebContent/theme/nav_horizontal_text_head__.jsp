<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="sitenav" type="com.ibm.etools.siteedit.sitelib.core.SiteNavBean" scope="request"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<body>
<table border="0" cellspacing="0" cellpadding="0"><tbody><tr valign="top">
<c:forEach var="item" items="${sitenav.items}" begin="0" step="1" varStatus="status">
	<c:choose>
		<c:when test="${item.self || (item.ancestor && !status.first)}">
			<td><c:out value='<a class="${sitenav.navclass}" style="${sitenav.navstyle}" href="${item.href}">&#0187;&nbsp;${item.label}</a>' escapeXml='false'/></td>
		</c:when>
		<c:when test="${item.group}">
			<td><c:out value='<span class="${sitenav.navclass}" style="${sitenav.navstyle}"> ${item.label}:</span>' escapeXml='false'/></td>
		</c:when>
		<c:otherwise>
			<td><c:out value='<a class="${sitenav.navclass}" style="${sitenav.navstyle}" href="${item.href}">${item.label}</a>' escapeXml='false'/></td>
		</c:otherwise>
	</c:choose>
</c:forEach>
</tr></tbody></table>
</body>
</html>
