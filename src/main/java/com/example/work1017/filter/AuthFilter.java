package com.example.work1017.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可选：初始化时的逻辑
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;

        // 定义不需要过滤的 URL，比如登录和注册页面
        String loginURI = httpRequest.getContextPath() + "/login.jsp";
        String registerURI = httpRequest.getContextPath() + "/register.jsp";
        String loginActionURI = httpRequest.getContextPath() + "/UserServlet?action=login";
        String registerActionURI = httpRequest.getContextPath() + "/UserServlet?action=register";

        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isRegisterRequest = httpRequest.getRequestURI().equals(registerURI);
        boolean isLoginAction = httpRequest.getRequestURI().equals(loginActionURI);
        boolean isRegisterAction = httpRequest.getRequestURI().equals(registerActionURI);

        // 如果已经登录，或者是登录和注册请求，继续请求处理
        if (user != null || isLoginRequest || isRegisterRequest || isLoginAction || isRegisterAction) {
            chain.doFilter(request, response); // 放行请求
        } else {
            // 如果未登录，重定向到登录页面
            httpResponse.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
        // 可选：销毁时的逻辑
    }
}
