<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
장비 배송을 추가 한다.
로그인된 세션에 따라 동작을 달리 하며,
본사일 경우엔 배송,
지사일 경우엔 반납이 된다.
param.
	id_req = 관련된 요청ID. 요청과 연관이 없다면 0.
	no_delivery = 송장번호
	notice = 전달사항
	json = 관련된 장비 정보가 담긴 json 문자열. 최소한 아래와 같은 형식을 가져야 한다.
		{reqList: [{id_equip: "장비ID"}, ...]}
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipTransAdd");
%>