<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
본사에서 지사로 배송중인 목록.
로그인된 사용자의 세션값(id_corp)에 따라 결과를 달리 함.
본사는 모든 배송 내역을 볼 수 있으나
지사는 자신에게만 오는 내역만 볼 수 있다.
param.
	page = 페이지
	count = 한번에 보여줄 행 개수
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipTransList");
%>