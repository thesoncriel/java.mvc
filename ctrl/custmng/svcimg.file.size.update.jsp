<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	서버에 저장된 모든 가맹점 계약서 이미지 목록을 전달 한다.
param.
	id_cust - 가맹점ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("svcimgFileSizeUpdate");
%>