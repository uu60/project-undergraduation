<template>
  <div>
    <el-container>
      <el-aside width="70%"></el-aside>
      <el-main>
        <div id="login">
          <h1 v-cloak>{{ this.$store.state.projectName }}</h1>
          <el-form
            ref="form"
            label-width="80px"
            :rules="rules"
            :model="form"
            @keyup.enter.native="doLogin"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" show-password></el-input>
            </el-form-item>
            <el-form-item style="text-align: center">
              <el-button type="primary" @click="doLogin" v-loading="loginning"
                >登录</el-button
              >
              <el-button
                type="text"
                style="float: right"
                @click="dialogFormVisible = true"
                >注册账号</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-main>
    </el-container>
    <el-dialog title="注册账号" :visible.sync="dialogFormVisible" width="500px">
      <el-form
        ref="form1"
        :model="form1"
        style="margin-left: 20%; margin-right: 20%"
        :rules="rules"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form1.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form1.password" show-password></el-input>
        </el-form-item>
        <el-form-item style="text-align: center">
          <el-button type="primary" @click="doRegister" v-loading="registering"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import md5 from "js-md5";
export default {
  data: function () {
    return {
      form: {
        username: "",
        password: "",
      },
      form1: {},
      dialogFormVisible: false,
      loginning: false,
      registering: false,
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 6, message: "密码最少6位", trigger: "blur" },
        ],
      },
    };
  },
  beforeCreate() {
    if (this.$store.state.token) {
      this.$router.push("/home");
    }
  },
  computed: {
    hashedPwd() {
      return md5(this.form.password);
    },
    hashedPwd1() {
      return md5(this.form1.password);
    },
  },
  methods: {
    doLogin() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loginning = true;
          if (this.$store.state.token) {
            // console.log("forbidden relogin.");
            this.$router.push({
              name: "Home",
            });
            return;
          }
          this.$axios({
            type: "get",
            url: this.$store.state.backendAddress + "/login/do_login",
            params: { username: this.form.username, password: this.hashedPwd },
          })
            .then((res) => {
              console.log(res);
              // code == 200说明登录验证成功
              if (res.data.code == 200) {
                // console.log("received login token: " + res.data.token);
                // console.log("this.username = " + this.username + " will be stored");
                this.$store.commit("setToken", res.data.token);
                this.$store.commit("setUsername", this.form.username);
                this.$router.push("/home");
              } else if (res.data.code == 410) {
                this.$message.error("用户不存在。");
              } else {
                this.$message.error("密码错误。");
              }
              this.loginning = false;
            })
            .catch((err) => {
              this.$message.error(this.$store.state.serverErrMsg);
              console.log(err.msg);
              this.loginning = false;
            });
        }
      });
    },
    doRegister() {
      this.registering = true;
      this.$refs["form1"].validate((valid) => {
        if (valid) {
          this.$axios
            .post(this.$store.state.backendAddress + "/register/do_register", {
              username: this.form1.username,
              password: this.hashedPwd1,
            })
            .then((res) => {
              if (res.data.code == 200) {
                this.$message({
                  type: "success",
                  message: "注册成功",
                });
                this.dialogFormVisible = false;
              } else if (res.data.code == 413) {
                this.$message.error("参数不正确，注册失败");
              } else {
                this.$message.error(this.$store.state.serverErrMsg);
              }
              this.registering = false;
            })
            .catch((err) => {
              console.log(err);
              this.$message.error(this.$store.state.serverErrMsg);
              this.registering = false;
            });
        } else {
          this.registering = false;
        }
      });
    },
  },
};
</script>

<style>
body {
  margin: 0;
}
</style>

<style scoped>
.el-container {
  background: url("~@/assets/login/aside.jpeg");
  background-size: 300px;
  height: 100vh;
  min-width: 1000px;
}

#login {
  width: 100%;
  max-width: 300px;
}

.el-main {
  padding: 20px;
  height: 100%;
  background: rgba(230, 235, 228, 0.9);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

h1 {
  text-align: center;
}
</style>
