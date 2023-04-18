import os
import sys
import time

import o_utils.utils

sys.path.append(os.getcwd() + "/yolov5")
sys.path.append(os.getcwd() + "/detectron2")
from concurrent.futures import ThreadPoolExecutor

from flask import Flask, request
from gevent import pywsgi
import requests
import json

import y_detect
import d_detect
from o_utils.R import R

pool = ThreadPoolExecutor(max_workers=4)
spring_address = "http://localhost:8081"
YOLO_V5 = 1
FASTER_RCNN = 2

app = Flask(__name__)


@app.route('/analyse', methods=["POST"])
def analyse():  # put application's code here
    # 接收springboot发送过来的分析请求，包含任务ID（用于完成时返回）和待分析文件路径
    req_data = request.get_json()
    req_dict = json.loads(json.dumps(req_data))
    task_id = req_dict["taskId"]
    model = req_dict["model"]
    file_path = req_dict["filePath"]
    conf = req_dict["conf"]

    # 开启新线程进行推理，避免springboot长时间阻塞占用资源
    pool.submit(do_analyse, task_id, model, file_path, conf)

    # 响应springboot接收到消息
    return R.ok().get()


# 对图片调用模型进行推理
def do_analyse(task_id, model, source, conf):
    try:
        begin = o_utils.utils.current_time_millis()
        if model == 1:
            result_path = y_detect.main(task_id, source, conf)
        elif model == 2:
            result_path = d_detect.main(task_id, source, conf)
        end = o_utils.utils.current_time_millis()
        print("cost time: " + str(end - begin) + "ms")
        # 通知springboot任务完成了
        if requests.get(spring_address + "/api/success",
                        {"taskId": task_id, "resultPath": result_path}).status_code != 200:
            raise Exception("网络问题")

    except Exception as e:
        print(e)
        idx = 0
        # 通知springboot任务处理失败了
        # 失败则重试一小时
        while idx < 360 and requests.get(spring_address + "/api/failure",
                                       {"taskId": task_id, "exception": e}).status_code != 200:
            time.sleep(10)
            idx += 1
            continue


# 处理所有的异常
@app.errorhandler(Exception)
def internal_server_error(e):
    return R.error(500, e).get()


if __name__ == '__main__':
    server = pywsgi.WSGIServer(("0.0.0.0", 8082), app)
    server.serve_forever()
