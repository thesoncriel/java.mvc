<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.coupon.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	사용된 쿠폰 목록을 가져온다.
param.
	DT_START - 시작일
	DT_END - 종료일
*/
CouponController ctrl = new CouponController(request, response, session);
ctrl.run("usedCouponList");
%>