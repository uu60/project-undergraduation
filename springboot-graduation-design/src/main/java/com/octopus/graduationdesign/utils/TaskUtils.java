package com.octopus.graduationdesign.utils;

import com.octopus.graduationdesign.properties.OProperties;

import java.util.concurrent.ConcurrentHashMap;

public class TaskUtils {
    public static String getModelName(Integer model) {
        switch (model) {
            case OProperties.TASK_YOLO_V5:
                return "YOLO v5";
            case OProperties.TASK_FASTER_RCNN:
                return "Faster-RCNN";
            default:
                return "其他模型";
        }
    }
}
