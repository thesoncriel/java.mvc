<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	정산 자료를 수집하여 쌓는다.
param.
	dt_settled - 자료를 수집할 일자.
		예: 2017-02-01 - 당월 수집이지만 '일'을 01로 덧붙여 줘야 한다. 
	page - 페이지
	count - 개수
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("collectAppend");
%>