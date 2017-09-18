<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
지사에서 장비를 요청한다.
param.
	equip_type = 장비종류. (STB, ASTB 등)
	req_cnt = 요청개수
	reason = 요청사유
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipReqAdd");
%>