<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	매출세금계산서 출력을 위한
	신규 폐업 자료를 업로드 한다.
param.
	tax_bill_close_excel = 사용될 엑셀파일
	yyyymm_use = 사용연월. yyyy-MM-dd 형식의 문자열
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("taxbillCloseUpload");
%>