
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
            <p v-if="videoDuration !== null">
              视频时长：{{ formatDuration(videoDuration) }}
            </p>
            <p v-else>
              视频时长: 正在加载视频时长信息...
            </p>
            <p>浏览量: {{ state.video.PageView }}</p>
            <el-button
                type="primary"
                @click="handleCollect"
                v-if="state.fileParams.CollectId"
            >
              <el-icon><StarFilled /></el-icon> 取消收藏
            </el-button>
            <el-button
                type="primary"
                @click="handleCollect"
                v-else
            >
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
        chapters: []
      },
      isFavorite: false
      ,

      fileParams: {
        CollectId: null // 根据实际情况设置
      }
    });
    const videoDuration = ref(null);

    const fetchVideoDetail = () => {
      const videoId = route.params.VideoId; // 使用 useRoute() 获取当前路由参数
      state.value.loading = true;
      axios.get(`/api/videos/${videoId}`)
          .then(res => {

            state.value.video.Url = res.url;
            state.value.video.VideoName = res.videoName;
            state.value.video.PageView = res.pageView;
            state.value.video.VideoTeacher = res.videoTeacher;
            state.value.video.VideoIntro = res.videoIntro;
            state.value.loading = false;
            state.value.fileParams.CollectId=res.collectId;
            // chapters: []
            //获取视频时长并将其赋值给 videoDuration ：
            const videoElement = document.createElement('video');
            videoElement.src = state.value.video.Url;
            videoElement.onloadedmetadata = () => {
              videoDuration.value = Math.round(videoElement.duration);
            };
            isFavorite.value = res.isFavorite; // 假设后端返回的数据有 isFavorite 字段
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
    const handleCollect = async () => {
      console.log('CollectId:', state.fileParams.CollectId);
      console.log('Token:', state.token);

      try {
        if (state.fileParams.CollectId) {
          // 取消收藏
          await axios.delete('/api/collect/remove', {
            data: {ids: [state.fileParams.CollectId]},
            headers: {'token': state.token}
          });
          ElMessage.success('已取消收藏');
        } else {
          // 收藏
          const params = {
            videoId: videoId.value,
          };
          await axios.post('/api/collect/add', params, {
            headers: {'token': state.token}
          });
          ElMessage.success('已收藏');
        }
        fetchVideoDetail();// 重新获取详情以更新页面状态
      } catch (error) {
        console.error('Failed to update collect status:', error.response ? error.response.data : error);
        ElMessage.error('操作失败: ' + (error.response ? error.response.data.message : error.message));
      }
    };

    const toggleFavorite = () => {
      const videoId = route.params.VideoId;

      if (state.value.isFavorite) {
        axios.post(`/api/collect/remove`, {videoId, userId})
            .then(() => {
              state.value.isFavorite = false;
              ElMessage.success('已取消收藏');
            })
            .catch(error => {
              ElMessage.error('取消收藏失败');
              console.error('取消收藏失败', error);
            });
      } else {
        axios.post(`/api/collect/add`, {videoId, userId})
            .then(() => {
              state.value.isFavorite = true;
              ElMessage.success('已收藏');
            })
            .catch(error => {
              ElMessage.error('收藏失败');
              console.error('收藏失败', error);
            });
      }
    };


// //添加收藏
//     const toggleFavorite = () => {
//       isFavorite.value = !isFavorite.value;
//       fetchVideoDetail();
//     };
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
      goToChapter,
      formatDuration,
      videoDuration,
      handleCollect
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
  padding: 10px;
  gap: 20px;
}

.video-chapters {/* 章节 */
  flex: 2;
  display: flex;
  align-items: stretch;
}

.info-blocks {/* 授课老师和课程介绍 */
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 1px;
}

.teacher-info,
.course-description {
  flex: 2;
  margin-bottom: 10px; /* 确保两个信息块之间有间隙 */
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