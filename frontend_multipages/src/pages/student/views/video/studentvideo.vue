<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>全部课程</h1>

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
import {Delete, Plus} from "@element-plus/icons-vue";
import { ref, reactive,onMounted} from 'vue'
import { ElMessage } from 'element-plus'
import axios from '@/utils/axios'
import { useRouter } from 'vue-router'

import WangEditor from 'wangeditor'
import {localGet} from '@/utils'
import DialogAddUser from "@/components/DialogAddStu.vue";

const searchText = ref('');
const searchOption = ref('VideoName');
const router = useRouter()
const state = reactive({
  loading: false,
  videos: [{ }],
  total: 0,
  currentPage: 1,
  pageSize: 8,
});
// 初始化加载数据
onMounted(() => {
  getVideos();
});
//获取分类列表
const getVideos = () => {
  state.loading = true;
  axios.get('/api/videos', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
    }
  }).then(res => {
    state.videos = res.list
    state.total = res.totalCount
    state.currentPage = res.currentPage
    state.loading = false
  }).catch(error => {
    ElMessage.error('获取视频列表失败');
    state.loading = false;
  });
};


const changePage = (val) => {
  state.currentPage = val;
  getVideos();
};
//搜索，关键字和搜索选项
const handleSearch =()=> {
  console.log('搜索关键词:', this.searchText);
  console.log('搜索选项:', this.searchOption);
  getVideos();
};
// 重置搜索
const resetSearch = () => {
  searchText.value = '';
  searchOption.value = 'VideoName';
};


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
  width: 100%; /* 设置宽度为100%，占据父容器的全部宽度 */
  margin: 0 auto; /* 让网格居中显示 */
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 每行四列 */
  gap: 50px; /* 列之间的间隔 */
}

.course-item-content {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  height: 100%; /* 调整高度 */
  width: 100%; /* 调整每个格子的宽度 */
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
.course-video{
  width: 100%;
  text-align: center;
  aspect-ratio: auto 628 / 353;
  height: 50%;
}
</style>