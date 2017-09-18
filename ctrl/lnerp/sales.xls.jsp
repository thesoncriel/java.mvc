<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	매출내역 자료를 다운로드 한다.
param.
	yyyymm_use = 자료를 다운받는 사용연월 (ex: 2017-01-01)
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("salesXls");
%>