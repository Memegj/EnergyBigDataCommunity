<template>
  <el-card class="account-container">
    <el-form :model="state.nameForm" :rules="state.rules" ref="nameRef" label-width="80px" label-position="right" class="demo-ruleForm">
      <el-form-item label="昵称" prop="nickName">
        <el-input style="width: 200px" v-model="state.nameForm.nickName"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="userEmail">
        <el-input style="width: 200px" v-model="state.nameForm.userEmail" :value="state.nameForm.userEmail"></el-input>
      </el-form-item>
      <el-form-item label="院系" prop="userCollege">
        <el-input style="width: 200px" v-model="state.nameForm.userCollege" :value="state.nameForm.userCollege"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="submitName">确认修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card class="account-container">
    <el-form :model="state.passForm" :rules="state.rules" ref="passRef" label-width="80px" label-position="right" class="demo-ruleForm">
      <el-form-item label="原密码：" prop="oldpass">
        <el-input style="width: 200px" v-model="state.passForm.oldpass"></el-input>
      </el-form-item>
      <el-form-item label="新密码：" prop="newpass">
        <el-input style="width: 200px" v-model="state.passForm.newpass"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="submitPass">确认修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import md5 from 'js-md5'
import {localGet} from "@/utils";

const nameRef = ref(null)
const passRef = ref(null)
const state = reactive({
  user: null,
  nameForm: {
    nickName: '',
    userEmail:'',
    userCollege:''
  },
  passForm: {
    oldpass: '',
    newpass: ''
  },
  rules: {
    nickName: [
      { required: 'true', message: '昵称不能为空', trigger: ['change'] }
    ],
    oldpass: [
      { required: 'true', message: '原密码不能为空', trigger: ['change'] }
    ],
    newpass: [
      { required: 'true', message: '新密码不能为空', trigger: ['change'] }
    ]
  },
})
onMounted(() => {
  axios.get('/user/profile', {
        params:
            {str_token: localGet('token')}
      }
  ).then(res => {
    state.user = res
    state.nameForm.nickName = res.NickName
    state.nameForm.userEmail = res.UserEmail
    state.nameForm.userCollege = res.UserCollege
  })
})
const submitName = () => {
  nameRef.value.validate((vaild) => {
    if (vaild) {
      axios.put('/user/name', {
        nickname: state.nameForm.nickName,
        useremail: state.nameForm.userEmail,
        usercollege: state.nameForm.userCollege,
        token: localGet('token')
      }).then(() => {
        ElMessage.success('修改成功')
        window.location.reload()
      })
    }
  })
}
const submitPass = () => {
  passRef.value.validate((vaild) => {
    if (vaild) {
      axios.put('/user/password', {
        originalPassword: md5(state.passForm.oldpass),
        newPassword: md5(state.passForm.newpass),
        token: localGet('token')
      }).then(() => {
        ElMessage.success('修改成功')
        window.location.reload()
      })
    }
  })
}
</script>

<style>
.account-container {
  margin-bottom: 20px;
}
</style>
