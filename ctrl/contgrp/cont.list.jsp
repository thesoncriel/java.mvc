<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.contgrp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	사용되는 시네호텔 컨텐츠 목록을 가져온다.
param.
	page - 페이지
	count - 개수
*/
ContentGroupController ctrl = new ContentGroupController(request, response, session);
ctrl.run("contList");
%>