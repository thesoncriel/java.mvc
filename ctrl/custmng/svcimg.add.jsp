<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 가맹점의 계약서 이미지를 추가 한다.
param.
	svcimg - 변경될 이미지 (jpg binary)
	id_cust_svc - 계약서ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("svcimgAdd");
%>