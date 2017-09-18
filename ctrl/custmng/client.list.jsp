<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
가맹점의 클라이언트 목록 정보를 가져온다.
param.
id_cust - 가맹점ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("clientList");
%>