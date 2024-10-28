<template>
  <div class="wrapper" :style="background" >
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="Please enter your account number" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="Please enter password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="Please confirm the password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="register">register</el-button>
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      // 顶部导航背景图片配置
      background: {
        // 背景图片地址
        backgroundImage: 'url(' + require('../assets/background.jpg') + ')',
        // 背景图片是否重复
        backgroundRepeat: 'no-repeat',
        // 背景图片大小
        backgroundSize: 'cover',
        // 背景颜色
        backgroundColor: '#000',
        // 背景图片位置
        backgroundPosition: 'center top'
      },
      user: {},
      rules: {
        username: [
          { required: true, message: 'Please enter your username', trigger: 'blur' },
          { min: 3, max: 10, message: 'The length is 3 to 5 characters', trigger: 'blur' }
        ],
        userPassword: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 6, max: 20, message: 'The length is 6 to 20 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 6, max: 20, message: 'The length is 6 to 20 characters', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    register() {
      console.log(this.user.password)
      console.log(this.user.confirmPassword)
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("The two passwords are different")
            return false
          }
          this.request.post("/user", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("Registered successfully")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  overflow: hidden;
}
</style>
