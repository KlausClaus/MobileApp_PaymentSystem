<template>
  <div id="app">
    <!-- Router view for displaying routed components -->
    <router-view class="main-container"/>
  </div>
</template>

<style>
/* Global reset and box-sizing */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* Basic styling for the HTML and body */
html, body {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
  "Helvetica Neue", Arial, sans-serif;
  -webkit-font-smoothing: antialiased; /* Anti-aliasing for smoother fonts */
  -moz-osx-font-smoothing: grayscale;
}

/* Root container styling */
#app {
  min-height: 100vh;
  /* Background with a subtle gradient */
  background: linear-gradient(145deg, rgba(255, 248, 248, 0.44) 0%, rgba(199, 178, 178, 0.96) 100%);
}

/* Main container to hold routed components */
.main-container {
  min-height: 100vh;
  padding: 20px;
}

/* Element UI component style optimization */
.el-button {
  border-radius: 4px;
}

.el-input__inner {
  border-radius: 4px;
}

.el-card {
  border-radius: 4px;
  border: none;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08) !important; /* Subtle shadow for card elements */
}

.el-table {
  border-radius: 4px;
}

/* Scrollbar customization */
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

/* Common utility classes for layout and alignment */
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

/* Text truncation utility */
.text-overflow {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Page title styling */
.page-title {
  margin-bottom: 20px;
  color: #1f2f3d;
  font-size: 20px;
  font-weight: 500;
}

/* Spacing utilities */
.mb-10 { margin-bottom: 10px; }
.mb-20 { margin-bottom: 20px; }
.mt-10 { margin-top: 10px; }
.mt-20 { margin-top: 20px; }
.mr-10 { margin-right: 10px; }
.ml-10 { margin-left: 10px; }

/* Text color utilities */
.text-primary { color: #1890ff; }
.text-success { color: #52c41a; }
.text-warning { color: #faad14; }
.text-danger { color: #f5222d; }
.text-gray { color: #666; }

/* Background color utilities */
.bg-white { background-color: #fff; }
.bg-gray { background-color: #f5f5f5; }
.bg-primary { background-color: #e6f7ff; }
.bg-success { background-color: #f6ffed; }
.bg-warning { background-color: #fffbe6; }
.bg-danger { background-color: #fff1f0; }

/* Responsive design adjustments */
@media screen and (min-width: 1600px) {
  .main-container {
    padding: 20px 40px; /* Add more padding for larger screens */
  }
}

@media screen and (max-width: 768px) {
  .main-container {
    padding: 10px; /* Reduce padding for smaller screens */
  }

  .el-card {
    margin-bottom: 10px; /* Add margin between cards for mobile layout */
  }
}
</style>
