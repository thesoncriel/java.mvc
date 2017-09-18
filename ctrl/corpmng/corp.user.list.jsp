<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 지사의 사용자 목록 가져오기
param.
	id_corp = 지사ID
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("corpUserList");
%>