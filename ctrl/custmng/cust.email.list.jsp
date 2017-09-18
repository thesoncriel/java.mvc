<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 가맹점 ID 로 이메일 정보를 가져 온다.
param.
	id_custs = 7000xxxx,7000111xx 와 같은 형식으로 쉼표(,)로 구분되어진 가맹점ID 집합.
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("custEmailList");
%>