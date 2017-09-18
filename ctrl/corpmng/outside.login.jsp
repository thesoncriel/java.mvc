<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	지사 로그인 정보를 확인한다.
param.
	user_id = 로그인ID
	user_pw = 비밀번호
*/
CorpLoginController ctrl = new CorpLoginController(request, response, session);
ctrl.run("outsideLogin");
%>