<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>全部课程</h1>
        <div class="search-inputs">
          <input type="text" v-model="searchQuery" placeholder="输入关键词...">
          <el-select v-model="selectedCategory" placeholder="类别检索" @change="search">
            <el-option label="按课程名字" value="VideoName"></el-option>
            <el-option label="按授课老师" value="VideoTeacher"></el-option>
          </el-select>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button type="warning" @click="resetSearch">清空</el-button>
        </div>
      </div>
    </template>
    <div class="course-grid">
      <router-link v-for="video in state.videos" :key="video.VideoId" :to="'/video/' + video.videoId" class="course-item">
        <div class="course-item-content">
          <video :src="video.url" controls class="course-video">你的浏览器不支持视频播放</video>
          <div class="course-details">
            <div class="course-name">视频名称：{{ video.videoName }}</div>
            <div class="course-intro">简介：{{ video.videoIntro }}</div>
            <div class="course-meta">
              <span>上传人: {{ video.videoTeacher }}</span><br>
              <span>上传时间: {{ video.uploadTime }}</span>
            </div>
          </div>
        </div>
      </router-link>
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
import {ref, reactive, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import axios from '@/utils/axios'
import {useRouter} from 'vue-router'

const searchQuery = ref('')
const selectedCategory = ref('VideoName')
const router = useRouter()
const state = reactive({
  loading: false,
  videos: [],
  total: 0,
  currentPage: 1,
  pageSize: 8,
})

// 初始化加载数据
onMounted(() => {
  getReferences()
})

// 获取视频列表
const getReferences = () => {
  state.loading = true
  axios.get('/api/videos', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
      searchQuery: searchQuery.value,
      category: selectedCategory.value,
    }
  }).then(res => {
    state.videos = res.list
    state.total = res.totalCount
    state.currentPage = res.currentPage
    state.loading = false
  }).catch(error => {
    ElMessage.error('获取视频列表失败')
    state.loading = false
  })
}

const search = () => {
  state.currentPage = 1
  getReferences()
}

const changePage = (val) => {
  state.currentPage = val
  getReferences()
}

const resetSearch = () => {
  searchQuery.value = ''
  selectedCategory.value = 'VideoName'
  state.currentPage = 1
  getReferences()
}
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

.course-video {
  width: 100%;
  text-align: center;
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
</style>
