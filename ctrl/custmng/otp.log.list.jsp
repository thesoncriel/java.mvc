<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	OTP 발급 기록 목록을 가져 온다.
param.
	dt_start 	- 검색 시작 날짜
	dt_end 		- 검색 끝 날짜
	page		- 페이지
	count		- 한번에 가져 올 개수
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("otpLogList");
%>