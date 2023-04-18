package com.octopus.graduationdesign.service;

import com.octopus.graduationdesign.pojo.vo.FailedTaskVo;
import com.octopus.graduationdesign.pojo.vo.ProcessingTaskVo;
import com.octopus.graduationdesign.pojo.dao.Task;
import com.octopus.graduationdesign.pojo.vo.SuccessfulTaskVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface TaskService {
    String checkBeforeSaveFile(String username, String uuid);

    boolean saveFile(String username, MultipartFile file, String targetDirPath);

    void addTask(Task task);

    List<ProcessingTaskVo> getProcessingTasks(String username);

    List<SuccessfulTaskVo> getSuccessfulTask(String username);

    List<FailedTaskVo> getFailedTask(String username);

    boolean zipResultToResp(Integer id, HttpServletResponse response);

    void deleteTaskById(Integer id);
}
