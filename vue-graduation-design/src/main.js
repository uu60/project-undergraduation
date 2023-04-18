import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import axios from "axios";

Vue.use(ElementUI);
// 全局注册axios，调用方式是this.$axios
axios.defaults.withCredentials = true;
Vue.prototype.$axios = axios;
Vue.config.productionTip = false;

// ajax请求拦截
axios.interceptors.request.use(
  (config) => {
    // 判断是否存在token，如果存在将每个页面的header都添加token
    if (store.state.token) {
      config.headers.common["Login-Token"] = store.state.token;
    }
    return config;
  },
  (error) => {
    // 请求错误
    return Promise.reject(error);
  }
);

// ajax响应拦截
axios.interceptors.response.use(
  (response) => {
    if (response.data.code == 412) {
      store.commit("delTokenAndUsername");
      router.replace("/login");
    }
    return response;
  },
  (error) => {
    // 默认除了2XX之外都为错误
    return Promise.reject(error.response);
  }
);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
