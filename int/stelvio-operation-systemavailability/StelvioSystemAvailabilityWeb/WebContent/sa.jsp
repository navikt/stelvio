<%@page import="no.stelvio.common.systemavailability.*"%>
<%-- tpl:insert page="/theme/JSP-C-03_blue_.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://www.ibm.com/siteedit/sitelib" prefix="siteedit"%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">

<%-- tpl:put name="headarea" --%>
		<title>System Availability List</title>
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
	SystemAvailabilityStorage availStorage = new SystemAvailabilityStorage();

	// Turn on stubbing for all services
	if (request.getParameter("btnStubOn") != null) {
		availStorage.turnOnAllStubs();
	}
	
	// Turn off stubbing for all services
	else if (request.getParameter("btnStubOff") != null) {
		availStorage.turnOffAllStubs();
	}

	
	// Turn on recording for all services
	else if (request.getParameter("btnRecordOn") != null) {
		availStorage.turnOnAllRecords();
	}
	
	// Turn off recording for all services
	else if (request.getParameter("btnRecordOff") != null) {
		availStorage.turnOffAllRecords();
	}
	
	// Slette SystemName
	else if (request.getParameter("deleteSystemName") != null) {
		SystemAvailabilityStorage storage = new SystemAvailabilityStorage();
		storage.deleteAvailabilityRecord(request.getParameter("systemName"));
	}
	
	%>

		<FORM action="sarec.jsp" METHOD="POST">
			<input type="hidden" name="operation" value="add">
			<input type="submit" value="Add System availability record">
		</FORM>

		<Table border="1">
			<TBody>
				<TR>
					<TH>System Name</TH>
					<TH>Unavailable</TH>
					<TH>Reason</TH>
					<TH>Stubbed</TH>
					<TH>Record stub data</TH>
					<TH COLSPAN="2">Modify</TH>
				</TR>

				<%
					AvailabilityRecord rec;
					java.util.Iterator i = availStorage.listAvailabilityRecordSystemNames().iterator();
					int antall = 0;
					String stil = "";
					
					while (i.hasNext()) {
						rec = (AvailabilityRecord) availStorage.getAvailabilityRecord((String) i.next());
						antall++;
						stil = "pair";
						if (antall % 2 == 0) stil = "odd";
				%>
					<TR class="<%=stil%>">
						<TD><%=rec.systemName%></TD>
						<TD><%=rec.getUnavailableString()%></TD>
						<TD><%=rec.getUnavailableReasonString()%></TD>
						<TD><%=rec.getStubbedString()%></TD>
						<TD><%=rec.getRecordStubDataString()%></TD>
						<TD><FORM action="sarec.jsp" METHOD="POST">
								<input type="hidden" name="systemName" value="<%=rec.systemName%>">
								<input type="submit" value="Edit">
							</FORM>
						</TD>
						<TD>
							<FORM action="sa.jsp" METHOD="POST">
								<input type="hidden" name="systemName" value="<%=rec.systemName%>">
								<input type="submit" name="deleteSystemName" value="Delete">
							</FORM>
						</TD>
					</TR>
				<%
					}
				%>
		</TBody>		
		</Table>
		<br>
		
		<!-- Skru av/på stub/recording på alle tjenestene -->
		<FORM action="sa.jsp" METHOD="POST">
			<table style="text-align:center">
				<tr>
				
					<td><input type="submit" value="All stubs on" name="btnStubOn"></td>
					<td><input type="submit" value="All records on" name="btnRecordOn"></td>
				</tr>
				<tr>
					<td><input type="submit" value="All stubs off" name="btnStubOff"></td>
					<td><input type="submit" value="All records off" name="btnRecordOff"></td>
				</tr>
			</table>
		</FORM>
		
		<!-- Scanner serveren for deployede prod-moduler -->
		<br /><A href="scan.jsp">Scan for SystemNames in WPS deployed modules</A>
		
		<br>
		
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
