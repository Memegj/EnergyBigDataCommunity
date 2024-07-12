<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <!-- 表单内容 -->
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <!-- 表单项 -->
        <el-form-item label="数据集名称" prop="dataName">
          <el-input style="width: 600px" v-model="state.fileParams.DataName" placeholder="请输入数据集名称"/>
        </el-form-item>
        <el-form-item label="数据集简介" prop="dataAbstract">
          <el-input style="width: 600px" v-model="state.fileParams.DataAbstract" placeholder="请输入数据集简介"/>
        </el-form-item>
        <el-form-item label="文件">
          <div v-if="!state.fileParams.file_path">
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
            </el-upload>
          </div>
          <div v-else>
            <div style="display: inline-flex; align-items: center;">
              <a :href="state.fileParams.file_path" :download="state.fileParams.DataName" class="file-uploader">文件下载</a>
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
        <el-form-item label="团队选择" v-if="!state.fileParams.isPublic" prop="teamId" style="position: relative; z-index: 100;">
          <el-select v-model="state.fileParams.TeamId" placeholder="请选择团队" style="width: 200px; position: relative; z-index: 101;">
            <el-option v-for="item in state.teamlist" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据集详情">
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
import { reactive, ref, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue'
import axios from '@/utils/axios'
import WangEditor from 'wangeditor'
import { ElMessage } from 'element-plus'
import { localGet } from '@/utils'
import { useRoute } from 'vue-router'

const route = useRoute()
const dataId = ref(route.params.dataId)
const editor = ref(null)
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/dataset/files',
  token: localGet('token') || '',
  fileParams: {
    DataName: '',
    DataAbstract: '',
    DataDetails: '',
    TeamId: '',
    Url: '',
    isPublic: true, // 默认设为公开
    DownloadTimes: 0,
    file_path: ''
  },
  rules: {
    DataName: [
      { required: true, message: '请填写数据集名称', trigger: ['blur'] }
    ],
    DataIntro: [
      { required: true, message: '请填写数据集简介', trigger: ['blur'] }
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
  teamlist: [] // 添加 teamlist 初始化
})

let instance

onMounted(async () => {
  await getDetail(dataId.value) // 确保获取详情之后再初始化编辑器
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
  instance.config.uploadImgServer = 'api/datasets/images'
  Object.assign(instance.config, {
    onchange() {
      console.log('change')
    },
  })
  instance.create()
  instance.txt.html(state.fileParams.DataDetails) // 设置编辑器内容为 HTML
  await getTeamOptions()
})

onBeforeUnmount(() => {
  instance.destroy()
  instance = null
})

const handlePublicChange = () => {
  if (state.fileParams.isPublic) {
    state.fileParams.TeamId = null; // 当 "是否公开" 被选中时，将 TeamId 设为 null
  }
}

const submitForm = () => {
  fileRef.value.validate((valid) => {
    if (valid) {
      handlePublicChange()
      const payload = {
        dataId: dataId.value,
        dataName: state.fileParams.DataName,
        dataAbstract: state.fileParams.DataAbstract,
        dataDetails: instance.txt.html(),
        url: state.fileParams.Url,
        teamId: state.fileParams.TeamId
      };
      console.log('提交的数据:', payload); // 打印提交的数据
      axios.put('/dataset', payload)
          .then(res => {
            if (res === true) { // 注意这里要用 res.data，而不是 res
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
  const allowedTypes = ['zip', 'rar', 'jpg', 'jpeg', 'png', 'pdf']
  const suffix = file.name.split('.').pop().toLowerCase()
  if (!allowedTypes.includes(suffix)) {
    ElMessage.error('请上传 zip,rar,jpg,jpeg,png,pdf 格式的文件')
    return false
  }
  return true
}

const handleUrlSuccess = (val) => {
  state.fileParams.file_path = val.data[0] + val.data[1] || ''
  state.fileParams.Url = val.data[1]
  state.uploadProgress = null
}

const getHost = () => {
  const {protocol, hostname, port} = window.location
  return `${protocol}//${hostname}${port ? `:${port}` : ''}`
}

const getTeamOptions = () => {
  //读取和设置姓名下拉框
  axios.get('/dataset/teamlist')
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

const getDetail = async (id) => {
  const res = await axios.get(`/dataset/${id}`)
  console.log('Response:', res); // 打印响应数据
  state.fileParams = {
    DataName: res.datasets.dataName,
    DataAbstract: res.datasets.dataAbstract,
    DataDetails: res.datasets.dataDetails,
    file_path: res.hostUrl + res.datasets.url, // 构建完整的文件下载链接
    Url: res.datasets.url,
    TeamId: res.datasets.teamId,
    isPublic: res.datasets.teamId === null // 根据 TeamId 设置 isPublic
  }

}
</script>

<style scoped>

</style>
