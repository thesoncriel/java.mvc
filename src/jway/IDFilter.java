package com.jway;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class IDFilter extends Util implements Filter {
	
	private IDBlockProvider provider;
	private String redirectPath;

	public void destroy() {
		provider = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		String ID = req.getParameter("ID");
		String id = req.getParameter("id");
		String sID = isEmpty(ID) ? id : ID;
		HttpServletRequest request = (HttpServletRequest)req;
		
		if (isEmpty(sID)){
			chain.doFilter(req, res);
			return;
		}
				
		if (provider.hasBlocked(sID)){
			if (provider.hasTimeout(sID)){
				System.out.println(sID + " = 블럭 대상이었으나 시간이 경과하여 패스하게 됨. 기존 블럭 내용은 삭제.");
				chain.doFilter(req, res);
			}
			else{
				System.out.println(sID + " = 블럭 대상임.");
				
				if (!isEmpty(redirectPath)){
					request.getRequestDispatcher(redirectPath).forward(req, res);
				}
				
				//res.getWriter().println("해당 IP는 블럭 되었습니다. 고객센터에 문의하시길 바랍니다.");
			}
		}
		else{
			System.out.println(sID + " = 블럭 대상이 아님. 패스 함.");
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig conf) throws ServletException {
		try{
			provider = IDBlockProvider.getInstance(conf.getInitParameter("blockTime"));
			redirectPath = conf.getInitParameter("redirect");
		}
		catch(Exception ex){}
	}
}
