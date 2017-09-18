<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 지사에 대한 SMS 인증을 강제 추가 한다.
param.
	id_corp = 지사 ID
	ip = 허용 IP
*/
CorpManageController ctrl = new CorpManageController(request, response, session);
ctrl.run("corpSmsAuthAllow");
%>