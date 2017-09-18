package com.jway.corpmng;

import com.jway.*;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IPFilter extends Util implements Filter {
	private AllowedIPDictionary allowIP;
	private String redirectPath;
	
	public void destroy() {
		allowIP = null;
	}

	public void doFilter(ServletRequest _req, ServletResponse _res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = ( (HttpServletRequest)_req );
		HttpServletResponse res = (HttpServletResponse)_res;
		HttpSession session = req.getSession();
		String ID = req.getParameter("ID");
		String id = req.getParameter("id");
		String mac = req.getParameter("MAC");
		String sID = isEmpty(ID) ? id : ID;
		String sAddr = req.getRemoteAddr();
		Object oMacSession = session.getAttribute("MAC");
		
		System.out.println("IPFilter::doFilter ---------------------------");
		System.out.println("IP: " + sAddr);

		// MAC 주소가 비어있지 않다면 이 값으로 인증한다 가정하고 IP 값을 대체 한다.
		if (!isEmpty(mac)){
			sAddr = mac;
			session.setAttribute("MAC", mac);
		}
		// MAC 주소 파라메터는 비었는데 세션의 MAC 값이 존재 한다면 그 것을 이용한다.
		else if (oMacSession != null){
			sAddr = (String)oMacSession;
		}
		
		if (isEmpty(sID)){
			if (allowIP.isAllowed(sAddr)){
				chain.doFilter(req, res);
				
				return;
			}
		}
		else{
			if (allowIP.isAllowedBoth(sAddr, sID)){
				chain.doFilter(req, res);
				
				return;
			}
		}
		
		if (!isEmpty(redirectPath)){
			//req.getRequestDispatcher(redirectPath).forward(req, res);
			res.sendRedirect(redirectPath);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		allowIP = AllowedIPDicProvider.getInstance();
		redirectPath = config.getInitParameter("redirect");
	}
}
