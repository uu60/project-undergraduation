package com.octopus.graduationdesign.mapper;

import com.octopus.graduationdesign.pojo.dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User getUserByUsername(@Param("username") String username);

    void addUser(User user);
}
