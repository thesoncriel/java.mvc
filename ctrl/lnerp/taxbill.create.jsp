<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	신규 폐업 자료를 바탕으로
	매출세금계산서 자료를 생성 한다.
param.
	yyyymm_use = 사용연월. yyyy-MM-dd 형식의 문자열
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("taxbillCreate");
%>