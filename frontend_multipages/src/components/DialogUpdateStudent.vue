<template>
  <el-dialog
      :title= '学生修改'
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="姓名" prop="UserName">
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
      <el-form-item label="所属团队" prop="TeamName">
        <el-select ref="role_select" v-model="state.ruleForm.TeamName" placeholder="请选择学生所在团队" @change="getSelect">
          <el-option
              v-for="item in state.nameData" :key="item.value" :label="item.label" :value="[item.key,item.value]">
          </el-option>
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
import {onMounted, reactive, ref} from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import md5 from "js-md5";
import {localGet} from "@/utils/index.js";


const props = defineProps({
  reload: Function
})
const formRef = ref(null)

const state = reactive({
  nameData:[],
  visible: false,
  ruleForm: {
    UserId:"",
    UserName: "",
    NickName: "",
    UserEmail:"",
    UserCollege:"",
    user_role: "",
    TeamName: "",
    TeamId:"",
    User_teamId:""

  },
  rules: {
    UserId: [
      { required: 'true', message: '学号不能为空', trigger: ['change'] }
    ],
    UserName: [
      { required: 'true', message: '姓名不能为空', trigger: ['change'] }
    ],
    NickName: [
      { required: 'true', message: '昵称不能为空', trigger: ['change'] }
    ],
    UserEmail: [
      { required: 'true', message: '邮箱不能为空', trigger: ['change'] }
    ],UserCollege: [
      { required: 'true', message: '院系不能为空', trigger: ['change'] }
    ],
    TeamName: [
      { required: 'true', message: '请选择所属团队', trigger: ['change'] }
    ]
  },
})
// 获取详情
const getDetail = (userId, teamId) => {
  // 确保同时传入userId和teamId
  axios.get(`/students/${userId}?teamId=${encodeURIComponent(teamId)}`)
    .then(res => {
    state.ruleForm = {
      UserId: res.userId,
      UserName: res.userName,
      NickName: res.nickName,
      UserEmail: res.userEmail,
      UserCollege: res.userCollege,
      TeamName: res.teamName,
      TeamId:res.teamId,
      User_teamId:res.user_teamId
    }
  })
}
onMounted(() => {
  //绑定下拉框
  getNameOptions()

})
const getNameOptions = () => {
  //读取和设置姓名下拉框
  axios.get('/students/get_name_options')
      .then(res=> {
        state.nameData = [];
        res.forEach(item => {
          console.log(item);
          let arr = {
            value: item.teamName,
            label: item.teamName,
            key:item.teamId,
          };
          state.nameData.push(arr);
        });
      })
      .catch(function (err) {
        console.log(err)
      })
}

// 开启弹窗
const open = (userId) => {
  state.visible = true
  if (userId) {
    state.userId=userId[0]
    state.teamId=userId[1]
    // 如果是有 id 传入，证明是修改模式
    getDetail(userId[0],userId[1])
  }
}
// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      axios.put('/students/update', {
        userId: state.ruleForm.UserId,
        userName: state.ruleForm.UserName,
        nickName: state.ruleForm.NickName,
        userEmail: state.ruleForm.UserEmail,
        userCollege: state.ruleForm.UserCollege,
        teamName: state.ruleForm.TeamName,
        teamId: state.ruleForm.TeamId,
        user_teamId:state.ruleForm.User_teamId

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
  state.ruleForm.TeamName = value[1]
  state.ruleForm.TeamId=value[0]
}
defineExpose({ open, close })
</script>