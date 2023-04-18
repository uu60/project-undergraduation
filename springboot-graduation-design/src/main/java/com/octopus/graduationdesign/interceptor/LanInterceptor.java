package com.octopus.graduationdesign.interceptor;


import com.google.gson.Gson;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.utils.HttpUtils;
import com.octopus.graduationdesign.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LanInterceptor implements HandlerInterceptor {

    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpUtils.isPermitLanOnly(request) && !HttpUtils.isInternalIp(request)) {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = gson.toJson(R.error(OProperties.HTTP_LOGIN_EXPIRED, "")).getBytes();
            outputStream.write(bytes);
            return false;
        }
        return true;
    }
}
