<template>
  <div class="wrapper" :style="background">
    <!-- Welcome Section -->
    <div style="text-align: center; margin-top: 200px">
      <h1 style="font-size: 50px; color: white">
        Tuition Management System
      </h1>
    </div>

    <!-- Login Form Section -->
    <div style="margin: 200px auto; margin-top: 0; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px">
        <b>Login</b>
      </div>

      <el-form :model="user" :rules="rules" ref="userForm">
        <!-- Username Input -->
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>

        <!-- Password Input -->
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>

        <!-- Verification Code Input -->
        <el-form-item>
          <el-col :span="24">
            <el-input
                placeholder="Please enter the verification code"
                v-model="code"
            ></el-input>
            <div class="login-code" width="100%" @click="refreshCode">
              <!-- Verification Code Component -->
              <dentify :identifyCode="identifyCode"></dentify>
            </div>
          </el-col>
        </el-form-item>

        <!-- Role Selector -->
        <el-form-item prop="role">
          <el-select v-model="user.role" placeholder="Please select">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <!-- Action Buttons -->
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">Login</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">Register</el-button>
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
      // Background styling for the page
      background: {
        backgroundImage: `url(${require('../assets/background.jpg')})`, // Background image
        backgroundRepeat: 'no-repeat', // Prevent background repetition
        backgroundSize: 'cover', // Cover entire container
        backgroundColor: '#000', // Fallback background color
        backgroundPosition: 'center top' // Position background image
      },
      user: {}, // User data
      options: [
        { value: '2', label: 'Student' },
        { value: '1', label: 'Admin' },
      ],
      code: '', // User-entered verification code
      identifyCodes: "1234567890abcdefjhijklinopqrsduvwxyz", // Pool of characters for verification code
      identifyCode: "", // Generated verification code
      rules: {
        username: [
          { required: true, message: 'Please enter your username', trigger: 'blur' },
          { min: 3, max: 10, message: 'The length is 3 to 10 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter your password', trigger: 'blur' },
          { min: 1, max: 20, message: 'The length is 1 to 20 characters', trigger: 'blur' }
        ],
      },
    };
  },
  mounted() {
    // Initialize the verification code on component mount
    this.identifyCode = "";
    this.makeCode(this.identifyCodes, 4);
  },
  methods: {
    // Handles user login
    login() {
      this.request.post("/user/login", this.user).then(res => {
        if (res.code === '200') {
          // Store user data and navigate based on role
          localStorage.setItem("user", JSON.stringify(res.data[0]));
          const redirectPath = this.user.role === '1' ? "/" : "/front/home";
          this.$router.push(redirectPath);
          this.$message.success("Login successful");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // Resets the verification code
    refreshCode() {
      this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
    },
    // Generates the verification code
    makeCode(chars, length) {
      for (let i = 0; i < length; i++) {
        this.identifyCode += chars[this.randomNum(0, chars.length)];
      }
    },
    // Generates a random number between min and max
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
  }
};
</script>

<style>
.wrapper {
  height: 100vh;
  overflow: hidden; /* Prevent scrolling */
}
</style>
