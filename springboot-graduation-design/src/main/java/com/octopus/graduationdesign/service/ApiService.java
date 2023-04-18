package com.octopus.graduationdesign.service;

public interface ApiService {
    void notifyFlaskHandle(Integer taskId, Integer model, Double conf, String filePath);

    void handleFailedTask(Integer taskId, String exception);

    void handleSucceededTask(Integer taskId, String resultPath);
}
