<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	상품별 정산금액 설정 내용을 목록화 하여 전달 한다.
param.
	page - 페이지
	count - 개수
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("prodcfgList");
%>