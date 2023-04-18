package com.octopus.graduationdesign.controller;

import com.octopus.graduationdesign.service.ApiService;
import com.octopus.graduationdesign.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService internalService;

    /**
     * 接收flask任务完成的请求
     */
    @GetMapping("/success")
    public R success(Integer taskId, String resultPath) {
        internalService.handleSucceededTask(taskId, resultPath);
        // 响应给flask接收到了
        return R.ok();
    }

    @GetMapping("/failure")
    public R failure(Integer taskId, String exception) {
        internalService.handleFailedTask(taskId, exception);
        // 响应给flask收到了
        return R.ok();
    }
}
