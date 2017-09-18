<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점에서 사용되는 장비들에 대한 목록을 출력한다.
param.
	no_serial = 관리번호
	mac = MAC 주소
	have_type = 소유주 (0~2)
	id_nm_cust = 가맹점 정보 (이름 혹은 ID)
	id_nm_corp = 지사 정보 (이름 혹은 ID)
	page = 페이지
	count = 한번에 보여줄 행 개수
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipList");
%>