<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <div class="header">
        <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
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
                <el-button type="primary" slot="trigger" size="small">重新上传</el-button>
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
          <el-button type="primary" @click="submitForm">提交更改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue'
import axios from '@/utils/axios'
import WangEditor from 'wangeditor'
import { ElMessage } from 'element-plus'
import { localGet } from '@/utils'
import { useRoute, useRouter, onBeforeRouteLeave } from 'vue-router'

const route = useRoute()
const router = useRouter()
const videocontentId = ref(route.params.videocontentId)
const videoId = ref(route.params.videoId)
const editor = ref(null)
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/video/files',
  token: localGet('token') || '',
  fileParams: {
    VideocontentName: '',
    VideocontentUrl: '',
    file_path: ''
  },
  selectedType: 'single', // 默认选择单条视频
  rules: {
    VideoName: [
      { required: true, message: '请填写视频名称', trigger: ['blur'] }
    ],
  },
  videoUploadProgress: null,
})

let instance

onMounted(async () => {
  await getDetail(videocontentId.value) // 确保获取详情之后再初始化编辑器

  instance = new WangEditor(editor.value)
  instance.config.showLinkImg = false
  instance.config.showLinkImgAlt = false
  instance.config.showLinkImgHref = false
  instance.config.uploadImgMaxSize = 100 * 1024 * 1024 // 100M
  instance.config.uploadFileName = 'file'
  instance.config.uploadImgHeaders = {
    token: state.token
  }
  instance.config.uploadImgHooks = {
    customInsert: function (insertImgFn, result) {
      if (result.data && result.data.length) {
        result.data.forEach(item => insertImgFn(item))
      }
    }
  }
  instance.config.uploadImgServer = 'api/video/images'
  Object.assign(instance.config, {
    onchange() {
      console.log('change')
    },
  })
  instance.create()
  await getTeamOptions()
})

onBeforeUnmount(() => {
  instance.destroy()
  instance = null
})

const submitForm = () => {
  fileRef.value.validate((valid) => {
    if (valid) {
      const payload = {
        videocontentId: videocontentId.value,
        videocontentName: state.fileParams.VideocontentName,
        videocontentUrl: state.fileParams.VideocontentUrl
      };
      axios.put('/videocontent', payload)
          .then(res => {
            if (res === true) {
              ElMessage.success('更新成功');
              state.visible = false;
              router.push({ path: `/admin/video_detail/${videoId.value}` });
            } else {
              ElMessage.error('更新失败');
            }
          })
          .catch(error => {
            console.error('请求出错', error);
          });
    }
  })
}

const handleBeforeUpload = (file) => {
  const allowedTypes = ['zip', 'rar', 'jpg', 'jpeg', 'png', 'pdf', 'mp4', 'mp3']
  const suffix = file.name.split('.').pop().toLowerCase()
  if (!allowedTypes.includes(suffix)) {
    ElMessage.error('请上传 zip,rar,jpg,jpeg,png,pdf,mp4,mp3 格式的文件')
    return false
  }
  return true
}

const handleUrlSuccess = (val) => {
  state.fileParams.file_path = val.data[0] + val.data[1] || ''
  state.fileParams.VideocontentUrl = val.data[1]
  state.videoUploadProgress = null // 上传成功后重置进度条
}

const getTeamOptions = () => {
  axios.get('/video/teamlist')
      .then(function (res) {
        state.teamlist = []
        res.forEach(item => {
          let arr = {
            value: item.teamId,
            label: item.teamName
          }
          state.teamlist.push(arr)
        })
      })
      .catch(function (err) {
        console.log(err)
      })
}

const getDetail = async (id) => {
  const res = await axios.get(`/videocontentdetail/${id}`)
  console.log('Response:', res); // 打印响应数据
  state.fileParams = {
    VideocontentName: res.videocontent.videocontentName,
    VideocontentUrl: res.videocontent.videocontentUrl,
    file_path: res.hostUrl + res.videocontent.videocontentUrl
  }
}

const handleUploadProgress = (event, type) => {
  if (type === 'picture') {
    state.pictureUploadProgress = event.percent
  } else if (type === 'video') {
    state.videoUploadProgress = event.percent
  }
}


const goBack = () => {
    router.back();
}


</script>

<style scoped>
.header {
  display: flex;
  justify-content: flex-end;
}
</style>
