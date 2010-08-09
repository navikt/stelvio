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
	AvailabilityRecord rec = null;
	
	String operation = (String)request.getParameter("operation");
	if (operation == null) operation = "";
	
	if (operation.equals("add")) {
		rec = new AvailabilityRecord();		
		rec.systemName = "";
	}
	else
	{
		rec = (AvailabilityRecord)storage.getAvailabilityRecord(request.getParameter("systemName"));
		
		if (operation.equals("modify")) {
			if (rec == null) {
				rec = new AvailabilityRecord();
			}
			rec.systemName = (String)request.getParameter("systemName");			
			storage.storeAvailabilityRecord(rec);
			%>
			<font color="red">Saved at <%=new java.util.Date().toString()%></font>
			<%
		}
		else if (operation.equals("Delete")) {
			storage.deleteAvailabilityRecord(request.getParameter("systemName"));
		}
		
		rec = (AvailabilityRecord)storage.getAvailabilityRecord(request.getParameter("systemName"));	
	}
	
	
	if (request.getParameter("stubbing") != null || request.getParameter("recording") != null)
	{
		rec = (AvailabilityRecord)storage.getAvailabilityRecord(request.getParameter("systemName"));
		OperationAvailabilityRecord operationRec = rec.findOrCreateOperation(request.getParameter("operationName"));
		
		String statusStub = (String) request.getParameter("stubbing");
		String statusRec = (String) request.getParameter("recording");
		
		if ("YES".equals(statusStub)) {
			operationRec.stubbed = false;
		}
		else if ("NO".equals(statusStub)) {
			operationRec.stubbed = true;
			operationRec.recordStubData = false;
		}
		else if ("PARTLY".equals(statusStub)) {
			operationRec.stubbed = true;
			operationRec.recordStubData = false;
		}
		
		if ("YES".equals(statusRec)) {
			operationRec.recordStubData = false;
		}
		else if ("NO".equals(statusRec)) {
			operationRec.recordStubData = true;
			operationRec.stubbed = false;
		}
		else if ("PARTLY".equals(statusRec)) {
			operationRec.recordStubData = true;
			operationRec.stubbed = false;
		}
		
		storage.storeAvailabilityRecord(rec);
	}
	
	
	if (rec != null) {
		
		%>
		<FORM action="sarec.jsp" METHOD="POST">
		<input type="hidden" name="operation" value="modify"><br>
		<table>
			<tr>
				<td>SystemName: </td>
				<td><input name="systemName" type="text" value="<%=rec.systemName%>" size="32"></td>
			</tr>
			<tr>
				<td>Currently Unavailable: </td>
				<td><%=rec.getUnavailableString()%></td>
			</tr>
			<tr>
				<td>Downtime Reason: </td>
				<td><%=rec.getUnavailableReasonString()%></td>
			</tr>
			<tr>
				<td>Stubbed: </td>
				<td><%=rec.getStubbedString()%></td>
			</tr>
			<tr>
				<td>Record stub data: </td>
				<td><%=rec.getRecordStubDataString()%></td>
			</tr>
		</table>

		<br>
		<p><input type="submit" value="Save"> <br>
		</p>
		</FORM>
		<FORM action="sarec.jsp" METHOD="POST">
			<input type="hidden" name="operation" value="Delete">
			<input type="hidden" name="systemName" value="<%=rec.systemName%>">
			<input type="submit" value="Delete">
		</FORM>


		<H2>Known Operations</H2>
		<TABLE border="1">
			<TR>
				<TH>Operation</TH>
				<TH>Unavailable</TH>
				<TH>Reason</TH>
				<TH style="text-align:center;">Stubbed</TH>
				<TH >Record Stub Data</TH>
				<TH></TH>
			</TR>
		<%
		java.util.Iterator i = rec.operations.iterator();// availStorage.listAvailabilityRecordSystemNames().iterator();
		int antall = 0;
		String stil = "";
		
		while (i.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord)i.next();
			antall++;
			stil = "pair";
			if (antall % 2 == 0) stil = "odd";
		%>
			<TR class="<%=stil%>">
				<TD><%=opRec.operationName%></TD>
				<TD><%=opRec.getUnavailableString()%></TD>
				<TD><%=opRec.getUnavailableReason() %></TD>
				<TD width="80px" style="text-align:center;">
					<FORM action="sarec.jsp" METHOD="POST">
						<input type="hidden" name="systemName" value="<%=rec.systemName%>">
						<input type="hidden" name="operationName" value="<%=opRec.operationName%>">
						<%
						String tekst = opRec.getStubbedString();
						if ("ALL".equals(opRec.operationName)) {
							tekst = rec.getStubbedString();
						}
						if (tekst == null || "".equals(tekst) || " ".equals(tekst)) {
							tekst = "NO";
						}
						%>
						<input type="submit" name="stubbing" value="<%=tekst%>">
					</FORM>
				</TD>
				<TD style="text-align:center;">
					<FORM action="sarec.jsp" METHOD="POST">
						<input type="hidden" name="systemName" value="<%=rec.systemName%>">
						<input type="hidden" name="operationName" value="<%=opRec.operationName%>">
						<%
						tekst = opRec.getRecordStubDataString();
						if ("ALL".equals(opRec.operationName)) {
							tekst = rec.getRecordStubDataString();
						}
						if (tekst == null || "".equals(tekst) || " ".equals(tekst)) {
							tekst = "NO";
						}
						%>
						<input type="submit" name="recording" value="<%=tekst%>">
					</FORM>
				</TD>
				<TD>
					<FORM action="oprec.jsp" METHOD="POST">
						<input type="hidden" name="systemName" value="<%=rec.systemName%>">
						<input type="hidden" name="operationName" value="<%=opRec.operationName%>">
						<input type="submit" value="Edit">
						<input type="submit" name="operation" value="Delete">
					</FORM>
				</TD>
			</TR>
			<%	
		}%>
		</TABLE>

		<%
	}
	
	// rec is null
	else if (operation.equals("Delete")) {
		%> Record for system <%=request.getParameter("systemName")%> is deleted <%
	}
	
	// Feilmelding ut
	else { %>
		<b>No SystemName found in parameter...</b><br />
	<% } %>
	
	<br>
	<A href="sa.jsp">Back to overview</A>


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
