<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <el-form-item label="文件名" prop="filename">
          <el-input style="width: 600px" v-model="state.fileParams.filename" placeholder="请输入文件名称"/>
        </el-form-item>
        <el-form-item label="文件">
          <el-upload
              class="file-uploader"
              :action="state.uploadFileServer"
              accept=".zip,.rar"
              :headers="{ token: state.token }"
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              :on-success="handleUrlSuccess"
              :on-progress="handleUploadProgress"
          >
            <a v-if="state.fileParams.file_path" :href="state.fileParams.file_path" download="state.fileParams.filename"
               class="file-uploader">文件下载</a>
            <el-icon v-else class="file-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <el-progress v-if="state.uploadProgress !== null" :percentage="state.uploadProgress"/>
        </el-form-item>
        <el-form-item label="文件详情">
          <div ref="editor"></div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAdd">立即创建</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted, onBeforeUnmount, getCurrentInstance} from 'vue'
import axios from '@/utils/axios'
import WangEditor from 'wangeditor'
import {ElMessage, ElProgress} from 'element-plus'
import {localGet} from '@/utils'

const editor = ref(null)
const {proxy} = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/datasets/files',
  token: localGet('token') || '',
  fileParams: {
    filename: '',
    upload_user: '',
    upload_time: '',
    file_path: ''
  },
  rules: {
    filename: [
      {required: true, message: '请填写文件名称', trigger: ['blur']}
    ]
  },
  uploadProgress: null
})

let instance

onMounted(() => {
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
  instance.config.uploadImgServer = 'api/file_upload/images'
  Object.assign(instance.config, {
    onchange() {
      console.log('change')
    },
  })
  instance.create()
})

onBeforeUnmount(() => {
  instance.destroy()
  instance = null
})

const submitAdd = () => {
  fileRef.value.validate((valid) => {
    if (valid) {
      let params = {
        filename: state.fileParams.filename,
        file_path: state.fileParams.file_path,
        file_info: instance.txt.html()
      }
      axios.post('/datasets/file_save', params).then(res => {
        if (res) {
          ElMessage.success('添加成功')
        } else {
          ElMessage.error('添加失败')
        }
      })
    }
  })
}

const handleBeforeUpload = (file) => {
  const allowedTypes = ['zip', 'rar']
  const suffix = file.name.split('.').pop().toLowerCase()
  if (!allowedTypes.includes(suffix)) {
    ElMessage.error('请上传 zip、rar 格式的文件')
    return false
  }
  return true
}

const handleUrlSuccess = (response) => {
  state.fileParams.file_path = response.data || ''
  state.uploadProgress = null // 上传成功后重置进度条
}

const handleUploadProgress = (event, file) => {
  state.uploadProgress = event.percent
}
</script>

<style scoped>
</style>
