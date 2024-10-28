<template>
  <div id="app">
    <router-view class="main-container"/>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
  "Helvetica Neue", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.main-container {
  min-height: 100vh;
  padding: 20px;
}

/* Element UI 样式优化 */
.el-button {
  border-radius: 4px;
}

.el-input__inner {
  border-radius: 4px;
}

.el-card {
  border-radius: 4px;
  border: none;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08) !important;
}

.el-table {
  border-radius: 4px;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* 通用样式类 */
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-overflow {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 页面标题样式 */
.page-title {
  margin-bottom: 20px;
  color: #1f2f3d;
  font-size: 20px;
  font-weight: 500;
}

/* 常用间距 */
.mb-10 { margin-bottom: 10px; }
.mb-20 { margin-bottom: 20px; }
.mt-10 { margin-top: 10px; }
.mt-20 { margin-top: 20px; }
.mr-10 { margin-right: 10px; }
.ml-10 { margin-left: 10px; }

/* 常用颜色 */
.text-primary { color: #1890ff; }
.text-success { color: #52c41a; }
.text-warning { color: #faad14; }
.text-danger { color: #f5222d; }
.text-gray { color: #666; }

/* 背景色 */
.bg-white { background-color: #fff; }
.bg-gray { background-color: #f5f5f5; }
.bg-primary { background-color: #e6f7ff; }
.bg-success { background-color: #f6ffed; }
.bg-warning { background-color: #fffbe6; }
.bg-danger { background-color: #fff1f0; }

/* 响应式布局 */
@media screen and (min-width: 1600px) {
  .main-container {
    padding: 20px 40px;
  }
}

@media screen and (max-width: 768px) {
  .main-container {
    padding: 10px;
  }

  .el-card {
    margin-bottom: 10px;
  }
}
</style>