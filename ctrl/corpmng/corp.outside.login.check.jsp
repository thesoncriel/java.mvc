<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	지사 정보로 로그인 한다.
param.
	user_id = 로그인ID
	user_pw = 비밀번호
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("corpOutsideLoginCheck");
%>