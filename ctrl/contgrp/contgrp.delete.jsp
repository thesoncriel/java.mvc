<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.contgrp.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	컨텐츠 그룹 내용을 삭제 한다.
	정확히는 못쓰게 한다.
param.
	id_group - 삭제 할 컨텐츠그룹ID.
*/
ContentGroupController ctrl = new ContentGroupController(request, response, session);
ctrl.run("contgrpDelete");
%>