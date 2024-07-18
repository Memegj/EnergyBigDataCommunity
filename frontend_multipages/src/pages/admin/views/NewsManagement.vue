<template>
  <div style="margin-bottom: 10px;font-size: 25px" class="page-title">新闻管理</div>
  <el-card class="news-container">
    <template #header>
      <div class="header">
        <div style="margin-bottom: 10px">
          <el-input v-model="state.titlefrominput" style="width: 200px" placeholder="请输入标题"></el-input>
          <el-button  type="info" style="margin-left: 10px;background-color: olivedrab" @click="findBySearch">查询</el-button >
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">增加</el-button>
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
    <el-table
        :load="state.loading"
        ref="multipleTable"
        :data="state.tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55"
      >
      </el-table-column>
      <el-table-column
          prop="title"
          label="新闻标题"
      >
      </el-table-column>
      <el-table-column
          prop="uploadtime"
          label="上传时间"
          width="400"
      >
      </el-table-column>
      <el-table-column
          label="操作"
          width="220"
      >
        <template #default="scope">
          <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row)">修改</a>
          <el-popconfirm
              title="确定删除吗？"
              confirmButtonText='确定'
              cancelButtonText='取消'
              @confirm="handleDeleteOne(scope.row.id)"
          >
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
        @current-change="changePage"
    />
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref, toRefs, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'


// 添加分类
const addRef = ref(null)
const router = useRouter() // 声明路由实例
const state = reactive({
  titlefrominput:'',
  loading: false,
  tableData: [], // 数据列表
  multipleSelection: [], // 选中项
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 10, // 分页大小
  type: 'add' // 操作类型
})
const handleAdd = () => {
  router.push('/admin/AddNews')
}
onMounted(() => {
  getNews()
})

// 获取列表信息
const getNews = () => {
  state.loading = true
  axios.get('/news/getnew', {
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
// 查询
const findBySearch = () => {
  state.loading = true; // 开始加载状态
  // 发送查询请求
  axios.get('/news/search', {
    params: {
      title: state.titlefrominput, // 获取搜索框中的标题内容
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
    }
  }).then(res => {
    state.tableData = res.list; // 更新表格数据为查询结果
    state.total = res.totalCount; // 更新总条数
    state.currentPage = res.currPage; // 更新当前页
    state.loading = false; // 结束加载状态
  }).catch(err => {
    console.error('查询失败:', err);
    ElMessage.error('查询失败，请重试'); // 提示用户查询失败
    state.loading = false; // 结束加载状态
  });
}

const changePage = (val) => {
  state.currentPage = val
  getNews()
}

// 修改分类
const handleEdit = (item) => {
  const itemJson = JSON.stringify(item);
  router.push({path:'/admin/AddNews',query: {item:itemJson}})

  // state.type = 'edit'
  // addRef.value.open(id)
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
  axios.delete('/news/delete', {
    data: {
      ids: state.multipleSelection.map(i => i.id)
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getNews()
  })
}
// 单个删除
const handleDeleteOne = (id) => {
  axios.delete('/news/delete', {
    data: {
      ids: [id]
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getNews()
  })
}
const table_data_reload = () => {
  getNews()
}
</script>

<style>

</style>