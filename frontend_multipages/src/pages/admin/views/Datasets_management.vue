<template>
  <div>
    <!-- 上方 Card -->
    <el-card class="account-container">
      <h2 style="line-height: 10px">数据集管理</h2>
      <div style="line-height: 30px">
        这是您个人上传的资料的私人视图。要查看其他人上传的内容，请前往相关内容搜索。
      </div>
    </el-card>

    <!-- 下方 Card -->
    <el-card class="references-container">
      <!-- 搜索 Row -->
      <el-row class="search-row">
        <el-col :span="22">
          <el-input placeholder="搜索你的作品" v-model="searchQuery" class="search-input">
            <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
          </el-input>
        </el-col>
        <el-col :span="2" style="text-align: right;">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>
      </el-row>

      <!-- 操作 Row -->
      <el-row class="action-row">
        <el-col :span="16">
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
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
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-select v-model="selectedCategory" placeholder="类别检索" class="category-select" @change="handleCategoryChange">
            <el-option label="按时间排序" value="time"></el-option>
            <el-option label="按团队分组" value="team"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-table
          :data="state.tableData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
      >
        <el-table-column
            type="selection"
            width="55"
        >
        </el-table-column>
        <el-table-column
            prop="dataName"
            label="名称"
            width="140"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="uploadTime"
            label="上传时间"
            width="250"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="teamName"
            label="是否公开"
            width="150"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            prop="dataAbstract"
            label="简介"
            width="390"
            header-align="center"
            align="center"
        >
        </el-table-column>
        <el-table-column
            label="操作"
            width="100"
            header-align="center"
            align="center"
        >
          <template #default="scope">
            <a style="cursor: pointer; margin-right: 10px" @click.stop="handleEdit(scope.row.dataId)">修改</a>
            <el-popconfirm
                title="确定删除吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="() => handleDeleteOne(scope.row.dataId)"
            >
              <template #reference>
                <a style="cursor: pointer" @click.stop>删除</a>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          background
          layout="prev, pager, next"
          :total="state.total"
          :page-size="state.pageSize"
          :current-page="state.currentPage"
          @current-change="changePage"
      />
      <DialogAddStu ref="addStu" :reload="table_data_reload"/>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {Plus, Delete} from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddStu from "@/components/DialogAddStu.vue";

const router = useRouter()
const selectedCategory = ref('time') // 默认按时间排序
const searchQuery = ref('') // 添加搜索查询

const state = reactive({
  loading: false,
  tableData: [],
  multipleSelection: [],
  total: 0,
  currentPage: 1,
  pageSize: 5,
  isPublic: ''
})

onMounted(() => {
  getReferences()
})

const getReferences = (searchQuery = '') => {
  state.loading = true
  let params = {
    pageNumber: state.currentPage,
    pageSize: state.pageSize,
    searchQuery: searchQuery // 将搜索查询参数添加到请求参数中
  }
  console.log('Selected Category:', selectedCategory.value) // Debug log
  if (selectedCategory.value === 'team') {
    // 按团队分组
    axios.get('/dataset/listByTeam', {params}).then(res => {
      state.tableData = res.list
      state.total = res.totalCount
      state.currentPage = res.currPage
      state.loading = false
    })
  } else {
    // 按时间排序（默认）
    axios.get('/dataset/list', {params}).then(res => {
      state.tableData = res.list
      state.total = res.totalCount
      state.currentPage = res.currPage
      state.loading = false
    })
  }
}

const search = () => {
  getReferences(searchQuery.value) // 传递搜索查询参数
}

const handleCategoryChange = () => {
  getReferences(searchQuery.value) // 传递搜索查询参数
}

const changePage = (val) => {
  state.currentPage = val
  getReferences(searchQuery.value) // 传递搜索查询参数
}

const handleAdd = () => {
  router.push('/admin/datasetupload')
}

const handleEdit = (dataId) => {
  router.push({
    name: 'datasetedit',
    params: {
      dataId: dataId
    }
  })
}

const handleSelectionChange = (val) => {
  state.multipleSelection = val
}

const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('请选择项')
    return
  }
  axios.delete('/dataset', {
    data: {
      ids: state.multipleSelection.map(i => i.dataId)
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getReferences(searchQuery.value) // 传递搜索查询参数
  })
}

const handleDeleteOne = (dataId) => {
  axios.delete('/dataset', {
    data: {
      ids: [dataId]
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getReferences(searchQuery.value) // 传递搜索查询参数
  })
}

const handleRowClick = (row, column, event) => {
  // 如果点击的元素是操作列中的元素，不处理跳转
  if (event.target.tagName === 'A' || event.target.tagName === 'BUTTON') {
    return
  }
  router.push({
    name: 'datasetDetail',
    params: {
      dataId: row.dataId
    }
  })
}

const table_data_reload = () => {
  getReferences(searchQuery.value) // 传递搜索查询参数
}
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
