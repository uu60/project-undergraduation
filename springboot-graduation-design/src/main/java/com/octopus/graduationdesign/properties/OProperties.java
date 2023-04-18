package com.octopus.graduationdesign.properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class OProperties {
    // 1 HTTP相关
    // 1.1 通用
    public final static int HTTP_SUCCESS = 200;
    public final static int HTTP_INTERNAL_SERVER_ERROR = 500;
    public final static int HTTP_ILLEGAL_ACCESS = 403;
    // 1.2 登录注册相关
    public final static int HTTP_NO_SUCH_USER = 410;
    public final static int HTTP_WRONG_PWD = 411;
    public final static int HTTP_LOGIN_EXPIRED = 412;
    public final static int HTTP_ILLEGAL_PARAMS = 413;
    public final static int HTTP_REPEAT_USERNAME = 414;
    // 1.3 任务相关
    public final static int HTTP_FILE_INTERNAL_ERROR = 520;
    public final static int HTTP_TOO_FREQUENT = 521;

    // 2 URL相关
    public final static HashSet<String> URL_NO_NEED_LOGIN_SET =
            new HashSet<>(Arrays.asList("/login/*", "/register/*"));
    public final static HashSet<String> URL_LAN_ONLY_SET =
            new HashSet<>(Collections.singletonList("/api/*"));
    static {
        URL_NO_NEED_LOGIN_SET.addAll(URL_LAN_ONLY_SET);
    }

    // 3 参数相关
    // 3.1 模型
    // YOLO
    public final static int TASK_YOLO_V5 = 1;
    // FASTER_RCNN
    public final static int TASK_FASTER_RCNN = 2;
    // 3.2
    // 上传完成
    public final static int TASK_UPLOAD_DONE = 1;
    // 正在分析
    public final static int TASK_PROCESSING_STATE = 2;
    // 分析完成，任务结束
    public final static int TASK_SUCCEEDED_STATE = 3;
    // 任务失败
    public final static int TASK_FAILED_STATE = -1;
    // 任务删除
    public final static int TASK_DELETED_STATE = -2;
}
