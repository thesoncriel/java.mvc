<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	IP 허용 작업을 할 가맹점 목록을 가져온다.
param.
	page = 페이지 번호. 기본 1
	count = 보여줄 개수. 기본 10
*/
CorpManageController ctrl = new CorpManageController(request, response, session);
ctrl.run("corpList");
%>