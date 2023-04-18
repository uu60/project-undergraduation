package com.octopus.graduationdesign.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotifyReqVo {
    private Integer taskId;
    private Integer model;
    private Double conf;
    private String filePath;
}
