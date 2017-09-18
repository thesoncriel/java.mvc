<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 상세 정보를 가져온다.
param.
	id_cust - 가맹점ID
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("custDetail");
%>