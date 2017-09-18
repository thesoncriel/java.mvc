<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	클라이언트 정보에 대한 동기화 목록을 가져 온다.
param.
	mode - 검색 기준 설정. 
		WOW=와우시네, [empty]=시네호텔(PTV - 기본)
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("periodList");
%>