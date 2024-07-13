<template>
  <el-card class="references-container">
    <template #header>
      <div class="search-bar">
        <h1>视频详情</h1>
        <el-button type="primary" @click="goback">返回</el-button>
      </div>
    </template>
    <div class="container">
      <!-- 视频详情 -->
      <el-card class="video-card video-details">
        <div class="video-header">
          <video ref="videoPlayer" :src="video.url" controls class="video-cover">你的浏览器不支持视频播放</video>
          <div class="video-info">
            <h1>{{ video.videoName }}</h1>
            <p v-if="video.videoDuration !== null">
              视频时长: {{ formatDuration(video.videoDuration) }}
            </p>
            <p v-else>
              正在加载视频信息...
            </p>
            <p>浏览量: {{ video.pageView }}</p>
            <el-button type="primary" @click="toggleFavorite">
              {{ isFavorite ? '取消收藏' : '收藏' }}
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
            <el-menu-item
                v-for="(chapter, index) in video.chapters"
                :key="index"
                @click="goToChapter(index)"
            >
              {{ chapter }}
            </el-menu-item>
          </el-menu>
        </el-card>

        <div class="info-blocks">
          <!-- 授课教师 -->
          <el-card class="video-card teacher-info">
            <div slot="header" class="clearfix">
              <span>授课教师</span>
            </div>
            <p>{{ video.videoTeacher }}</p>
          </el-card>

          <!-- 课程介绍 -->
          <el-card class="video-card course-description">
            <div slot="header" class="clearfix">
              <span>课程介绍</span>
            </div>
            <p>{{ video.videoIntro }}</p>
          </el-card>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      video: {
        url: 'https://v6.huanqiucdn.cn/4394989evodtranscq1500012236/d271fe9d1253642700900646890/v.f100830.mp4',
        videoName: '教学视频标题',
        duration: '2小时30分钟',
        pageView: 12345,
        chapters: ['第一章', '第二章', '第三章'],
        videoTeacher: '老师姓名',
        videoIntro: '课程简介内容...',
        videoDuration: null
      },
      isFavorite: false,
    };
  },
  mounted() {
    this.getVideoDuration();
  },
  methods: {
    getVideoDuration() {
      const videoElement = this.$refs.videoPlayer;

      // 等待 video 元素加载元数据后再获取时长
      videoElement.addEventListener('loadedmetadata', () => {
        this.video.videoDuration = Math.floor(videoElement.duration);
      });

      // 处理视频加载失败的情况
      videoElement.addEventListener('error', () => {
        console.error('视频加载失败');
        // 可以设置一个默认的视频时长或者显示加载失败信息
        this.video.videoDuration = 0; // 设置默认时长为0或者null，视情况而定
      });
    },
    formatDuration(seconds) {
      const hours = Math.floor(seconds / 3600);
      const minutes = Math.floor((seconds % 3600) / 60);
      const remainingSeconds = Math.floor(seconds % 60);
      return `${hours > 0 ? hours + '小时' : ''}${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    },

    toggleFavorite() {
      this.isFavorite = !this.isFavorite;
    },
    goToChapter(index) {
      console.log(`跳转到章节 ${index + 1}`);
    },
    goback() {
      this.$router.push('/student/studentvideo');
    },
  },
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