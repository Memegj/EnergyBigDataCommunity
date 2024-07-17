<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <el-form-item label="视频名称" prop="VideocontentName">
          <el-input style="width: 600px" v-model="state.fileParams.VideocontentName" placeholder="请输入视频名称"/>
        </el-form-item>

        <el-form-item label="视频">
          <div>
            <div style="display: inline-flex; align-items: center;">
              <a v-if="state.fileParams.file_path" :href="state.fileParams.file_path" :download="state.fileParams.VideocontentName" class="file-uploader">视频播放</a>
              &nbsp;&nbsp;&nbsp;
              <el-upload
                  class="file-uploader"
                  :action="state.uploadFileServer"
                  accept="mp4,mp3"
                  :headers="{ token: state.token }"
                  :show-file-list="false"
                  :before-upload="handleBeforeUpload"
                  :on-success="handleUrlSuccess"
                  :on-progress="(event) => handleUploadProgress(event, 'video')"
              >
                <el-button type="primary" slot="trigger" size="small">视频上传</el-button>
              </el-upload>
            </div>
          </div>
          <el-progress v-if="state.videoUploadProgress !== null" :percentage="state.videoUploadProgress"/>
        </el-form-item>

        <el-form-item label="视频预览">
          <div v-if="state.fileParams.file_path">
            <video :src="state.fileParams.file_path" controls style="width: 100%;"></video>
          </div>
          <div v-else class="preview-placeholder">
            <p class="preview-tip">请先上传视频进行预览</p>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitAdd">立即创建</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, getCurrentInstance } from 'vue';
import axios from '@/utils/axios';
import { ElMessage } from 'element-plus';
import { localGet } from '@/utils';
import { useRoute } from 'vue-router';  // 导入 useRoute

const route = useRoute();  // 获取路由实例

const { proxy } = getCurrentInstance();
const fileRef = ref(null);

const state = reactive({
  uploadFileServer: 'api/video/files',
  uploadPictureServer: 'api/video/picture',
  token: localGet('token') || '',
  fileParams: {
    VideocontentName: '',
    VideocontentUrl: '',
    VideoId: route.query.videoId || '',  // 获取传递过来的 videoId
    isPublic: true,
    file_path: ''
  },
  rules: {
    VideocontentName: [
      { required: true, message: '请填写视频名称', trigger: ['blur'] }
    ]
  },
  pictureUploadProgress: null,
  videoUploadProgress: null,
});

const submitAdd = () => {
  fileRef.value.validate((valid) => {
    if (valid) {
      let params = {
        videocontentName: state.fileParams.VideocontentName,
        videocontentUrl: state.fileParams.VideocontentUrl,
        videoId: state.fileParams.VideoId,
      };
      console.log('params', params);
      axios.post('/videocontent/file_save', params).then(res => {
        if (res == true) {
          ElMessage.success('添加成功');
        } else ElMessage.error("添加失败");
      });
    }
  });
};

const handleBeforeUpload = (file) => {
  const allowedTypes = ['zip', 'rar', 'jpg', 'jpeg', 'png', 'pdf', 'mp4', 'mp3'];
  const suffix = file.name.split('.').pop().toLowerCase();
  if (!allowedTypes.includes(suffix)) {
    ElMessage.error('请上传 zip,rar,jpg,jpeg,png,pdf,mp4,mp3 格式的文件');
    return false;
  }
  return true;
};

const handleUrlSuccess = (val) => {
  state.fileParams.file_path = val.data[0] + val.data[1] || '';
  state.fileParams.VideocontentUrl = val.data[1];
  state.videoUploadProgress = null; // 上传成功后重置进度条
};

const getTeamOptions = () => {
  axios.get('/videocontent/teamlist')
      .then(function (res) {
        state.teamlist = [];
        res.forEach(item => {
          let arr = {
            value: item.teamId,
            label: item.teamName
          };
          state.teamlist.push(arr);
        });
      })
      .catch(function (err) {
        console.log(err);
      });
};

    const handleUploadProgress = (event, type) => {
  if (type === 'picture') {
    state.pictureUploadProgress = event.percent;
  } else if (type === 'video') {
    state.videoUploadProgress = event.percent;
  }
};

const handlePublicChange = (value) => {
  if (!value) {
    getTeamOptions();
  } else {
    state.fileParams.TeamId = '';
  }
};

onMounted(async () => {
  await getTeamOptions();
});
</script>

<style scoped>
.file_upload {
  padding: 20px;
}

.file_upload_container {
  max-width: 800px;
  margin: 0 auto;
}

.preview-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  background-color: #f5f5f5;
  border: 1px dashed #d9d9d9;
}

.preview-tip {
  color: #999;
  font-size: 14px;
}

.file-uploader {
  text-decoration: none;
  color: #409EFF;
}
</style>