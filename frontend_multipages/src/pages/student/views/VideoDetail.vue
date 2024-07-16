<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>视频详情</h1>
        <el-button type="primary" @click="goBack">返回</el-button>
      </div>
    </template>
    <div class="container">
      <!-- 视频详情 -->
      <el-card class="video-card video-details">
        <div class="video-header">
          <video :src="state.fileParams.Url" controls class="video-cover">你的浏览器不支持视频播放</video>
          <div class="video-info">
            <h1>{{ state.fileParams.VideoName }}</h1>
            <p v-if="videoDuration !== null">
              视频时长：{{ formatDuration(videoDuration) }}
            </p>
            <p v-else>
              视频时长: 正在加载视频时长信息...
            </p>
            <p>浏览量: {{ state.fileParams.PageView }}</p>
            <el-button type="primary" @click="handleCollect" v-if="state.fileParams.CollectId">
              <el-icon><StarFilled /></el-icon> 取消收藏
            </el-button>
            <el-button type="primary" @click="handleCollect" v-else>
              <el-icon><Star /></el-icon> 添加收藏
            </el-button>
          </div>
        </div>
      </el-card>

      <div class="main-content">
        <!-- 视频合集 -->
        <el-card class="video-card video-chapters">
          <div slot="header" class="clearfix">
            <span>视频合集</span>
          </div>
          <el-menu>
            <el-menu-item v-for="(chapter, index) in state.fileParams.chapters" :key="index" @click="goToChapter(index)">
              {{ chapter.name }}
            </el-menu-item>
          </el-menu>
        </el-card>

        <div class="info-blocks">
          <!-- 授课教师 -->
          <el-card class="video-card teacher-info">
            <div slot="header" class="clearfix">
              <span>授课教师</span>
            </div>
            <p>{{ state.fileParams.VideoTeacher }}</p>
          </el-card>

          <!-- 课程介绍 -->
          <el-card class="video-card course-description">
            <div slot="header" class="clearfix">
              <span>课程介绍</span>
            </div>
            <p>{{ state.fileParams.VideoIntro }}</p>
          </el-card>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { localGet } from "@/utils/index.js";
import { User } from '@element-plus/icons-vue';

const videoDuration = ref(null);
const route = useRoute();
const videoId = ref(route.params.videoId);
const state = reactive({
  token: localGet('token') || '',
  fileParams: {
    Url: '',
    VideoName: '',
    PageView: '',
    VideoTeacher: '',
    VideoIntro: '',
    chapters: [],
    CollectId: ''
  }
});

const formatDuration = (duration) => {
  if (duration === null) return '';
  const hours = Math.floor(duration / 3600);
  const minutes = Math.floor((duration % 3600) / 60);
  const seconds = duration % 60;
  return `${hours > 0 ? `${hours}:` : ''}${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
};

const getDetail = async (id) => {
  try {
    const res = await axios.get(`/videodetail/${id}`);
    console.log('Response:', res);
    state.fileParams = {
      VideoName: res.video.videoName,
      PageView: res.video.pageView,
      VideoTeacher: res.video.videoTeacher,
      VideoIntro: res.video.videoIntro,
      CollectId: res.video.collectId,
      Url: res.video.url
    };
    const videoElement = document.createElement('video');
    videoElement.src = state.fileParams.Url;
    videoElement.onloadedmetadata = () => {
      videoDuration.value = Math.round(videoElement.duration);
    };
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
};
const goBack = () => {
  router.push('/student/video');
};

const handleCollect = async () => {
  console.log('CollectId:', state.fileParams.CollectId);
  console.log('Token:', state.token);

  try {
    if (state.fileParams.CollectId) {
      // 取消收藏
      await axios.delete('/videos/collect', {
        data: {ids: [state.fileParams.CollectId]},
        headers: {'token': state.token}
      });
      ElMessage.success('已取消收藏');
    } else {
      // 收藏
      const params = {
        videoId: videoId.value,
      };
      await axios.post('/videos/collect', params, {
        headers: {'token': state.token}
      });
      ElMessage.success('已收藏');
    }
    getDetail(videoId.value); // 重新获取详情以更新页面状态
  } catch (error) {
    console.error('Failed to update collect status:', error.response ? error.response.data : error);
    ElMessage.error('操作失败: ' + (error.response ? error.response.data.message : error.message));
  }
};

const goToChapter = (index) => {
  console.log(`Navigating to chapter ${index + 1}`);
  getDetail(videoId.value);
};

onMounted(() => {
  getDetail(videoId.value);
});
</script>

<style scoped>
.references-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.video-details {
  flex: 0 0 auto;
  padding: 20px;
}

.main-content {
  display: flex;
  flex: 1;
  padding: 10px;
  gap: 20px;
}

.video-chapters {
  flex: 2;
  display: flex;
  align-items: stretch;
}

.info-blocks {
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 1px;
}

.teacher-info,
.course-description {
  flex: 2;
  margin-bottom: 10px;
}

.course-description {
  margin-bottom: 0;
}

.video-header {
  display: flex;
  gap: 20px;
}

.video-cover {
  width: 40%;
  height: auto;
}

.video-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.video-card {
  padding: 20px;
}

.el-card__header {
  font-weight: bold;
  font-size: 18px;
}
</style>
