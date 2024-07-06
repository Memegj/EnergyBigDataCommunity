<template>
  <el-dialog
    :title="新增用户"
    v-model="state.visible"
    width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="用户名" prop="username">
        <el-input type="text" v-model="state.ruleForm.username"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input type="text" v-model="state.ruleForm.nickname"></el-input>
      </el-form-item>
      <el-form-item label="初始密码" prop="password">
        <el-input type="text" v-model="state.ruleForm.password" placeholder="123456"></el-input>
      </el-form-item>
      <el-form-item label="用户角色" prop="user_role">
        <el-select ref="role_select" v-model="state.ruleForm.user_role" placeholder="请选择用户角色" @change="getSelect">
          <el-option label="学生" value="student" />
          <el-option label="老师" value="teacher"/>
          <el-option label="管理员" value="admin"/>
        </el-select>
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="state.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import md5 from "js-md5";

const props = defineProps({
  reload: Function
})
const formRef = ref(null)
const role_select = ref(null)
const state = reactive({
  visible: false,
  ruleForm: {
    username: "",
    nickname: "",
    password: "",
    user_role: ""
  },
  rules: {
    username: [
      { required: 'true', message: '名称不能为空', trigger: ['change'] }
    ],
    nickname: [
      { required: 'true', message: '昵称不能为空', trigger: ['change'] }
    ],
    password: [
      { required: 'true', message: '密码不能为空', trigger: ['change'] }
    ],
    user_role: [
      { required: 'true', message: '请选择用户角色', trigger: ['change'] }
    ]
  }
})
// 开启弹窗
const open = () => {
  state.visible = true
  state.ruleForm = {
    username: "",
    nickname: "",
    password: "123456",
    user_role: ""
    }
}
// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
        axios.post('/users/user_add', {
          username: state.ruleForm.username,
          nickname: state.ruleForm.nickname,
          password: md5(state.ruleForm.password),
          user_role: state.ruleForm.user_role
        }).then(res => {
          if(res == true)
          {
            ElMessage.success('添加成功')
            state.visible = false
            if (props.reload) props.reload()
          }
          else ElMessage.error("添加失败")
        })
    }
  })
}
const getSelect = (value) => {
  state.ruleForm.user_role = value
}
defineExpose({ open, close })
</script>