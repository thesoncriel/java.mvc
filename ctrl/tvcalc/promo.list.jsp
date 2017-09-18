<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	프로모션 목록을 출력 한다.
param.
	page
	count
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("promoList");
%>