<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.lnerp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	엑셀 파일을 다운로드 한다.
param.
	file = 내부에 저장된 파일명
*/
BaseController ctrl = new LinenetERPController(request, response, session);
ctrl.run("excelDownload");
%>