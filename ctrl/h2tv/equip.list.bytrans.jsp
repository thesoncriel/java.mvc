<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
배송중인 장비 목록을 가져 온다.
param.
	id_trans = 배송ID
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipListByTrans");
%>