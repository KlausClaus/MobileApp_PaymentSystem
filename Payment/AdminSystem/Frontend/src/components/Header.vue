<template>
  <div style="line-height: 60px; display: flex">
    <!-- Left section: Breadcrumb navigation and collapse button -->
    <div style="flex: 1;">
      <!-- Button to toggle the collapse of the sidebar -->
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

      <!-- Breadcrumb navigation -->
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <!-- Root breadcrumb item -->
        <el-breadcrumb-item :to="'/'">Home page</el-breadcrumb-item>
        <!-- Uncomment this line to show the current path -->
        <!-- <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item> -->
      </el-breadcrumb>
    </div>

    <!-- Right section: User avatar and dropdown menu -->
    <el-dropdown style="width: 100px; cursor: pointer">
      <div style="display: inline-block">
        <!-- User avatar -->
        <img :src="user.avatarUrl" alt=""
             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <!-- User nickname -->
        <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <!-- Dropdown menu for user actions -->
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <!-- Navigate to personal information page -->
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="toGeren">Personal Info</span>
        </el-dropdown-item>
        <!-- Logout action -->
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">Logout</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    // Class for the collapse button (passed from parent component)
    collapseBtnClass: String,
  },
  // Uncomment to use computed property for the current path name
  // computed: {
  //   currentPathName() {
  //     return this.$store.state.currentPathName; // Monitors the current path name from Vuex store
  //   }
  // },
  data() {
    return {
      // Load user data from local storage, or initialize as an empty object if not found
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  methods: {
    // Emit an event to toggle the sidebar collapse state
    collapse() {
      this.$emit("asideCollapse")
    },
    // Logout the user and navigate to the login page
    logout() {
      this.$router.push("/login") // Redirect to login page
      localStorage.removeItem("user") // Remove user data from local storage
      this.$message.success("Logout successful") // Display success message
    },
    // Navigate to the personal information page
    toGeren() {
      this.$router.push("/person")
    }
  },
}
</script>

<style scoped>
/* Add styles here if needed */
</style>
