import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    // Root path with authentication requirement
    path: '/',
    requireAuth: true, // Indicates this route requires authentication
    component: () => import('../views/Manage.vue'), // Main layout component
    redirect: "/home", // Default redirect to the home page
    children: [
      // Nested routes for different pages
      { path: 'home', name: '', component: () => import('../views/Home.vue') }, // Home page
      { path: 'user', name: '', component: () => import('../views/User.vue') }, // User management page
      { path: 'tuitionInvoice', name: '', component: () => import('../views/TuitionInvoice.vue') }, // Tuition invoice management page
      { path: 'student', name: '', component: () => import('../views/Student.vue') }, // Student management page
      { path: 'analysis', name: '', component: () => import('../views/Analysis.vue') }, // Statistical analysis page
    ]
  },
  {
    // About page route
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    // Login page route
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    // Registration page route
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
]

const router = new VueRouter({
  mode: 'history', // Use history mode to remove hash (#) from URLs
  base: process.env.BASE_URL, // Base URL for the application
  routes
})

// Global navigation guard
router.beforeEach((to, from, next) => {
  // Save the current route name to local storage for use in the Header component
  localStorage.setItem("currentPathName", to.name)

  // Trigger Vuex store mutation to update the path
  store.commit("setPath")

  // Allow the navigation to proceed
  next()
})

export default router
