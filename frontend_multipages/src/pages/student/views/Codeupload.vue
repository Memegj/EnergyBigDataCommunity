<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <el-form-item label="代码集名称" prop="codeName">
          <el-input style="width: 600px" v-model="state.fileParams.CodeName" placeholder="请输入代码集名称"/>
        </el-form-item>
        <el-form-item label="代码集简介" prop="codeAbstract">
          <el-input style="width: 600px" v-model="state.fileParams.CodeAbstract" placeholder="请输入代码集简介"/>
        </el-form-item>
        <el-form-item label="文件">
          <div v-if="!state.fileParams.file_path">
            <!-- 文件上传 -->
            <el-upload
                class="file-uploader"
                :action="state.uploadFileServer"
                accept="jpg,jpeg,png,pdf,rar,zip"
                :headers="{ token: state.token }"
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                :on-success="handleUrlSuccess"
            >
              <el-button type="primary" size="small">文件上传</el-button>
              <!-- 上传文件的加号图标 -->
            </el-upload>
          </div>
          <div v-else>
            <!-- 文件下载和重新上传按钮容器 -->
            <div style="display: inline-flex; align-items: center;">
              <!-- 文件下载链接 -->
              <a :href="state.fileParams.file_path" :download="state.fileParams.CodeName" class="file-uploader">文件下载</a>
              <!-- 文件重新上传 -->
              &nbsp;&nbsp;&nbsp;
              <el-upload
                  class="file-uploader"
                  :action="state.uploadFileServer"
                  accept="jpg,jpeg,png,pdf,rar,zip"
                  :headers="{ token: state.token }"
                  :show-file-list="false"
                  :before-upload="handleBeforeUpload"
                  :on-success="handleUrlSuccess"
              >
                <el-button type="primary" slot="trigger" size="small">重新上传</el-button>
              </el-upload>
            </div>
          </div>
          <el-progress v-if="state.uploadProgress !== null" :percentage="state.uploadProgress"/>
        </el-form-item>
        <el-form-item label="是否公开">
          <el-switch v-model="state.fileParams.isPublic" @change="handlePublicChange"/>
        </el-form-item>
        <el-form-item label="团队选择" v-if="!state.fileParams.isPublic" prop="TeamId" style="position: relative; z-index: 100;">
          <el-select v-model="state.fileParams.TeamId" placeholder="请选择团队" style="width: 200px; position: relative; z-index: 101;">
            <el-option v-for="item in state.teamlist" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="代码集详情">
          <div ref="editor" style="position: relative; z-index: 1;"></div>
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
import { ElMessage, ElProgress } from 'element-plus'
import { localGet } from '@/utils'

const editor = ref(null)
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/code/files',
  token: localGet('token') || '',
  fileParams: {
    CodeName: '',
    CodeAbstract: '',
    CodeDetails: '',
    TeamId: '',
    Url: '',
    isPublic: true, // 默认设为公开
    DownloadTimes: 0,
    file_path: ''
  },
  rules: {
    CodeName: [
      {required: true, message: '请填写代码集名称', trigger: ['blur']}
    ],
    CodeIntro: [
      {required: true, message: '请填写代码集简介', trigger: ['blur']}
    ],
    TeamId: [
      { validator: (rule, value, callback) => {
          if (!state.fileParams.isPublic && !value) {
            callback(new Error('请选择团队'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ]
  },
  uploadProgress: null,
})

let instance

onMounted(async () => {
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
  instance.config.uploadImgServer = 'api/code/images'
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

const submitAdd = () => {
  fileRef.value.validate((valid) => {
    if (valid) {

      let params = {
        codeName: state.fileParams.CodeName,
        codeAbstract: state.fileParams.CodeAbstract,
        codeDetails: instance.txt.html(),
        url: state.fileParams.Url,
        teamId: state.fileParams.TeamId,
        downloadTimes: state.fileParams.DownloadTimes
      }
      console.log('params', params)
      axios.post('/code/file_save', params).then(res => {
        if (res == true) {
          ElMessage.success('添加成功')
          // 如果响应结果为 true，显示添加成功的消息
        } else ElMessage.error("添加失败")
        // 否则显示添加失败的消息
      })
    }
  })
}

const handleBeforeUpload = (file) => {
  const allowedTypes = ['zip', 'rar', 'jpg','jpeg', 'png', 'pdf']
  const suffix = file.name.split('.').pop().toLowerCase()
  if (!allowedTypes.includes(suffix)) {
    ElMessage.error('请上传 zip, rar, jpg, jpeg, png, pdf 格式的文件')
    return false
  }
  return true
}

const handleUrlSuccess = (val) => {
  state.fileParams.file_path = val.data[0]+val.data[1] || ''
  state.fileParams.Url = val.data[1]
  state.uploadProgress = null // 上传成功后重置进度条
}

const getTeamOptions = () => {
  //读取和设置姓名下拉框
  axios.get('/code/teamlist')
      .then(function (res) {
        state.teamlist = [];
        res.forEach(item => {
          //console.log(item);
          let arr = {
            value: item.teamId,
            label: item.teamName
            //label和value都是同一个人名
          };
          state.teamlist.push(arr);
        });
      })
      .catch(function (err) {
        console.log(err)
      })
}

const handleUploadProgress = (event, file) => {
  state.uploadProgress = event.percent
}

const handlePublicChange = (value) => {
  if (!value) {
    getTeamOptions()
  } else {
    state.fileParams.TeamId = ''
  }
}

</script>

<style scoped>
</style>
