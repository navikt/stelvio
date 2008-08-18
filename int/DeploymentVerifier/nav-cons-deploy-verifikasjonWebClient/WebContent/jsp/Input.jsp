<%@page contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<TITLE>Inputs</TITLE>
</HEAD>
<BODY>
<p><font face="Arial"><b><i>Verifikasjon</i></b></font></p>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

boolean valid = true;

if(methodID != -1) methodID = Integer.parseInt(method);
switch (methodID){ 
case 2:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
</FORM>
<%
break;
case 5:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE>
<TR>
<TD COLSPAN="1" ALIGN="LEFT"><b><font size="-1" face="Arial">Service Endpoint:</font></b></TD>
<TD ALIGN="left"><font size="-1" face="Arial"><INPUT TYPE="TEXT" NAME="endpoint8" SIZE=20></font></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
<INPUT TYPE="RESET" VALUE="Clear">
</FORM>
<%
break;
case 10:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE><TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><b><font size="-1" face="Arial">WS verifikasjon:</font></b></TD>
<TD ALIGN="left"><font face="Arial"><input type="TEXT" name="typeVerifikasjon21" size="20" value="WS" disabled></font></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
</FORM>
<%
break;
case 23:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE><TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><b><font size="-1" face="Arial">CEI verifikasjon:</font></b></TD>
<TD ALIGN="left"><INPUT TYPE="TEXT" NAME="typeVerifikasjon34" SIZE=20 value="CEI" disabled></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
</FORM>
<%
break;
case 36:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE><TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><b><font size="-1" face="Arial">FEM verifikasjon:</font></b></TD>
<TD ALIGN="left"><INPUT TYPE="TEXT" NAME="typeVerifikasjon47" SIZE=20 value="FEM" disabled></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
</FORM>
<%
break;
case 49:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE><TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><b><font size="-1" face="Arial">SCA asynk verifikasjon:</font></b></TD>
<TD ALIGN="left"><INPUT TYPE="TEXT" NAME="typeVerifikasjon60" SIZE=20 value="SCA" disabled></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
</FORM>
<%
break;
case 1111111111:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<TABLE>
<TR>
<TD COLSPAN="1" ALIGN="LEFT"><b><font size="-1" face="Arial">Sett URL:</font></b></TD>
<TD ALIGN="left"><INPUT TYPE="TEXT" NAME="url1111111111" SIZE="33"></TD>
</TR>
</TABLE>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
<INPUT TYPE="RESET" VALUE="Clear">
</FORM>
<%
break;
case 1111111112:
valid = false;
%>
<FORM METHOD="POST" ACTION="Result.jsp" TARGET="result">
<INPUT TYPE="HIDDEN" NAME="method" VALUE="<%=method%>">
<BR>
<INPUT TYPE="SUBMIT" VALUE="Invoke">
<INPUT TYPE="RESET" VALUE="Clear">
</FORM>
<font face="Arial" size="3">
<%
break;
}
if (valid) {
%> Select a method to test. </font>
<%
    return;
}
%>

</BODY>
</HTML>
