<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="stelvio.MetronomeWSEXP_MetronomeHttpPortProxy"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="javax.xml.datatype.DatatypeFactory"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Sent tick
<%
MetronomeWSEXP_MetronomeHttpPortProxy proxy = new MetronomeWSEXP_MetronomeHttpPortProxy();
GregorianCalendar calendar = new GregorianCalendar();
proxy.tick(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
out.println(calendar.getTime());
%>
</body>
</html>