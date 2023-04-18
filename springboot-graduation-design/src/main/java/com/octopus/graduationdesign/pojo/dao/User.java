package com.octopus.graduationdesign.pojo.dao;

import lombok.Data;

import java.util.Date;

/**
 * 对应数据库的t_user
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Date createTime;
}
