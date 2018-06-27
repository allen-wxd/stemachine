package com.wistron.web.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		 	HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) resp;
	        //获取根目录所对应的绝对路径
	        String currentURL = request.getRequestURI();
	        //截取到当前文件名用于比较
	        String targetURL = currentURL.substring(currentURL.lastIndexOf("/"),currentURL.length());
	        /*String URL = currentURL.substring(currentURL.lastIndexOf("/"),currentURL.length());
	        System.out.println(URL);*/
	        //如果session不为空就返回该session，如果为空就返回null
	        HttpSession session = request.getSession(false);
	        if(!"/Login.do".equals(targetURL)){
	            //判断当前页面是否是重顶次昂后的登陆页面页面，如果是就不做session的判断，防止死循环
	        	if("/login.action".equals(targetURL)){
	        		 chain.doFilter(request, response);
	        		 return;
	        	}
	            if(session==null||session.getAttribute("loginname")==null){
	                //如果session为空表示用户没有登陆就重定向到login.jsp页面
	                response.sendRedirect("/Stem/Login.do");
	                return;
	            }
	        }
	        //继续向下执行
	        chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		
	}

}
