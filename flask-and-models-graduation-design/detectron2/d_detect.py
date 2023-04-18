import os.path

import cv2
import torch.cuda

from detectron2.utils.logger import setup_logger

setup_logger()
from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.utils.visualizer import Visualizer, ColorMode
from detectron2.data import MetadataCatalog
from detectron2.config import get_cfg

from o_utils.utils import current_time_millis, save_statistical_chart

MetadataCatalog.get("dataset").thing_classes = ["", "fallen_tree", "litter", "vegetation_loss",
                                                "well_loss"]
MetadataCatalog.get("dataset").thing_colors = [(0, 0, 0), (0, 255, 0), (255, 0, 0), (255, 255, 0), (0, 0, 255)]
cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-Detection/faster_rcnn_R_50_FPN_1x.yaml"))
cfg.MODEL.WEIGHTS = os.path.join(os.getcwd(), "detectron2", "weights", "best.pth")
if not torch.cuda.is_available():
    cfg.MODEL.DEVICE = "cpu"
# 每张图片选择的element数量
cfg.MODEL.ROI_HEADS.BATCH_SIZE_PER_IMAGE = 64
class_num = 4
# 类别数 + 1（加上背景）
cfg.MODEL.ROI_HEADS.NUM_CLASSES = class_num + 1

vid = ["mp4", "mov"]


# 获取视频帧用于写结果
# 返回帧集合和帧率
def get_frames_from_video(videos_path):
    vid_cap = cv2.VideoCapture(videos_path)
    fps = int(vid_cap.get(cv2.CAP_PROP_FPS))
    w = int(vid_cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    h = int(vid_cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

    success, image = vid_cap.read()
    frames = []
    while success:
        frames.append(image)
        success, image = vid_cap.read()
    return frames, fps, w, h


def write_frames_to_video(frames, target_path, fps, w, h):
    # count = 1
    vid_writer = cv2.VideoWriter(target_path, cv2.VideoWriter_fourcc(*'mp4v'), fps, (w, h))
    for f in frames:
        vid_writer.write(f)
    vid_writer.release()


def get_predicted_img(predictor, im, vid=False):
    outputs = predictor(im)
    v = Visualizer(im[:, :, ::-1], MetadataCatalog.get("dataset"), instance_mode=ColorMode.SEGMENTATION)
    out = v.draw_instance_predictions(outputs["instances"].to("cpu"))
    if not vid:
        return out.get_image()[:, :, ::-1]
    return out.get_image()[:, :, ::-1], outputs["instances"].pred_classes


def handle_video(file_path, predictor, save_dir, file_name):
    frames, fps, w, h = get_frames_from_video(file_path)
    result_frames = []
    total = len(frames)
    idx = 0
    # 在这里建立四个数组记录整个视频每一帧四种对象的个数
    # 按类别id顺序("green", "fallen_tree", "litter", "vegetation_loss", "well_loss")
    frames_objects_num = []
    for i in range(class_num + 1):
        frames_objects_num.append([])
    for f in frames:
        # pred_classes是这一帧中所有检测到对象的类别id
        img, pred_classes = get_predicted_img(predictor, f, True)
        temp = []
        for i in range(class_num + 1):
            temp.append(0)
        for c in pred_classes:
            temp[c] += 1
        for i in range(class_num + 1):
            frames_objects_num[i].append(temp[i])
        result_frames.append(img)
        idx += 1
        print(f"----frame({idx}/{total}) done.")
    # 所有帧处理完成，保存标注图像
    write_frames_to_video(result_frames, os.path.join(save_dir, file_name), fps, w, h)
    save_statistical_chart(frames_objects_num, total, fps, os.path.join(save_dir, file_name), class_num, True)


def main(task_id, source, conf):
    cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = conf  # set threshold for this model

    predictor = DefaultPredictor(cfg)
    save_dir = f"/Users/dujianzhang/graduation_design/done/{task_id}_{current_time_millis()}"
    os.mkdir(save_dir)

    for root, dirs, files in os.walk(source):
        for i, file in enumerate(files):
            print(f"processing({i + 1}/{len(files)}): " + file)
            file_path = os.path.join(root, file)
            # 如果当前遍历是视频
            if file.split(".")[len(file.split(".")) - 1] in vid:
                handle_video(file_path, predictor, save_dir, file)
            # 否则为图片
            else:
                im = cv2.imread(file_path)
                cv2.imwrite(os.path.join(save_dir, file), get_predicted_img(predictor, im))
            print(f"file({i + 1}/{len(files)}) done.")
    return save_dir
