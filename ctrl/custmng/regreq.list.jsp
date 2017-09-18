<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록 현황 목록을 가져온다.
param.
	reg_state - 등록 상태 
		Y=등록완료, N=등록대기
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("regreqList");
%>