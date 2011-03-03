<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="query" class="form.Query" scope="session"/>
<jsp:setProperty property="*" name="query"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg"%>
<%@page import="javax.xml.ws.soap.SOAPFaultException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="service.Echo"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SAML</title>
</head>
<body>
	<form action="?input=Hello">
		<input type="text" name="input" value="<% out.println(query.getInput() == null ? "Hello" : query.getInput()); %>"><br/>
		<input type="submit"/>
	</form>
	<%
		out.println("echo(" + query.getInput() + ") =");
		try {
			out.println(Echo.echo(query.getInput()));
		} catch (EchoEchoFault1Msg e) {
			out.println("Fault1:" + e.getMessage());
		} catch (SOAPFaultException e) {
			out.println("Ikke-deklarert feil: " + e.getMessage());
			out.println("<br/><br/><pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		} catch (Exception e) {
			out.println("Feil: " + e.getMessage());
			out.println("<br/><br/><pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
</body>
</html>