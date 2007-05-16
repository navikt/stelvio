<%-- tpl:insert page="/theme/JSP-C-02_blue.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://www.ibm.com/siteedit/sitelib" prefix="siteedit"%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">

<%-- tpl:put name="headarea" --%>
<title>listtest.jsp</title>
<%-- /tpl:put --%>

<LINK rel="stylesheet" href="/StelvioSystemAvailabilityWeb/theme/C_master_blue__.css" type="text/css">

<!--  Matching stylesheet for JSF components, use instead of 'stylesheet.css' or remove the reference -->
<LINK rel="stylesheet" href="/StelvioSystemAvailabilityWeb/theme/C_stylesheet_blue.css" type="text/css">

</head>

<body>
<!-- start header area -->
<div class="topAreaBox"></div>
<div class="topAreaLogo"><a href="/your_project/index.jsp"><img src="images/logos/sample-logo-1.gif" alt="return to home page" border="0" /></a>
<a href="#navskip"><IMG src="/StelvioSystemAvailabilityWeb/theme/1x1.gif" alt="skip to page's main contents" border=0 width="1" height="1"></a></div>
<!-- end header area -->

<!-- start header navigation bar -->
<div class="topNavBk"></div>
<div class="topNav"><siteedit:navbar target="home,topchildren" spec="/StelvioSystemAvailabilityWeb/theme/nav_horizontal_text_head__.jsp"></siteedit:navbar></div>
<!-- Optional secondary header navigation area.  To use:
	 1.  Define a group in the Site Navigation Map
	 2.  Set the following navbar to use the group as its Destination Group -->
<siteedit:navbar spec="/StelvioSystemAvailabilityWeb/theme/nav_vertical_list_head__.jsp" group="group2"></siteedit:navbar>
<!-- end header navigation bar bar -->

<!-- start left-hand navigation -->
<table class="mainBox" border="0" cellpadding="0" width="100%" height="87%" cellspacing="0"><tbody><tr>
<td class="leftNavTD" align="left" valign="top"><div class="leftNavBox"><siteedit:navbar spec="/StelvioSystemAvailabilityWeb/theme/nav_vertical_tree_left.jsp" targetlevel="1-5" onlychildren="true" navclass="leftNav"></siteedit:navbar></div></td>
<!-- end left-hand navigation -->

<!-- start main content area -->
<td class="mainContentWideTD" align="left" valign="top"><div class="mainContentWideBox">
<siteedit:navtrail start="" end="" target="home,parent,ancestor,self" separator=":"	spec="/StelvioSystemAvailabilityWeb/theme/nav_horizontal_trail_head.jsp"></siteedit:navtrail><a name="navskip"><IMG border="0" src="/StelvioSystemAvailabilityWeb/theme/1x1.gif" width="1" height="1" alt="Beginning of page content"></a><%-- tpl:put name="bodyarea" --%>
Insert body content here.

<%-- /tpl:put --%>

<!-- Optional footer area.  To use the footer: 
	 1.  Define a group in the Site Navigation Map
	 2.  Set the following navbar to use the group as its Destination Group
	 3.  Set the User Data of the navbar to include the company name, copyright data, etc. -->
<div class="mainContentWidth"><siteedit:navbar spec="/StelvioSystemAvailabilityWeb/theme/nav_horizontal_text_foot__.jsp" group="group" uservalue="&copy; 2005 $Company-Name" targetlevel=""></siteedit:navbar></div>
<!-- end footer area -->
</div></td>
</tr></tbody></table>
<!-- end main content area -->
</body>
</html>
<%-- /tpl:insert --%>