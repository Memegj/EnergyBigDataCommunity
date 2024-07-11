<template>
  <el-dialog
      :title="state.type == 'add' ? '文献新增' : '文献修改'"
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="团队名称" prop="teamName">
        <el-input type="text" v-model="state.ruleForm.teamName"></el-input>
      </el-form-item>
      <el-form-item label="团队简介" prop="teamIntro">
        <el-input type="text" v-model="state.ruleForm.teamIntro"></el-input>
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

const props = defineProps({
  type: String,
  reload: Function
})
const formRef = ref(null)
const state = reactive({
  visible: false,
  ruleForm: {
    teamName: "",
    teamIntro:""
  },
  rules: {
    teamName: [
      { required: 'true', message: '团队名称不能为空', trigger: ['change'] }
    ],
    teamIntro: [
      { required: 'true', message: '团队简介不能为空', trigger: ['change'] }
    ]
  },
  teamId: ''
})
// 获取详情
const getDetail = (teamId) => {
  axios.get(`/team/${teamId}`).then(res => {
    state.ruleForm = {
      teamName: res.teamName,
      teamIntro:res.teamIntro
    }
  })
}
// 开启弹窗
const open = (teamId) => {
  state.visible = true
  if (teamId) {
    // 如果有 excelid 传入，证明是修改模式
        state.teamId = teamId,// 将传入的 excelid 赋值给 state 对象的 excelid 属性
        state.type = 'edit'
    getDetail(teamId) // 调用 getDetail 函数获取详细信息
  } else {
    state.type = 'add'
    // 否则为新增模式
    state.ruleForm = {
      teamName: "" // 初始化 excelname 字段为空字符串
     , teamIntro:""

    };
  }
};

// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (props.type == 'add') {
        axios.post('/team', {
          teamName: state.ruleForm.teamName,
          teamIntro: state.ruleForm.teamIntro
        }).then(res => {
          if (res == true) {
            ElMessage.success('添加成功')
            state.visible = false
            if (props.reload) props.reload()
          } else ElMessage.error("添加失败")
        })
      }
      else {
        axios.put('/team', {
         teamId: state.teamId,
          teamName: state.ruleForm.teamName,
          teamIntro: state.ruleForm.teamIntro
        }).then(res => {
          if (res == true) {
            ElMessage.success('更新成功')
            state.visible = false
            if (props.reload) props.reload()
          } else ElMessage.error("更新失败")
        })
      }
    }
  })
}
defineExpose({ open, close })
</script>