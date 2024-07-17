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
          <video v-if="state.fileParams.file_path && !isImage" :src="state.fileParams.file_path" controls class="video-cover">你的浏览器不支持视频播放</video>
          <img v-else :src="state.fileParams.file_path" class="video-cover" alt="封面图片" />
          <div class="video-info">
            <h1>{{ state.fileParams.VideoName }}</h1>
            <p v-if="videoDuration !== null">
              视频时长：{{ formatDuration(videoDuration) }}
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
            <p class="teacher-name">{{ state.fileParams.VideoTeacher }}</p>
          </el-card>

          <!-- 课程介绍 -->
          <el-card class="video-card course-description">
            <div slot="header" class="clearfix">
              <span>课程介绍</span>
            </div>
            <div class="course-intro" v-html="state.fileParams.VideoIntro"></div>
          </el-card>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { localGet } from "@/utils/index.js";
import { useRouter } from 'vue-router';

const videoDuration = ref(null);
const route = useRoute();
const router = useRouter();
const fromPage = route.query.fromPage;
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
    CollectId: '',
    file_path: '',
  }
});
const isImage = ref(false);

const formatDuration = (duration) => {
  if (duration === null) return '';
  const hours = Math.floor(duration / 3600);
  const minutes = Math.floor((duration % 3600) / 60);
  const seconds = duration % 60;
  return `${hours > 0 ? `${hours}:` : ''}${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
};

const incrementPageView = async (id) => {
  try {
    const payload = {
      videoId: videoId.value,
      pageView: state.fileParams.PageView,
      teamId: state.fileParams.TeamId
    };
    console.log('提交的数据:', payload); // 打印提交的数据
    axios.put('/video', payload)
        .then(res => {
        })
        .catch(error => {
          console.error('请求出错', error);
        });
}catch (error) {
    console.error('Failed to increment PageView:', error);
  }
};

const getDetail = async (id) => {
  try {
    const res = await axios.get(`/videodetail/${id}`);
    state.fileParams = {
      VideoName: res.video.videoName,
      PageView: res.video.pageView + 1,
      VideoTeacher: res.video.videoTeacher,
      VideoIntro: res.video.videoIntro,
      TeamId: res.video.teamId,
      CollectId: res.video.collectId,
      file_path: res.video.url ? res.hostUrl + res.video.url : res.hostUrl + res.video.picture,
    };
    isImage.value = !res.video.url;

    await incrementPageView(id); // 调用增量函数

    if (res.video.url) {
      const videoElement = document.createElement('video');
      videoElement.src = state.fileParams.file_path;
      videoElement.onloadedmetadata = () => {
        videoDuration.value = Math.round(videoElement.duration);
      };
    }
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
};

const goBack = () => {
  if (fromPage === 'videoManage') {
    router.push('/student/video_manage');
  } else {
    router.push('/student/video');
  }
};

const handleCollect = async () => {
  try {
    if (state.fileParams.CollectId) {
      await axios.delete('/video/collect', {
        data: { ids: [state.fileParams.CollectId] },
        headers: { 'token': state.token }
      });
      ElMessage.success('已取消收藏');
    } else {
      await axios.post('/video/collect', { videoId: videoId.value }, {
        headers: { 'token': state.token }
      });
      ElMessage.success('已收藏');
    }
    getDetail(videoId.value); // 重新获取详情以更新页面状态
  } catch (error) {
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

.teacher-info {
  flex: 1; /* 设置授课教师框的高度 */
  margin-bottom: 10px;
}

.course-description {
  flex: 2; /* 设置课程介绍框的高度 */
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

.teacher-name {
  white-space: nowrap; /* 不换行 */
  overflow: hidden; /* 超出隐藏 */
  text-overflow: ellipsis; /* 省略号 */
}

.course-intro {
  max-height: 200px; /* 设置最大高度 */
  overflow-y: auto; /* 添加垂直滚动条 */
}
</style>
