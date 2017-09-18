<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점의 계약ID를 이용하여 IESM_CUST_SVC_SYNC_VODS (무슨 테이블인지 모르겠음 -_-) 의 업데이트 일자를 오늘로 변경 한다.
param.
id_cust - 가맹점ID
*/
CustClientController ctrl = new CustClientController(request, response, session);
ctrl.run("clientSyncDate");
%>