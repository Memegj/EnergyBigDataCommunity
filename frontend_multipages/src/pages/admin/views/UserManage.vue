<template>
  <el-card class="user-container">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="handleAddUser">添加用户</el-button>
        <el-button type="success" :icon="Check" @click="handleSolve">解除禁用</el-button>
        <el-button type="danger" :icon="Delete" @click="handleForbid">禁用用户</el-button>
      </div>
    </template>
    <Table  action='/users' ref="table">
      <template #column>
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="nickname"
          label="昵称"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="登录名"
        >
        </el-table-column>
        <el-table-column
          label="身份状态"
        >
          <template #default="scope">
            <span :style="scope.row.locked == 0 ? 'color: green;' : 'color: red;'">
              {{ scope.row.locked == 0 ? '正常' : '禁用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="是否注销"
        >
          <template #default="scope">
            <span :style="scope.row.deleted == 0 ? 'color: green;' : 'color: red;'">
              {{ scope.row.deleted == 0 ? '正常' : '注销' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
            prop="user_role"
            label="用户角色">
        </el-table-column>
        <el-table-column
            prop="team"
            label="所属团队">
        </el-table-column>
        <el-table-column
          prop="register_time"
          label="注册时间"
        >
        </el-table-column>
      </template>
    </Table>
  </el-card>
  <DialogAddUser ref='addUser' :reload="reload_list"></DialogAddUser>
</template>

<script setup>
import { ref } from 'vue'
import Table from '@/components/Table.vue'
import DialogAddUser from "@/components/DialogAddUser.vue";
import { ElMessage } from 'element-plus'
import { Plus, Delete, Check } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
let addUser = ref(null)
let table = ref(null)
const handleSolve = () => {
if (!table.value.state.multipleSelection.length) {
  ElMessage.error('请选择项')
  return
}
axios.put(`/users/0`, {
  ids: table.value.state.multipleSelection.map(item => item.userid)
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
  ids: table.value.state.multipleSelection.map(item => item.userid)
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
</script>

<style>

</style>