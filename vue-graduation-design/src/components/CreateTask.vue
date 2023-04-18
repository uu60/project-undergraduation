<template>
  <div>
    <el-container>
      <el-header>
        <el-row style="height: 100%">
          <el-col
            :span="12"
            style="height: 100%; display: flex; align-items: center"
          >
            <h1 style="font-size: 20px; margin: 0px">新建任务</h1>
          </el-col>
        </el-row>
      </el-header>
      <el-main id="form-container">
        <el-form label-width="150px" :rules="rules" :model="form" ref="form">
          <el-form-item label="任务名称" prop="name">
            <el-input
              v-model="form.name"
              id="name-input"
              type="textarea"
              placeholder="输入任务名称"
              autosize
            ></el-input>
          </el-form-item>
          <el-form-item
            label="待分析视频或图片"
            :required="true"
            :error="fileError"
          >
            <el-upload
              ref="upload"
              :action="action"
              :data="extraParams"
              accept=".jpg,.jpeg,.png,.mp4,.mov"
              :on-change="handleChange"
              :on-remove="handleRemove"
              :on-success="handleSuccess"
              :on-error="handleError"
              :with-credentials="true"
              :file-list="fileList"
              :auto-upload="false"
              :headers="headers"
              :multiple="true"
            >
              <el-button size="small" type="primary">点击选择文件</el-button>
              <div slot="tip" class="el-upload__tip">
                上传多个1GB以内的图片或视频文件
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item label="置信度阈值" prop="conf">
            <el-input
              v-model="form.conf"
              id="conf-input"
              oninput="value=value.replace(/[^0-9.]/g,'')"
              placeholder="0 ~ 1的小数"
            ></el-input>
          </el-form-item>
          <el-form-item label="要使用模型的" prop="model">
            <el-radio-group v-model="form.model">
              <el-radio :label="1">YOLO v5</el-radio>
              <el-radio :label="2">Faster-RCNN</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="任务备注" prop="comment">
            <el-input
              v-model="form.comment"
              id="comment-input"
              type="textarea"
              placeholder="输入任务备注"
              autosize
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="onSubmit"
              v-loading="uploading"
              :disabled="uploading"
              >立即创建</el-button
            >
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    const confValidator = (rule, value, callback) => {
      if (value.indexOf(".") != -1 && value.split(".").length > 2) {
        callback(new Error("请输入正确格式的小数")); //防止输入多个小数点
      } else if (value.indexOf(".") != -1 && value.split(".")[1].length > 2) {
        callback(new Error("最多两位小数")); //小数点后两位
      } else if (value <= 0 || value >= 1) {
        callback(new Error("请输入(0, 1)区间的小数"));
      } else {
        callback();
      }
    };
    return {
      form: {
        name: "我的任务",
        conf: "0.7",
        model: 1,
        comment: "",
      },
      rules: {
        name: [
          { required: true, message: "任务名称必填", trigger: "blur" },
          { max: 30, message: "长度要在 30 个字符之内", trigger: "change" },
        ],
        // file: [{ validator: fileValidator }],
        conf: [
          { required: true, message: "置信度必填", trigger: "blur" },
          { validator: confValidator },
        ],
        model: [{ required: true, message: "模型必选" }],
      },
      fileList: [],
      uploading: false,
      fileError: "",
      filePath: "",
      doneFileNum: 0,
      extraParams: {
        uuid: "",
      },
      action: this.$store.state.backendAddress + "/task/upload_file",
    };
  },
  computed: {
    headers() {
      return {
        "Login-Token": this.$store.state.token,
      };
    },
  },
  methods: {
    // 这里回调函数传入的fileList并不是绑定的this.fileList
    // this.fileList如果有更新，会在列表中显示
    // this.fileList是已上传列表，回调传入的是待上传列表
    validateFile() {
      if (this.fileList.length == 0) {
        this.fileError = "必须上传文件";
        return false;
      } else {
        this.fileError = "";
        return true;
      }
    },
    handleChange(file, fileList) {
      this.fileList = fileList;
      this.validateFile();
    },
    handleRemove(file, fileList) {
      this.fileList = fileList;
      this.validateFile();
    },
    handleSuccess(response, file, fileList) {
      if (response.code != 200) {
        if (response.code == 412) {
          this.$store.commit("delTokenAndUsername");
          this.$router.replace("/login");
          this.$message.error("登录状态失效，请重新登录");
        } else {
          this.handleError();
          this.fileList = fileList;
        }
      } else {
        this.doneFileNum++;
        if (this.doneFileNum == fileList.length) {
          // 文件上传成功了，收到服务器传来刚才上传的目录位置
          this.filePath = response.filePath;
          // 解除加载遮罩
          this.uploading = false;
          // 提交表单的其他内容
          this.doSubmit();
        }
      }
    },
    handleError() {
      this.$message.error("文件上传失败，请重试");
      this.uploading = false;
    },
    getUuid() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
        /[xy]/g,
        function (c) {
          var r = (Math.random() * 16) | 0,
            v = c == "x" ? r : (r & 0x3) | 0x8;
          return v.toString(16);
        }
      );
    },
    doSubmit() {
      this.$axios
        .post(this.$store.state.backendAddress + "/task/submit_task", {
          name: this.form.name,
          model: this.form.model,
          conf: this.form.conf,
          comment: this.form.comment,
          filePath: this.filePath,
        })
        .then((res) => {
          if (res.data.code == 200) {
            this.$message({
              type: "success",
              message: "任务提交成功！",
            });
            // 清除文件列表
            this.fileList.slice(0, 0);
            this.form.conf = 0.7;
            this.form.model = 0;
            this.$router.replace("/home/my_processing_task");
          }
        })
        .catch((err) => {
          this.$message.error(this.$store.state.serverErrMsg);
          console.log(err.msg);
        });
    },
    onSubmit() {
      let _this = this;
      _this.$refs["form"].validate((valid) => {
        // 验证通过
        if (valid && _this.validateFile()) {
          let uuid = _this.getUuid();
          _this.extraParams.uuid = uuid;
          console.log(_this.extraParams.uuid);
          // 点击提交表单后开始上传文件
          _this.$refs.upload.submit();
          // 这期间提示用户等待，在handleSuccess处理文件上传成功的回调里提交表单的其他数据
          _this.uploading = true;
          _this.$message({
            type: "info",
            message: "请等待文件上传完成",
          });
        } else {
          _this.validateFile();
          console.log("error submit");
        }
      });
    },
  },
};
</script>

<style>
#conf-input {
  width: 150px;
}
#name-input {
  width: 300px;
}
#comment-input {
  width: 300px;
}
.el-upload-list {
  width: 500px;
}
</style>
