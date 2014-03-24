<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Virgo consumer</title>
</head>
<body>
<%@ page import="no.stelvio.consumer.virgo.*" %>
<%@ page import="javax.naming.*" %>
<%


VirgoConsumer consumer;

try {
    InitialContext ctx = new InitialContext();
    consumer = (VirgoConsumer) ctx.lookup("ejblocal:no.stelvio.consumer.virgo.VirgoConsumer");
} catch (NamingException e) {
    e.printStackTrace();
    throw new RuntimeException(e);
}

String reply = consumer.echo("Hei");

%>

<h1>Reply from provider</h1> <%= reply.toString() %>
</body>
</html>