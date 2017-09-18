<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	ID, IP 및 SMS 인증 내역등 메모리에 적재된 내용을 삭제 한다.
param.
	없음.
*/
CorpLoginController ctrl = new CorpLoginController(request, response, session);
ctrl.run("memCacheClear");
%>