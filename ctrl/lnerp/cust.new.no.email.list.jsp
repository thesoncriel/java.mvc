<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	임시 테이블에 입력된 신규 거래처를 기반으로
	이메일이 없는 가맹점 내용을 가져 온다.
param.
	없음
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custNewNoEmailList");
%>