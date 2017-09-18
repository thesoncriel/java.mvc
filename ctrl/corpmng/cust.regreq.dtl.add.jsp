<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록 요청 상세 내역을 추가한다.
param.
	id_reg_seq = 등록 요청 ID. 없으면 새로 만들어서 사용한다.
	nm_cust
	ssn
	nm_ceo
	tel_major
	tel_hp
	email
	addr1
	addr2
	id_manage_corp
	nm_manage_corp
	nm_uchicorp
	nm_uchiman
	nm_prod
	ip1_head
	ip1_start
	ip1_end
	ip2_head
	ip2_start
	ip2_end
	ip3_head
	ip3_start
	ip3_end
	ip_cnt
	memo
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("custRegreqDtlAdd");
%>