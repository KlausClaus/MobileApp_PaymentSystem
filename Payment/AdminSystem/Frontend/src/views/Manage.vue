<template>
  <el-container style="min-height: 100vh">
    <!-- Sidebar Section -->
    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <!-- Sidebar Component -->
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
    </el-aside>

    <el-container>
      <!-- Header Section -->
      <el-header style="border-bottom: 1px solid #ccc;">
        <!-- Header Component -->
        <Header :collapseBtnClass="collapseBtnClass" :collapse="isCollapse" />
      </el-header>

      <!-- Main Content Section -->
      <el-main>
        <!-- Displays child routes using router-view -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold', // Icon class for the collapse button
      isCollapse: false, // State for sidebar collapse
      sideWidth: 200, // Width of the sidebar
      logoTextShow: true, // Determines if the logo text is visible
    };
  },
  components: {
    Aside,
    Header
  },
  methods: {
    // Toggles the sidebar collapse state
    collapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        // Collapsed state
        this.sideWidth = 64;
        this.collapseBtnClass = 'el-icon-s-unfold'; // Change icon for expand
        this.logoTextShow = false; // Hide logo text
      } else {
        // Expanded state
        this.sideWidth = 200;
        this.collapseBtnClass = 'el-icon-s-fold'; // Change icon for collapse
        this.logoTextShow = true; // Show logo text
      }
    }
  }
};
</script>
