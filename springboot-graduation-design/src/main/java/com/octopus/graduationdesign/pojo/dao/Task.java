package com.octopus.graduationdesign.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Task {
    private Integer id;
    private String name;
    private String username;
    // 选择使用的模型
    private Integer model;
    private Double conf;
    private Integer state;
    private String filePath;
    private String resultPath;
    private Date createTime;
    private Date doneTime;
    private String exception;
    private String comment;

    public Task(Integer id, Integer state) {
        this.id = id;
        this.state = state;
    }

    // 出错的任务
    public Task(Integer id, Integer state, Date doneTime, String exception) {
        this.id = id;
        this.state = state;
        this.doneTime = doneTime;
        this.exception = exception;
    }

    // 成功的任务
    public Task(Integer id, Integer state, String resultPath, Date doneTime) {
        this.id = id;
        this.state = state;
        this.resultPath = resultPath;
        this.doneTime = doneTime;
    }

    public Task(Integer id, String name, Integer model, Double conf, Date createTime, Date doneTime, Integer state, String exception, String comment) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.conf = conf;
        this.createTime = createTime;
        this.doneTime = doneTime;
        this.state = state;
        this.exception = exception;
        this.comment = comment;
    }
}
