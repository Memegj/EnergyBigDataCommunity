<template>
  <el-dialog
      :title="state.type == 'add' ? '学生信息新增' : '学生信息修改'"
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="学生姓名" prop="stuname">
        <el-input type="text" v-model="state.ruleForm.stuname"></el-input>
      </el-form-item>
      <el-form-item label="学生班级" prop="stuclass">
        <el-input type="text" v-model="state.ruleForm.stuclass"></el-input>
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
    stuname: "",
    stuclass: "",
    id:"",
    type: 'add' // 操作类型
  },
  rules: {
    stuname: [
      { required: 'true', message: '姓名不能为空', trigger: ['change'] }
    ],
    stuclass: [
      { required: 'true', message: '班级不能为空', trigger: ['change'] }
    ]
  }
})

// 获取详情
const getDetail = (id) => {
  axios.get(`/stu/${id}`).then(res => {
    state.ruleForm = {
      stuname: res.stuname,
      stuclass: res.stuclass,
    }
  })
}

// 开启弹窗
const open = (id) => {
  state.visible = true
  if (id) {
    state.id=id,
    state.type = 'edit'  // 设置为修改模式
    // 如果是有 id 传入，证明是修改模式
    getDetail(id)
  } else {
    state.type = 'add'  // 设置为新增模式
    // 否则为新增模式
    state.ruleForm = {
      stuname: "",
      stuclass: ""
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
      if (state.type == 'add') {
      axios.post('/stu', {
        stuname: state.ruleForm.stuname,
        stuclass: state.ruleForm.stuclass
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
      else {
        axios.put('/stu', {
          id: state.id, // 传递 id，动态为 state 添加 id 属性
          stuname: state.ruleForm.stuname,
          stuclass: state.ruleForm.stuclass
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