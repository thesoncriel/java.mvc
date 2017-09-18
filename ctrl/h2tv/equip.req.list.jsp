<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
가맹점에서 요청한 장비 목록.
로그인된 사용자의 세션값(id_corp)에 따라 결과를 달리 함.
param.
	page = 페이지
	count = 한번에 보여줄 행 개수
	status = 토글되어 보여줄 상태
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipReqList");
%>