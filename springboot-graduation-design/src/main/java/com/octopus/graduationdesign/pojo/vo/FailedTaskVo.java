package com.octopus.graduationdesign.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedTaskVo {
    private String name;
    private String model;
    private Double conf;
    private String createTime;
    private String doneTime;
    private String exception;
    private String comment;
}
