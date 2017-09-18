package com.jway.corpmng;

import java.util.*;
import javax.servlet.http.*;

import com.jway.*;

public class SessionInfoOutside extends SessionInfo {

	@SuppressWarnings("rawtypes")
	public SessionInfoOutside(HttpSession session, HttpServletRequest request, Map loginInfo) {
		this.remoteIP = request.getRemoteAddr();
		this.sessionId = session.getId();
		
		if (loginInfo == null){
			this.login = false;
			
			return;
		}
		
		this.idUser = loginInfo.get("id_user").toString();
		this.idLogin = loginInfo.get("id_login").toString();
		this.nmUser = loginInfo.get("nm_user").toString();
		this.idCorp = loginInfo.get("id_corp").toString();
		this.nmCorp = loginInfo.get("nm_corp").toString();
		this.login = true;
		
		try{
			this.debug = Integer.parseInt(request.getParameter("debug")) == SessionInfo.DEBUG_CODE;
		}
		catch(Exception ex){
			this.debug = false;
		}
	}

}
