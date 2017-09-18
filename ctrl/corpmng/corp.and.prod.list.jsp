<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.corpmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
지사(관리점, 유치점) 및 상품 목록 가져오기
param.
	없음
*/
CorpOutsideManageController ctrl = new CorpOutsideManageController(request, response, session);
ctrl.run("corpAndProdList");
%>