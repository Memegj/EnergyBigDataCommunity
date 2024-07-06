<template>
  <el-dialog
    :title="state.type == 'add' ? '文献新增' : '文献修改'"
    v-model="state.visible"
    width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="文献标题" prop="title">
        <el-input type="text" v-model="state.ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input type="text" v-model="state.ruleForm.author"></el-input>
      </el-form-item>
      <el-form-item label="发表时间" prop="publish_year">
        <el-input type="text" v-model="state.ruleForm.publish_year" ></el-input>
      </el-form-item>
      <el-form-item label="文件上传" prop="file">
        <el-input type="text" v-model="state.ruleForm.file_path"></el-input>
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
  type: String,
  reload: Function
})
const formRef = ref(null)
const state = reactive({
  visible: false,
  ruleForm: {
    title: "",
    author: "",
    publish_year: "",
    file_path: ""
  },
  rules: {
    title: [
      { required: 'true', message: '文献标题不能为空', trigger: ['change'] }
    ],
    author: [
      { required: 'true', message: '作者不能为空', trigger: ['change'] }
    ],
    publish_year: [
      { required: 'true', message: '发表年份不能为空', trigger: ['change'] }
    ],
    file_path: [
      { required: 'true', message: '文件不能为空', trigger: ['change'] }
    ]
  },
  id: ''
})
// 获取详情
const getDetail = (id) => {
  axios.get(`/references/${id}`).then(res => {
    state.ruleForm = {
      title: res.title,
      author: res.author,
      publish_year: res.publish_year,
      file_path: res.file_path
    }
  })
}
// 开启弹窗
const open = (id) => {
  state.visible = true
  if (id) {
    state.id = id
    // 如果是有 id 传入，证明是修改模式
    getDetail(id)
  } else {
    // 否则为新增模式
    state.ruleForm = {
      title: "",
      author: "",
      publish_year: "",
      file_path: ""
    }
  }
}
// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (props.type == 'add') {
        axios.post('/references', {
          title: state.ruleForm.title,
          author: state.ruleForm.author,
          publish_year: state.ruleForm.publish_year,
          file_path: state.ruleForm.file_path
        }).then(res => {
          if (res == true) {
            ElMessage.success('添加成功')
            state.visible = false
            if (props.reload) props.reload()
          } else ElMessage.error("添加失败")
        })
      }
      else {
        axios.put('/references', {
          id: state.id,
          title: state.ruleForm.title,
          author: state.ruleForm.author,
          publish_year: state.ruleForm.publish_year,
          file_path: state.ruleForm.file_path
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