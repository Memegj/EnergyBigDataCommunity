<template>
  <div class="container">
    <el-card class="info-card">
      <div class="card-header">
        <div class="user-info">
          <el-icon>
            <User />
          </el-icon>
          <span class="user-label">上传人: {{ state.fileParams.UserName }}</span>
          <span class="time-label">更新时间: {{ formattedUploadTime }}</span>
        </div>
        <div class="download-section">
          <div class="download-info">
            <span class="download-label">下载量: {{ state.fileParams.DownloadTimes }}</span>
            <el-button type="primary" @click="handleDownload">下载 ({{ formattedFileSize }})</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="details-card">
      <h2>{{ state.fileParams.LiterName }}</h2>
      <p>作者: {{ state.fileParams.LiterAuthor }}</p>
      <p>来源: {{ state.fileParams.Sources }}</p>
      <p>类型: {{ state.fileParams.LiterType }}</p>
      <p>关键词: {{ state.fileParams.KeyWord }}</p>
      <h3>文献摘要</h3>
      <div v-html="state.fileParams.LiterDigest"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { localGet } from "@/utils/index.js";
import { User } from '@element-plus/icons-vue';

const route = useRoute();
const literId = ref(route.params.literId);
const state = reactive({
  token: localGet('token') || '',
  fileParams: {
    LiterName: '',
    LiterAuthor: '',
    LiterType: '',
    LiterDigest: '',
    Sources: '',
    KeyWord: '',
    UserName: '',
    UploadTime: '',
    DownloadTimes: 0,
    file_path: '',
    FileSize: 0,
    TeamId: '',
    UserId: '',
    CollectId: null
  }
});

const formattedUploadTime = computed(() => {
  const date = new Date(state.fileParams.UploadTime);
  const options = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  };
  return date.toLocaleString('zh-CN', options).replace(',', ' ');
});

const formattedFileSize = computed(() => {
  const size = state.fileParams.FileSize;
  if (size < 1024) return `${size} 字节`;
  else if (size < 1024 * 1024) return `${(size / 1024).toFixed(2)} KB`;
  else return `${(size / (1024 * 1024)).toFixed(2)} MB`;
});

const getDetail = async (id) => {
  try {
    const res = await axios.get(`/literature/${id}`);
    console.log('Response:', res);
    state.fileParams = {
      LiterName: res.literature.literName,
      LiterAuthor: res.literature.literAuthor,
      LiterDigest: res.literature.literDigest,
      LiterType: res.literature.literType,
      Sources: res.literature.sources,
      KeyWord: res.literature.keyWord,
      UserId: res.literature.userId,
      UserName: res.literature.userName,
      UploadTime: res.literature.uploadTime,
      DownloadTimes: res.literature.downloadTimes,
      file_path: res.hostUrl + res.literature.url,
      FileSize: res.literature.fileSize,
      TeamId: res.literature.teamId,
      CollectId: res.literature.collectId
    };
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
};

const handleDownload = async () => {
  try {
    const updatedDownloadTimes = state.fileParams.DownloadTimes + 1;
    await axios.put('/literature/detail', {
      literId: literId.value,
      downloadTimes: updatedDownloadTimes,
      teamId: state.fileParams.TeamId
    }, {
      headers: {
        'token': state.token
      }
    });
    state.fileParams.DownloadTimes = updatedDownloadTimes;
    window.location.href = state.fileParams.file_path;
  } catch (error) {
    console.error('Failed to update download count:', error);
  }
};


onMounted(() => {
  getDetail(literId.value);
});
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保容器占满视口高度 */
}

.info-card {
  margin-bottom: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-label {
  margin-left: 8px;
  margin-right: 16px;
}

.time-label {
  margin-left: 8px;
}

.download-section {
  display: flex;
  align-items: center;
}

.download-info {
  display: flex;
  align-items: center;
}

.download-label {
  margin-right: 16px;
  font-size: 14px;
  width: 80px; /* 增加宽度 */
}

.details-card {
  flex-grow: 1; /* 允许 details-card 占满剩余空间 */
  padding: 90px; /* 添加内边距 */
  text-align: center; /* 内容居中 */
  display: flex;
  flex-direction: column; /* 纵向排列内容 */
  justify-content: flex-start; /* 让内容从顶部开始排列 */
  overflow-y: auto; /* 添加垂直滚动 */
}

.liter-digest {
  flex-grow: 1; /* 使文献摘要部分可扩展 */
  overflow-y: auto; /* 添加滚动条 */
}
</style>