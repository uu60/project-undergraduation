package com.octopus.graduationdesign.service.impl;

import com.octopus.graduationdesign.mapper.TaskMapper;
import com.octopus.graduationdesign.pojo.dao.Task;
import com.octopus.graduationdesign.pojo.vo.FailedTaskVo;
import com.octopus.graduationdesign.pojo.vo.ProcessingTaskVo;
import com.octopus.graduationdesign.pojo.vo.SuccessfulTaskVo;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.ApiService;
import com.octopus.graduationdesign.service.TaskService;
import com.octopus.graduationdesign.utils.FileUtils;
import com.octopus.graduationdesign.utils.TaskUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Value("${file.undone-location}")
    private String undoneLocation;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ThreadPoolTaskExecutor pool;
    @Autowired
    private ApiService internalService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Override
    public String checkBeforeSaveFile(String username, String uuid) {
        // 创建目录
        // 每个用户有一个文件夹，用户每次上传按时间戳建立新的文件夹
        // /Users/dujianzhang/graduation_design/admin/16xxx
        String targetDirPath = undoneLocation + username + "/" + uuid;
        File targetDir = new File(targetDirPath);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            return OProperties.HTTP_FILE_INTERNAL_ERROR + "";
        }
        return targetDirPath;
    }

    @Override
    public boolean saveFile(String username, MultipartFile file, String targetDirPath) {
        try {
            FileUtils.saveFile(username, file, targetDirPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addTask(Task task) {
        taskMapper.addTask(task);
        // 子线程去通知flask处理,避免网络问题阻塞
        pool.execute(() -> {
            internalService.notifyFlaskHandle(task.getId(), task.getModel(), task.getConf(), task.getFilePath());
        });
    }

    @Override
    public List<ProcessingTaskVo> getProcessingTasks(String username) {
        List<Task> tasks = taskMapper.getTasksByState(username,
                Arrays.asList(OProperties.TASK_PROCESSING_STATE, OProperties.TASK_UPLOAD_DONE));
        return tasks.stream().map(task -> {
            return new ProcessingTaskVo(
                    task.getName(),
                    TaskUtils.getModelName(task.getModel()),
                    task.getConf(),
                    simpleDateFormat.format(task.getCreateTime()),
                    task.getState() == OProperties.TASK_UPLOAD_DONE ? "文件上传完成" : "正在分析",
                    task.getComment());
        }).collect(Collectors.toList());
    }

    @Override
    public List<SuccessfulTaskVo> getSuccessfulTask(String username) {
        List<Task> tasks = taskMapper.getTasksByState(username,
                Collections.singletonList(OProperties.TASK_SUCCEEDED_STATE));
        return tasks.stream().map(task -> {
            return new SuccessfulTaskVo(
                    task.getId(),
                    task.getName(),
                    TaskUtils.getModelName(task.getModel()),
                    task.getConf(),
                    simpleDateFormat.format(task.getCreateTime()),
                    simpleDateFormat.format(task.getDoneTime()),
                    task.getComment()
            );

        }).collect(Collectors.toList());
    }

    @Override
    public List<FailedTaskVo> getFailedTask(String username) {
        List<Task> tasks = taskMapper.getTasksByState(username,
                Collections.singletonList(OProperties.TASK_FAILED_STATE));
        return tasks.stream().map(task -> {
            return new FailedTaskVo(
                    task.getName(),
                    TaskUtils.getModelName(task.getModel()),
                    task.getConf(),
                    simpleDateFormat.format(task.getCreateTime()),
                    simpleDateFormat.format(task.getDoneTime()),
                    task.getException(),
                    task.getComment()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public boolean zipResultToResp(Integer id, HttpServletResponse response) {
        // 获取结果目录
        try {
            FileUtils.zipDirToOutput(taskMapper.getResultPathById(id), response.getOutputStream());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        taskMapper.deleteTask(new Task(id, OProperties.TASK_DELETED_STATE));
    }
}
