package com.octopus.graduationdesign.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessfulTaskVo {
    private Integer id;
    private String name;
    private String model;
    private Double conf;
    private String createTime;
    private String doneTime;
    private String comment;
}
