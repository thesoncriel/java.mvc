<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 지사에 대한 IP 허용을 추가 한다.
param.
	id_corp = 지사 ID
	ip = 허용 IP (C-Class)
*/
CorpManageController ctrl = new CorpManageController(request, response, session);
ctrl.run("corpIpAdd");
%>