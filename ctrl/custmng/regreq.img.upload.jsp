<%@ page language="java" import="java.util.*, 
com.jway.*,
com.jway.custmng.*" pageEncoding="UTF-8"
%><%
/*
Desc.
	가맹점 등록을 사진 이미지와 함께 요청한다.
param.
	regreqimg - 사진 찍은 이미지 
*/
CustManageController ctrl = new CustManageController(request, response, session);
ctrl.run("regreqImgUpload");
%>