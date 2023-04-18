package com.octopus.graduationdesign.config;

import com.octopus.graduationdesign.interceptor.LanInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LanInterceptor lanInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lanInterceptor)
                .addPathPatterns("/**") // 配置拦截路径（所有路径都拦截）
                .excludePathPatterns("/test2"); // 配置排除的路径
    }
}
