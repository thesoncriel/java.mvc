<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점에서 사용될 클라이언트를 추가 한다.
param.
id_cust - 가맹점ID
client_mac - 클라이언트 기기의 MAC 주소
*/
CustClientController ctrl = new CustClientController(request, response, session);
ctrl.run("clientAdd");
%>