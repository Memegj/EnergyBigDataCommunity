<template>
  <el-dialog
      :title="新增学生"
      v-model="state.visible"
      width="400px"
  >
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="100px" class="user-form">
      <el-form-item label="学号" prop="UserId">
        <el-input type="text" v-model="state.ruleForm.UserId"></el-input>
      </el-form-item>
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
      <el-form-item label="初始密码" prop="UserPassword">
        <el-input type="text" v-model="state.ruleForm.UserPassword" placeholder="123456"></el-input>
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
    UserPassword: "",
    TeamName: "",
    TeamId:""

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
    UserPassword: [
      { required: 'true', message: '密码不能为空', trigger: ['change'] }
    ],
    TeamName: [
      { required: 'true', message: '请选择所属团队', trigger: ['change'] }
    ]
  }
})
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
            key:item.teamId
          };
          state.nameData.push(arr);
        });
      })
      .catch(function (err) {
        console.log(err)
      })
}

// 开启弹窗
const open = () => {
  state.visible = true
  state.ruleForm = {
    UserId:"",
    UserName: "",
    NickName: "",
    UserEmail:"",
    UserCollege:"",
    UserPassword: "123456",
    TeamName: "",
    TeamId:""
  }
}
// 关闭弹窗
const close = () => {
  state.visible = false
}
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      axios.post('/students/stu_add', {
        userId: state.ruleForm.UserId,
        userName: state.ruleForm.UserName,
        nickName: state.ruleForm.NickName,
        userEmail: state.ruleForm.UserEmail,
        userCollege: state.ruleForm.UserCollege,
        userPassword: md5(state.ruleForm.UserPassword),
        teamName: state.ruleForm.TeamName,
        teamId: state.ruleForm.TeamId

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
  state.ruleForm.TeamName = value[1]
  state.ruleForm.TeamId=value[0]
}


defineExpose({ open, close })
</script>