<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	'거래처등록' 파일을 업로드 작업을 중지 시킨다.
param.
	없음
*/
LinenetERPController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custErpUploadStop");
%>