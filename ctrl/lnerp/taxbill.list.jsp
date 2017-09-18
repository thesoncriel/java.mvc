<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	생성된 매출세금계산서 파일 목록을 가져 온다.
param.
	page = 페이지
	count = 개수
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("taxbillList");
%>