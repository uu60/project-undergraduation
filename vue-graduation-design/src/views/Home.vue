<template>
  <div class="container">
    <el-row type="flex" justify="space-between">
      <el-col :span="12">
        <div id="head-left">
          <img src="@/assets/home/logo.jpg" width="40px" height="40px" />
          <span v-cloak id="project-name">{{
            this.$store.state.projectName
          }}</span>
        </div>
      </el-col>
      <el-col :span="6">
        <el-dropdown id="head-right" @command="handleClickOnAvatar">
          <span class="el-dropdown-link" id="head-right-content">
            <span v-cloak id="username">{{ this.$store.state.username }}</span>
            <el-avatar :src="circleUrl"></el-avatar>
          </span>
          <el-dropdown-menu slot="dropdown" style="margin-right: 30px">
            <el-dropdown-item command="logout" style="padding: 0px"
              ><span style="margin: 0px 20px">退出登录</span></el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-row>
    <el-container>
      <!-- <el-aside width="200px"
        ></el-aside
      > -->
      <el-menu :router="true" :default-active="defaultActive">
        <el-menu-item index="/home/welcome">
          <i class="el-icon-house"></i>
          <span slot="title">欢迎页</span>
        </el-menu-item>
        <el-menu-item index="/home/create_task">
          <i class="el-icon-plus"></i>
          <span slot="title">新建任务</span>
        </el-menu-item>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>我的任务</span>
          </template>
          <el-menu-item index="/home/my_processing_task">处理中</el-menu-item>
          <el-menu-item index="/home/my_successful_task">已完成</el-menu-item>
          <el-menu-item index="/home/my_failed_task">已失败</el-menu-item>
        </el-submenu>
      </el-menu>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import logoImg from "@/assets/home/logo.jpg";
export default {
  data() {
    return {
      logo: logoImg,
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      defaultActive: "/home/welcome",
    };
  },
  methods: {
    handleClickOnAvatar(command) {
      switch (command) {
        case "logout":
          this.$store.commit("delTokenAndUsername");
          this.$router.replace("/login");
          break;
      }
    },
    updateDefaultActive() {
      let path = this.$route.path;
      let beginIndex = path.indexOf("/home");
      // /home/后的字符串中第一个/的index
      this.defaultActive = path.substring(beginIndex);
    },
  },
  beforeMount() {
    this.updateDefaultActive();
  },
  beforeUpdate() {
    this.updateDefaultActive();
  },
};
</script>

<style scoped>
.el-row {
  height: 75px;
  padding: 10px 0;
  background-color: #eaeffb;
}
.el-main {
  background: #f9fafc;
}
#router-view-container {
  padding: 0;
}
#head-left {
  display: flex;
  align-items: center;
  position: relative;
  top: 50%;
  transform: translate(0, -50%);
}
#head-right {
  position: relative;
  top: 50%;
  transform: translate(0, -50%);
}
#head-right-content {
  display: flex;
  align-items: center;
}
#username {
  font-size: 20px;
  margin-right: 10px;
}
.container {
  min-width: 1000px;
  height: 100vh;
}
.el-container {
  height: calc(100vh - 75px);
}
img {
  margin: auto 10px;
  border-radius: 40px;
}
#project-name {
  font-size: 25px;
  margin: auto 0px;
}
.el-dropdown {
  float: right;
  margin: auto 10px;
}
.el-dropdown-link {
  cursor: pointer;
}
.el-avatar {
  size: 30px;
}
.el-menu {
  width: 200px;
}
ul {
  width: 200px;
}
li {
  width: 200px;
}
</style>
