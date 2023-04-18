package com.octopus.graduationdesign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
//设置session过期时间,默认是1800秒
@EnableRedisHttpSession
public class SessionConfig {
}
