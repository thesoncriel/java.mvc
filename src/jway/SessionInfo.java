package com.jway;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.imnetpia.exbill.EXServer;
import com.imnetpia.exbill.manager.admin.IELoginInfo;

import co.kr.ontown.as.AS_USER;
import co.kr.ontown.common.util.CookieUtil;

public class SessionInfo {
	public static int DEBUG_CODE = 11644;
	
	protected String idUser;
	protected String idLogin;
	protected String nmUser;
	protected String nmCorp;
	protected String idCorp;
	protected String remoteIP;
	protected String sessionId;
	
	protected boolean debug;
	protected boolean login;
	
	public SessionInfo(){}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SessionInfo(HttpSession session, HttpServletRequest request, IDBExecute db){
		IELoginInfo loginInfo = null;
		String sIdUser = "";
		String sIdLogin = "";
		String sNmUser = "";
		String sIdCorp = "";
		Object oBts = session.getAttribute("bts");
		EXServer bts = (oBts != null)? (EXServer)oBts : null;
		
		// EXServer Session정보가 살아있는지 확인
		System.out.println("SPA Module: SessionInfo initialize [begin]");
		System.out.println("Remote IP:" + request.getRemoteAddr());
		System.out.println("Session Timeout :" +  session.getMaxInactiveInterval());
		
		this.remoteIP = request.getRemoteAddr();
		this.sessionId = session.getId();
		
		if (bts != null){
			loginInfo = bts.getIELoginInfo();
		}
		
		if (loginInfo != null){
			sIdLogin = loginInfo.gLoginID;
			sIdUser = loginInfo.gUserID;
		}
		
		if (loginInfo == null || sIdUser == null || "".equals(sIdUser)){
			try {
				sIdLogin = CookieUtil.getCookie(request, "asId");
			} catch (Exception e) {}
			try{
				sIdUser = session.getAttribute("__id_user").toString();
			} catch (Exception e) {}
			try {
				sNmUser = CookieUtil.getCookie(request, "asName");
			} catch (Exception e) {}
			try {
				sIdCorp = CookieUtil.getCookie(request, "asCorp");
			} catch (Exception e) {}
		}
		else{
			sNmUser = loginInfo.gNmUser;
			sIdCorp = loginInfo.gIdDept; // IEMA_USER.ID_DEPT <--> IESM_CORP.ID_CORP
		}
		
		if (sIdUser == null || "".equals(sIdUser)){
			try{
				AS_USER loginConf = null;
				String[][] loginData = null;
				Vector elem = null;
				
				elem = new Vector();
				elem.add(sIdLogin);
				loginConf = new AS_USER();
	            loginData = bts.QuerySelectPS(loginConf.Select_AS_USER("", "", ""),  elem);
	            sIdUser = loginData[0][12];
	            
	            session.setAttribute("__id_user", sIdUser);
		    }catch (Exception ex) {}
		}
		
		this.idUser = sIdUser;
		this.idLogin = sIdLogin;
		this.nmUser = sNmUser;
		this.idCorp = sIdCorp;
		this.nmCorp = sNmUser;
		
		try{
			this.debug = Integer.parseInt(request.getParameter("debug")) == DEBUG_CODE;
		} catch (Exception e) {
			this.debug = false;
		}
		
		if (this.remoteIP != null){
			if ( remoteIP.equals("127.0.0.1") ) {
				session.setMaxInactiveInterval(24*60*60);	// 24시간
		// 방화벽 교체로 인해 제거 20120220
//			} else if ( remoteIP.equals("61.105.116.4") ) {
//				session.setMaxInactiveInterval(1*60*60);	// 1시간
			} else if ( remoteIP.equals("112.217.110.114") ) {
				session.setMaxInactiveInterval(1*60*60);	// 1시간
			}
			System.out.println("Change Session Timeout :" +  session.getMaxInactiveInterval());
		}
		
		if (this.sessionId == null || bts == null || sIdUser == null || "".equals(sIdUser)){
			this.login = false;
			System.out.println("EXServer Session is NULL");
			System.out.println("sessionId=" + this.sessionId);
			System.out.println("bts=" + bts);
			System.out.println("sIdUser=" + sIdUser);
		}
		else{
			this.login = true;
		}
		
		//System.out.println("SPA Module: SessionInfo initialize [end]");
	}
	
	
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getNmUser() {
		return nmUser;
	}
	public void setNmUser(String nmUser) {
		this.nmUser = nmUser;
	}
	public String getIdCorp() {
		return idCorp;
	}
	public void setIdCorp(String idCorp) {
		this.idCorp = idCorp;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public String getIdLogin() {
		return idLogin;
	}
	public void setIdLogin(String idLogin) {
		this.idLogin = idLogin;
	}

	public String getNmCorp() {
		return nmCorp;
	}

	public void setNmCorp(String nmCorp) {
		this.nmCorp = nmCorp;
	}
	
}
