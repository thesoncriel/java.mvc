<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
소유된 장비 목록을 가져 온다.
로그인된 세션에 따라 출력이 달라지며
각 본사와 지사는 각자가 실제 소유하고 있는 장비 목록만 출력 받게 된다.
id_cust를 별도로 주면 해당 가맹점이 소유하는 목록만 출력한다.
param.
	id_cust = 가맹점ID (선택사항)
	transret = 반납여부 (선택사항) - 빈 값이 아닐경우 본사도 지사(온타운직영점) 취급하여 목록을 전달한다.
	include_trans = 배송중인 것도 포함 (선택사항)
	page = 페이지
	count = 개수
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipOwnList");
%>