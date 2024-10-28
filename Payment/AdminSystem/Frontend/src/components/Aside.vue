<template>
  <el-menu class="aside-menu"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#fff"
           :default-active="$route.path"
           router
  >
    <!-- Logo区域 -->
    <div class="logo-container">
      <img src="../assets/logo.png" alt="" class="logo-img">
      <span class="title"> Management </span>
    </div>

    <!-- 菜单区域 -->
    <div class="menu-container">
      <el-menu-item index="/home" class="menu-item">
        <i class="el-icon-s-home"></i>
        <span>Home</span>
      </el-menu-item>

      <el-menu-item index="/user" class="menu-item">
        <i class="el-icon-user"></i>
        <span>User Management</span>
      </el-menu-item>

      <el-menu-item index="/tuitionInvoice" class="menu-item">
        <i class="el-icon-document"></i>
        <span>Tuition Bill Management</span>
      </el-menu-item>

      <el-menu-item index="/student" class="menu-item">
        <i class="el-icon-reading"></i>
        <span>Student Management</span>
      </el-menu-item>

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
      admin: localStorage.getItem("admin") ? JSON.parse(localStorage.getItem("admin")) : null,
      photo: localStorage.getItem("photo") ? JSON.parse(localStorage.getItem("photo")) : null,
    }
  },
}
</script>

<style scoped>
.aside-menu {
  min-height: 100%;
  overflow-x: hidden;
  border-right: none;
  width: 240px !important;
}

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

.menu-container {
  margin-top: 10px;
}

.menu-item {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}

/* 图标样式 */
i {
  font-size: 18px;
  width: 24px;
  text-align: center;
  margin-right: 8px;
}

/* 菜单项悬停效果 */
.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

/* 选中菜单项样式 */
.el-menu-item.is-active {
  background-color: #1890ff !important;
}

/* 滚动条美化 */
.aside-menu::-webkit-scrollbar {
  width: 6px;
}

.aside-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.aside-menu::-webkit-scrollbar-track {
  background: transparent;
}

/* 去除 element-ui 默认样式 */
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

/* 处理文字溢出 */
:deep(.el-menu-item span) {
  display: inline-block;
  width: calc(100% - 40px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>