<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.contgrp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	특정 컨텐츠에 컨텐츠 그룹 내용을 적용 시킨다.
param.
	id_group - 적용될 컨텐츠그룹ID
	id_contents - 적용할 컨텐츠ID
	apply - 적용 여부. yes 면 적용, 아니면 해제
*/
ContentGroupController ctrl = new ContentGroupController(request, response, session);
ctrl.run("contApply");
%>