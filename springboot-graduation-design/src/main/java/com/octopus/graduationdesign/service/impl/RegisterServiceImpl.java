package com.octopus.graduationdesign.service.impl;

import com.octopus.graduationdesign.mapper.UserMapper;
import com.octopus.graduationdesign.pojo.dao.User;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        if (ObjectUtils.isEmpty(user.getUsername()) || ObjectUtils.isEmpty(user.getPassword())) {
            return OProperties.HTTP_ILLEGAL_PARAMS;
        }
        User userByUsername = userMapper.getUserByUsername(user.getUsername());
        if (userByUsername != null) {
            return OProperties.HTTP_REPEAT_USERNAME;
        }
        user.setCreateTime(new Date());
        userMapper.addUser(user);
        return OProperties.HTTP_SUCCESS;
    }
}
