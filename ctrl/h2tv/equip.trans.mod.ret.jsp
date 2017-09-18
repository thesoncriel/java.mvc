<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
지사에서 반납 정보를 수정한다.
param.
	id_trans = 변경할 배송 정보의 ID
	no_delivery = 송장번호
	notice = 전달사항
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipTransModRet");
%>