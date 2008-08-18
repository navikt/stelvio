<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<p><font face="Arial" color="black"><b><i>Verifkasjon status</i></b></font></p>

<jsp:useBean id="jspid" scope="session" class="nav_cons_deploy_verifikasjon.VerifikasjonProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
jspid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = jspid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
        java.lang.String endpoint_0idTemp  = endpoint_0id;
        jspid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        String typeVerifikasjon_2id=  request.getParameter("typeVerifikasjon21");
        java.lang.String typeVerifikasjon_2idTemp  = typeVerifikasjon_2id;
        %>
        <jsp:useBean id="nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_1id" scope="session" class="nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq" />
        <%
        nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_1id.setTypeVerifikasjon(typeVerifikasjon_2idTemp);
        nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opWS10mtemp = jspid.opWS(nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_1id);
if(opWS10mtemp == null){
%>
<%=opWS10mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT"><b><font face="Arial">WS:</font></b></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Status:</font></TD>
<TD><font face="Arial">
<%
if(opWS10mtemp != null){
java.lang.String typestatus13 = opWS10mtemp.getStatus();
        String tempResultstatus13 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typestatus13));
        %>
        <%= tempResultstatus13 %>
        <%
}%>
</font></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Aksjon:</font></TD>
<TD><font face="Arial">
<%
if(opWS10mtemp != null){
java.lang.String typeaksjon17 = opWS10mtemp.getAksjon();
        String tempResultaksjon17 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeaksjon17));
        %>
        <%= tempResultaksjon17 %>
        <%
}%>
</font></TD>
</TABLE>
<%
}
break;
case 23:
        gotMethod = true;
        String typeVerifikasjon_4id=  request.getParameter("typeVerifikasjon34");
        java.lang.String typeVerifikasjon_4idTemp  = typeVerifikasjon_4id;
        %>
        <jsp:useBean id="nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_3id" scope="session" class="nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq" />
        <%
        nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_3id.setTypeVerifikasjon(typeVerifikasjon_4idTemp);
        nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opCEI23mtemp = jspid.opCEI(nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_3id);
if(opCEI23mtemp == null){
%>
<%=opCEI23mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT"><p><b><font face="Arial">CEI:</font></b></p></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><p><font face="Arial">Status:</font></p></TD>
<TD><p><font face="Arial">
<%
if(opCEI23mtemp != null){
java.lang.String typestatus26 = opCEI23mtemp.getStatus();
        String tempResultstatus26 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typestatus26));
        %>
        <%= tempResultstatus26 %>
        <%
}%>
</font></p></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><p><font face="Arial">Aksjon:</font></p></TD>
<TD><p><font face="Arial">
<%
if(opCEI23mtemp != null){
java.lang.String typeaksjon30 = opCEI23mtemp.getAksjon();
        String tempResultaksjon30 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeaksjon30));
        %>
        <%= tempResultaksjon30 %>
        <%
}%>
</font></p></TD>
</TABLE>
<%
}
break;
case 36:
        gotMethod = true;
        String typeVerifikasjon_6id=  request.getParameter("typeVerifikasjon47");
        java.lang.String typeVerifikasjon_6idTemp  = typeVerifikasjon_6id;
        %>
        <jsp:useBean id="nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_5id" scope="session" class="nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq" />
        <%
        nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_5id.setTypeVerifikasjon(typeVerifikasjon_6idTemp);
        nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opFEM36mtemp = jspid.opFEM(nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_5id);
if(opFEM36mtemp == null){
%>
<%=opFEM36mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT"><b><font face="Arial">FEM:</font></b></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Status:</font></TD>
<TD><font face="Arial">
<%
if(opFEM36mtemp != null){
java.lang.String typestatus39 = opFEM36mtemp.getStatus();
        String tempResultstatus39 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typestatus39));
        %>
        <%= tempResultstatus39 %>
        <%
}%>
</font></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Aksjon:</font></TD>
<TD><font face="Arial">
<%
if(opFEM36mtemp != null){
java.lang.String typeaksjon43 = opFEM36mtemp.getAksjon();
        String tempResultaksjon43 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeaksjon43));
        %>
        <%= tempResultaksjon43 %>
        <%
}%>
</font></TD>
</TABLE>
<%
}
break;
case 49:
        gotMethod = true;
        String typeVerifikasjon_8id=  request.getParameter("typeVerifikasjon60");
        java.lang.String typeVerifikasjon_8idTemp  = typeVerifikasjon_8id;
        %>
        <jsp:useBean id="nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_7id" scope="session" class="nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq" />
        <%
        nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_7id.setTypeVerifikasjon(typeVerifikasjon_8idTemp);
        nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opSCA49mtemp = jspid.opSCA(nav_cons_deploy_verifikasjon1ASBOVerifikasjonReq_7id);
if(opSCA49mtemp == null){
%>
<%=opSCA49mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT"><b><font face="Arial">SCA:</font></b></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Status:</font></TD>
<TD><font face="Arial">
<%
if(opSCA49mtemp != null){
java.lang.String typestatus52 = opSCA49mtemp.getStatus();
        String tempResultstatus52 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typestatus52));
        %>
        <%= tempResultstatus52 %>
        <%
}%>
</font></TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT"><font face="Arial">Aksjon:</font></TD>
<TD><font face="Arial">
<%
if(opSCA49mtemp != null){
java.lang.String typeaksjon56 = opSCA49mtemp.getAksjon();
        String tempResultaksjon56 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeaksjon56));
        %>
        <%= tempResultaksjon56 %>
        <%
}%>
</font></TD>
</TABLE>
<%
}
break;
}
} catch (Exception e) { 
%>
<b><u><font face="Arial" color="red">Exception</font>:</u></b>
<%= e %>
<%
return;
}
if(!gotMethod){
%>
<font face="Arial" color="red">result: N/A</font>
<%
}
%>
</BODY>
</HTML>