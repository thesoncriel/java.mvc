<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.contgrp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	컨텐츠 그룹 목록을 가져온다.
param.
	page - 페이지
	count - 개수
*/
ContentGroupController ctrl = new ContentGroupController(request, response, session);
ctrl.run("contgrpList");
%>