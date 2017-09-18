<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	계약서 이미지를 가져온다.
param.
	id_cust_svc - 계약서 ID 
	seq - 계약서 순서 (1~3)
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("svcimgImg");
%>