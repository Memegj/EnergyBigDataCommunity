<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>全部课程</h1>
        <el-button type="primary" :icon="Plus" @click="handleAdd">上传视频</el-button>
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
        <div class="search-inputs">
          <input type="text" v-model="searchText" placeholder="输入关键词...">
          <select v-model="searchOption">
            <option value="VideoName">按课程名字</option>
            <option value="VideoTeacher">按发布人</option>
          </select>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="warning" @click="resetSearch">清空</el-button>
        </div>
      </div>
    </template>
    <div class="course-grid">
      <div class="select-all-container">
        <el-checkbox v-model="allSelected" @change="handleSelectAll"></el-checkbox>
      </div>
      <div v-for="video in state.videos" :key="video.videoId" class="course-item">
        <el-checkbox v-model="selectedVideos" :label="video.videoId" class="video-checkbox"></el-checkbox>
        <router-link :to="'/video/' + video.videoId">
          <div class="course-item-content">
            <video :src="video.url" controls class="course-video">你的浏览器不支持视频播放</video>
            <div class="course-details">
              <div class="course-name">视频名称：{{ video.videoName }}</div>
              <div class="course-intro">简介：{{ video.videoIntro }}</div>
              <div class="course-meta">
                <span>上传人: {{ video.userName }}</span><br>
                <span>上传时间: {{ video.uploadTime }}</span>
              </div>
            </div>
          </div>
        </router-link>
      </div>
    </div>

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
import { Delete, Plus } from "@element-plus/icons-vue";
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from '@/utils/axios';
import { useRouter } from 'vue-router';

const searchText = ref('');
const searchOption = ref('VideoName');
const router = useRouter();
const state = reactive({
  loading: false,
  videos: [],
  multipleSelection: [],
  total: 0,
  currentPage: 1,
  pageSize: 8
});
const selectedVideos = ref([]); // 添加这个数组来存储选中的视频ID
const allSelected = ref(false); // 全选按钮的状态

// 初始化加载数据
onMounted(() => {
  getVideos();
});

// 获取分类列表
// 获取分类列表
const getVideos = () => {
  state.loading = true;
  axios.get('/api/videos', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize
    }
  })
      .then(res => {
        state.videos = res.list; // 确保数据结构正确
        state.total = res.totalCount; // 确保数据结构正确
        state.currentPage = res.currPage;
        state.loading = false;
      })
      .catch(error => {
        ElMessage.error('获取视频列表失败');
        state.loading = false;
      });
};


const changePage = (val) => {
  state.currentPage = val;
  getVideos();
};

const handleSearch = () => {
  getVideos();
};

const resetSearch = () => {
  searchText.value = '';
  searchOption.value = 'VideoName';
  getVideos();
};

// 批量删除
const handleDelete = () => {
  if (!selectedVideos.value.length) {
    ElMessage.error('请选择项');
    return;
  }

  axios.delete('/api/videos', {
    data: {
      ids: selectedVideos.value
    }
  })
      .then(() => {
        ElMessage.success('删除成功');
        selectedVideos.value = []; // 清空已选中视频ID
        allSelected.value = false; // 重置全选按钮状态
        getVideos(); // 删除成功后重新获取视频列表

      })
      .catch(error => {
        ElMessage.error('删除失败');
      });
};


// 更新选中状态
const handleSelectionChange = (val) => {
  selectedVideos.value = val;
}

// 全选功能
const handleSelectAll = (value) => {
  if (value) {
    selectedVideos.value = state.videos.map(video => video.videoId);
  } else {
    selectedVideos.value = [];
  }
};
defineExpose({ state: state, getVideos: getVideos })
</script>

<style>
.search-inputs input[type="text"] {
  width: 200px;
  padding: 8px;
  font-size: 14px;
  margin-right: 10px;
}
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}
.course-grid {
  width: 100%;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 50px;
  position: relative; /* 使子元素的绝对定位相对于此元素 */
}

.select-all-container {
  position: absolute;
  top: -40px; /* 根据需要调整 */
  left: 0;
  z-index: 2;
}

.course-item-content {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.course-image {
  max-width: 100%;
  text-align: center;
  aspect-ratio: auto 628 / 353;
  height: 50%;
}

.course-details {
  flex: 1;
}

.course-meta {
  margin-top: auto;
  font-size: 14px;
  color: #777;
}

.course-image {
  width: 100%;
  aspect-ratio: auto 628 / 353;
  height: 50%;
}

.search-inputs {
  display: flex;
  align-items: center;
}

.search-inputs input[type="text"] {
  width: 200px;
  padding: 8px;
  font-size: 14px;
  margin-right: 10px;
}

.search-inputs select {
  padding: 8px;
  font-size: 14px;
  margin-right: 10px;
}

.course-video {
  width: 100%;
  text-align: center;
  aspect-ratio: auto 628 / 353;
  height: 50%;
}

.video-checkbox {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 1;
}

.course-item {
  position: relative;
}
</style>