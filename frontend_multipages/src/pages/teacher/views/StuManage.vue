<template>
  <el-card class="user-container">
    <template #header>
      <div class="header">
        <el-row>
        <el-button type="primary" :icon="Plus" @click="handleAddStudent">添加学生</el-button>
        <el-button type="success" :icon="Check" @click="handleSolve">解除禁用</el-button>
        <el-button type="danger" :icon="Delete" @click="handleForbid">禁用学生</el-button>
        <el-popconfirm
            title="确定删除吗？"
            confirmButtonText='确定'
            cancelButtonText='取消'
            @confirm="handleDelete"
        >
          <template #reference>
            <el-button type="danger" :icon="Delete">批量删除</el-button>
          </template>
        </el-popconfirm>
        <el-form-item label="团队名称" prop="TeamName" style="font-size: 15px;margin-left: 260px" label-width="auto" >
          <el-select ref="role_select" v-model="state.TeamName" placeholder="请选择团队名称" style="width: 200px" @change="getSelect">
            <el-option
                v-for="item in state.nameData" :key="item.value" :label="item.label" :value="[item.key,item.value]">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item >
          <div style="text-align:right;margin-left: 18px">
            <el-button type="primary" @click="searchHandler">查询</el-button>
            <el-button type="primary"  @click="searchAll">返回全部</el-button>
          </div>
        </el-form-item>

        </el-row>

      </div>
    </template>
    <Table  ref="table" action='/students/list'>
      <template #column>
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="userId"
          label="学号"
          header-align="center"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userName"
          label="姓名"
          header-align="center"
          align="center"
        >
        </el-table-column>
        <el-table-column
            prop="userCollege"
            label="所属院系"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="teamName"
            label="所属团队"
            header-align="center"
            align="center">

        </el-table-column>
        <el-table-column
          label="身份状态"
          header-align="center"
          align="center"
        >
          <template #default="scope">
            <span :style="scope.row.userLocked == 0 ? 'color: green;' : 'color: red;'">
              {{ scope.row.userLocked == 0 ? '正常' : '禁用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="registerTime"
          label="注册时间"
          header-align="center"
          align="center"
        >
        </el-table-column>
        <el-table-column
            label="操作"
            width="220"
            header-align="center"
            align="center"

        >
          <template #default="scope">
            <a style="cursor: pointer; margin-right: 10px" @click="handleEdit([scope.row.userId,scope.row.teamId])">修改</a>
            <el-popconfirm
                title="确定删除吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="handleDeleteOne([scope.row.userId,scope.row.teamId])"
            >
              <template #reference>
                <a style="cursor: pointer">删除</a>
              </template>
            </el-popconfirm>
            &nbsp;
            <el-popconfirm
                title="确定重置吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="handleResetPassword(scope.row.userId)"
            >
              <template #reference>
                <a style="cursor: pointer">重置密码</a>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </template>
    </Table>

  </el-card>
  <DialogAddStudent ref='addStudent' :reload="reload_list"></DialogAddStudent>
  <DialogUpdateStudent ref='updateStudent' :reload="reload_list" ></DialogUpdateStudent>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import Table from '@/pages/teacher/views/Table_Teacher.vue'
import DialogAddStudent from "@/components/DialogAddStudent.vue";
import { ElMessage } from 'element-plus'
import { Plus, Delete, Check } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogUpdateStudent from "@/components/DialogUpdateStudent.vue";
import md5 from "js-md5";
let addStudent = ref(null)
let table = ref(null)
let updateStudent = ref(null)

const handleSolve = () => {
if (!table.value.state.multipleSelection.length) {
  ElMessage.error('请选择项')
  return
}
axios.put(`/students/0`, {
  ids: table.value.state.multipleSelection.map(item => item.userId)
}).then(() => {
  ElMessage.success('解除成功')
  table.value.getList()
})
}
const handleForbid = () => {
if (!table.value.state.multipleSelection.length) {
  ElMessage.error('请选择项')
  return
}
axios.put(`/students/1`, {
  ids: table.value.state.multipleSelection.map(item => item.userId)
}).then(() => {
  ElMessage.success('禁用成功')
  table.value.getList()
})
}
const state = reactive({
  nameData:[],
  visible: false,
  TeamName: "",
  TeamId:""
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
const getSelect = (value) => {
  state.TeamName = value[1]
  table.value.state.TeamId=value[0]
}
const searchHandler = () => {
  table.value.getList()
}
const searchAll = () => {
  state.loading = true
  state.TeamName=null
  table.value.state.TeamId=null
  table.value.getList()
}

const handleAddStudent = () => {
  addStudent.value.open()
}
const reload_list = () => {
  table.value.getList()
}
// 批量删除
const handleDelete = () => {
  if (!table.value.state.multipleSelection.length) {
    ElMessage.error('请选择项')
    return
  }
  axios.delete('/students', {
    data: {
      teamId:table.value.state.multipleSelection.map(i => i.teamId),
      ids: table.value.state.multipleSelection.map(i => i.userId)
    }
  }).then(() => {
    ElMessage.success('删除成功')
    table.value.getList()
  })
}
// 单个删除
const handleDeleteOne = (userId) => {
  axios.delete('/students', {
    data: {
      teamId:[userId[1]],
      ids: [userId[0]]
    }
  }).then(() => {
    ElMessage.success('删除成功')
    table.value.getList()
  })
}
const handleEdit = (userId) => {

  updateStudent.value.open(userId)
}
const handleResetPassword=(userId) => {
  axios.put('/students/password', {
    userId: userId,
    userPassword:md5("123456")

  }).then(() => {
    ElMessage.success('成功重置为：123456')
    table.value.getList()
  })
}
</script>

<style>

</style>