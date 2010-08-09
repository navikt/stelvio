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

	<%
	SystemAvailabilityStorage availStorage = new SystemAvailabilityStorage();
	java.util.List prodListe = availStorage.listInstalledProdModules();
	File fil;
	String app;
	
	
	// Klikket på btnVelgProd
	if (request.getParameter("btnVelgProd") != null && request.getParameterValues("prodApp") != null) {
		%><a href="scan.jsp">Go back for choosing prod-modules again</a><br /><%
		
		// Listen inneholder index til valgte prod-moduler i prodListe-objektet
		String[] prodApp = request.getParameterValues("prodApp");
		int index = 0;
		
		for (int i = 0; i < prodApp.length; i++) {
			index = Integer.parseInt(prodApp[i]);
			fil = (File)prodListe.get(index);
			
			// Prod-modulens navn: "nav-prod-pen-penApp.jar" blir til "nav-prod-pen-pen"
			app = availStorage.getProdModuleName(fil);
			
			// Skriver ut modulens navn i fet skrift:
			%><br /><b><%=app%>:</b><br /><%
			
			String output[] = availStorage.addSystemNamesFromProdModule(fil);
			
			for (int x = 0; x < output.length; x++) {
				%><%=output[x]%> is added!<br /><%
			}
		}
	}
	
	// Lister ut installerte prod-moduler og ber bruker velge hvilke som skal scannes for SystemNames
	else {
		int size = prodListe.size();
		
		if (size == 0) {
			%><h2>You have not deployed any prod-modules.</h2><%
		}
		else {
			%>
			<h2>Choose prod-modules to add their SystemNames:</h2>
			
			<form name="myForm" method="POST" action="scan.jsp">
			<%
			for (int i = 0; i < size; i++) {
				fil = (File) prodListe.get(i);
				app = availStorage.getProdModuleName(fil);
				%>
				<label><input type="checkbox" name="prodApp" value="<%=i%>" /> <%=app%></label><br />
				<%
			}
			%>
			<a href="#" onClick="check(document.myForm.prodApp)">Check/clear all</a><br />
			<br />
			<input type="submit" name="btnVelgProd" value="Next" />
			</form>
			
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
