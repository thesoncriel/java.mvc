<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
배송/반납 상세 정보를 가져 온다.
param.
	id_trans = 배송ID
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipTransDetail");
%>