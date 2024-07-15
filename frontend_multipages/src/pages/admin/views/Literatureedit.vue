<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <el-form-item label="文献名称" prop="literName">
          <el-input style="width: 600px" v-model="state.fileParams.LiterName" placeholder="请输入文献名称"/>
        </el-form-item>
        <el-form-item label="文献作者" prop="literAuthor">
          <el-input style="width: 600px" v-model="state.fileParams.LiterAuthor" placeholder="请输入文献作者"/>
        </el-form-item>
        <el-form-item label="文献类型" prop="literType">
          <el-select v-model="state.fileParams.LiterType" placeholder="请选择文献类型" style="width: 200px;">
            <el-option label="期刊" value="期刊"></el-option>
            <el-option label="图书" value="图书"></el-option>
            <el-option label="报纸" value="报纸"></el-option>
            <el-option label="会议文献" value="会议文献"></el-option>
            <el-option label="学位论文" value="学位论文"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文献来源" prop="sources">
          <el-input style="width: 600px" v-model="state.fileParams.Sources" placeholder="请输入文献来源"/>
        </el-form-item>
        <el-form-item label="文献关键词" prop="keyWord">
          <el-input style="width: 600px" v-model="state.fileParams.KeyWord" placeholder="请输入文献关键词"/>
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
              <a :href="state.fileParams.file_path" :download="state.fileParams.LiterName" class="file-uploader">文件下载</a>
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
        <el-form-item label="文献摘要">
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
const literId = ref(route.params.literId)
const editor = ref(null)
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
const state = reactive({
  uploadFileServer: 'api/literature/files',
  token: localGet('token') || '',
  fileParams: {
    LiterName: '',
    LiterDigest: '',
    LiterAuthor: '',
    LiterType: '',
    Sources: '',
    KeyWord: '',
    TeamId: '',
    Url: '',
    isPublic: true, // 默认设为公开
    DownloadTimes: 0,
    file_path: ''
  },
  rules: {
    LiterName: [
      {required: true, message: '请填写文献名称', trigger: ['blur']}
    ],
    LiterAuthor: [
      {required: true, message: '请填写文献作者', trigger: ['blur']}
    ],
    LiterDigest: [
      {required: true, message: '请填写文献摘要', trigger: ['blur']}
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
  uploadProgress: null,
  teamlist: [] // 添加 teamlist 初始化
})

let instance

onMounted(async () => {
  await getDetail(literId.value) // 确保获取详情之后再初始化编辑器
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
  instance.config.uploadImgServer = 'api/literature/images'
  Object.assign(instance.config, {
    onchange() {
      console.log('change')
    },
  })
  instance.create()
  instance.txt.html(state.fileParams.LiterDigest) // 设置编辑器内容为 HTML
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
        literId: literId.value,
        literName: state.fileParams.LiterName,
        literAuthor: state.fileParams.LiterAuthor,
        literType: state.fileParams.LiterType,
        literDigest: instance.txt.html(),
        sources: state.fileParams.Sources,
        keyWord: state.fileParams.KeyWord,
        url: state.fileParams.Url,
        teamId: state.fileParams.TeamId
      };
      console.log('提交的数据:', payload); // 打印提交的数据
      axios.put('/literature', payload)
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

const getTeamOptions = () => {
  //读取和设置姓名下拉框
  axios.get('/literature/teamlist')
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
  const res = await axios.get(`/literature/${id}`)
  console.log('Response:', res); // 打印响应数据
  state.fileParams = {
    LiterName: res.literature.literName,
    LiterAuthor: res.literature.literAuthor,
    LiterType: res.literature.literType,
    LiterDigest: res.literature.literDigest,
    Sources: res.literature.sources,
    KeyWord: res.literature.keyWord,
    file_path: res.hostUrl + res.literature.url, // 构建完整的文件下载链接
    Url: res.literature.url,
    TeamId: res.literature.teamId,
    isPublic: res.literature.teamId === null // 根据 TeamId 设置 isPublic
  }

}
</script>

<style scoped>

</style>
