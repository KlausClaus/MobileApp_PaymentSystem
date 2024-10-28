import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import request from "@/utils/request";
import store from "@/store";


import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css';


Vue.config.productionTip = false
Vue.use(ElementUI,{size: "small"});

Vue.prototype.request = request


new Vue({
  router,
  store,
  Viewer,
  render: h => h(App)
}).$mount('#app')



