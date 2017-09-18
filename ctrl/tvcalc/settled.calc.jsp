<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	계산을 수행한다.
param.
	yyyymm = 수행 할 정산월
	page = 수행 할 순서
	count = 한번에 수행 할 개수
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("settledCalc");
%>