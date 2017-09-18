<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
장비 소유자를 지정된 지사로 되돌린다.
param.
	id_equip = 장비ID
	id_cust = 가맹점ID
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipMoveCorp");
%>