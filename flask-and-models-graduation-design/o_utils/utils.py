import time
import matplotlib.pyplot as plt


def current_time_millis():
    return int(round(time.time() * 1000))


def save_statistical_chart(frames_objects_num, frame_num, fps, save_path, class_num, background=False):
    # 根据上面收集的frames_objects_num绘制统计图
    # 将frames_objects
    # 以下代码从全局设置字体为Arial Unicode MS，解决显示中文问题【mac】
    # 设置font.sans-serif 或 font.family 均可
    plt.rcParams['font.sans-serif'] = ['Arial Unicode MS']
    # plt.rcParams['font.family']=['Arial Unicode MS']
    # 解决中文字体下坐标轴负数的负号显示问题
    plt.rcParams['axes.unicode_minus'] = False
    plt.figure()
    plt.xlabel("时间(s)")
    plt.ylabel("目标数量")
    # 根据fps计算x轴
    x = [f / fps for f in range(frame_num)]
    labels = ["树木倒伏", "绿化垃圾", "绿化破损", "井盖缺失"]
    for i in range(class_num):
        plt.plot(x, frames_objects_num[i + 1 if background else i], label=labels[i])
    plt.legend()
    plt.savefig(save_path + "分析结果.jpg")
