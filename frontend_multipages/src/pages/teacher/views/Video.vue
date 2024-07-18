<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>全部课程</h1>
        <div class="search-inputs">
          <input type="text" v-model="searchQuery" placeholder="输入关键词...">
          <el-select v-model="selectedCategory" placeholder="类别检索" @change="search" style="width: 150px;">
            <el-option label="按课程名称" value="VideoName"></el-option>
            <el-option label="按授课老师" value="VideoTeacher"></el-option>
            <el-option label="按团队名称" value="TeamName"></el-option>
          </el-select>&nbsp&nbsp
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button type="warning" @click="resetSearch">清空</el-button>
        </div>
      </div>
    </template>
    <div class="course-grid">
      <div
          v-for="video in state.videos"
          :key="video.videoId"
          class="course-item"
          @click="navigateToVideo(video.videoId)"
      >
        <div class="course-item-content">
          <img :src="state.hostUrl + video.picture" alt="视频封面" class="course-image" />
          <div class="course-details">
            <div class="course-name" style="font-weight: bold;">视频名称：{{ video.videoName }}</div>
            <div class="course-intro" style="color: #777;">简介：{{ stripHTML(video.videoIntro) }}</div>
            <div class="course-meta">
              <span>上传人: {{ video.userName }}</span><br />
              <span>上传时间: {{ video.uploadTime }}</span>
            </div>
          </div>
        </div>
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
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from '@/utils/axios.js';
import { useRouter } from 'vue-router';

const searchQuery = ref('');
const selectedCategory = ref('VideoName');
const router = useRouter();
const state = reactive({
  loading: false,
  videos: [],
  total: 0,
  currentPage: 1,
  pageSize: 8,
  hostUrl: '',
});

// 初始化加载数据
onMounted(() => {
  getReferences();
});

// 获取视频列表
const getReferences = () => {
  state.loading = true;
  axios.get('/video_search', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
      searchQuery: searchQuery.value,
      category: selectedCategory.value,
    }
  }).then(res => {
    state.videos = res.pageresult.list;
    state.total = res.pageresult.totalCount;
    state.currentPage = res.pageresult.currPage;
    state.hostUrl = res.hostUrl;
    state.loading = false;
  }).catch(error => {
    ElMessage.error('获取视频列表失败');
    state.loading = false;
  });
};

const navigateToVideo = (videoId) => {
  router.push(`/teacher/video_detail1/${videoId}`);
};

const search = () => {
  getReferences();
};

const changePage = (val) => {
  state.currentPage = val;
  getReferences();
};

const resetSearch = () => {
  searchQuery.value = '';
  selectedCategory.value = 'VideoName';
  state.currentPage = 1;
  getReferences();
};

// 移除 HTML 标签
const stripHTML = (html) => {
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = html;
  return tempDiv.textContent || tempDiv.innerText || '';
};
</script>

<style scoped>
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.button-container {
  display: flex;
  margin-right: 300px; /* 调整左侧间距 */
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

.course-grid {
  width: 100%;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 50px;
  position: relative;
  margin-bottom: 80px;
}

.select-all-container {
  position: absolute;
  top: -40px;
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
  position: relative;
}

.course-image {
  width: 100%;
  height: 50%;
  text-align: center;
  aspect-ratio: auto 628 / 353;
}

.course-details {
  display: flex;
  flex-direction: column;
  justify-content: flex-end; /* 使内容靠下 */
  flex: 1;
  margin-top: 10px; /* 增加上边距 */
  padding: 10px;
}

.course-meta {
  margin-top: auto;
  font-size: 14px;
  color: #777;
}

.edit-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(255, 255, 255, 0.5);
  border: none;
  cursor: pointer;
  padding: 10px;
  border-radius: 50%;
  transition: background 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-button:hover {
  background: rgba(255, 255, 255, 0.8);
}

.large-icon {
  font-size: 16px;
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
