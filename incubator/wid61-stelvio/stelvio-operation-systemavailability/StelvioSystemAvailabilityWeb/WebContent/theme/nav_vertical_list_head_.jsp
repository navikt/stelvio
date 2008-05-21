<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="sitenav"
	type="com.ibm.etools.siteedit.sitelib.core.SiteNavBean" scope="request" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<HTML>
<BODY>
<c:forEach var="item" items="${sitenav.items}" begin="0" step="1"
	varStatus="status">
	<c:choose>
		<c:when test="${status.first}">
			<div class="topNavRight">
			<ul>
			</ul>
			</div>
		</c:when>
		<c:when test="${!item.group}">
			<li><c:out
				value='<A href="${item.href}" class="${sitenav.navclass}" style="${sitenav.navstyle}">${item.label}</A>'
				escapeXml='false' /></li>
			<c:if test="${status.last}">
			</c:if>
		</c:when>
	</c:choose>
</c:forEach>
</BODY>
</HTML>
