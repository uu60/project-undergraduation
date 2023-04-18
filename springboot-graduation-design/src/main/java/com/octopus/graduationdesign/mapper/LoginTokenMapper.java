package com.octopus.graduationdesign.mapper;

import com.octopus.graduationdesign.pojo.dao.LoginToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTokenMapper {
    void addToken(LoginToken token);

    void invalidTokenByUsername(String username);

    LoginToken getValidTokenByValue(String value);
}
