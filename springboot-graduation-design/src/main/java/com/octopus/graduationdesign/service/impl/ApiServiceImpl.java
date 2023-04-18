package com.octopus.graduationdesign.service.impl;

import com.octopus.graduationdesign.mapper.TaskMapper;
import com.octopus.graduationdesign.pojo.dao.Task;
import com.octopus.graduationdesign.pojo.vo.NotifyReqVo;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.ApiService;
import com.octopus.graduationdesign.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${flask.address}")
    private String flaskAddress;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void notifyFlaskHandle(Integer taskId, Integer model, Double conf, String filePath) {
        NotifyReqVo notifyVo = new NotifyReqVo(taskId, model, conf, filePath);
        try {
            R resp = restTemplate.postForObject(flaskAddress + "/analyse", notifyVo, R.class);
            taskMapper.updateProcessTask(new Task(taskId, OProperties.TASK_PROCESSING_STATE));
            if (resp == null) {
                handleFailedTask(taskId, "网络问题");
            }
        } catch (Exception e) {
            handleFailedTask(taskId, "网络问题");
        }
    }

    @Override
    public void handleFailedTask(Integer taskId, String exception) {
        taskMapper.updateFailTask(new Task(taskId, OProperties.TASK_FAILED_STATE, new Date(), exception));
    }

    @Override
    public void handleSucceededTask(Integer taskId, String resultPath) {
        taskMapper.updateSucceedTask(new Task(taskId, OProperties.TASK_SUCCEEDED_STATE, resultPath, new Date()));
    }
}
