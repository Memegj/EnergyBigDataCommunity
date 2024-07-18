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
        <div v-if="isImage">
          <el-card class="video-card video-chapters">
            <div slot="header" class="clearfix header-flex">
              <span class="video-collection-title">视频合集</span>

            </div>
            <el-menu>
              <div class="video-chapters-container">
                <el-table
                    :data="state.tableData"
                    tooltip-effect="dark"
                    style="width: 100%"
                    @selection-change="handleSelectionChange">

                  <el-table-column label="视频预览" width="200" header-align="center" align="center">
                    <template #default="scope">
                      <video :src="state.fileParams.hostUrl + scope.row.videocontentUrl" controls width="180" height="100">你的浏览器不支持视频播放</video>
                    </template>
                  </el-table-column>
                  <el-table-column prop="videocontentName" label="名称" width="200" header-align="center" align="center"></el-table-column>
                  <el-table-column prop="uploadTime" label="上传时间" width="250" header-align="center" align="center" :formatter="(row) => formatUploadTime(row.uploadTime)"></el-table-column>
                </el-table>
              </div>
            </el-menu>
          </el-card>
        </div>

        <div v-else class="no-video-chapters">
          <el-card class="video-card video-chapters" style="height: 90%;">
            <div slot="header" class="clearfix header-flex">
              <span class="video-collection-title">视频合集</span>

            </div>
            <el-menu>
              <div class="video-chapters-container" style="text-align: center;">
                <h3 style="width: 100%;">无视频合集</h3>
              </div>
            </el-menu>
          </el-card>
        </div>
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
import {Delete} from "@element-plus/icons-vue";

const videoDuration = ref(null);
const route = useRoute();
const router = useRouter();
const fromPage = route.query.fromPage;
const videoId = ref(route.params.videoId);
const state = reactive({
  token: localGet('token') || '',
  tableData: [], // 数据列表
  multipleSelection: [], // 选中项
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

const formatUploadTime = (timestamp) => {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}
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
    state.tableData = res.videocontent
    state.loading = false
    state.fileParams = {
      VideoName: res.video.videoName,
      PageView: res.video.pageView + 1,
      VideoTeacher: res.video.videoTeacher,
      VideoIntro: res.video.videoIntro,
      TeamId: res.video.teamId,
      CollectId: res.video.collectId,
      file_path: res.video.url ? res.hostUrl + res.video.url : res.hostUrl + res.video.picture,
      hostUrl: res.hostUrl,
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
}
const handleSelectionChange = (val) => {
  state.multipleSelection = val
}

const goBack = () => {
  if (fromPage === 'videoManage') {
    router.push('/teacher/video_manage');
  } else {
    router.push('/teacher/video');
  }
};
const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('请选择项')
    return
  }
  axios.delete('/videoo', {
    data: {
      ids: state.multipleSelection.map(i => i.videocontentId)
    }
  }).then(() => {

    ElMessage.success('删除成功')
    getDetail(videoId.value); // 更新视频详情数据

  })
}
const handleDeleteOne = (videocontentId) => {
  axios.delete('/videoo', {
    data: {
      ids: [videocontentId]
    }
  }).then(() => {

    ElMessage.success('删除成功')
    getDetail(videoId.value); // 更新视频详情数据
  })
}
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


const handleAdd = () => {
  router.push({
    path: '/teacher/videocontentupload',
    query: { videoId: videoId.value }
  });
};
const handleEdit = (videocontentId) => {
  router.push({
    name: 'videocontentedit',
    params: {
      videocontentId: videocontentId,
      videoId: videoId.value
    }
  })
}
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
  align-items: stretch;
  height: 88%;
}

.no-video-chapters {
  display: flex;
  flex: 1 1 40%;
  height: 100%;
  margin-left: 20px;
}


.video-chapters-container {
  max-height: 400px;
  overflow-y: auto;
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

.header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button-group {
  display: flex;
  gap: 10px;
  padding: 8px;
}
</style>

