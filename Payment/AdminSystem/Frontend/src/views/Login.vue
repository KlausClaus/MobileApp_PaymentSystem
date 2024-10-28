<template>
    <div class="wrapper" :style="background" >
    <div style="text-align: center;margin-top: 200px">
      <h1 style="font-size: 50px;color: white">
        Tuition management system
      </h1>
    </div>
    <div style="margin: 200px auto ;margin-top: 0; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Denlu</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item>

          <el-col :span=24>
            <el-input
                placeholder="Please enter the verification code"
                v-model="code"
            ></el-input>
            <div class="login-code" width="100%" @click="refreshCode">
              <!--验证码组件-->
              <dentify :identifyCode="identifyCode"></dentify></div
            ></el-col>
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="user.role" placeholder="Please select">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="login">login</el-button>
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">register</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import dentify from "./dentify";
export default {
  name: "Login",
  components: {
    dentify,
  },
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
      role: "",
      options: [
         {
          value: '2',
          label: '学生'
        }, {
          value: '1',
          label: '管理员'
        },
      ],
      code:'',
      identifyCodes: "1234567890abcdefjhijklinopqrsduvwxyz", //随机串内容,从这里随机抽几个显示验证码
      identifyCode: "", //验证码图片内容
      rules: {
        username: [
          { required: true, message: 'Please enter your username', trigger: 'blur' },
          { min: 3, max: 10, message: 'The length is 3 to 5 characters', trigger: 'blur' }
        ],
        userPassword: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'The length is 1 to 20 characters', trigger: 'blur' }
        ],
      },
    }
  },
  mounted() {
    // 初始化验证码
    this.identifyCode = "";
    //参数：（1）随机串内容。（2）验证码显示位数
    this.makeCode(this.identifyCodes, 4);
  },
  methods: {
    login() {
      this.request.post("/user/login", this.user).then(res => {
        if (res.code === '200') {
          if (this.user.role==='1'){
            localStorage.setItem("user", JSON.stringify(res.data[0]))
            this.$router.push("/")
            this.$message.success("Login successful")
          }else if (this.user.role==='2') {
            localStorage.setItem("user", JSON.stringify(res.data[0]))
            this.$router.push("/front/home")
            this.$message.success("Login successful")
          }

        }else {
          this.$message.error(res.msg)
        }
      })

    },
    // 重置验证码
    refreshCode() {
      this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
    },
    //获取验证码的值
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        //通过循环获取字符串内随机几位
        this.identifyCode +=
            this.identifyCodes[this.randomNum(0, this.identifyCodes.length)];
      }
    },
    //随机数字：用于当角标拿字符串的值
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },


  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  overflow: hidden;
}
</style>
