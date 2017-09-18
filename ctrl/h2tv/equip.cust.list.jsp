<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
가맹점에서 요청한 장비 목록.
로그인된 사용자의 세션값(id_corp)에 따라 결과를 달리 함.
param.
	search_method = 0:전체, 1:가맹점명, 2:지역명, 3:가맹점ID, 4:사장이름 
	id_cust = 검색할 가맹점ID, 가맹점 이름, 장소 등
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipCustList");
%>