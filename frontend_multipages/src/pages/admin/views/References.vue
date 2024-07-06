<template>
  <el-card class="references-container">
    <template #header>
      <div class="header">
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
          label="文献标题"
      >
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="上传时间"
          width="200"
      >
      </el-table-column>
      <el-table-column
          label="操作"
          width="220"
      >
        <template #default="scope">
          <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.id)">修改</a>
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
    <DialogAddReference ref='addRef' :reload="table_data_reload" :type="state.type" />
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
const route = useRoute() // 获取路由参数
const state = reactive({
  loading: false,
  tableData: [], // 数据列表
  multipleSelection: [], // 选中项
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 10, // 分页大小
  type: 'add' // 操作类型
})
onMounted(() => {
  getReferences()
})

// 获取分类列表
const getReferences = () => {
  state.loading = true
  axios.get('/references/list', {
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
  getReferences()
}

// 添加分类
const handleAdd = () => {
  state.type = 'add'
  addRef.value.open()
}
// 修改分类
const handleEdit = (id) => {
  state.type = 'edit'
  addRef.value.open(id)
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
  axios.delete('/references', {
    data: {
      ids: state.multipleSelection.map(i => i.id)
    }
  }).then(() => {
    ElMessage.success('删除成功')
    getReferences()
  })
}
// 单个删除
const handleDeleteOne = (id) => {
  axios.delete('/references', {
    data: {
      ids: [id]
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

<style>

</style>