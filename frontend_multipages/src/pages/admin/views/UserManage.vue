<template>
  <el-card class="user-container">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="handleAddUser">添加用户</el-button>
        <el-button type="success" :icon="Check" @click="handleSolve">解除禁用</el-button>
        <el-button type="danger" :icon="Delete" @click="handleForbid">禁用用户</el-button>
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
      </div>
    </template>
    <Table  action='/users/list' ref="table" >
      <template #column>
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="userName"
            label="用户名"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="nickName"
            label="昵称"
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
            prop="userEmail"
            label="用户邮箱"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="user_role"
            label="用户角色"
            header-align="center"
            align="center">

        </el-table-column>
        <el-table-column
            prop="registerTime"
            label="注册时间"
            header-align="center"
            align="center"
        >
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
            label="操作"
            width="220"
            header-align="center"
            align="center"

        >
          <template #default="scope">
            <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.userId)">修改</a>
            <el-popconfirm
                title="确定删除吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="handleDeleteOne(scope.row.userId)"
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
  <DialogAddUser ref='addUser' :reload="reload_list" ></DialogAddUser>
  <DialogUpdateUser ref='updateUser' :reload="reload_list" ></DialogUpdateUser>
</template>

<script setup>
import { ref } from 'vue'
import Table from '@/components/Table.vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete, Check } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddUser from "@/components/DialogAddUser.vue";
import DialogUpdateUser from "@/components/DialogUpdateUser.vue";
let addUser = ref(null)
let updateUser = ref(null)
let table = ref(null) //响应式引用
import md5 from "js-md5";

const handleSolve = () => {
  if (!table.value.state.multipleSelection.length) {
    ElMessage.error('请选择项')
    return
  }
  axios.put(`/users/0`, {
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
  axios.put(`/users/1`, {
    ids: table.value.state.multipleSelection.map(item => item.userId)
  }).then(() => {
    ElMessage.success('禁用成功')
    table.value.getList()
  })
}


const handleAddUser = () => {
  addUser.value.open()
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
  axios.delete('/users', {
    data: {
      ids: table.value.state.multipleSelection.map(i => i.userId)
    }
  }).then(() => {
    ElMessage.success('删除成功')
    table.value.getList()
  })
}
// 单个删除
const handleDeleteOne = (userId) => {
  axios.delete('/users', {
    data: {
      ids: [userId]
    }
  }).then(() => {
    ElMessage.success('删除成功')
    table.value.getList()
  })
}
const handleResetPassword=(userId) => {
  axios.put('/users/password', {
    userId: userId,
    userPassword:md5("123456")

  }).then(() => {
    ElMessage.success('成功重置为：123456')
    table.value.getList()
  })
}
// 修改分类
const handleEdit = (userId) => {
  updateUser.value.open(userId)
}
</script>

<style>

</style>