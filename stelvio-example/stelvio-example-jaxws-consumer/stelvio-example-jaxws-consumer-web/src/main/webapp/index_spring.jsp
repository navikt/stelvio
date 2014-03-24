<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Virgo Spring consumer</title>
</head>
<body>

<h1>Reply from provider</h1> ${applicationScope.virgoService.echo("Hei Spring") }
<br>
${applicationScope.componentIdHolder.getComponentId() }
<br>

</body>
</html>