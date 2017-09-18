<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점의 특정 클라이언트에 대한 활성/비활성 여부를 설정 한다. 
param.
	yn_use - 설정값.
		Y=활성, N=비활성
	id_cust - 가맹점ID
	client_mac - 대상 클라이언트의 MAC 번호
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("clientEnable");
%>