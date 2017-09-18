<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	정산 자료를 수집 대상의 모든 개수를 가져 온다.
param.
	dt_settled - 자료를 수집할 일자.
		예: 2017-02-01 - 당월 수집이지만 '일'을 01로 덧붙여 줘야 한다. 
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("collectCount");
%>