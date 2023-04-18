package com.octopus.graduationdesign.controller;

import com.octopus.graduationdesign.pojo.dao.LoginToken;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.LoginService;
import com.octopus.graduationdesign.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
//@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/do_login")
    public R doLogin(String username, String password, HttpSession session) {
        log.info("username: {}, password: {}", username, password);
        Boolean result = loginService.validatePassword(username, password);
        if (result == null) {
            return R.error(OProperties.HTTP_NO_SUCH_USER, null);
        }
        if (!result) {
            return R.error(OProperties.HTTP_WRONG_PWD, null);
        }
        session.setAttribute("username", username);
        String value = UUID.randomUUID().toString();
        loginService.addLoginToken(new LoginToken(null, value, username, new Date(), LoginToken.NO_EXPIRE));
        return R.ok().put("token", value);
    }

    @GetMapping("/is_login")
    public R isLogin(HttpServletRequest request) {
        String token = request.getHeader("Login-Token");
        if (!loginService.isLogin(token)) {
            return R.error(OProperties.HTTP_LOGIN_EXPIRED, "");
        }
        return R.ok();
    }
}
