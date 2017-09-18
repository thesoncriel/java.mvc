<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	프로모션을 추가 한다.
param.
	id_prod = 상품ID
	amt_cfg = 금액
	prod_type = 상품형태 (wow, ptv)
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("promoAdd");
%>