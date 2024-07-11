<template>
  <el-card class="codes-container">
    <template #header>
      <div class="header">
        <el-input
            v-model="searchText"
            placeholder="请输入搜索内容"
            style="width: 600px; margin-right: 10px;">
        </el-input>
        <el-select v-model="searchType" placeholder="请选择搜索类型" style="width: 150px; margin-right: 10px;">
          <el-option label="标题" value="codename"></el-option>
          <el-option label="上传人" value="username"></el-option>
        </el-select>
        <el-button type="primary"   @click="handleSearch">搜索</el-button>
      </div>
    </template>
    <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%">
      <el-table-column prop="codename" label="Code名称"></el-table-column>
      <el-table-column prop="uploadtime" label="上传时间" width="200"></el-table-column>
      <el-table-column prop="username" label="上传人" width="200"></el-table-column>
    </el-table>
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="changePage">
    </el-pagination>
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref, toRefs, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddReference from '@/components/DialogAddReference.vue'

const addRef = ref(null)
const router = useRouter() // 声明路由实例
useRoute();
// 获取路由参数
const state = reactive({
  loading: false,
  tableData: [], // 数据列表
  multipleSelection: [], // 选中项
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 10, // 分页大小
  type: 'search' // 操作类型
})
onMounted(() => {
  getExcel()
})

// 获取分类列表
const getExcel = () => {
  state.loading = true
  axios.get('/excel1/list1', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
    }
  }).then(res => {
    state.tableData = res.list
    state.total = res.totalCount
    state.currentPage = res.currPage
    state.loading = false
  })
}
const changePage = (val) => {
  state.currentPage = val
  getExcel()
}

// 添加分类
const handleSearch = () => {
  state.type = 'search'
  addRef.value.open()
}

const table_data_reload = () => {
  getExcel()
}

</script>

<style>
.header {
  display: flex;
  align-items: center;
}
</style>
