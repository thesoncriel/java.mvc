<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록 요청 이미지를 가져온다.
param.
	id_reg_seq - 등록 요청 ID 
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("regreqAddByRemote");
%>