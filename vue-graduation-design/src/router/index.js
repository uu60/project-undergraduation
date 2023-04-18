import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import Welcome from "../components/Welcome.vue";
import CreateTask from "../components/CreateTask";
import MySuccessfulTask from "../components/MySuccessfulTask.vue";
import MyProcessingTask from "../components/MyProcessingTask.vue";
import MyFailedTask from "../components/MyFailedTask.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      notNeedLogin: true,
    },
  },
  {
    path: "/home",
    component: Home,
    children: [
      {
        path: "",
        redirect: "welcome",
      },
      {
        path: "welcome",
        name: "Welcome",
        component: Welcome,
      },
      {
        path: "create_task",
        name: "CreateTask",
        component: CreateTask,
      },
      {
        path: "my_successful_task",
        name: "MySuccessfulTask",
        component: MySuccessfulTask,
      },
      {
        path: "my_processing_task",
        name: "MyProcessingTask",
        component: MyProcessingTask,
      },
      {
        path: "my_failed_task",
        name: "MyFailedTask",
        component: MyFailedTask,
      }
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

// 验证登陆
router.beforeEach(function (to, from, next) {
  if (to.meta.notNeedLogin) {
    //表示不需要登录
    next(); //继续往后走
  } else {
    // console.log("[main.js 13] page needs login");
    //页面是否登录
    if (localStorage.getItem("token")) {
      //本地存储中是否有token(uid)数据
      // console.log("[main.js 17] already login");
      next(); //表示已经登录
    } else {
      //next可以传递一个路由对象作为参数 表示需要跳转到的页面
      // console.log("[main.js 21] not login yet, redirect to /login");
      next("/login");
    }
  }
});

export default router;
