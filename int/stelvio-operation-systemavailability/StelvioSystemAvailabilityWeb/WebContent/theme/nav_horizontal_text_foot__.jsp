<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="sitenav" type="com.ibm.etools.siteedit.sitelib.core.SiteNavBean" scope="request"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<HTML>
<BODY>
<c:forEach var="item" items="${sitenav.items}" begin="0" step="1" varStatus="status">
	<c:choose>
		<c:when test="${status.first}">
<div class="footer"><table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top"><td style="width: 45%"><c:out value='${sitenav.userData}' escapeXml='false' /></td>
		</c:when>
		<c:when test="${!item.group}">
<TD><c:out value='<A href="${item.href}" class="${sitenav.navclass}" style="${sitenav.navstyle}">${item.label}</A>' escapeXml='false'/></TD>
			<c:if test="${status.last}">
</tr></table></div>
			</c:if>
		</c:when>
	</c:choose>
</c:forEach>
</BODY>
</HTML>
