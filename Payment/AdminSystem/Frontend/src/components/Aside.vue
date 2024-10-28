<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">Tuition management system</b>
    </div>
    <div >
      <el-menu-item index="/home">
        <template slot="title">
          <i class="el-icon-house"></i>
          <span slot="title">home</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/user">
        <i class="el-icon-s-custom"></i>
        <span slot="title">User management</span>
      </el-menu-item>
      <el-menu-item index="/tuitionInvoice">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Tuition bill management</span>
      </el-menu-item>

      <el-menu-item index="/student">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Student management</span>
      </el-menu-item>
      <el-menu-item index="/analysis">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Statistical analysis</span>
      </el-menu-item>

    </div>


  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      admin: localStorage.getItem("admin") ? JSON.parse(localStorage.getItem("admin")) : null,
      photo: localStorage.getItem("photo") ? JSON.parse(localStorage.getItem("photo")) : null,
    }
  },
}
</script>

<style scoped>

</style>
