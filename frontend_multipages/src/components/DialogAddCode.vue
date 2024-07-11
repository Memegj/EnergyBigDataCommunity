<template>
  <el-dialog
      :title="state.type == 'add' ? '文献新增' : '文献修改'"
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="Code名称" prop="codename">
        <el-input type="text" v-model="state.ruleForm.codename"></el-input>
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
    codename: ""
  },
  rules: {
    codename: [
      { required: 'true', message: 'excel标题不能为空', trigger: ['change'] }
    ]
  },
  codeid: ''
})
// 获取详情
const getDetail = (codeid) => {
  axios.get(`/excel1/${codeid}`).then(res => {
    state.ruleForm = {
      codename: res.codename,
    }
  })
}
// 开启弹窗
const open = (codeid) => {
  state.visible = true
  if (codeid) {
    // 如果有 excelid 传入，证明是修改模式
    state.codeid = codeid,// 将传入的 excelid 赋值给 state 对象的 excelid 属性
    state.type = 'edit'
    getDetail(codeid) // 调用 getDetail 函数获取详细信息
  } else {
    state.type = 'add'
    // 否则为新增模式
    state.ruleForm = {
      codename: "" // 初始化 excelname 字段为空字符串

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
        axios.post('/excel1', {
          codename: state.ruleForm.codename
        }).then(res => {
          if (res == true) {
            ElMessage.success('添加成功')
            state.visible = false
            if (props.reload) props.reload()
          } else ElMessage.error("添加失败")
        })
      }
      else {
        axios.put('/excel1', {
          codeid: state.codeid,
          codename: state.ruleForm.codename
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