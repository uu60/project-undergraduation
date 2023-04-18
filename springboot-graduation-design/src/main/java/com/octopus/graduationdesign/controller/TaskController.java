package com.octopus.graduationdesign.controller;

import com.octopus.graduationdesign.pojo.dao.Task;
import com.octopus.graduationdesign.pojo.vo.FailedTaskVo;
import com.octopus.graduationdesign.pojo.vo.ProcessingTaskVo;
import com.octopus.graduationdesign.pojo.vo.SuccessfulTaskVo;
import com.octopus.graduationdesign.pojo.vo.TaskVo;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.TaskService;
import com.octopus.graduationdesign.utils.FileUtils;
import com.octopus.graduationdesign.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/upload_file")
    public R uploadFile(MultipartFile file, String uuid, HttpSession session) {
        String username = (String) session.getAttribute("username");
        synchronized (FileUtils.getUploadLocks().get(username)) {
            String res = taskService.checkBeforeSaveFile(username, uuid);
            if (res.equals(OProperties.HTTP_FILE_INTERNAL_ERROR + "")) {
                return R.error(OProperties.HTTP_FILE_INTERNAL_ERROR);
            }
            if (!taskService.saveFile(username, file, res)) {
                return R.error(OProperties.HTTP_FILE_INTERNAL_ERROR);
            }
            return R.ok().put("filePath", res);
        }
    }

    @PostMapping("/submit_task")
    public R submitTask(@RequestBody TaskVo taskVo, HttpSession session) {
        Task task = new Task(null,
                taskVo.getName(),
                (String) session.getAttribute("username"),
                taskVo.getModel(), taskVo.getConf(),
                OProperties.TASK_PROCESSING_STATE,
                taskVo.getFilePath(),
                null,
                new Date(),
                null,
                null,
                taskVo.getComment());
        taskService.addTask(task);
        return R.ok();
    }

    @GetMapping("/processing_task")
    public R processingTask(HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<ProcessingTaskVo> processingTasks = taskService.getProcessingTasks(username);
        return R.ok().put("tasks", processingTasks);
    }

    @GetMapping("/successful_task")
    public R successfulTask(HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<SuccessfulTaskVo> processingTasks = taskService.getSuccessfulTask(username);
        return R.ok().put("tasks", processingTasks);
    }

    @GetMapping("/failed_task")
    public R failedTask(HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<FailedTaskVo> processingTasks = taskService.getFailedTask(username);
        return R.ok().put("tasks", processingTasks);
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") Integer id, HttpServletResponse response) {
        String fileName = "result.zip";
        response.setHeader("content-type","application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(fileName,"UTF-8"));
        }catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        boolean res = taskService.zipResultToResp(id, response);
    }

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        taskService.deleteTaskById(id);
        return R.ok();
    }
}
