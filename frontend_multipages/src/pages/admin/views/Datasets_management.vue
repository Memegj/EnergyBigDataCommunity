<template>
  <div>
    <!-- 上方 Card -->
    <el-card class="account-container">
      <h2>数据集管理</h2>
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
          <el-select v-model="selectedCategory" placeholder="类别检索" class="category-select">
            <el-option label="类别1" value="1"></el-option>
            <el-option label="类别2" value="2"></el-option>
            <el-option label="类别3" value="3"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-table
          :data="state.tableData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="dataName"
            label="名称"
            width="140"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            prop="uploadTime"
            label="上传时间"
            width="250"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            label="是否公开"
            width="150"
            header-align="center"
            align="center">
          <template #default="scope">
            <span>{{ getPublicStatus(scope.row) }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="dataAbstract"
            label="简介"
            width="390"
            header-align="center"
            align="center">
        </el-table-column>
        <el-table-column
            label="操作"
            width="100"
            header-align="center"
            align="center">
          <template #default="scope">
            <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.dataId)">修改</a>
            <el-popconfirm
                title="确定删除吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="handleDeleteOne(scope.row.dataId)">
              <template #reference>
                <a style="cursor: pointer">删除</a>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!--总数超过一页，再展示分页器-->
      <el-pagination
          background
          layout="prev, pager, next"
          :total="state.total"
          :page-size="state.pageSize"
          :current-page="state.currentPage"
          @current-change="changePage" />
      <DialogAddStu ref='addStu' :reload="table_data_reload"></DialogAddStu>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddStu from "@/components/DialogAddStu.vue";

const addRef = ref(null)
const router = useRouter() // 声明路由实例
const route = useRoute() // 获取路由参数
let addStu = ref(null)
const state = reactive({
  loading: false,
  tableData: [], // 数据列表
  multipleSelection: [], // 选中项
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 5, // 分页大小
  isPublic: ''
})
onMounted(() => {
  getReferences()
})

// 获取分类列表
const getReferences = () => {
  state.loading = true
  axios.get('/dataset/list', {
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

const getPublicStatus = (row) => {
  if (!row.teamId) {
    return '公开'
  } else {
    getTeamName(row.dataId)
    return '对\"'+state.isPublic+'\"公开'
  }
}

const getTeamName = (dataId) => {
  axios.get(`/dataset/teamname/${dataId}`).then(res => {
    state.isPublic = res.teamName
  })
}

const changePage = (val) => {
  state.currentPage = val
  getReferences()
}

// 添加分类
const handleAdd = () => {
  router.push('/admin/datasetupload')
}
// 修改分类
const handleEdit = (dataId) => {
  addStu.value.open(dataId)
}
// 选择项
const handleSelectionChange = (val) => {
  state.multipleSelection = val
}
// 批量删除
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
    getReferences()
  })
}
// 单个删除
const handleDeleteOne = (dataId) => {
  axios.delete('/dataset', {
    data: {
      ids: [dataId]
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getReferences()
  })
}
const table_data_reload = () => {
  getReferences()
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
