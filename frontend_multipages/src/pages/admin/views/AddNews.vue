<template>
  <div style="margin-bottom: 10px;font-size: 25px" class="page-title">
    {{state.title}}
  </div>
  <div>
    <el-button type="primary" @click="goBack" style="position: absolute; top: 60px; right: 10px;">回到上一页</el-button>
  </div>
  <div class="file_upload">
    <el-card class="file_upload_container">

      <el-form :model="state.fileParams" :rules="state.rules" ref="fileRef" label-width="100px" class="fileParams">
        <el-form-item label="新闻标题">
          <el-input v-model="state.fileParams.Title" placeholder="请输入新闻标题" />
        </el-form-item>

        <el-form-item label="作者">
          <el-input v-model="state.fileParams.ReleaseAuthor" placeholder="请输入新闻作者" />
        </el-form-item>
        <el-form-item label="发表时间">
          <el-date-picker
              v-model="state.fileParams.ReleaseTime"
              type="datetime"
              placeholder="选择发表时间"
              style="width: 200px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item  label="新闻内容概述" class="content-input">
          <el-input style="height: 100px" v-model="state.fileParams.Content" placeholder="请输入简要的概述"/>
        </el-form-item>
        <el-form-item label="新闻链接">
          <el-input v-model="state.fileParams.Url" placeholder="请输入新闻链接" />
        </el-form-item>

        <el-form-item v-if="!state.isUpData" >
          <el-button type="primary" @click="submitAdd">立即添加</el-button>

        </el-form-item>
        <el-form-item v-else>
          <el-button   type="primary" @click="submitUpData">立即修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted, onBeforeUnmount,onBeforeMount, getCurrentInstance } from 'vue'
import axios from '@/utils/axios'
import WangEditor from 'wangeditor'
import { ElMessage } from 'element-plus'
import {localGet} from '@/utils'
import { useRouter } from 'vue-router'
const router = useRouter() // 声明路由实例
const editor = ref(null)
const fileRef = ref(null)
const state = reactive({
  title:"新增新闻",
  isUpData:false,
  fileParams: {
    id:0,
    Title:'',
    ReleaseAuthor:'',
    ReleaseTime:'',
    Url:'',
    Content:''
  },
  rules: {
    Title: [
      { required: 'true', message: '请填写新闻标题', trigger: ['change'] }
    ],
    ReleaseAuthor: [
      { required: 'true', message: '请填写作者', trigger: 'change' }
    ],
    ReleaseTime: [
      { required: 'true', message: '请填写发表时间', trigger: 'change' }
    ],
    Url: [
      { required: 'true', message: '请填写新闻链接', trigger: 'change' }
    ]
  }
})

const goBack = () => {
  router.push("/admin/NewsManagement").then(() => {
    window.location.reload(); // 在页面跳转完成后刷新页面
  });
}

onMounted(() => {
  // 从路由中获取参数，比如newsId
  const itemJson = router.currentRoute._value.query.item;

  if (itemJson) {
    state.title='修改新闻';
    // 将JSON字符串转换回对象
    const item = JSON.parse(itemJson);
    console.log(item); // 现在你可以使用item对象了
    state.fileParams.Title= item.title;
    state.fileParams.Content= item.content;
    state.fileParams.ReleaseAuthor= item.releaseauthor;
    state.fileParams.ReleaseTime= item.releasetime;
    state.fileParams.Url= item.url;
    state.fileParams.id= item.id;
    state.isUpData=true;
  }


})
onBeforeUnmount(() => {
  instance.destroy()
  let instance;
  instance = null
})
// onBeforeMount(() => {
//   console.log("1")
//   console.log(router.query)
//
//
// })
const submitAdd = () => {
  fileRef.value.validate((vaild) => {

    if (vaild) {

      // 默认新增用 post 方法
      let httpOption = axios.post
      let params = {
        title: state.fileParams.Title,
        release:state.fileParams.ReleaseAuthor,
        time:state.fileParams.ReleaseTime,
        url:state.fileParams.Url,
        content:state.fileParams.Content
      }
      console.log('params', params)
      httpOption('/news/NewsAdd', params).then(() => {
        ElMessage.success( '添加成功')
      })



    }
  })
}


const submitUpData = () => {
  fileRef.value.validate((vaild) => {

    if (vaild) {

      // 默认新增用 post 方法
      let httpOption = axios.post
      let params = {
        tid:state.fileParams.id,
        title: state.fileParams.Title,
        release:state.fileParams.ReleaseAuthor,
        time:state.fileParams.ReleaseTime,
        url:state.fileParams.Url,
        content:state.fileParams.Content
      }
      console.log('params', params)
      httpOption('/news/upDataNews/'+state.fileParams.id, params).then(() => {
        ElMessage.success( '修改成功')
      })



    }
  })
}


</script>
<style scoped>

</style>