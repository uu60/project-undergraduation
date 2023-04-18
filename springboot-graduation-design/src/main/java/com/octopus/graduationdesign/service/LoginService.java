package com.octopus.graduationdesign.service;

import com.octopus.graduationdesign.pojo.dao.LoginToken;

public interface LoginService {
    Boolean validatePassword(String username, String password);

    boolean isLogin(String tokenValue);

    LoginToken getToken(String tokenValue);

    void addLoginToken(LoginToken token);
}
