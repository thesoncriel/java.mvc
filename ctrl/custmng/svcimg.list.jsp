<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 가맹점의 계약서와 그에 따른 이미지 목록을 가져 온다.
param.
	id_cust - 가맹점ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("svcimgList");
%>