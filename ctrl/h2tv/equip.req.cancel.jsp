<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
장비 요청을 취소 한다.
param.
	id_req = 요청ID
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipReqCancel");
%>