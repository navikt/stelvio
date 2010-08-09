<%@page import="no.stelvio.common.systemavailability.*"%>  
<%-- tpl:insert page="/theme/JSP-C-03_blue_.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://www.ibm.com/siteedit/sitelib" prefix="siteedit"%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">

<%-- tpl:put name="headarea" --%>
<title>System Availability Record</title>
<%-- /tpl:put --%>

<LINK rel="stylesheet" href="/StelvioSystemAvailabilityWeb/theme/C_master_blue_.css" type="text/css">

<!--  Matching stylesheet for JSF components, use instead of 'stylesheet.css' or remove the reference -->
<LINK rel="stylesheet" href="/StelvioSystemAvailabilityWeb/theme/C_stylesheet_blue.css" type="text/css">

</head>

<body>
<!-- start header area -->
<div class="topAreaBox"></div>
<div class="topAreaLogo"><a href="/StelvioSystemAvailabilityWeb/sa.jsp"><img src="/StelvioSystemAvailabilityWeb/theme/sample-logo-2.png" alt="return to home page" border="0" /></a>
<a href="#navskip"><IMG src="/StelvioSystemAvailabilityWeb/theme/1x1.gif" alt="skip to page's main contents" border=0 width="1" height="1"></a></div>
<!-- end header area -->

<!-- start header navigation bar -->
<div class="topNavBk"></div>
<div class="topNav"><siteedit:navbar target="home,topchildren" spec="/StelvioSystemAvailabilityWeb/theme/nav_horizontal_text_head_.jsp"></siteedit:navbar></div>
<!-- Optional secondary header navigation area.  To use:
	 1.  Define a group in the Site Navigation Map
	 2.  Set the following navbar to use the group as its Destination Group -->
<siteedit:navbar spec="/StelvioSystemAvailabilityWeb/theme/nav_vertical_list_head_.jsp" group="group2"></siteedit:navbar>
<!-- end header navigation bar bar -->

<!-- start main content area -->
<div class="mainWideBox"><a name="navskip"><IMG border="0" src="/StelvioSystemAvailabilityWeb/theme/1x1.gif" width="1" height="1" alt="Beginning of page content"></a><%-- tpl:put name="bodyarea" --%>


<%
	SystemAvailabilityStorage storage = new SystemAvailabilityStorage();
	OperationAvailabilityRecord rec = null;
	String operation = (String)request.getParameter("operation");
	AvailabilityRecord sRec = storage.getAvailabilityRecord(request.getParameter("systemName"));
	if (operation == null) operation="";
	
	if (operation.equals("add")) {
		rec = new OperationAvailabilityRecord();
		rec.unAvailable = false;
		rec.recordStubData = false;
		rec.stubbed = false;
		rec.operationName = "";
		rec.unavailableReason = "";
	}
	
	else if (null != request.getParameter("operationName")) {
		rec = sRec.findOrCreateOperation(request.getParameter("operationName"));

		if (operation.equals("modify")) {
			if (rec == null) {
				rec = new OperationAvailabilityRecord();
			}
			//rec.systemName=(String)request.getParameter("systemName");
			rec.operationName = (String)request.getParameter("operationName");
			
			if (request.getParameter("unAvailable") != null && ((String)(request.getParameter("unAvailable"))).equals("unAvailable")) {
				rec.unAvailable = true;
			} else {
				rec.unAvailable = false;
			}
			
			if (request.getParameter("unavailableReason") != null) {
				rec.unavailableReason = (String)request.getParameter("unavailableReason");
			}

			if (request.getParameter("stubbed") != null && ((String)(request.getParameter("stubbed"))).equals("stubbed"))  {
				rec.stubbed = true;
			} else {
				rec.stubbed = false;
			}
			
			if (request.getParameter("recordStubData") != null && ((String)(request.getParameter("recordStubData"))).equals("recordStubData")) {
				rec.recordStubData = true;
			} else {
				rec.recordStubData = false;
			}
			
			storage.storeAvailabilityRecord(sRec);
			%>
			<font color=red>Saved at <%=new java.util.Date().toString()%></font>
			<%
		}
		if (operation.equals("Delete")) {
			sRec.deleteOperationAvailabilityRecord(request.getParameter("operationName"));
			storage.storeAvailabilityRecord(sRec);
		}
		
		rec = sRec.findOperation(request.getParameter("operationName"));	
	}
	
	if (rec != null) {
		
		
		%>
		<FORM action="oprec.jsp" METHOD=POST>
			<input type="hidden" name="operation" value="modify"><br>
			<input type="hidden" name="systemName" value="<%=sRec.systemName%>">
			<table>
				<tr>
					<td>Operation Name: </td>
					<td><input name="operationName" type="text" value="<%=rec.operationName%>" size="32"></td>
				</tr>
				<tr>
					<td>Currently Unavailable: </td>
					<td><input name="unAvailable" type="checkbox" value="unAvailable" <%=rec.unAvailable?"Checked":""%>></td>
				</tr>
				<tr>
					<td>Downtime Reason: </td>
					<td><input name="unavailableReason" type="text" value="<%=rec.unavailableReason %>" size="32"></td>
				</tr>
				<tr>
					<td>Stubbed: </td>
					<td><input name="stubbed" type="checkbox" value="stubbed" <%=rec.stubbed?"Checked":""%>></td>
				</tr>
				<tr>
					<td>Record stub data: </td>
					<td><input name="recordStubData" type="checkbox" value="recordStubData" <%=rec.recordStubData?"Checked":""%>></td>
				</tr>
			</table>
			
			<br>
			<input type="submit" value="Save">	<br>
		</FORM>
		<FORM action="oprec.jsp" METHOD=POST>
			<input type="hidden" name="operation" value="Delete">
			<input type="hidden" name="systemName" value="<%=sRec.systemName%>">
			<input type="hidden" name="operationName" value="<%=rec.operationName%>">
			<input type="submit" value="Delete">
		</FORM>
	<%	
	}
	
	// rec er null
	else if (operation.equals("Delete")) {
		%> Record for operation <%=request.getParameter("operationName")%> is deleted <%
	}
	
	if (sRec != null) {
%>

<br>
<Form action="sarec.jsp" METHOD=POST>
	<input type="hidden" name="systemName" value="<%=sRec.systemName%>">
	<input type="submit" value="Back to operation overview">
</Form>

<%
}

else { %>
	<b>No SystemName found in parameter...</b><br />
	<A href="sa.jsp">Back to overview</A>
<% } %>


<%-- /tpl:put --%>

<!-- Optional footer area.  To use the footer: 
	 1.  Define a group in the Site Navigation Map
	 2.  Set the following navbar to use the group as its Destination Group
	 3.  Set the User Data of the navbar to include the company name, copyright data, etc. -->
<div class="mainContentWidth">
<siteedit:navbar spec="/StelvioSystemAvailabilityWeb/theme/nav_horizontal_text_foot_.jsp" group="group" uservalue="&copy; 2010 NAV Pensjon" targetlevel=""></siteedit:navbar>
</div>
<!-- end footer area -->
</div>
<!-- end main content area -->
</body>
</html>
<%-- /tpl:insert --%>