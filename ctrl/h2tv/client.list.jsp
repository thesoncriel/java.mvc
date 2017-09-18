<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점의 클라이언트 목록 정보를 가져온다.
param.
id_cust - 가맹점ID
*/
CustClientController ctrl = new CustClientController(request, response, session);
ctrl.run("clientList");
%>