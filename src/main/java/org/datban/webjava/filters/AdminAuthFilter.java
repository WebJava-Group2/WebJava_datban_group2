package org.datban.webjava.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.datban.webjava.services.UserService;
import org.datban.webjava.models.User;

@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String loginURI = httpRequest.getContextPath() + "/admin/login";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        User sessionUser = null;
        if (session != null) {
            sessionUser = (User) session.getAttribute("adminUser");
        }
        User dbUser = null;
        if (sessionUser != null) {
            try {
                dbUser = userService.getUserByEmail(sessionUser.getEmail());
            } catch (Exception e) {
                dbUser = null;
            }
        }
        boolean isLoggedIn = dbUser != null && "admin".equals(dbUser.getRole());
        if (isLoggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            if (session != null) {
                session.invalidate();
            }
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute("error", "Có ai đó đã thay đổi dữ liệu tài khoản của bạn. Vui lòng đăng nhập lại");
            httpResponse.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }
} 