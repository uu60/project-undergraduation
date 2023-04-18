package com.octopus.graduationdesign.service.impl;

import com.octopus.graduationdesign.mapper.LoginTokenMapper;
import com.octopus.graduationdesign.mapper.UserMapper;
import com.octopus.graduationdesign.pojo.dao.LoginToken;
import com.octopus.graduationdesign.pojo.dao.User;
import com.octopus.graduationdesign.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTokenMapper tokenMapper;

    /**
     * @param username 用户名
     * @param password 密码
     * @return null: 用户不存在; False: 密码错误; True: 登录成功
     */
    @Override
    public Boolean validatePassword(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        String truePwd;
        if (user == null || (truePwd = user.getPassword()) == null) {
            return null;
        }
        return truePwd.equalsIgnoreCase(password);
    }

    @Override
    public boolean isLogin(String tokenValue) {
        LoginToken token = tokenMapper.getValidTokenByValue(tokenValue);
        // 如果token不存在或者过期了
        return token != null && !token.getExpireTime().before(new Date());
    }

    public LoginToken getToken(String tokenValue) {
        return tokenMapper.getValidTokenByValue(tokenValue);
    }

    @Override
    public void addLoginToken(LoginToken token) {
        tokenMapper.invalidTokenByUsername(token.getUsername());
        tokenMapper.addToken(token);
    }
}
