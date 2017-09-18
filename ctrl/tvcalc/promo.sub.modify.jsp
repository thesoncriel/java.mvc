<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	프로모션 상품별 할인율을 설정 한다.
param.
	id_promo = 프로모션ID
	id_prod = 상품ID
	dc_rate = 할인율
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("promoSubModify");
%>