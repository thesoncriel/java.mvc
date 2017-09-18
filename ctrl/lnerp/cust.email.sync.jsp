<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	신규 거래처 내용을 기반으로 이메일이 없는 가맹점에 한하여
	ONS와 이메일 내용을 동기화 한다.
param.
	page = 나누어서 동기화 할 때 사용될 페이지 번호 (기본값:1)
	count = 한번에 수행될 개수 (기본값:10)
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custEmailSync");
%>