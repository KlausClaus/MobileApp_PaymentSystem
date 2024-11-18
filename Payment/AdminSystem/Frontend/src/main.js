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


// Set language for ElementUI (default to English)
ElementLocale.use(enLocale)

// Use ElementUI with a global size option
Vue.use(ElementUI)

// Attach request utility to Vue prototype for global access
Vue.prototype.request = request

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

Vue.config.productionTip = false

// Use ElementUI with a global size option
Vue.use(ElementUI,{size: "small"});

Vue.prototype.request = request


new Vue({
  router, // Vue Router
  store, // Vuex Store
  Viewer,
  render: h => h(App)
}).$mount('#app')// Render the App component



