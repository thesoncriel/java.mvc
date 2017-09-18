<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	SMS 인증 코드를 요청한다.
param.
	없음
*/
CorpLoginController ctrl = new CorpLoginController(request, response, session);
ctrl.run("smsAuthCodeReq");
%>