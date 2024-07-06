<template>
  <div class="file_upload">
    <el-card class="file_upload_container">
      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">

        <!-- 表单项，输入文件名 -->
        <el-form-item label="文件名"  prop="filename">
          <el-input style="width: 600px" v-model="state.fileParams.filename" placeholder="请输入文件名称"/>
        </el-form-item>

        <!-- 表单项，文件上传和下载 -->
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
              <el-icon class="file-uploader-icon"><Plus /></el-icon>
              <!-- 上传文件的加号图标 -->
            </el-upload>
          </div>
          <div v-else>
            <!-- 文件下载和重新上传按钮容器 -->
            <div style="display: inline-flex; align-items: center;">
              <!-- 文件下载链接 -->
              <a :href="state.fileParams.file_path" :download="state.fileParams.filename" class="file-uploader">文件下载</a>
              <!-- 文件重新上传 -->
              &nbsp&nbsp&nbsp
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
        </el-form-item>

        <!-- 表单项，文件详情 -->
        <el-form-item label="文件详情">
          <div ref='editor'></div>
        </el-form-item>

        <!-- 表单项，提交按钮 -->
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

// 定义引用和响应式数据
const editor = ref(null)
// 定义 editor 引用，用于获取编辑器实例
const { proxy } = getCurrentInstance()
const fileRef = ref(null)
// 定义 fileRef 引用，用于获取表单实例
const state = reactive({
  uploadFileServer: 'api/aa/files',
  // 文件上传服务器地址，即谁来处理这个文件↑
  token: localGet('token') || '',
  // 获取本地存储的 token
  fileParams: {
    filename:'',
    upload_user: '',
    upload_time: '',
    file_path: '',
    file_save_path:''
  },
  // 表单数据模型

  rules: {
    filename: [
      { required: 'true', message: '请填写文件名称', trigger: ['blur'] }
    ]
    // 文件名验证规则：必填，提示信息为 "请填写文件名称"，在失去焦点时触发验证
  }
})
let instance
// 定义编辑器实例

onMounted(() => {
  // 在组件挂载时执行的函数

  instance = new WangEditor(editor.value)
  // 创建 WangEditor 实例，绑定到 editor 引用的 DOM 元素
  instance.config.showLinkImg = false
  // 禁用显示插入链接图片的按钮
  instance.config.showLinkImgAlt = false
  // 禁用显示设置图片替换文字的按钮
  instance.config.showLinkImgHref = false
  // 禁用显示设置图片链接的按钮
  instance.config.uploadImgMaxSize = 2 * 1024 * 1024 // 2M
  // 设置上传图片的最大尺寸为 2MB
  instance.config.uploadFileName = 'file'
  // 设置上传文件的字段名称为 'file'
  instance.config.uploadImgHeaders = {
    token: state.token
    // 设置上传图片请求的头部信息，包含 token
  }
  // 图片返回格式不同，需要自定义返回格式
  instance.config.uploadImgHooks = {
    // 自定义上传图片的钩子函数
    // 图片上传并返回了结果，想要自己把图片插入到编辑器中
    // 例如服务器端返回的不是 { errno: 0, data: [...] } 这种格式，可使用 customInsert
    customInsert: function(insertImgFn, result) {
      console.log('result', result)
      // 输出服务器返回的结果
      // result 即服务端返回的接口
      // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
      if (result.data && result.data.length) {
        // 如果服务器返回的数据存在且有长度
        result.data.forEach(item => insertImgFn(item))
        // 遍历数据，调用 insertImgFn 插入图片到编辑器中
        //WangEditor 是一个富文本编辑器。具体来说，instance 是 WangEditor 的一个实例。
        //instance被绑定到了 editor 引用对应的 DOM 元素(editor.value)上。
        // insertImgFn 作为 customInsert 的参数，用于实现将图片插入编辑器内容的操作。
      }
    }
  }
  instance.config.uploadImgServer = 'api/aa/images'
  // 设置上传图片的处理地址
  Object.assign(instance.config, {
    // 合并配置
    onchange() {
      console.log('change')
      // 配置内容改变时的回调函数
    },
  })
  instance.create()
  // 创建编辑器实例
})
onBeforeUnmount(() => {
  // 在组件卸载前执行的函数
  instance.destroy()
  // 销毁编辑器实例
  instance = null
  // 置空实例变量
})
const submitAdd = () => {
  // 定义提交表单函数
  fileRef.value.validate((vaild) => {
    // 调用表单的验证方法
    //fileRdf是定义的一个引用，用来引用el-form的一个实例，在上面写了ref=fileRef
    if (vaild) {
      // 如果验证通过，默认新增用 post 方法
      let params = {
        filename: state.fileParams.filename,
        // 获取文件名
        file_path: state.fileParams.file_save_path,
        //获取文件路径
        file_info: instance.txt.html()
        // 获取编辑器中的 HTML 内容作为文件详情
      }
      console.log('params', params)
      //输出表单参数
      axios.post('/aa/file_save', params).then(res => {
        // 发送 POST 请求提交表单数据，url是处理提交数据的方法路径
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
  // 定义上传前的处理函数
  // 这个钩子函数会在文件上传前被调用,并且会接收到当前正在上传的文件对象作为参数,file 参数代表即将上传的文件对象
  /* file 对象是一个包含文件信息的 JavaScript 对象，通常具有以下属性：
  name: 文件名,例如 "example.jpg"
  size: 文件大小(以字节为单位)
  type: 文件的 MIME 类型,例如 "image/jpeg"
  uid: 文件的唯一标识符
  lastModified: 文件的最后修改时间戳
  lastModifiedDate: 文件的最后修改日期对象 */
  const sufix = file.name.split('.')[1] || ''
  // 获取文件的后缀名
  if (!['jpg', 'jpeg', 'png'].includes(sufix)) {
    // 如果文件后缀名不是 jpg、jpeg、png 之一
    ElMessage.error('请上传 jpg、jpeg、png 格式的图片')
    // 显示错误消息
    return false
  }
}
const handleUrlSuccess = (val) => {
  // 定义上传成功后的处理函数
  state.fileParams.file_path = val.data[0]+val.data[1] || ''
  state.fileParams.file_save_path = val.data[1]
  // 将服务器返回的文件路径保存到表单数据模型中
  //val 是服务器的响应数据，它通常是一个包含上传结果的对象。
  /*假设"data": "http://example.com/path/to/file",val.data 就是服务器返回的文件路径 "http://example.com/path/to/file",
  这个路径被赋值给 state.fileParams.file_path。*/
}

</script>
<style scoped>

</style>