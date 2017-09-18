<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 목록 정보를 가져온다.
param.
	search_method - 검색 기준 설정. 
		1=가맹점명, 2=지역명, 3=가맹점ID, 4=가맹점주, 0=앞의 4가지를 모두 검색(기본)
	id_cust - 가맹점ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("custList");
%>