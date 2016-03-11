<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JAAS testpage</title>
</head>
<body>
	<%@ page import="java.util.*"%>
	<%@ page import="java.security.*"%>
	<%@ page import="javax.security.auth.*"%>
	<%@ page import="com.ibm.websphere.security.auth.*"%>
	<%@ page import="com.ibm.websphere.security.cred.*"%>
	<%@ page import="com.ibm.wsspi.security.token.*"%>

<%

Subject subject = com.ibm.websphere.security.auth.WSSubject.getCallerSubject();
ArrayList<Principal> principals = new ArrayList<Principal>(subject.getPrincipals());
ArrayList<Object> privcreds = new ArrayList<Object>(subject.getPrivateCredentials());
ArrayList<Object> pubcreds = new ArrayList<Object>(subject.getPublicCredentials());
StringBuffer subjectstring = new StringBuffer();
StringBuffer principalstring = new StringBuffer();
StringBuffer privcredstring = new StringBuffer();
StringBuffer pubcredstring = new StringBuffer();
subjectstring.append(subject.toString());
for(int i=0;i < principals.size();i++) {
    principalstring.append("<br>Principal "  + i + " : "+ principals.get(i).getName());
}
for(int i=0;i < privcreds.size();i++) {
    privcredstring.append("<br>Private credential Name"  + i + " : "+ ((Token) privcreds.get(i)).getName());
    privcredstring.append("<br>Private credential UniqueId"  + i + " : "+ ((Token) privcreds.get(i)).getUniqueID());
    privcredstring.append("<br>Private credential Principal"  + i + " : "+ ((Token) privcreds.get(i)).getPrincipal());
    //subjectstring.append("<br>Private credential "  + i + " : "+ privcreds.get(i));
}
for(int i=0;i < pubcreds.size();i++) {
    pubcredstring.append("<br>Public credential "  + i + " : "+ pubcreds.get(i));
}
%>
<h1>Information on JAAS Subject</h1>
<table border="1" style="width:100%">
	<tr>
		<th>Description</th>
		<th>Value</th>
	</tr>
	<tr>
		<td>Subject</td>
		<td><%= subjectstring.toString() %></td>
	</tr>
	<tr>
		<td>Principals</td>
		<td><%= principalstring.toString() %></td>
	</tr>
	<tr>
		<td>Private credentials</td>
		<td><%= privcredstring.toString() %></td>
	</tr>
	<tr>
		<td>Public credentials</td>
		<td><%= pubcredstring.toString() %></td>
	</tr>
</table>
</body>
</html>