<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	ERP 거래처 내용 갱신 진행 상태를 확인 한다.
param.
	없음.
*/
LinenetERPController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custErpUploadProgress");
%>