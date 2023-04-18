package com.octopus.graduationdesign.pojo.vo;

import lombok.Data;

@Data
public class TaskVo {
    private String name;
    private Integer model;
    private Double conf;
    private String filePath;
    private String comment;
}
