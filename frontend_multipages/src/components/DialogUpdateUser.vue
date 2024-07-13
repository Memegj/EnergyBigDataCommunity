<template>
  <el-dialog
      :title= '用户修改'
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="用户名" prop="UserName">
        <el-input type="text" v-model="state.ruleForm.UserName"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="NickName">
        <el-input type="text" v-model="state.ruleForm.NickName"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="UserEmail">
        <el-input type="text" v-model="state.ruleForm.UserEmail"></el-input>
      </el-form-item>
      <el-form-item label="所属院系" prop="UserCollege">
        <el-input type="text" v-model="state.ruleForm.UserCollege"></el-input>
      </el-form-item>
      <el-form-item label="用户角色" prop="user_role">
        <el-select ref="role_select" v-model="state.ruleForm.user_role"  @change="getSelect">
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

const state = reactive({
  visible: false,
  ruleForm: {
    UserName: "",
    NickName: "",
    UserEmail:"",
    UserCollege:"",
    user_role: ""

  },
  rules: {
    UserName: [
      { required: 'true', message: '名称不能为空', trigger: ['change'] }
    ],
    NickName: [
      { required: 'true', message: '昵称不能为空', trigger: ['change'] }
    ],
    UserEmail: [
      { required: 'true', message: '邮箱不能为空', trigger: ['change'] }
    ],UserCollege: [
      { required: 'true', message: '院系不能为空', trigger: ['change'] }
    ],
    user_role: [
      { required: 'true', message: '请选择用户角色', trigger: ['change'] }
    ]
  },
})
// 获取详情
const getDetail = (userId) => {
  axios.get(`/users/${userId}`).then(res => {
    state.ruleForm = {
      UserId: res.userId,
      UserName: res.userName,
      NickName: res.nickName,
      UserEmail: res.userEmail,
      UserCollege: res.userCollege,
      user_role: res.user_role
    }
  })
}
// 开启弹窗
const open = (userId) => {
  state.visible = true
  if (userId) {
    state.userId=userId
    // 如果是有 id 传入，证明是修改模式
    getDetail(userId)
  }
}
// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      axios.put('/users/update', {
        userId: state.ruleForm.UserId,
        userName: state.ruleForm.UserName,
        nickName: state.ruleForm.NickName,
        userEmail: state.ruleForm.UserEmail,
        userCollege: state.ruleForm.UserCollege,
        user_role: state.ruleForm.user_role

      }).then(res => {
        if (res == true) {
          ElMessage.success('修改成功')
          state.visible = false
          if (props.reload) props.reload()
        } else ElMessage.error("修改失败")
      })
    }
  })
}
const getSelect = (value) => {
  state.ruleForm.user_role = value
}
defineExpose({ open, close })
</script>