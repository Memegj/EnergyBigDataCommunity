<template>
  <div class="container">
    <!-- 视频详情 -->
    <el-card class="video-card video-details">
      <div slot="header" class="clearfix">
        <span>视频详情</span>
      </div>
      <div class="video-header">
        <img :src="video.coverImage" alt="Video Cover" class="video-cover" />
        <div class="video-info">
          <h1>{{ video.title }}</h1>
          <p>时长: {{ video.duration }}</p>
          <p>浏览量: {{ video.views }}</p>
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
          <p>{{ video.teacher }}</p>
        </el-card>

        <!-- 课程介绍 -->
        <el-card class="video-card course-description">
          <div slot="header" class="clearfix">
            <span>课程介绍</span>
          </div>
          <p>{{ video.description }}</p>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      video: {
        coverImage: 'path/to/cover.jpg',
        title: '教学视频标题',
        duration: '2小时30分钟',
        views: 12345,
        chapters: ['第一章', '第二章', '第三章'],
        teacher: '老师姓名',
        description: '课程简介内容...',
      },
      isFavorite: false,
    };
  },
  methods: {
    toggleFavorite() {
      this.isFavorite = !this.isFavorite;
    },
    goToChapter(index) {
      // 实现跳转到指定章节的逻辑
      console.log(`跳转到章节 ${index + 1}`);
    },
  },
};
</script>

<style scoped>
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
  width: 300px;
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
