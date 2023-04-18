package com.octopus.graduationdesign.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LoginToken {
    // 当前时间 + 10年
    public final static Date NO_EXPIRE = new Date(System.currentTimeMillis() + 10L * 365 * 24 * 60 * 60 * 1000);
    private Integer id;
    private String value;
    private String username;
    private Date createTime;
    private Date expireTime;
}
