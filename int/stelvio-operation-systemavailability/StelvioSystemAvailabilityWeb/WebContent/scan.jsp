<%@page import="no.stelvio.common.systemavailability.*"%>
<%@page import="java.io.File"%>
<%-- tpl:insert page="/theme/JSP-C-03_blue_.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://www.ibm.com/siteedit/sitelib" prefix="siteedit"%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">

<%-- tpl:put name="headarea" --%>
		<title>System Availability Scan</title>
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


	<SCRIPT LANGUAGE="JavaScript">			
		function check(field) {
			var allEmpty = true;
			
			for (i = 0; i < field.length; i++) {
				if (field[i].checked == true) {
					allEmpty = false;
					break;
				}
			}
			
			for (i = 0; i < field.length; i++) {
				field[i].checked = allEmpty;
			}
		}
	</SCRIPT>

	<%
	SystemAvailabilityStorage availStorage = new SystemAvailabilityStorage();
	java.util.List navListe = availStorage.listInstalledNAVModules();
	File fil;
	String app;
	
	
	// Klikket på btnModulerValgt
	if (request.getParameter("btnModulerValgt") != null && request.getParameterValues("navApp") != null) {
		%><a href="scan.jsp">Go back for choosing nav-modules again</a><br /><%
		
		// Listen inneholder index til valgte nav-moduler i navListe-objektet
		String[] navApp = request.getParameterValues("navApp");
		int index = 0;
		String form = "";
		int size = 0;
		
		for (int i = 0; i < navApp.length; i++) {
			index = Integer.parseInt(navApp[i]);
			fil = (File)navListe.get(index);
			String output[] = availStorage.readSystemNamesFromNAVModule(fil);
			
			if (output != null) {
				// Bruke tabell for kolonner
				if (size > 1400) {
					if (!form.startsWith("<td")) {
						form = "<td style='padding: 1px 25px 1px 1px; vertical-align: top;'>" + form;
					}
					form += "</td><td style='padding: 1px 25px 1px 1px; vertical-align: top;'>";
					size = 0;
				}
				
				// Modulens navn: "nav-ent-pen-vedtakApp.jar" blir til "nav-ent-pen-vedtak"
				app = availStorage.getNAVModuleName(fil);
				// Skriver ut modulens navn i fet skrift:
				String form2 = "<br /><b>" + app + ":</b><br />";
				
				for (int x = 0; x < output.length; x++) {
					form2 += "<label>" +
						"<input type='checkbox' CHECKED name='systemnames' value='" + output[x] + "' />" + output[x] + 
						"</label><br />";
				}
				form += form2;
				size += form2.length();
			}
		}
		
		if (!"".equals(form)) {
			if (form.startsWith("<td")) {
				form = "<table><tr>" + form + "</tr></table>"; 
			}
			form = "<h2>Select SystemNames to add:</h2>" +
				"<form name='myForm' method='POST' action='scan.jsp'>" + form + 
					"<br /><a href='#' onClick='check(document.myForm.systemnames)'>check/clear all</a><br />" +
					"<br /><input type='submit' name='btnSystemNamesValgt' value='Next' />" +
				"</form>";
		}
		else {
			form = "<h2>Did not found any SystemNames</h2>";
		}
		
		%><%=form%><%
	}
	
	
	// Klikket på btnSystemNamesValgt
	else if  (request.getParameter("btnSystemNamesValgt") != null && request.getParameterValues("systemnames") != null) {
		%><a href="scan.jsp">Go back for choosing nav-modules again</a><br /><%
		
		String[] systemnames = request.getParameterValues("systemnames");
		String[] added = availStorage.addSystemNames(systemnames);
		
		for (int i = 0 ; i < added.length; i++) {
			%><%=added[i]%> is added!<br /><%
		}
	}
	
	
	// Lister ut installerte nav-moduler og ber bruker velge hvilke som skal scannes for SystemNames
	else {
		int size = navListe.size();
		
		if (size == 0) {
			%><h2>You have not deployed any nav-modules.</h2><%
		}
		
		else {
			%>
			<h2>Select modules to scan for SystemNames:</h2>
			
			<form name="myForm" method="POST" action="scan.jsp">
			<%
			for (int i = 0; i < size; i++) {
				fil = (File) navListe.get(i);
				app = availStorage.getNAVModuleName(fil);
				%>
				<label><input type="checkbox" name="navApp" value="<%=i%>" /> <%=app%></label><br />
				<%
			}
			
			%>
			<a href="#" onClick="check(document.myForm.navApp)">check/clear all</a><br />
			<br />
			<input type="submit" name="btnModulerValgt" value="Next" />
			</form>
			<%
			
			if (request.getParameter("btnModulerValgt") != null) {
				%><h5>You did not select any modules in the list!</h5><%
			}
		}
	}
	%>
	
	<br /><A href="sa.jsp">Back to overview</A>
	
	<br />
	
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
