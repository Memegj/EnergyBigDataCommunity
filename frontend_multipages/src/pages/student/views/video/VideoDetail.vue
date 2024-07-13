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
          <video :src="state.video.Url" controls class="video-cover">你的浏览器不支持视频播放</video>
          <div class="video-info">
            <h1>{{ state.video.VideoName }}</h1>
            <p v-if="state.video.videoDuration !== null">
              视频时长: {{ formatDuration(state.video.videoDuration) }}
            </p>
            <p v-else>
              视频时长:正在加载视频时长信息...
            </p>
            <p>浏览量: {{ state.video.PageView }}</p>
            <el-button type="primary" @click="toggleFavorite">
              {{ isFavorite.value ? '取消收藏' : '收藏' }}
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
            <el-menu-item v-for="(chapter, index) in state.video.chapters" :key="index" @click="goToChapter(index)">
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
            <p>{{ state.video.VideoTeacher }}</p>
          </el-card>

          <!-- 课程介绍 -->
          <el-card class="video-card course-description">
            <div slot="header" class="clearfix">
              <span>课程介绍</span>
            </div>
            <p>{{ state.video.VideoIntro }}</p>
          </el-card>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from '@/utils/axios';
import { ElMessage } from 'element-plus';

export default {
  setup() {
    const isFavorite = ref(false);
    const router = useRouter();
    const route = useRoute();
    const state = ref({
      loading: false,
      video: {
        Url: '',
        VideoName:'',
        PageView:0,
        VideoTeacher:'',
        VideoIntro:'',
        videoDuration: null,
        chapters: []
      }
    });

    const fetchVideoDetail = () => {
      const videoId = router.currentRoute.value.params.VideoId;
      state.value.loading = true;
      axios.get(`/api/videos/${videoId}`)
          .then(res => {
            state.value.video = res.data;
            state.value.loading = false;
          })
          .catch(error => {
            ElMessage.error('获取视频详情失败');
            console.error('获取视频详情页面失败', error);
            state.value.loading = false;
          });
    };
//解析视频时长
    const formatDuration = (duration) => {
      if (duration === null) return '';
      const hours = Math.floor(duration / 3600);
      const minutes = Math.floor((duration % 3600) / 60);
      const seconds = duration % 60;
      const hoursDisplay = hours > 0 ? `${hours}:` : '';
      const minutesDisplay = `${minutes < 10 && hours > 0 ? '0' : ''}${minutes}:`;
      const secondsDisplay = seconds < 10 ? '0' : '';
      return `${hoursDisplay}${minutesDisplay}${secondsDisplay}${seconds}`;
    };
//返回视频首页
    const goBack = () => {
      router.push('/student/studentvideo');
    };
//添加收藏
    const toggleFavorite = () => {
      isFavorite.value = !isFavorite.value;
      fetchVideoDetail();
    };
//章节索引
    const goToChapter = (index) => {
      console.log(`Navigating to chapter ${index + 1}`);
      fetchVideoDetail();
    };

    onMounted(fetchVideoDetail);

    return {
      state,
      isFavorite,
      goBack,
      toggleFavorite,
      goToChapter,
      formatDuration
    };
  }
};
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
  padding: 20px;
  gap: 20px;
}

.video-chapters {
  flex: 1;
  display: flex;
  align-items: stretch;
}

.info-blocks {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.teacher-info,
.course-description {
  flex: 1;
  margin-bottom: 20px; /* 确保两个信息块之间有间隙 */
}

.course-description {
  margin-bottom: 0; /* 最后一项不需要底部间隙 */
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