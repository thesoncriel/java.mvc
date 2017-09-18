<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	경영팀에서 전해준 ERP 출신 자료인 '거래처등록' 파일을 업로드하여
	그 내용을 라인넷에 등록한다.
	만약 존재한다면 update를, 없다면 insert를 수행한다.
param.
	erp_cust_excel = 거래처등록 파일 (xls)
*/
LinenetERPController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custErpUpload");
%>