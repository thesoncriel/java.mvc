<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 지사에 대한 IP 허용 목록을 가져 온다.
param.
	id_corp = 지사 ID (필수)
*/
CorpManageController ctrl = new CorpManageController(request, response, session);
ctrl.run("corpIpList");
%>