package com.octopus.graduationdesign.filter;

import com.google.gson.Gson;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.LoginService;
import com.octopus.graduationdesign.utils.FileUtils;
import com.octopus.graduationdesign.utils.HttpUtils;
import com.octopus.graduationdesign.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Order(1)
public class LoginFilter implements Filter {

    @Autowired
    private LoginService loginService;
    @Autowired
    private Gson gson;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 替换掉请求尾部的所有/
        boolean isAllowedPath = HttpUtils.isNoNeedLogin(request);
        if (isAllowedPath) {
            filterChain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession();
            String tokenHeader = request.getHeader("Login-Token");
            String token = tokenHeader != null ? tokenHeader : request.getParameter("token");
            if (loginService.isLogin(token)) {
                String username = loginService.getToken(token).getUsername();
                session.setAttribute("username", username);
                if (!FileUtils.getUploadLocks().contains(username)) {
                    FileUtils.getUploadLocks().put(username, new Object());
                }
                filterChain.doFilter(request, response);
            } else {
                ServletOutputStream outputStream = response.getOutputStream();
                byte[] bytes = gson.toJson(R.error(OProperties.HTTP_LOGIN_EXPIRED, "")).getBytes();
                outputStream.write(bytes);
            }
        }
    }
}
