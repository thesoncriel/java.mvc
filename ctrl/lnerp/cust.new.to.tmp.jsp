<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	예상되는 신규 거래처 내용을 임시 테이블에 입력 한다.
param.
	없음
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("custNewToTmp");
%>