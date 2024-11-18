<template>
  <el-menu class="aside-menu"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#fff"
           :default-active="$route.path"
           router
  >
    <!-- Logo area -->
    <div class="logo-container">
      <img src="../assets/logo.png" alt="" class="logo-img">
      <span class="title"> Management </span>
    </div>

    <!-- Menu area -->
    <div class="menu-container">
      <!-- Home menu item -->
      <el-menu-item index="/home" class="menu-item">
        <i class="el-icon-s-home"></i>
        <span>Home</span>
      </el-menu-item>

      <!-- User management menu item -->
      <el-menu-item index="/user" class="menu-item">
        <i class="el-icon-user"></i>
        <span>User Manage</span>
      </el-menu-item>

      <!-- Tuition invoice management menu item -->
      <el-menu-item index="/tuitionInvoice" class="menu-item">
        <i class="el-icon-document"></i>
        <span>Bill Manage</span>
      </el-menu-item>

      <!-- Student management menu item -->
      <el-menu-item index="/student" class="menu-item">
        <i class="el-icon-reading"></i>
        <span>Student Manage</span>
      </el-menu-item>

      <!-- Statistical analysis menu item -->
      <el-menu-item index="/analysis" class="menu-item">
        <i class="el-icon-data-analysis"></i>
        <span>Statistical Analysis</span>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      // Retrieve admin data from local storage, or set to null if not found
      admin: localStorage.getItem("admin") ? JSON.parse(localStorage.getItem("admin")) : null,
      // Retrieve photo data from local storage, or set to null if not found
      photo: localStorage.getItem("photo") ? JSON.parse(localStorage.getItem("photo")) : null,
    }
  },
}
</script>

<style scoped>
/* Styles for the side menu */
.aside-menu {
  min-height: 100%;
  overflow-x: hidden;
  border-right: none;
  width: 200px !important;
}

/* Logo section styles */
.logo-container {
  height: 60px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  background-color: rgb(38, 52, 69);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.12);
}

.logo-img {
  width: 45px;
  height: 45px;
  object-fit: contain;
  margin-right: 8px;
}

.title {
  color: white;
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
}

/* Styles for the menu container */
.menu-container {
  margin-top: 10px;
}

.menu-item {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}

/* Icon styles */
i {
  font-size: 18px;
  width: 24px;
  text-align: center;
  margin-right: 8px;
}

/* Hover effect for menu items */
.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

/* Active menu item styles */
.el-menu-item.is-active {
  background-color: #1890ff !important;
}

/* Scrollbar customization */
.aside-menu::-webkit-scrollbar {
  display: none;
}

.aside-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.aside-menu::-webkit-scrollbar-track {
  background: transparent;
}

/* Remove default Element-UI styles */
:deep(.el-menu) {
  border-right: none !important;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  padding: 0 20px !important;
}

:deep(.el-menu-item span) {
  margin-left: 0;
  font-size: 14px;
}

/* Handle text overflow in menu items */
:deep(.el-menu-item span) {
  display: inline-block;
  width: calc(100% - 40px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
