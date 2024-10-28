import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import request from "@/utils/request";
import enLocale from 'element-ui/lib/locale/lang/en'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
import ElementLocale from 'element-ui/lib/locale'
import store from "@/store";
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css';


// 设置语言
ElementLocale.use(enLocale)

// 使用 ElementUI
Vue.use(ElementUI)

// 挂载请求到全局
Vue.prototype.request = request

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

Vue.config.productionTip = false
Vue.use(ElementUI,{size: "small"});

Vue.prototype.request = request


new Vue({
  router,
  store,
  Viewer,
  render: h => h(App)
}).$mount('#app')



