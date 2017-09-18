<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	ERP가맹점과 라인넷 가맹점 정보의 차이점을 확인하고 엑셀로 저장 한다.
	차이점 확인 시 ERP Log 내용을 이용하기 때문에
	차이점을 확인하고 싶은 자료의 사용월(yyyymm_use)을 반드시 제시 해야 한다.
param.
	yyyymm_use = 정보 차이를 확인 할 일자 기준. (기본값: 현재 연월)
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custDiffCheck");
%>