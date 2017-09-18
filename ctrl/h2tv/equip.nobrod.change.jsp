<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
할당된 장비의 방송(혹은 채널)을 변경 한다.
param.
	id_equip = 장비ID
	no_brod = 방송번호. 현재는 1~2.
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipNobrodChange");
%>