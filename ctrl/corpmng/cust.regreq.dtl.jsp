<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록 요청 상세 내역을 가져온다.
param.
	id_reg_seq = 등록 요청 ID
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("custRegreqDtl");
%>