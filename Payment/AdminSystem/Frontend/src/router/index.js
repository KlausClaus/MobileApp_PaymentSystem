import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    requireAuth: true,
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      { path: 'home', name: '', component: () => import('../views/Home.vue')},
      { path: 'user', name: '', component: () => import('../views/User.vue')},
      { path: 'tuitionInvoice', name: '', component: () => import('../views/TuitionInvoice.vue')},
      { path: 'student', name: '', component: () => import('../views/Student.vue')},
      { path: 'analysis', name: '', component: () => import('../views/Analysis.vue')},

    ]
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },


]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  next()  // 放行路由
})

export default router
