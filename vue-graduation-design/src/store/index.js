import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    projectName: "市政绿化道路检测",
    backendAddress: "http://172.18.40.241:8081",
    token: localStorage.getItem("token") ? localStorage.getItem("token") : "",
    username: localStorage.getItem("username")
      ? localStorage.getItem("username")
      : "",
    serverErrMsg: "服务器出了点问题",
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token); //存储token
    },
    setUsername(state, username) {
      state.username = username;
      localStorage.setItem("username", username); //存储username
    },
    delTokenAndUsername(state) {
      state.token = "";
      localStorage.removeItem("token"); //删除token
      localStorage.removeItem("username"); //删除token
    },
  },
  actions: {},
  modules: {},
});
