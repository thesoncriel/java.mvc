<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.contgrp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	컨텐츠 그룹 내용을 추가/수정 한다.
param.
	id_group - 수정 할 컨텐츠그룹ID. 0이거나 없다면 신규로 만든다.
	group_name - 적용 할 그룹명.
*/
ContentGroupController ctrl = new ContentGroupController(request, response, session);
ctrl.run("contgrpModify");
%>