<template>
  <div class="wrapper" :style="background">
    <!-- Registration Form Container -->
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px">
        <b>Register</b>
      </div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <!-- Username Input -->
        <el-form-item prop="username">
          <el-input
              placeholder="Please enter your account number"
              size="medium"
              style="margin: 5px 0"
              prefix-icon="el-icon-user"
              v-model="user.username"
          ></el-input>
        </el-form-item>

        <!-- Password Input -->
        <el-form-item prop="password">
          <el-input
              placeholder="Please enter password"
              size="medium"
              style="margin: 5px 0"
              prefix-icon="el-icon-lock"
              show-password
              v-model="user.password"
          ></el-input>
        </el-form-item>

        <!-- Confirm Password Input -->
        <el-form-item prop="confirmPassword">
          <el-input
              placeholder="Please confirm the password"
              size="medium"
              style="margin: 5px 0"
              prefix-icon="el-icon-lock"
              show-password
              v-model="user.confirmPassword"
          ></el-input>
        </el-form-item>

        <!-- Action Buttons -->
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="register">Register</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      // Background styling for the page
      background: {
        backgroundImage: `url(${require('../assets/background.jpg')})`, // Background image
        backgroundRepeat: 'no-repeat', // Prevent background repetition
        backgroundSize: 'cover', // Cover the container
        backgroundColor: '#000', // Fallback background color
        backgroundPosition: 'center top' // Position the background image
      },
      user: {}, // User data model
      rules: {
        // Validation rules for the username
        username: [
          { required: true, message: 'Please enter your username', trigger: 'blur' },
          { min: 3, max: 10, message: 'The length is 3 to 10 characters', trigger: 'blur' }
        ],
        // Validation rules for the password
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 6, max: 20, message: 'The length is 6 to 20 characters', trigger: 'blur' }
        ],
        // Validation rules for confirming the password
        confirmPassword: [
          { required: true, message: 'Please confirm the password', trigger: 'blur' },
          { min: 6, max: 20, message: 'The length is 6 to 20 characters', trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    // Handles user registration
    register() {
      console.log(this.user.password);
      console.log(this.user.confirmPassword);

      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          // Ensure the two passwords match
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("The two passwords are different");
            return false;
          }
          // Submit the registration data
          this.request.post("/user", this.user).then(res => {
            if (res.code === '200') {
              this.$message.success("Registered successfully");
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    }
  }
};
</script>

<style>
.wrapper {
  height: 100vh; /* Full viewport height */
  overflow: hidden; /* Prevent scrolling */
}
</style>
