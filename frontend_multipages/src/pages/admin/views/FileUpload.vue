<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
      <el-form-item label="文件名"  prop="filename">
        <el-input style="width: 600px" v-model="state.fileParams.filename" placeholder="请输入文件名称"/>
        </el-form-item>
        <el-form-item label="文件">
          <el-upload
              class="file-uploader"
              :action= "state.uploadFileServer"
               accept="jpg,jpeg,png,pdf,rar,zip"
              :headers="{ token: state.token }"
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              :on-success="handleUrlSuccess"
          >
            <a v-if="state.fileParams.file_path" :href="state.fileParams.file_path" download="state.fileParams.filename" class="file-uploader">文件下载</a>
            <el-icon v-else class="file-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="文件详情">
          <div ref='editor'></div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAdd">立即创建</el-button>
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
import {localGet} from '@/utils'


const editor = ref(null)
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/file_upload/files',
  token: localGet('token') || '',
  fileParams: {
    filename:'',
    upload_user: '',
    upload_time: '',
    file_path: ''
  },
  rules: {
    filename: [
      { required: 'true', message: '请填写文件名称', trigger: ['blur'] }
    ]
  }
})
let instance
onMounted(() => {
  instance = new WangEditor(editor.value)
  instance.config.showLinkImg = false
  instance.config.showLinkImgAlt = false
  instance.config.showLinkImgHref = false
  instance.config.uploadImgMaxSize = 2 * 1024 * 1024 // 2M
  instance.config.uploadFileName = 'file'
  instance.config.uploadImgHeaders = {
    token: state.token
  }
  // 图片返回格式不同，需要自定义返回格式
  instance.config.uploadImgHooks = {
    // 图片上传并返回了结果，想要自己把图片插入到编辑器中
    // 例如服务器端返回的不是 { errno: 0, data: [...] } 这种格式，可使用 customInsert
    customInsert: function(insertImgFn, result) {
      console.log('result', result)
      // result 即服务端返回的接口
      // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
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
  fileRef.value.validate((vaild) => {
    if (vaild) {
      // 默认新增用 post 方法
      let params = {
        filename: state.fileParams.filename,
        file_path: state.fileParams.file_path,
        file_info: instance.txt.html()
      }
      console.log('params', params)
      axios.post('/file_upload/file_save', params).then(res => {
        if (res == true) {
          ElMessage.success('添加成功')
        } else ElMessage.error("添加失败")
      })
    }
  })
}
const handleBeforeUpload = (file) => {
  const sufix = file.name.split('.')[1] || ''
  if (!['jpg', 'jpeg', 'png'].includes(sufix)) {
    ElMessage.error('请上传 jpg、jpeg、png 格式的图片')
    return false
  }
}
const handleUrlSuccess = (val) => {
  state.fileParams.file_path = val.data || ''
}

</script>
<style scoped>

</style>