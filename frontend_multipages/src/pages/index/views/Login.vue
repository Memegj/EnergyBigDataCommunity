<template>
  <div class="login-body">
    <div class="login-container">
      <div class="head">
        <img class="logo" src="@/assets/创造太阳.png" alt=""/>
        <div class="name">
          <div class="title">能源大数据教学科研平台</div>
        </div>
      </div>
      <el-form label-position="top" :rules="state.rules" :model="state.ruleForm" ref="loginForm" class="login-form">
        <el-form-item label="账号" prop="UserName">
          <el-input type="text" v-model.trim="state.ruleForm.UserName" autocomplete="off"></el-input>
        </el-form-item>
        <br/>
        <el-form-item  label="密码" prop="UserPassword">
          <el-input type="password" v-model.trim="state.ruleForm.UserPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; height: 50px; margin-top: 20px;" type="primary" @click="submitForm">立即登录</el-button>
        </el-form-item>
      </el-form>
      <div class="tips">中国石油大学（华东）经济管理学院</div>
    </div>
  </div>
</template>

<script setup>
import axios from '@/utils/axios'
import md5 from 'js-md5'
import { reactive, ref } from 'vue'
import { localSet } from '@/utils'
//import { useRouter } from 'vue-router'

// import router_admin from '@/router/admin.js'
import router from '@/pages/index/router'
//const router = useRouter()
const loginForm = ref(null)
const state = reactive({
  ruleForm: {
    UserName: '',
    UserPassword: ''
  },
  checked: true,
  rules: {
    UserName: [
      { required: 'true', message: '账户不能为空', trigger: 'blur' }
    ],
    UserPassword: [
      { required: 'true', message: '密码不能为空', trigger: 'blur' }
    ]
  }
})
const submitForm = async () => {

  loginForm.value.validate((valid) => {
    if (valid) {
      axios.post('/user/login', {
        username: state.ruleForm.UserName,
        password: md5(state.ruleForm.UserPassword)
      }).then(res => {
        localSet('token', res.tokenStr)
        if(res.roleStr == 'admin') window.location.href = '/admin'
        //router.push('/admin')
        else if(res.roleStr == 'teacher') window.location.href = '/teacher'
        else if(res.roleStr == 'student') window.location.href = '/student'
        else if(res.roleStr == 'guest') window.location.href = '/'
        //router.push('/home')
        else if(res.roleStr == 'student') window.location.href = '/student'
        //router.push('/student')
        else if(res.roleStr == 'teacher') window.location.href = '/teacher'
        //router.push('/teacher')
        else{
          window.location.href = '/'
          //router.push('/home')
        }
          //window.location.href = '/home'

      })
    } else {
      console.log('error submit!!')
      return false;
    }
  })
}
const resetForm = () => {
  loginForm.value.resetFields();
}
</script>

<style scoped>
  .login-body {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    background-color: #fff;
  }
  .login-container {
    width: 500px;
    height: 450px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0px 21px 41px 0px rgba(0, 0, 0, 0.2);
  }
  .login-container .tips {
    font-size: 12px;
    color: #999;
    margin-top: 20px;
    text-align: center;
  }
  .head {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 0 20px 0;
  }
  .head img {
    width: 80px;
    height: 80px;
    margin-right: 10px;
  }
  .head .title {
    font-size: 22px;
    color: #01374d;
    font-weight: bold;
  }

  .login-form {
    width: 70%;
    margin: 0 auto;
  }
  .login-form .el-form--label-top .el-form-item__label {
    padding: 0;
  }
  .login-form >>> .el-form-item {
    margin-bottom: 0;
  }
</style>