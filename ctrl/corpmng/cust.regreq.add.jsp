<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록 요청 내역을 추가한다.
param.
	regreqimg = 요청 내용이 담겨있는 계약서 이미지
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("custRegreqAdd");
%>