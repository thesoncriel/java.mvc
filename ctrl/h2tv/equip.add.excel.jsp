<%@ page language="java" import="java.util.*, 
								 com.jway.*,
                                 com.jway.h2tv.*" pageEncoding="UTF-8"
%><%
/*
Desc.
H2TV 가맹점에서 사용될 장비들을 엑셀 파일(xls, xlsx)을 통해 추가 한다.
param.
excel_file - 업로드되는 엑셀파일.
*/
BaseController ctrl = new EquipController(request, response, session);
ctrl.run("equipAddExcel");
%>