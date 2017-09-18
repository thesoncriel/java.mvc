<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.tvcalc.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	프로모션을 추가 한다.
param.
	priority = 프로모션 순서. 높을 수록 우선순위가 높다.
	name = 명칭
	dt_start = 적용기간(시작)
	dt_end = 적용기간(종료)
	dt_svc_start = 해당 프로모션에 속한다면 강제로 설정 될 계약시작일
	apply_month = 가맹점이 실제 적용되는 유료 개월수. (보통 10개월)
	pay_month = 방송사에 실제 지급하는 개월수. (보통 12개월. 12보다 적다면 적은 수 만큼 지급하지 앟음)
	add_ip_cost = 추가 IP당 금액. 5개 초과 시 적용됨.
*/
TvCalcController ctrl = new TvCalcController(request, response, session);
ctrl.run("promoAdd");
%>