<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	클라이언트 정보를 동기화 한다.
param.
	url - 동기화를 수행하는 외부 URL
	timeout(선택) - 외부 URL 수행 시 적용 할 타임아웃 값. (기본값: 10초)
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("periodSubmit");
%>