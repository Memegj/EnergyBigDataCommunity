<template>
  <div>
    <!-- 上方 Card -->
    <el-card class="account-container">
      <h2>个人收藏</h2>
      <div style="line-height: 30px">
        这是您个人收藏内容的私人视图，您可以在这里对收藏的内容进行管理。
      </div>
    </el-card>

    <!-- 下方 Card -->
    <el-card class="references-container">
      <!-- 搜索 Row -->
      <el-row class="search-row">
        <el-col :span="22">
          <el-input placeholder="搜索你收藏的作品(名字、类别、上传人)" v-model="searchQuery" class="search-input">
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
          <el-popconfirm
              title="确定取消吗？"
              confirmButtonText='确定'
              cancelButtonText='取消'
              @confirm="handleDelete">
            <template #reference>
              <el-button type="danger">取消收藏</el-button>
            </template>
          </el-popconfirm>
        </el-col>
        <el-col :span="8" style="text-align: right;">
          <el-select v-model="selectedCategory" placeholder="类别检索" class="category-select" @change="filterData">
            <el-option label="文献资料" value="文献资料"></el-option>
            <el-option label="代码" value="代码"></el-option>
            <el-option label="数据集" value="数据集"></el-option>
            <el-option label="教学视频" value="教学视频"></el-option>
            <el-option label="全部类别" value="全部类别"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-table
          :data="paginatedFilteredData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
      >

        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="dataName" label="名称" width="300" header-align="center" align="center"></el-table-column>
        <el-table-column prop="collectType" label="类别" width="200" header-align="center" align="center"></el-table-column>
        <el-table-column prop="userName" label="上传人" width="200" header-align="center" align="center"></el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="200" header-align="center" align="center"></el-table-column>
        <el-table-column label="操作" width="200" header-align="center" align="center">
          <template #default="scope">
            <el-popconfirm
                title="确定取消吗？"
                confirmButtonText='确定'
                cancelButtonText='取消'
                @confirm="handleDeleteOne(scope.row.collectId)">
              <template #reference>
                <a style="cursor: pointer">取消收藏</a>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <el-pagination
          background
          layout="prev, pager, next"
          :total="filteredTotal"
          :page-size="state.pageSize"
          :current-page.sync="state.currentPage"
          @current-change="changePage"
      />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '@/utils/axios.js'

const searchQuery = ref('')
const selectedCategory = ref('')
const state = reactive({
  loading: false,
  tableData: [], // 原始数据列表
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 5, // 分页大小
  filteredData: [], // 根据条件过滤后的数据
  multipleSelection: [] // 选择项
})

onMounted(() => {
  getReferences()
})

// 计算属性，用于处理过滤后的分页数据
const paginatedFilteredData = computed(() => {
  const start = (state.currentPage - 1) * state.pageSize;
  const end = start + state.pageSize;
  return state.filteredData.slice(start, end);
});

const filteredTotal = computed(() => {
  return state.filteredData.length;
});

// 监听 searchQuery 的变化，重置过滤数据
watch(searchQuery, () => {
  if (!searchQuery.value) {
    filterData(); // 搜索框为空时，显示全部数据
  }
});

// 获取收藏列表
const getReferences = () => {
  state.loading = true;
  axios.get('/collect/list', {
    params: {
      pageNumber: 1,
      pageSize: 999, // 设置一个足够大的数值，获取全部数据
    }
  }).then(res => {
    state.tableData = res.list;
    state.total = res.totalCount;
    state.currentPage = res.currPage;
    state.loading = false;
    filterData(); // 过滤数据
  }).catch(error => {
    state.loading = false;
    console.error('获取收藏列表失败：', error);
  });
}

const handleRowClick = (row, column, event) => {
  // 如果点击的元素是操作列中的元素，不处理跳转
  if (event.target.tagName === 'A' || event.target.tagName === 'BUTTON') {
    return
  }

  // 根据 CollectType 属性跳转到不同的页面
  switch(row.collectType) {
    case '数据集':
      router.push({
        name: 'datasetDetail',
        params: {
          dataId: row.dataId
        }
      });
      break;
    case '代码':
      router.push({
        name: 'codeDetail',
        params: {
          codeId: row.codeId
        }
      });
      break;
    case '文献资料':
      router.push({
        name: 'literatureDetail',
        params: {
          literId: row.literId
        }
      });
      break;
    case '视频':
      router.push({
        name: 'videoDetail',
        params: {
          videoId: row.videoId
        }
      });
      break;
    default:
      console.error('未知的 CollectType:', row.collectType);
  }
}

// 根据选中的类别和搜索词过滤数据
const filterData = () => {
  let filtered = state.tableData;

  // 根据选中的类别过滤
  if (selectedCategory.value && selectedCategory.value !== "全部类别") {
    filtered = filtered.filter(item => item.collectType === selectedCategory.value);
  }

  // 根据搜索词过滤所有字段
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(item => {
      return ['dataName', 'collectType', 'userName'].some(key =>
          String(item[key]).toLowerCase().includes(query)
      );
    });
  }

  state.filteredData = filtered; // 将过滤后的数据保存到 filteredData 中
}

// 搜索方法
const search = () => {
  filterData(); // 直接使用 filterData 来过滤数据
}

// 处理选择变化
const handleSelectionChange = (val) => {
  state.multipleSelection = val;
}

// 批量删除
const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('请选择项');
    return;
  }
  axios.delete('/collect', {
    data: {
      ids: state.multipleSelection.map(i => i.collectId)
    }
  }).then(() => {
    ElMessage.success('删除成功');
    getReferences();
  });
}

// 单个删除
const handleDeleteOne = (collectId) => {
  axios.delete('/collect', {
    data: {
      ids: [collectId]
    }
  }).then(() => {
    ElMessage.success('删除成功');
    getReferences(); // 删除后刷新数据
  });
}

// 分页方法
const changePage = (val) => {
  state.currentPage = val;
  filterData(); // 分页时重新过滤数据
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
