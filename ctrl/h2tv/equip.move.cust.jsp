<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
장비 소유자를 지정된 가맹점으로 할당 한다.
param.
	id_equip = 장비ID
	id_cust = 가맹점ID
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipMoveCust");
%>