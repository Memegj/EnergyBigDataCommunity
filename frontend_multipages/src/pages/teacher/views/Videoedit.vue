<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">

        <!-- 视频类型选择下拉框 -->
        <el-form-item label="选择类型">
          <el-select v-model="state.selectedType" placeholder="请选择类型" style="width: 600px" @change="handleTypeChange">
            <el-option label="创建并上传单条视频" value="single"></el-option>
            <el-option label="创建视频合集" value="collection"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="state.selectedType === 'single' ? '视频名称' : '合集名称'" prop="videoName">
          <el-input style="width: 600px" v-model="state.fileParams.VideoName" :placeholder="state.selectedType === 'single' ? '请输入视频名称' : '请输入合集名称'"/>
        </el-form-item>

        <el-form-item label="授课教师" prop="videoTeacher">
          <el-input style="width: 600px" v-model="state.fileParams.VideoTeacher" placeholder="请输入授课教师"/>
        </el-form-item>

        <!-- 上传封面图 -->
        <el-form-item label="封面图">
          <div v-if="!state.fileParams.picture_path">
            <el-upload
                class="file-uploader"
                :action="state.uploadPictureServer"
                accept="jpg,jpeg,png"
                :headers="{ token: state.token }"
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                :on-success="handlePictureSuccess"
                :on-progress="(event) => handleUploadProgress(event, 'picture')"
            >
              <el-button type="primary" size="small">封面图上传</el-button>
            </el-upload>
          </div>
          <div v-else>
            <div style="display: inline-flex; align-items: center;">
              <a :href="state.fileParams.picture_path" :download="state.fileParams.VideoName" class="file-uploader">查看封面图</a>
              &nbsp;&nbsp;&nbsp;
              <el-upload
                  class="file-uploader"
                  :action="state.uploadPictureServer"
                  accept="jpg,jpeg,png"
                  :headers="{ token: state.token }"
                  :show-file-list="false"
                  :before-upload="handleBeforeUpload"
                  :on-success="handlePictureSuccess"
                  :on-progress="(event) => handleUploadProgress(event, 'picture')"
              >
                <el-button type="primary" slot="trigger" size="small">重新上传</el-button>
              </el-upload>
            </div>
          </div>
          <el-progress v-if="state.pictureUploadProgress !== null" :percentage="state.pictureUploadProgress"/>
        </el-form-item>

        <el-form-item label="视频" v-if="state.selectedType === 'single'">
          <div v-if="!state.fileParams.file_path">
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
              <el-button type="primary" size="small">视频上传</el-button>
            </el-upload>
          </div>
          <div v-else>
            <div style="display: inline-flex; align-items: center;">
              <a :href="state.fileParams.file_path" :download="state.fileParams.VideoName" class="file-uploader">视频播放</a>
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

        <el-form-item label="是否公开">
          <el-switch v-model="state.fileParams.isPublic" @change="handlePublicChange"/>
        </el-form-item>

        <el-form-item label="团队选择" v-if="!state.fileParams.isPublic" prop="TeamId" style="position: relative; z-index: 100;">
          <el-select v-model="state.fileParams.TeamId" placeholder="请选择团队" style="width: 200px; position: relative; z-index: 101;">
            <el-option v-for="item in state.teamlist" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="视频简介">
          <div ref="editor" style="position: relative; z-index: 1;"></div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted, onBeforeUnmount, getCurrentInstance} from 'vue'
import axios from '@/utils/axios'
import WangEditor from 'wangeditor'
import {ElMessage} from 'element-plus'
import {localGet} from '@/utils'
import {useRoute} from "vue-router";

const route = useRoute()
const videoId = ref(route.params.videoId)
const editor = ref(null)
const {proxy} = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/video/files',
  uploadPictureServer: 'api/video/picture',
  token: localGet('token') || '',
  fileParams: {
    VideoName: '',
    VideoTeacher: '',
    VideoIntro: '',
    TeamId: '',
    Url: '',
    Picture: '',
    picture_path: '',
    isPublic: true,
    PageView: 0,
    file_path: ''
  },
  selectedType: 'single', // 默认选择单条视频
  rules: {
    VideoName: [
      {required: true, message: '请填写视频名称', trigger: ['blur']}
    ],
    VideoIntro: [
      {required: true, message: '请填写视频简介', trigger: ['blur']}
    ],
    TeamId: [
      {
        validator: (rule, value, callback) => {
          if (!state.fileParams.isPublic && !value) {
            callback(new Error('请选择团队'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ]
  },
  pictureUploadProgress: null,
  videoUploadProgress: null,
})

let instance

onMounted(async () => {
  await getDetail(videoId.value) // 确保获取详情之后再初始化编辑器
  // 根据视频的 URL 设置默认类型
  state.selectedType = state.fileParams.Url ? 'single' : 'collection';

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
  instance.txt.html(state.fileParams.VideoIntro) // 设置编辑器内容为 HTML
  await getTeamOptions()
})

onBeforeUnmount(() => {
  instance.destroy()
  instance = null
})

const submitForm = () => {
  fileRef.value.validate((valid) => {
    if (valid) {
      handlePublicChange()
      const payload = {
        videoId: videoId.value,
        videoName: state.fileParams.VideoName,
        videoTeacher: state.fileParams.VideoTeacher,
        videoIntro: instance.txt.html(),
        url: state.fileParams.Url,
        picture: state.fileParams.Picture,
        teamId: state.fileParams.TeamId,
        pageView: state.fileParams.PageView
      };
      console.log('提交的数据:', payload); // 打印提交的数据
      axios.put('/video', payload)
          .then(res => {
            if (res === true) {
              ElMessage.success('更新成功');
              state.visible = false;
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
  state.fileParams.Url = val.data[1]
  state.videoUploadProgress = null // 上传成功后重置进度条
}

const handlePictureSuccess = (val) => {
  state.fileParams.Picture = val.data[1] || ''
  state.fileParams.picture_path = val.data[0] + val.data[1] || '' // 处理封面图成功响应
  state.pictureUploadProgress = null // 上传成功后重置进度条
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
  const res = await axios.get(`/videodetail/${id}`)
  console.log('Response:', res); // 打印响应数据
  state.fileParams = {
    VideoName: res.video.videoName,
    VideoTeacher: res.video.videoTeacher,
    VideoIntro: res.video.videoIntro,
    file_path: res.video.url ? res.hostUrl + res.video.url : null, // 当 url 为 null 时，file_path 也为 null
    Url: res.video.url,
    Picture: res.video.picture,
    picture_path: res.hostUrl + res.video.picture,
    TeamId: res.video.teamId,
    PageView: res.video.pageView,
    isPublic: res.video.teamId === null // 根据 TeamId 设置 isPublic
  }
}

const handleUploadProgress = (event, type) => {
  if (type === 'picture') {
    state.pictureUploadProgress = event.percent
  } else if (type === 'video') {
    state.videoUploadProgress = event.percent
  }
}

const handlePublicChange = (value) => {
  if (!value) {
    getTeamOptions()
  } else {
    state.fileParams.TeamId = ''
  }
}

const handleTypeChange = (value) => {
  if (value === 'collection') {
    state.fileParams.VideoName = '' // 重置视频名称
    state.fileParams.Url = '' // 重置视频路径
    state.fileParams.file_path = '' // 重置视频文件路径
  } else {
    state.fileParams.VideoName = '' // 重置合集名称
  }
}
</script>

<style scoped>
</style>
