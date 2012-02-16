<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="query" class="form.Query" scope="session"/>
<jsp:setProperty property="*" name="query"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="javax.xml.ws.soap.SOAPFaultException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="client.EchoClientSAML"%>
<%@page import="no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported"%>
<%@page import="no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable"%><html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SAML</title>
</head>
<body>
	<a href=".">Menu</a>
	<form action="?input=Hello">
		<input type="text" name="input" value="<%out.println(query.getInput() == null ? "Hello" : query.getInput());%>"><br/>
		<input type="submit"/>
	</form>
	<%
		out.println("echo(" + query.getInput() + ") =");
		try {
			out.println(EchoClientSAML.echo(query.getInput()));
		} catch (EchoQuackUnsupported e) {
			out.println("Fault1:" + e.getMessage());
		} catch (EchoServiceUnavailable e) {
			out.println("Fault2:" + e.getMessage());
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