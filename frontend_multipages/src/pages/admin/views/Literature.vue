<template>
  <div>
    <!-- 上方 Card -->
    <el-card class="account-container">
      <h2>文献资料</h2>
      <div style="line-height: 30px">
        探索、分析与共享文献资料
      </div>
    </el-card>

    <!-- 下方 Card -->
    <el-card class="references-container">
      <!-- 搜索 Row -->
      <el-row class="search-row">
        <el-col :span="4">
          <el-select v-model="selectedType" placeholder="选择类型" @change="search">
            <el-option label="全部类型" :value="null"></el-option>
            <el-option label="期刊" value="期刊"></el-option>
            <el-option label="图书" value="图书"></el-option>
            <el-option label="报纸" value="报纸"></el-option>
            <el-option label="会议文献" value="会议文献"></el-option>
            <el-option label="学位论文" value="学位论文"></el-option>
          </el-select>
        </el-col>
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        <el-col :span="17">
          <el-input placeholder="搜索" v-model="searchQuery" class="search-input">
            <el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
          </el-input>
        </el-col>
        <el-col :span="2" style="text-align: right;">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>
      </el-row>

      <el-table
          :data="state.tableLiterature"
          tooltip-effect="dark"
          style="width: 100%"
          @row-click="handleRowClick">
        <el-table-column
            prop="literName"
            label="名称"
            width="300"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="literAuthor"
            label="作者"
            width="80"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="sources"
            label="来源"
            width="200"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="literType"
            label="类型"
            width="100"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="teamName"
            label="团队"
            width="150"
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
            prop="uploadTime"
            label="上传时间"
            width="180"
            header-align="center"
            align="center"
            :formatter="(row) => formatUploadTime(row.uploadTime)">
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
import axios from '@/utils/axios'
import DialogAddStu from "@/components/DialogAddStu.vue"

const router = useRouter()
const state = reactive({
  loading: false,
  allLiterature: [],
  total: 0,
  currentPage: 1,
  pageSize: 5,
})
const searchQuery = ref('')
const selectedType = ref(null); // 默认选为 null

onMounted(() => {
  getAllReferences()
})

// 获取所有数据列表
const getAllReferences = () => {
  state.loading = true
  axios.get('/literature/list', {
    params: {
      pageNumber: 1,
      pageSize: 10000, // 假设数据量不会超过10000条
      LiterType: selectedType.value, // 传递文献类型
    }
  }).then(res => {
    state.allLiterature = res.list
    state.total = res.totalCount
    state.loading = false
    updateTableLiterature()
  })
}

const changePage = (val) => {
  state.currentPage = val
  getAllReferences()
}

const handleRowClick = (row) => {
  router.push({
    name: 'literatureDetail',
    params: {
      literId: row.literId
    }
  })
}

const table_data_reload = () => {
  getAllReferences()
}

// 搜索功能
const search = () => {
  state.currentPage = 1
  getAllReferences()
}

const formatUploadTime = (timestamp) => {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}

// 更新当前页数据
const updateTableLiterature = () => {
  state.tableLiterature = pagedData.value
}

// 过滤后的数据
const filteredData = computed(() => {
  return state.allLiterature.filter(row => {
    return ['literName', 'literAuthor', 'literType', 'literDigest', 'sources', 'userName', 'teamName', 'literId'].some(key => {
      return String(row[key]).toLowerCase().includes(searchQuery.value.toLowerCase())
    })
  })
})

// 当前页数据
const pagedData = computed(() => {
  return filteredData.value.slice((state.currentPage - 1) * state.pageSize, state.currentPage * state.pageSize)
})

</script>

<style scoped>
.account-container {
  margin-bottom: 5px;
  padding: 0;
}

.search-row {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
}
</style>
