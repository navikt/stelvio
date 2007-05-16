<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="sitenav" type="com.ibm.etools.siteedit.sitelib.core.SiteNavBean" scope="request"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<HTML>
<BODY>
<c:if test="${!empty sitenav.items[1]}">
<DIV class="navTrail">
	<c:out value='${sitenav.start}' escapeXml='false'/>
	<c:set var="skip" value="0" scope="request" />
	<c:forEach var="item" items="${sitenav.items}" begin="0" step="1" varStatus="status">
		<c:choose>
			<c:when test="${status.first || status.last}">
			</c:when>
			<c:otherwise>
				<c:if test="${skip == '1'}">
					<c:out value=':&nbsp;' escapeXml='false'/>
				</c:if>
				<c:out value='<A href="${item.href}" class="${sitenav.navclass}" style="${sitenav.navstyle}">${item.label}</A>' escapeXml='false'/>
				<c:set var="skip" value="1" scope="request" />
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<c:out value='${sitenav.end}' escapeXml='false'/>
	<c:if test="${skip == '1'}">
		<br />
	</c:if>
	<c:forEach var="lastitem" items="${sitenav.items}" begin="0" step="1" varStatus="status">
		<c:if test="${status.last}">
			<c:out value='<h1>${lastitem.label}</h1>' escapeXml='false'/>
		</c:if>
	</c:forEach>
</DIV>
</c:if>
</BODY>
</HTML>
