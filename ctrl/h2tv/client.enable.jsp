<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점의 특정 클라이언트 사용 상태를 활성화/비활성화 한다.
param.
id_client - 클라이언트ID
yn_use - Y=사용, N=미사용
*/
CustClientController ctrl = new CustClientController(request, response, session);
ctrl.run("clientEnable");
%>