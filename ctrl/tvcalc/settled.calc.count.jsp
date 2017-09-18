<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	계산을 수행 하기전 수행 할 개수를 가져 온다.
param.
	yyyymm = 수행 할 정산월
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("settledCalcCount");
%>