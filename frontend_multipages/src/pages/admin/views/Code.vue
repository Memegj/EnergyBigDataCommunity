<template>
  <div>
    <!-- 上方 Card -->
    <el-card class="account-container">
      <h2>代码集</h2>
      <div style="line-height: 30px">
        探索、分析与共享代码集
      </div>
    </el-card>

    <!-- 下方 Card -->
    <el-card class="references-container">
      <!-- 搜索 Row -->
      <el-row class="search-row">
        <el-col :span="22">
          <el-input placeholder="搜索" v-model="searchQuery" class="search-input">
            <el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
          </el-input>
        </el-col>
        <el-col :span="2" style="text-align: right;">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>
      </el-row>

      <el-table
          :data="pagedData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
        <el-table-column
            prop="codeName"
            label="名称"
            width="250"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="codeAbstract"
            label="简介"
            width="350"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="userName"
            label="上传人"
            width="100"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="teamName"
            label="团队"
            width="200"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="uploadTime"
            label="上传时间"
            width="180"
            header-align="center"
            align="center">
        </el-table-column>
      </el-table>

      <!-- 总数超过一页，再展示分页器 -->
      <el-pagination
          background
          layout="prev, pager, next"
          :total="filteredData.length"
          :page-size="state.pageSize"
          :current-page="state.currentPage"
          @current-change="changePage" />
      <DialogAddStu ref='addStu' :reload="table_data_reload"></DialogAddStu>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from '@/utils/axios'
import DialogAddStu from "@/components/DialogAddStu.vue"

const addRef = ref(null)
const router = useRouter() // 声明路由实例
const route = useRoute() // 获取路由参数
let addStu = ref(null)
const state = reactive({
  loading: false,
  allCode: [], // 所有数据列表
  multipleSelection: [], // 选中项
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 5, // 分页大小
  isPublic: ''
})
const searchQuery = ref('')
const searchTriggered = ref(false) // 用于标记是否点击了搜索按钮

onMounted(() => {
  getAllReferences()
})

// 获取所有数据列表
const getAllReferences = () => {
  state.loading = true
  axios.get('/code/list', {
    params: {
      pageNumber: 1,
      pageSize: 10000, // 假设数据量不会超过10000条
    }
  }).then(res => {
    state.allCode = res.list
    state.total = res.totalCount
    state.loading = false

    // 遍历 allCode 获取 userName 和 teamName
    state.allCode.forEach(row => {
      getUserName(row)
      getTeamName(row)
    })
    updateTableCode()
  })
}

const changePage = (val) => {
  state.currentPage = val
  updateTableCode()
}

const table_data_reload = () => {
  getAllReferences()
}

// 搜索功能
const search = () => {
  state.currentPage = 1
  searchTriggered.value = true
  updateTableCode()
}

// 更新当前页数据
const updateTableCode = () => {
  state.tableCode = pagedData.value
}

// 过滤后的数据
const filteredData = computed(() => {
  if (!searchTriggered.value) return state.allCode
  return state.allCode.filter(row => {
    return ['codeName', 'codeAbstract', 'userName', 'teamName'].some(key => {
      return String(row[key]).toLowerCase().includes(searchQuery.value.toLowerCase())
    })
  })
})

// 当前页数据
const pagedData = computed(() => {
  return filteredData.value.slice((state.currentPage - 1) * state.pageSize, state.currentPage * state.pageSize)
})

// 监听 searchQuery 的变化，重置标记
watch(searchQuery, (newVal) => {
  if (newVal === '') {
    searchTriggered.value = false
    updateTableCode() // 清除搜索内容后，重新显示所有数据
  }
})

</script>

<style scoped>
.account-container {
  margin-bottom: 5px;
  padding: 0;
}
.data-set-management {
  padding: 0;
}
.search-row {
  margin-bottom: 20px;
}
.search-input {
  width: 100%;
}
.action-row {
  margin-bottom: 20px;
}
.category-select {
  width: 150px;
}
</style>
