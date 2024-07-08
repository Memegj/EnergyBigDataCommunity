<script setup>
import { useRouter } from 'vue-router'
import {reactive} from "vue";
import {localGet} from "@/utils/index.js";
import axios from "@/utils/axios.js";
const router = useRouter()
const gotoHome = () => {
  router.push('/home')
}

//stt里面定义变量
const stt = reactive({
  username:''
})
// 初始化执行方法
const getUserInfo = ()=> {
  axios.get('/aa/bb').then(res => {
    //alert(res)
    stt.username = res
})
}
</script>

<template>
  <h1>This is an AA page页面</h1>
  <button @click="gotoHome"> 返回主页 </button>&nbsp
  <el-input style="width: 200px" v-model="stt.username" placeholder="点击获取文件名"/>&nbsp
  <el-button type="primary" @click="getUserInfo">获取用户名</el-button>

</template>

<style scoped>

</style>