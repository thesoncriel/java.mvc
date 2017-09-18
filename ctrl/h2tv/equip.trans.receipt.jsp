<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
배송된 장비를 수령한다.
로그인된 세션에 따라 동작을 달리 한다.
param.
	id_trans = 수령할 배송ID
	id_req = 관련된 요청ID. 요청과 연관이 없다면 0.
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipTransReceipt");
%>