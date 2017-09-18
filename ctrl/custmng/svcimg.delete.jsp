<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 가맹점의 계약서이미지 내용을 삭제 한다.
param.
	id_cust_svc - 계약서ID
	seq - 계약서 이미지 번호
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("svcimgDelete");
%>