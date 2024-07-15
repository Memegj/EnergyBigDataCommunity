<template>
  <div>
    <el-card class="info-card">
      <div class="card-header">
        <div>
          <div class="user-info">
            <el-icon>
              <User />
            </el-icon>
            <span class="user-label">上传人: {{ state.fileParams.UserName }}</span>
            <span class="time-label">更新时间: {{ formattedUploadTime }}</span>
          </div>
          <h2>{{ state.fileParams.DataName }}</h2>
          <p>{{ state.fileParams.DataAbstract }}</p>
        </div>
        <div class="download-section">
          <div class="download-info">
            <span class="download-label">下载量: {{ state.fileParams.DownloadTimes }}</span>
            <el-button type="primary" @click="handleDownload">下载 ({{ formattedFileSize }})</el-button>

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
      </div>
    </el-card>

    <el-card class="details-card">
      <h3>关于数据集</h3>
      <div v-html="state.fileParams.DataDetails"></div>
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
const dataId = ref(route.params.dataId);
const state = reactive({
  token: localGet('token') || '',
  fileParams: {
    DataName: '',
    DataAbstract: '',
    DataDetails: '',
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
    const res = await axios.get(`/dataset/${id}`);
    console.log('Response:', res);
    state.fileParams = {
      DataName: res.datasets.dataName,
      DataAbstract: res.datasets.dataAbstract,
      DataDetails: res.datasets.dataDetails,
      UserId: res.datasets.userId,
      UserName: res.datasets.userName,
      UploadTime: res.datasets.uploadTime,
      DownloadTimes: res.datasets.downloadTimes,
      file_path: res.hostUrl + res.datasets.url,
      FileSize: res.datasets.fileSize,
      TeamId: res.datasets.teamId,
      CollectId: res.datasets.collectId
    };
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
};

const handleDownload = async () => {
  try {
    const updatedDownloadTimes = state.fileParams.DownloadTimes + 1;
    await axios.put('/dataset/detail', {
      dataId: dataId.value,
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

const handleCollect = async () => {
  console.log('CollectId:', state.fileParams.CollectId);
  console.log('Token:', state.token);

  try {
    if (state.fileParams.CollectId) {
      // 取消收藏
      await axios.delete('/dataset/collect', {
        data: {ids: [state.fileParams.CollectId]},
        headers: {'token': state.token}
      });
      ElMessage.success('已取消收藏');
    } else {
      // 收藏
      const params = {
        dataId: dataId.value,
      };
      await axios.post('/dataset/collect', params, {
        headers: {'token': state.token}
      });
      ElMessage.success('已收藏');
    }
    getDetail(dataId.value); // 重新获取详情以更新页面状态
  } catch (error) {
    console.error('Failed to update collect status:', error.response ? error.response.data : error);
    ElMessage.error('操作失败: ' + (error.response ? error.response.data.message : error.message));
  }
};

onMounted(() => {
  getDetail(dataId.value);
});
</script>

<style scoped>
.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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
  flex-direction: column;
  align-items: flex-end;
}

.download-info {
  display: flex;
  align-items: center;
}

.download-label {
  margin-right: 16px;
  font-size: 14px;
}

.details-card {
  margin-top: 20px;
  width: 100%;
  height: 350px; /* 可以根据需要调整高度 */
  overflow-y: auto; /* 添加垂直滚动条 */
}

.data-details {
  padding: 20px; /* 添加内边距 */
}
</style>
