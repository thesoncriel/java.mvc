<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	SMS 서버와 동기화 시도 한다.
param.
	없음.
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("smsSync");
%>