<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	신규 거래처 파일 목록을 출력 한다.
param.
	page = 페이지
	count = 개수
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custNewList");
%>